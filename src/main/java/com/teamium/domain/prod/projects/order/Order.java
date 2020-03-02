/**
 * 
 */
package com.teamium.domain.prod.projects.order;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.teamium.domain.TeamiumException;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.invoice.LineWrapper;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.ResourceEntity;

/**
 * Describe the abstract layer of a production project 
 * @author sraybaud
 * @version 5.0.1 amount when add a new line
 */
@Entity
@DiscriminatorValue(value="order")
@NamedQueries({
	@NamedQuery(name=Order.QUERY_findBySource, query="SELECT o FROM Order o WHERE o.source = ?1 ORDER BY o.date DESC, o.id ASC")
})
public class Order extends Record implements Cloneable, Comparable<Order>, ResourceEntity{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -5329569370070348730L;
	
	/**
	 * Name of the query retrieving all orders for a given project
	 * @param 1 the given project
	 * @return the list of matching orders
	 */
	public static final String QUERY_findBySource = "findOrderBySource";
	
	/**
	 * The faceted resource corresponding to the order
	 */
	@OneToOne(mappedBy="entity",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private SupplyResource resource;
	
	/**
	 * Add the given booking to the current order
	 * @param line the line to order
	 * @return true if success, else returns false
	 * @throws TeamiumException order_line_add_exists, order_line_add_assigned
	 */
	public boolean addOrderLine(Line line) throws TeamiumException{
		try{
			if(line instanceof Booking){
				Booking booking = (Booking) line;
				if(booking.getResource()!=null && this.equals(booking.getResource().getEntity())){
					throw new TeamiumException("order_line_add_exists");
				}
				if((booking.getResource()!=null && booking.getResource() instanceof SupplyResource)){
					throw new TeamiumException("order_line_add_assigned");
				}
			}
			OrderLine li = new OrderLine();
			li.setItem((Booking)line);
			boolean success = super.addLine(li);
			if(line instanceof Booking){
				Booking booking = (Booking) line;
				if(success){
					booking.setResource(this.getResource());
					this.getResource().assignFunction(booking.getFunction());
				}
			}
			li.update();
			this.updateAmounts();
			return success;
		}
		catch(TeamiumException e){
			throw e;
		}
	}
	
	/**
	 * Remove the given booking from order lines
	 * @param li the booking to remove
	 * @return true if success, else returns false
	 */
	public boolean removeOrderLine(LineWrapper li) throws TeamiumException{
		if(!this.getLines().isEmpty()){
			for(Line line : this.getLines()){
				if(line instanceof OrderLine){
					OrderLine oline = (OrderLine) line;
					if(li.equals(oline)){
						if(super.removeLine(oline)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	

	/**
	 * Returns the faceted resource
	 * @return the faceted resource
	 */
	public SupplyResource getResource(){
		if(this.resource==null){
			this.resource = new SupplyResource();
			this.resource.setOrder(this);
		}
		return this.resource;
	}
	
	/**
	 * Compare the current object with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(Order o) {
		if(o==null) return 1;
		if(this.getDate()==null){
			if(o.getDate()!=null){
				return -1;
			}
		}else{
			int compare = this.getDate().compareTo(o.getDate());
			if(compare !=0) return compare;
		}
		if(this.getCompany()==null){
			if(o.getCompany()!=null){
				return -1;
			}
		}else{
			int compare = this.getCompany().compareTo(o.getCompany());
			if(compare !=0) return compare;
		}
		return 0;
	}
	
	/**
	 * Update resource name before update
	 */
	@PrePersist
	@PreUpdate
	@Override
	protected void beforeUpdate(){
		super.beforeUpdate();
		if(this.getResource()!=null){
			this.getResource().setName((this.getCompany()!=null? this.getCompany().getName() : "")+" #"+this.getId());
		}
	}
	
	/**
	 * Update fees amount
	 */
	@Override
	protected void updateFees(){
		//Fee amount are free on an order
	}
	
	/**
	 * Returns the string expression of a project
	 * @return the text
	 */
	@Override
	public String toString(){
		return super.toString()+(this.getDate()!=null? DF.format(this.getDate().getTime())+"-": "")+this.getCompany();
	}

	
	

}

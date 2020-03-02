package com.teamium.domain.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.resources.functions.Function;

/**
 * An XML line output
 * @author sraybaud
 * @version TEAM-43
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LineXml implements Comparable<LineXml> {

	/**
	 * Line id
	 */
	@XmlAttribute
	private Long id;
	
	/**
	 * Line position
	 */
	@XmlAttribute
	private Integer position;
	
	/**
	 * Function
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private List<FunctionXml> functions;
	
	@XmlElement(namespace=XmlOutput.XMLNS)
	private FunctionXml directParent;
	
	/**
	 * Quantity
	 */
	@XmlElement(namespace=XmlOutput.XMLNS, name="quantity")
	private Float costQuantity;	

	/**
	 * Quantity
	 */
	@XmlElement(namespace=XmlOutput.XMLNS, name="quantitySold")
	private Float saleQuantity;	
	
	
	/**
	 * Unit
	 */
	@XmlElement(namespace=XmlOutput.XMLNS, name="unit")
	private ItemXml costUnit;
	
	/**
	 * Unit
	 */
	@XmlElement(namespace=XmlOutput.XMLNS, name="unitSold")
	private ItemXml saleUnit;
	
	/**
	 * Local pricing
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private LocalPricingXml pricing;
	
	/**
	 * Local pricing
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private LocalPricingXml externalPricing;
	
	
	/**
	 * Subtotal
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float subtotal;
	
	/**
	 * Subtotal
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float subtotalInternal;
	
	/**
	 * Subtotal
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float subtotalExternal;
	
	/**
	 * Discount
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private RatedAmountXml discount;
	
	/**
	 * Occurence count
	 */
	@XmlElement(namespace=XmlOutput.XMLNS, name="occurenceCount")
	private Float occurrenceCount;
	
	/**
	 * Occurrence price size
	 */
	@XmlElement(namespace=XmlOutput.XMLNS, name="occurenceSize")
	private Float saleOccurrenceSize;
	
	/**
	 * Occurrence cost size
	 */
	@XmlElement(namespace=XmlOutput.XMLNS, name="occurrenceCostSize")
	private Float costOccurrenceSize;
	
	/**
	 * Assignment type
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String assignation;
	
	/**
	 * Comment about the line
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String comment;
	
	/**
	 * Used for creating summaries
	 * @param line
	 */
	public LineXml(LineXml line) {
		this.id=line.id;
		this.position=line.position;
		this.costQuantity= line.costQuantity == null ? null : line.costQuantity.floatValue();
		this.costUnit=line.costUnit;		this.costQuantity= line.costQuantity == null ? null : line.costQuantity.floatValue();
		this.saleUnit=line.saleUnit;
		this.pricing= new LocalPricingXml();
		this.pricing.setCurrency(line.pricing.getCurrency());
		this.pricing.setDiscount(line.pricing.getDiscount());
		this.pricing.setSubTotal(line.pricing.getSubTotal() == null ? null : line.pricing.getSubTotal().floatValue());
		this.pricing.setUnitPrice(line.pricing.getUnitPrice() == null ? null : line.pricing.getUnitPrice().floatValue());
		if(line.subtotal != null) 
		{
			this.subtotal=line.subtotal.floatValue();
			if(line.assignation.equals("external")) 
			{
				this.subtotalExternal = line.subtotal;
				this.subtotalInternal = 0F;
			}
			else 
			{
				this.subtotalInternal = line.subtotal;
				this.subtotalExternal = 0F;
			}
		}
		else
		{
			this.subtotal = 0F;
			this.subtotalInternal = 0F;
			this.subtotalExternal = 0F;
		}
		this.comment = line.comment;
	}

	/**
	 * Default constructor
	 */
	public LineXml() {
		super();
	}

	/**
	 * @return the functions
	 */
	protected List<FunctionXml> getFunctions() {
		if(this.functions==null) this.functions = new ArrayList<FunctionXml>();
		return functions;
	}
	
	/**
	 * @param functions the functions to set
	 */
	protected void setFunctions(List<FunctionXml> functions) {
		this.functions = functions;
	}

	/**
	 * @param obj the object to marshal
	 */
	public void marshal(Line obj){
		if(obj!=null) {
			this.id=obj.getId();
			if(obj.getFunction()!=null){
				this.functions=new ArrayList<FunctionXml>();
				LinkedList<Function> list = new LinkedList<Function>();
				Function current = obj.getFunction();
				while(current!=null){
					list.addFirst(current);
					current=current.getParent();
				}
				for(int i=0; i<list.size(); i++){
					FunctionXml f = new FunctionXml();
					f.marshal(list.get(i), i+1);
					this.functions.add(f);
				}
			}
			this.costQuantity=obj.getQtyTotalUsed();
			this.saleQuantity=obj.getQtyTotalSold();
			if(obj.getUnitUsed()!=null){
				this.costUnit = new ItemXml();
				this.costUnit.marshal(obj.getUnitUsed());
			}
			if(obj.getUnitSold()!=null)
			{
				this.saleUnit = new ItemXml();
				this.saleUnit.marshal(obj.getUnitSold());
			}
			this.pricing=new LocalPricingXml();
			this.pricing.marshal(obj);
			this.subtotal=this.pricing.getSubTotal();
			this.occurrenceCount = obj.getOccurrenceCount();
			this.saleOccurrenceSize = obj.getQtySoldPerOc();
			this.costOccurrenceSize = obj.getQtyUsedPerOc();
			if(obj.getDiscountRate()!=null){
				this.discount = new RatedAmountXml();
				this.discount.marshal(obj.getDiscountRate(), obj.getDiscount());
			}
			this.assignation = obj.getAssignation() == null ? "internal" : obj.getAssignation().getKey();
			String str = obj.getComment();
			this.comment=obj.getComment();
		}
	}
	
	/**
	 * Merges the given line with the current one by merging quantities and amounts
	 * To be merged, lines must have :
	 * - same currency,
	 * - same unit,
	 * - same function.
	 * @param line the line to merge
	 * @return true if the merging succeeds, returns false if lines can't be merged
	 */
	public boolean merge(LineXml line){
		try{
			this.id += line.id;
			if(subtotalInternal == null || subtotalExternal == null){subtotalInternal = 0F;subtotalExternal = 0F;}
			
			if(this.pricing==null){
				this.pricing=line.pricing;
				this.subtotal = this.pricing.getSubTotal();
				if(line.assignation.equals("external")) this.subtotalExternal += line.subtotal;
				else this.subtotalInternal += line.subtotal;
			}
			else{
				if(line.pricing!=null){
					Float subtotal = (this.pricing.getSubTotal()!=null? this.pricing.getSubTotal() : 0 )+(line.pricing.getSubTotal()!=null?line.pricing.getSubTotal() : 0);
					this.pricing.setSubTotal(subtotal);
					Float saleQuantity = (this.saleQuantity!=null? this.saleQuantity : 0)+(line.saleQuantity!=null? line.saleQuantity : 0);
					if(Math.abs(saleQuantity) > 0){
						Float unitPrice = ((this.pricing.getUnitPrice()!=null && this.saleQuantity!=null ? this.pricing.getUnitPrice()*this.saleQuantity : 0)+(line.pricing.getUnitPrice()!=null && line.saleQuantity!=null ? line.pricing.getUnitPrice()*line.saleQuantity : 0))/this.saleQuantity;
						this.pricing.setUnitPrice(unitPrice);
					}
					if(this.pricing.getDiscount()==null) this.pricing.setDiscount(line.pricing.getDiscount());
					else{
						if(line.pricing.getDiscount()!=null){
							Float amount = (this.pricing.getDiscount().getAmount()!=null? this.pricing.getDiscount().getAmount() : 0)+(line.pricing.getDiscount().getAmount()!=null? line.pricing.getDiscount().getAmount() : 0);
							this.pricing.getDiscount().setAmount(amount);
							Float rate = amount / this.saleQuantity*this.pricing.getUnitPrice();
							this.pricing.getDiscount().setRate(rate);
						}
					}
					this.subtotal += this.pricing.getSubTotal();
					if(line.assignation.equals("external")) this.subtotalExternal += line.subtotal;
					else this.subtotalInternal += line.subtotal;
				}
			}

			
			if(this.costQuantity==null) this.costQuantity=line.costQuantity;
			else{
				if(line.costQuantity!=null){
					this.costQuantity+=line.costQuantity;
				}
			}			
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	@Override
	public String toString()
	{
		String ret = new String();
		for(FunctionXml f : this.getFunctions())
			ret += (f.toString() + " ");
		return ret;
	}

	/**
	 * Method used for the summaries
	 * Only keep the function given in parameter
	 * @param f
	 */
	public void removeOtherFunction(FunctionXml f) {
		this.getFunctions().clear();
		this.getFunctions().add(f);
	}

	@Override
	public int compareTo(LineXml o) {
		int compare = 0;
		if(this.functions != null){
			if(o.functions == null){
				compare = -1;
			}else{
				//Sort lists
				Collections.sort(this.functions);
				Collections.sort(o.functions);
				
				if(!this.functions.isEmpty()){
					if(!o.functions.isEmpty()){
						compare = this.functions.get(0).compareTo(o.functions.get(0));
					}else{
						compare = -1;
					}
				}else{
					if(!o.functions.isEmpty()){
						compare = 1;
					}
				}
			}
		}else{
			if(o.functions != null){
				compare = 1;
			}
		}
		
		if(compare == 0){
			if(this.id != null){
				if(o.id == null){
					compare = -1;
				}else{
					compare = this.id.compareTo(o.id);
				}
			}else{
				if(o.id != null){
					compare = -1;
				}
			}
		}
		return compare;
	}
	
}

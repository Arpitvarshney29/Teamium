package com.teamium.domain.prod.projects.dashboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.resources.equipments.EquipmentFunction;
import com.teamium.domain.prod.resources.functions.ProcessFunction;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.domain.prod.resources.suppliers.SupplyFunction;

/**
 * Budget used in export
 * @author slopes
 *
 */
public class Budget {
	/**
	 * The lines used to calculated the amount
	 */
	private List<Booking> lines;
	/**
	 * The externalized lines
	 */
	private List<Booking> externalizedLines;

	public Budget(){
		this.lines = new LinkedList<Booking>();
	}

	/**
	 * Return the sum of total price in lines given in parameter
	 * @param lines The lines
	 * @return The amount
	 */
	public Float getSumOfTotalPrice(){
		Float amount = 0f;
		for(Line line : this.lines){
			amount += line.getTotalPrice() != null ? line.getTotalPrice() : 0f;
		}
		return amount;
	}

	/**
	 * Return the sum of free lance total price in lines given in parameter
	 * @param lines The lines
	 * @return The amount
	 */
	public Float getSumOfFreelanceTotalPrice(){
		Float amount = 0f;
		for(Line line : this.lines){
			if(line.getAssignation() != null && line.getAssignation().getKey().equals("freelance")){
				amount += line.getTotalPrice() != null ? line.getTotalPrice() : 0f;
			}
		}
		return amount;
	}

	/**
	 * Return the sum of ordered total price in lines given in parameter
	 * <strong>The externalized line must be define</strong>
	 * @param lines The lines
	 * @return The amount
	 */
	public Float getSumOfExternalizedTotalPrice(){
		Float amount = 0f;
		for(Line line : this.lines){
			if(line.getAssignation() != null && line.getAssignation().getKey().equals("external")){
				amount += line.getTotalPrice() != null ? line.getTotalPrice() : 0f;
			}
		}
		return amount;
	}
	
	/**
	 * Return the amount of internal price for the current budget
	 */
	public Float getSumOfInternalTotalPrice(){
		Float amount = 0f;
		for(Line line : this.lines){
			if(line.getAssignation() != null && line.getAssignation().getKey().equals("internal")){
				amount += line.getTotalPrice() != null ? line.getTotalPrice() : 0f;
			}
		}
		return amount;
	}
	
	/**
	 * Return the sum of quantity in lines given in parameter
	 * @param lines The lines
	 * @return The quantity
	 */
	public Float getSumOfQuantity(){
		Float quantity = 0f;
		for(Line line : lines){
			quantity += line.getQtyTotalUsed() != null ? line.getQtyTotalUsed() : 0;
		}
		return quantity;
	}

	/**
	 * @return the lines
	 */
	public List<Booking> getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(List<Booking> lines) {
		this.lines = lines;
	}

	/**
	 * @return the externalizedLines
	 */
	public List<Booking> getExternalizedLines() {
		if(this.externalizedLines == null){
			this.externalizedLines = new ArrayList<Booking>();
		}
		return externalizedLines;
	}

	/**
	 * @param externalizedLines the externalizedLines to set
	 */
	public void setExternalizedLines(List<Booking> externalizedLines) {
		this.externalizedLines = externalizedLines;
	}

}

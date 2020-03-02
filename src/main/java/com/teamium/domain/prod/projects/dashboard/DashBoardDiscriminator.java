package com.teamium.domain.prod.projects.dashboard;

import com.teamium.domain.prod.projects.Booking;

public class DashBoardDiscriminator implements Comparable<DashBoardDiscriminator> {
	/**
	 * The line
	 */
	private Booking line;
	
	/**
	 * Return the comparison between rated function, the rate unit and the currency
	 */
	@Override
	public int compareTo(DashBoardDiscriminator o) {
		int compare = -1;
		if(this.getLine().getFunction() != null){
			compare = this.getLine().getFunction().compareTo(o.getLine().getFunction());
			if(compare == 0 && this.getLine().getUnitUsed() != null){
				compare = this.getLine().getUnitUsed().compareTo(o.getLine().getUnitUsed());
				if (compare == 0 && this.getLine().getCurrency() != null && o.getLine().getCurrency() != null){
					compare = this.getLine().getCurrency().getCurrencyCode().compareTo(o.getLine().getCurrency().getCurrencyCode());
				}
			}
		}
		return compare;
	}

	/**
	 * Return true if the rated function, the rate unit and the currency are equals
	 * @return true if the rated function, the rate unit and the currency are equals
	 */
	public boolean equals(Object o){
		boolean equals = super.equals(o);
		//Test the function
		if(!equals && this.getLine().getFunction() != null && o instanceof DashBoardDiscriminator){
			DashBoardDiscriminator dbd = (DashBoardDiscriminator) o;
			equals = this.getLine().getFunction().equals(dbd.getLine().getFunction());
			//Test the unit
			if(equals && this.getLine().getUnitUsed() != null && dbd.getLine().getUnitUsed() != null){
				equals = this.getLine().getUnitUsed().getKey().equals(dbd.getLine().getUnitUsed().getKey());
				//Test the currency
				if(this.getLine().getCurrency() != null && equals){
					equals = this.getLine().getCurrency().equals(dbd.getLine().getCurrency());
				}else{
					equals = this.getLine().getCurrency() == null && dbd.getLine().getCurrency() == null;
				}
			}else{
				equals = this.getLine().getUnitUsed() == null && dbd.getLine().getUnitUsed() == null;
			}
		}
		return equals;
	}

	/**
	 * @return the line
	 */
	public Booking getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(Booking line) {
		this.line = line;
	}

}

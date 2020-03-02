/**
 * 
 */
package com.teamium.domain.prod.projects.dashboard;

/**
 * @author slopes
 *
 */
public class RecordBudget implements Comparable<RecordBudget> {
	/**
	 * The discrimininator
	 */
	private DashBoardDiscriminator dashBoardDiscriminator;
	/**
	 * The planned budget
	 */
	private Budget plannedBudget;
	/**
	 * The effective budget
	 */
	private Budget effectiveBudget;
	
	public RecordBudget(){
		this.effectiveBudget = new Budget();
		this.plannedBudget = new Budget();
	}
	/**
	 * @return the plannedBudget
	 */
	public Budget getPlannedBudget() {
		return plannedBudget;
	}
	/**
	 * @return the effectiveBudget
	 */
	public Budget getEffectiveBudget() {
		return effectiveBudget;
	}
	@Override
	public int compareTo(RecordBudget o) {
		return this.dashBoardDiscriminator.compareTo(o.dashBoardDiscriminator);
	}
	/**
	 * Return true if the discriminator of the current instance equals return true for the object given in parameter
	 */
	public boolean equals(Object obj){
		boolean equals = super.equals(obj);
		if(!equals && obj instanceof RecordBudget){
			RecordBudget recordBudget = (RecordBudget) obj;
			equals = this.dashBoardDiscriminator.equals(recordBudget);
		}
		return equals;
	}
	/**
	 * @return the dashBoardDiscriminator
	 */
	public DashBoardDiscriminator getDashBoardDiscriminator() {
		return dashBoardDiscriminator;
	}
	/**
	 * @param dashBoardDiscriminator the dashBoardDiscriminator to set
	 */
	public void setDashBoardDiscriminator(
			DashBoardDiscriminator dashBoardDiscriminator) {
		this.dashBoardDiscriminator = dashBoardDiscriminator;
	}
	
}

package com.teamium.domain.prod.projects.dashboard;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.domain.prod.resources.functions.RatedFunction;



public class DashBoard {
	/**
	 * The dash board lines
	 */
	private Set<RecordBudget> dashboardLines;
	/**
	 * Master function used to generate the discriminator
	 */
	private RatedFunction masterFunction;
	/**
	 * Externalized function used to generate the discriminator
	 */
	private RatedFunction externalizedFunction;

	public DashBoard(){
		dashboardLines = new TreeSet<RecordBudget>();
	}


	/**
	 * Add the line given in parameter to the right budget
	 * @param line The line
	 */
	public void addToBudget(Booking line){
		DashBoardDiscriminator dbd = this.getDiscriminatorInstance();
		dbd.setLine(line);
		RecordBudget recordBudget = null;
		
		Iterator<RecordBudget> recordBudgetIT = this.getDashBoardLines().iterator();
		while(recordBudgetIT.hasNext() && recordBudget == null){
			RecordBudget recordBudgetTemp = recordBudgetIT.next();
			if(recordBudgetTemp.getDashBoardDiscriminator().equals(dbd)){
				recordBudget = recordBudgetTemp;
			}
		}if(recordBudget == null){
			recordBudget = new RecordBudget();
			recordBudget.setDashBoardDiscriminator(dbd);
			this.getDashBoardLines().add(recordBudget);
		}
		if(line.getFunction() != null){
			if (this.isPlanned(line)){
				recordBudget.getPlannedBudget().getLines().add(line);
				//If it is an externalized function the line is add to the externalized line list
				if(line.getFunction().equals(this.externalizedFunction)){
					recordBudget.getPlannedBudget().getExternalizedLines().add(line);
				}
			}else{
				recordBudget.getEffectiveBudget().getLines().add(line);
				//If it is an externalized function the line is add to the externalized line list
				if(line.getFunction().equals(this.externalizedFunction)){
					recordBudget.getEffectiveBudget().getExternalizedLines().add(line);
				}
			}
		}
	}

	/**
	 * Return true if the line given in parameter is in a quotation
	 * @param line The line
	 * @return True if the line has to be affect to the planned budget
	 */
	private boolean isPlanned(Line line){
		//TODO Revoir la deuxième condition ( line.getRecord renvoie une javassit comme class )
		return line.getRecord() != null &&  !(line.getRecord().getClass().equals(Project.class)) && !(line.getRecord().getClass().equals(Program.class)) ;
	}

	/**
	 * Return the dash board lines
	 * @return the dash board lines
	 */
	public Set<RecordBudget> getDashBoardLines(){
		return this.dashboardLines;
	}
	
	/**
	 * Return an instance of dash board discriminator
	 * @return An instance of dash board discriminator
	 */
	private DashBoardDiscriminator getDiscriminatorInstance(){
		return new DashBoardDiscriminator();
	}


	/**
	 * @return the masterFunction
	 */
	public RatedFunction getMasterFunction() {
		return masterFunction;
	}


	/**
	 * @return the externalizedFunction
	 */
	public RatedFunction getExternalizedFunction() {
		return externalizedFunction;
	}


	/**
	 * @param masterFunction the masterFunction to set
	 */
	public void setMasterFunction(RatedFunction masterFunction) {
		this.masterFunction = masterFunction;
	}


	/**
	 * @param externalizedFunction the externalizedFunction to set
	 */
	public void setExternalizedFunction(RatedFunction externalizedFunction) {
		this.externalizedFunction = externalizedFunction;
	}

}

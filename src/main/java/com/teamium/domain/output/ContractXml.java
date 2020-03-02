package com.teamium.domain.output;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.teamium.domain.output.adapters.CalendarDateAdapter;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.domain.prod.resources.staff.contract.Contract;
import com.teamium.domain.prod.resources.staff.contract.ContractLine;
import com.teamium.domain.prod.resources.staff.contract.ThresholdCouple;
import com.teamium.domain.utils.NumberToLetter;

/**
 * The xml representation of a contract
 * @author JS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ContractXml{
	
	/**
	 * Id
	 */
	@XmlAttribute
	private Long id;
	
	/**
	 * The sale entity
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private CompanyXml saleEntity;
	
	/**
	 * The staff member
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private StaffXml staffMember;
	
	/**
	 * The function
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String function;
	
	/**
	 * The day length
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Integer dayLength;
	
	/**
	 * The contract start
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar contractStart;
	
	/**
	 * The contract end
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar contractEnd;
	
	/**
	 * the print date
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Calendar printDate;
	
	/**
	 * the contract length
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Integer contractLength;
	
	/**
	 * Salary per unit
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float rate;
	
	/**
	 * The unit
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private ItemXml unit;
	
	/**
	 * The contract lines
	 */
	@XmlElement(name="line",namespace=XmlOutput.XMLNS)
	@XmlElementWrapper(namespace=XmlOutput.XMLNS)
	private List<ContractLineXml> lines;
	
	/**
	 * The sale representative
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private ContactXml saleRepresentative;
	
	/**
	 * Currency
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private ItemXml currency;
	
	/**
	 * the operation
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String operation;
	
	/**
	 * the description
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String description;
	
	/**
	 * the coefficiant
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Integer coefficiant;
	
	/**
	 * the assedic number
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String numAssedic;
	
	/**
	 * the contract total
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float contractTotal;
	
	/**
	 * the contract total in letter
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String contractTotalString;
	
	/**
	 * the rate in letters
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String rateString;
	
	/**
	 * The category
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String category;
	
	/**
	 * Project Title
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String projectTitle;
	
	/**
	 * Marshal a contract into a contract xml
	 * @param obj
	 */
	public void marshal(Contract obj)
	{
		if(obj != null)
		{
			this.id = obj.getId();
			if(obj.getEmployer() != null){
				CompanyXml xml = new CompanyXml();
				xml.marshal(obj.getEmployer());
				this.saleEntity = xml;
			}
			if(obj.getStaffMember() != null){
				StaffXml xml = new StaffXml();
				xml.marshal(obj.getStaffMember());
				this.staffMember = xml;
			}
			if(obj.getSaleRepresentative() != null)
			{
				this.saleRepresentative = new ContactXml();
				this.saleRepresentative.marshal(obj.getSaleRepresentative());
			}
			/*if(((RatedFunction)obj.getFunction()).getMasterFunction() != null)
				this.function = ((RatedFunction)obj.getFunction()).getMasterFunction().getValue();
			else*/
				this.function = obj.getFunction().getValue();
			this.contractStart = obj.getHireDate();
			this.contractEnd = obj.getEndDate();
			// Get the smallest threshold
			int lowestThreshold = 0;
			for (ThresholdCouple tc : obj.getConvention().getThresholdCouples()) {
				if(lowestThreshold == 0 || lowestThreshold > tc.getThreshold())
					lowestThreshold = tc.getThreshold();
			}
			this.dayLength = lowestThreshold;
			this.printDate = obj.getPrintedDate();
			if(this.contractStart != null && this.contractEnd != null){
				this.contractLength = contractEnd.get(Calendar.DAY_OF_YEAR) - contractStart.get(Calendar.DAY_OF_YEAR) + 1;
			}
			this.rate = obj.getRate();
			if(obj.getUnit() != null){
				unit = new ItemXml();
				unit.marshal(obj.getUnit());
			}
			this.lines = new ArrayList<ContractLineXml>();
			for(ContractLine cl : obj.getLines()){
				ContractLineXml xml = new ContractLineXml();
				xml.marshal(cl);
				this.lines.add(xml);
			}
			if(obj.getUnit() != null)
			{
				this.unit = new ItemXml();
				unit.marshal(obj.getUnit());
			}
			currency = new ItemXml();
			currency.marshal(obj.getLines().get(0).getBookings().get(0).getCurrency()); //TODO Changer pour la currency du contrat
			operation = obj.getOperation();
			description = obj.getDescription();
			coefficiant = obj.getCoefficiant();
			numAssedic = obj.getNumAssedic();
			contractTotal = obj.getContractTotal();
			if(obj.getStaffCategory() != null)
				category = obj.getStaffCategory().getLabel();
			try{
				/**
				 * NumberToLetter converter;
				 * converter = (NumberToLetter) Class.forName(NumberToLetter.CLASSPATH+obj.getLocale().getLanguage()).newInstance();
				 **/
				 //rateString = converter.convert(obj.getRate());
				 //contractTotalString = converter.convert(obj.getContractTotal().longValue());
				 RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(Locale.FRANCE, RuleBasedNumberFormat.SPELLOUT);
				 rateString = rbnf.format(obj.getRate());
				 contractTotalString = rbnf.format(obj.getContractTotal());
			}
			catch(Exception e) {
				contractTotalString = "..............................................";
				rateString = "..............................................";
			}
			if(obj.getLinkedRecord() != null)
				projectTitle = ((Project)obj.getLinkedRecord()).getTitle();
			
			
		}
	}

}

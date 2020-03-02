package com.teamium.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.output.edition.Edition;
import com.teamium.domain.prod.export.EntityExport;
import com.teamium.domain.prod.export.ExportFile;
import com.teamium.domain.prod.export.PeriodicExport;
import com.teamium.domain.prod.export.ResultsetExport;
import com.teamium.domain.prod.projects.CombinationType;
import com.teamium.domain.prod.projects.CompletionStatus;
import com.teamium.domain.prod.projects.ProjectCategory;
import com.teamium.domain.prod.resources.ResourceInformationKey;
import com.teamium.domain.prod.resources.contacts.RatePolicyType;
import com.teamium.domain.prod.resources.functions.Assignation;
import com.teamium.domain.prod.resources.functions.Category;
import com.teamium.domain.prod.resources.functions.FunctionType;
import com.teamium.domain.prod.resources.functions.units.DayUnit;
import com.teamium.domain.prod.resources.functions.units.DuplicableUnit;
import com.teamium.domain.prod.resources.functions.units.HourUnit;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.domain.prod.resources.functions.units.WeekUnit;
import com.teamium.domain.prod.resources.staff.contract.ContractEdition;
import com.teamium.domain.prod.resources.staff.contract.ContractType;
import com.teamium.domain.prod.resources.staff.contract.ExtraTime;
import com.teamium.domain.prod.resources.staff.contract.SocialConvention;

@XmlRootElement(name="resultset",namespace=TeamiumConstants.XMLNS)
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlResultset {
	
	/**
	 * The object entity class
	 */
	@XmlAttribute(namespace=TeamiumConstants.XMLNS)
	private String entityClass;
	
	/**
	 * The object description
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private XmlLocalizedLabel description;
	
	/**
	 * The set of xml entities and derived types
	 */
	@XmlElements({
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=XmlEntity.class,name="record"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=Category.class,name="category"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=ClassType.class,name="classType"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=CompletionStatus.class,name="completionStatus"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=ContractType.class,name="contractType"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=FunctionType.class,name="functionType"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=ProfileType.class,name="profileType"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=Theme.class,name="theme"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=RatePolicyType.class,name="ratePolicyType"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=HourUnit.class,name="hourUnit"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=DayUnit.class,name="dayUnit"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=WeekUnit.class,name="weekUnit"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=RateUnit.class,name="rateUnit"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=DuplicableUnit.class,name="duplicableUnit"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=SocialConvention.class,name="convention"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=ExtraTime.class,name="extraTime"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=Vat.class,name="vat"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=Edition.class,name="edition"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=ContractEdition.class,name="contractEdition"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=ExportFile.class,name="exportFile"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=EntityExport.class,name="entityExport"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=ResultsetExport.class,name="resultsetExport"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=PeriodicExport.class,name="periodicExport"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=ProjectCategory.class,name="projectCategory"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=ResourceInformationKey.class,name="resourceInformationKey"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=CombinationType.class,name="combinationType"),
	@XmlElement(namespace=TeamiumConstants.XMLNS,type=Assignation.class,name="assignation")
	})
	private List<AbstractXmlEntity> records;

	
	/**
	 * @return the entityClass
	 */
	public String getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the description
	 */
	public XmlLocalizedLabel getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(XmlLocalizedLabel description) {
		this.description = description;
	}
	
	/**
	 * @return the record
	 */
	public List<AbstractXmlEntity> getRecords() {
		if(this.records==null) this.records = new ArrayList<AbstractXmlEntity>();
		return records;
	}

	/**
	 * @param records the record to set
	 */
	public void setRecord(List<AbstractXmlEntity> records) {
		this.records = records;
	}

	/** (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((entityClass == null) ? 0 : entityClass.hashCode());
		result = prime * result + ((records == null) ? 0 : records.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XmlResultset other = (XmlResultset) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (entityClass == null) {
			if (other.entityClass != null)
				return false;
		} else if (!entityClass.equals(other.entityClass))
			return false;
		if (records == null) {
			if (other.records != null)
				return false;
		} else if (!records.equals(other.records))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" of "+this.entityClass+" : "+this.getRecords().size()+" record(s)";
	}
	
	
}

/**
 * 
 */
package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.invoice.Invoice;
import com.teamium.domain.prod.resources.staff.contract.Contract;


/**
 * A generic record xml output
 * @author sraybaud
 *
 */
@XmlRootElement(name="output", namespace=XmlOutput.XMLNS)
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlOutput {
	/**
	 * public static final 
	 */
	public static final String XMLNS = "http://www.softvallee.com/teamium/xml";
	
	/**
	 * Record ID
	 */
	@XmlAttribute(name="server_url_root")
	private String serverUrlRoot;
	
	@XmlAttribute(name="document_url_root")
	private String documentUrlRoot;

	@XmlAttribute(name="locale")
	private String locale;

	/**
	 * Record
	 */
	@XmlElements({
		@XmlElement(name="record", namespace=XMLNS, type=RecordXml.class),
		@XmlElement(name="project", namespace=XMLNS, type=ProjectXml.class),
		@XmlElement(name="invoice", namespace=XMLNS, type=InvoiceXml.class)
	})
	private RecordXml record;
	
	/**
	 * The invoice
	 */
	@XmlElement(namespace=XMLNS)
	private ContractXml contract;
	
	/**
	 * @param serverUrlRoot the server_root to set
	 */
	public void setServerUrlRoot(String serverUrlRoot) {
		if(serverUrlRoot!=null){
			this.serverUrlRoot = serverUrlRoot;
		}else{
			this.serverUrlRoot=null;
		}
	}

	public String getDocumentUrlRoot() {
		return documentUrlRoot;
	}

	public void setDocumentUrlRoot(String documentUrlRoot) {
		this.documentUrlRoot = documentUrlRoot;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * 
	 */
	public void marshal(XmlOutputEntity entity){
		if(entity!=null){
			
			if(entity instanceof Record){
				if(entity instanceof AbstractProject)
				{
					this.marshal((AbstractProject) entity);
				}
				else if(entity instanceof Invoice)
				{
					this.marshal((Invoice)entity);
				}
				else{
					this.marshal((Record) entity);					
				}
			}
			else if(entity instanceof Contract){
				this.marshal((Contract)entity);
			}
			
		}
	}
	
	
	/**
	 * Marhsal l'objet fourni en argument en XML
	 * @param obj l'objet à convertir en XML
	 */
	private void marshal(Record obj){
		if(obj!=null){
			RecordXml xml = new RecordXml();
			xml.marshal(obj);
			this.record = xml;
		}
	}
	
	/**
	 * Marhsal l'objet fourni en argument en XML
	 * @param obj l'objet à convertir en XML
	 */
	private void marshal(Invoice obj){
		if(obj!=null){
			InvoiceXml xml = new InvoiceXml();
			xml.marshal(obj);
			this.record = xml;
		}
	}
	
	/**
	 * Marhsal l'objet fourni en argument en XML
	 * @param obj l'objet à convertir en XML
	 */
	private void marshal(AbstractProject obj){
		if(obj!=null){
			ProjectXml xml = new ProjectXml();
			xml.marshal(obj);
			this.record = xml;
		}
	}
	
	/**
	 * Marhsal l'objet fourni en argument en XML
	 * @param obj l'objet à convertir en XML
	 */
	private void marshal(Contract obj){
		if(obj!=null){
			ContractXml xml = new ContractXml();
			xml.marshal(obj);
			this.contract = xml;
		}
	}
	
}

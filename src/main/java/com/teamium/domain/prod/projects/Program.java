/**
 * 
 */
package com.teamium.domain.prod.projects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.Channel;
import com.teamium.domain.Document;
import com.teamium.domain.ProductionUnit;
import com.teamium.domain.Theme;
import com.teamium.domain.prod.Record;

/**
 * @author slopesneves
 * @author jsdimpre - NovaRem A TV show
 */
@Entity
@DiscriminatorValue(value = Program.DISCRIMINATOR)
@NamedQueries({
		@NamedQuery(name = Program.QUERY_findAll, query = "SELECT p FROM Program p ORDER BY p.start DESC, p.title DESC"),
		@NamedQuery(name = Program.QUERY_countAll, query = "SELECT COUNT(p) FROM Program p"),
		@NamedQuery(name = Program.QUERY_findByIds, query = "SELECT p FROM Program p WHERE p.id IN (?1)"),
		@NamedQuery(name = Program.QUERY_findLikeKeyword, query = "SELECT p FROM Program p WHERE LOWER(p.title) LIKE ?1 ORDER BY p.title"), })
public class Program extends AbstractProject {

	/**
	 * Find all TV show in persistence
	 */
	public static final String QUERY_findAll = "findAllProgram";

	/**
	 * Count all TV show in persistence context
	 */
	public static final String QUERY_countAll = "countAllProgram";

	/**
	 * The query to find the TV show matching with the given id
	 */
	public static final String QUERY_findByIds = "findProgramByIds";

	/**
	 * The TV show alias used in queries
	 */
	public static final String QUERY_ENTITY_ALIAS = "t";

	/**
	 * Name of the query retrieving programs, which the title is matching the given
	 * keyword
	 * 
	 * @param 1 the given keyword in lower case
	 * @return the matching programs
	 */
	public static final String QUERY_findLikeKeyword = "findProgramLikeKeyword";

	/**
	 * Discriminator
	 */
	public static final String DISCRIMINATOR = "program";

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 5264749011328241369L;

	/**
	 * The reference, equals to id when is null
	 */
	@Column(name = "c_reference")
	private String reference;

	/**
	 * Title
	 */
	@Column(name = "c_program_title")
	private String title;

	/**
	 * The duration of the TV show in minutes
	 */
	@Column(name = "c_minute_duration")
	private Integer minuteDuration;

	/**
	 * The year
	 */
	@Column(name = "c_year")
	private Integer year;

	/**
	 * The equipment photo
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "c_idlogo")
	private Document document;

	/**
	 * The TV channel
	 */
	@ManyToOne
	@JoinColumn(name = "c_channel")
	private Channel channel;


	/**
	 * Analytic account number
	 */
	@Column(name = "c_analyticaccountnumber")
	private String analyticAccountNumber;

	/**
	 * List of linked projects
	 */
	@OneToMany
	@JoinColumn(name = "c_program")
	private List<AbstractProject> linkedRecords;

	/**
	 * The production unit
	 */
	@ManyToOne
	@JoinColumn(name = "c_produnit")
	private ProductionUnit prodUnit;

	/**
	 * The start date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "c_start")
	private Calendar start;

	/**
	 * The number of episode
	 */
	@Column(name = "c_nbepisodes")
	private Integer nbEpisodes;

	/**
	 * The number of session
	 */
	@Column(name = "c_nbsessions")
	private Integer nbSessions;

	/**
	 * program sessioned
	 */
	@Column(name = "c_sessioned")
	private boolean sessioned;

	/**
	 * program season
	 */
	@Column(name = "c_season")
	private String season;

	/**
	 * @return the reference
	 */
	public String getReference() {
		if (this.reference == null) {
			this.reference = this.getId() == null ? "" : this.getId().toString();
		}
		return this.reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the start
	 */
	public Calendar getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Calendar start) {
		this.start = start;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the minuteDuration
	 */
	public Integer getMinuteDuration() {
		return minuteDuration;
	}

	/**
	 * @param minuteDuration the minuteDuration to set
	 */
	public void setMinuteDuration(Integer minuteDuration) {
		this.minuteDuration = minuteDuration;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * @return the prodUnit
	 */
	public ProductionUnit getProdUnit() {
		return prodUnit;
	}

	/**
	 * @param prodUnit the prodUnit to set
	 */
	public void setProdUnit(ProductionUnit prodUnit) {
		this.prodUnit = prodUnit;
	}

	/**
	 * @return the nbEpisodes
	 */
	public Integer getNbEpisodes() {
		return nbEpisodes;
	}

	/**
	 * @param nbEpisodes the nbEpisodes to set
	 */
	public void setNbEpisodes(Integer nbEpisodes) {
		this.nbEpisodes = nbEpisodes;
	}

	/**
	 * @return the nbSessions
	 */
	public Integer getNbSessions() {
		if (nbSessions != null)
			this.setNbSessions(nbSessions);
		return nbSessions;
	}

	/**
	 * @param nbSessions the nbSessions to set
	 */
	public void setNbSessions(Integer nbSessions) {
		if (nbEpisodes != null && nbEpisodes > 0 && nbSessions != null) {
			Integer nbS = nbSessions;
			while (nbS > 0) {
				if (nbEpisodes % nbS == 0) {
					this.nbSessions = nbS;
					break;
				}
				nbS--;
			}
		}
	}

	public List<AbstractProject> getLinkedRecords() {
		if (linkedRecords == null)
			linkedRecords = new ArrayList<AbstractProject>();
		return linkedRecords;
	}

	public void setLinkedRecords(List<AbstractProject> linkedRecords) {
		this.linkedRecords = linkedRecords;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	/**
	 * @return the analyticAccountNumber
	 */
	public String getAnalyticAccountNumber() {
		return analyticAccountNumber;
	}

	/**
	 * @param analyticAccountNumber the analyticAccountNumber to set
	 */
	public void setAnalyticAccountNumber(String analyticAccountNumber) {
		this.analyticAccountNumber = analyticAccountNumber;
	}

	/**
	 * return The previsional budget
	 */
	public Float getPrevisionalCostPerEpisode() {
		return this.getCalcTotalCost();
	}

	/**
	 * return The previsional price
	 */
	public Float getPrevisionalPricePerEpisode() {
		return this.getCalcTotalPrice();
	}

	/**
	 * return The previsional price with VAT
	 */
	public Float getPrevisionalPriceIVATPerEpisode() {
		return this.getTotalNetPrice();
	}

	/**
	 * return the total previsional budget
	 */
	public Float getPrevisionalTotalCost() {
		Float totalCost = nbEpisodes != null && nbEpisodes > 0 ? this.getCalcTotalCost() * nbEpisodes
				: this.getCalcTotalCost();
		return totalCost;
	}

	/**
	 * return the total previsional price
	 */
	public Float getPrevisionalTotalPrice() {
		Float totalPrice = nbEpisodes != null && nbEpisodes > 0 ? this.getCalcTotalPrice() * nbEpisodes
				: this.getCalcTotalPrice();
		return totalPrice;
	}

	/**
	 * return the total previsional price with VAT
	 */
	public Float getPrevisionalTotalPriceIVAT() {
		Float totalPriceIVAT = nbEpisodes != null && nbEpisodes > 0 ? this.getTotalNetPrice() * nbEpisodes
				: this.getCalcTotalPriceIVAT();
		return totalPriceIVAT;
	}

	/**
	 * return the session cost
	 */
	public Float getPrevisionalCostForSession() {
		return nbSessions != null && nbSessions > 0 ? this.getTotalNetPrice() / nbSessions
				: this.getPrevisionalTotalCost();
	}

	/**
	 * return the session price
	 */
	public Float getPrevisionalPriceForSession() {
		return nbSessions != null && nbSessions > 0 ? this.getPrevisionalTotalPrice() / nbSessions
				: this.getPrevisionalTotalPrice();
	}

	/**
	 * return the session price with VAT
	 */
	public Float getPrevisionalPriceIVATForSession() {
		return nbSessions != null && nbSessions > 0 ? this.getPrevisionalTotalPriceIVAT() / nbSessions
				: this.getPrevisionalTotalPriceIVAT();
	}

	/**
	 * Return the real cost
	 */
	public Float getRealProgramCost() {
		Float totalCost = 0f;
		for (Record r : this.getLinkedRecords()) {
			totalCost += r.getTotalCost();
		}
		return totalCost;
	}

	/**
	 * Return the real price
	 */
	public Float getRealProgramPrice() {
		Float totalPrice = 0f;
		for (Record r : this.getLinkedRecords()) {
			totalPrice += r.getTotalPrice();
		}
		return totalPrice;
	}

	/**
	 * Return the real price IVAT
	 */
	public Float getRealProgramPriceIVAT() {
		Float totalIVAT = 0f;
		for (Record r : this.getLinkedRecords()) {
			totalIVAT += r.getTotalPriceIVAT();
		}
		return totalIVAT;
	}

	/**
	 * Return the average real cost
	 */
	public Float getAverageCost() {
		if (this.getLinkedRecords().size() > 0)
			return this.getRealProgramCost() / this.getLinkedRecords().size();
		return 0f;
	}

	/**
	 * Return the average real price
	 */
	public Float getAveragePrice() {
		if (this.getLinkedRecords().size() > 0)
			return this.getRealProgramPrice() / this.getLinkedRecords().size();
		return 0f;
	}

	/**
	 * Return the average real price IVAT
	 */
	public Float getAveragePriceIVAT() {
		if (this.getLinkedRecords().size() > 0)
			return this.getRealProgramPriceIVAT() / this.getLinkedRecords().size();
		return 0f;
	}

	/**
	 * @return the sessioned
	 */
	public boolean isSessioned() {
		return sessioned;
	}

	/**
	 * @param sessioned the sessioned to set
	 */
	public void setSessioned(boolean sessioned) {
		this.sessioned = sessioned;
	}

	/**
	 * @return the season
	 */
	public String getSeason() {
		return season;
	}

	/**
	 * @param season the season to set
	 */
	public void setSeason(String season) {
		this.season = season;
	}

}

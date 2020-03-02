/**
 * 
 */
package com.teamium.domain.prod.projects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.teamium.domain.Category;
import com.teamium.domain.Theme;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.resources.Asset;
import com.teamium.domain.prod.resources.Director;
import com.teamium.domain.prod.resources.contacts.Customer;

/**
 * Describe the abstract layer of a production project
 * 
 * @author sraybaud
 *
 */
@Entity
@NamedQueries({
		@NamedQuery(name = Quotation.QUERY_completeSubCategoryWithCategory, query = "SELECT DISTINCT ap.subCategory FROM AbstractProject ap WHERE ap.category = ?1 AND LOWER(ap.subCategory) LIKE ?2") })
public abstract class AbstractProject extends Record implements Comparable<AbstractProject> {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -5329569370070348730L;

	/**
	 * Name of the query retrieving the sub category for the given keyword and the
	 * given category
	 * 
	 * @param 1 the given keyword
	 * @return the list of sub category
	 */
	public static final String QUERY_completeSubCategoryWithCategory = "abstractProject_completeSubCategoryWithCategory";

	/**
	 * Agency
	 */
	@ManyToOne
	@JoinColumn(name = "c_project_idagency")
	private Customer agency;

	/**
	 * production place
	 */
	@Column(name = "c_prodaddress")
	private String prodAddress;

	/**
	 * production Organisation
	 */
	@Column(name = "c_prodorganisation")
	private String prodOrganisation;

	/**
	 * Final client
	 */
	@Column(name = "c_client")
	private String client;

	/**
	 * Director
	 */
	@ManyToOne
	@JoinColumn(name = "c_project_iddirector")
	private Director director;

	/**
	 * Project title
	 */
	@Column(name = "c_project_title")
	private String title;

	/**
	 * Project category
	 */
//	@Embedded
//	@AttributeOverride(name = "key", column = @Column(name = "c_project_category"))
//	private ProjectCategory category;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_category_id", nullable = false)
	private Category category;

	/**
	 * Project sub category
	 */
	@Column(name = "c_project_subcategory")
	private String subCategory;

	/**
	 * A TV show
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH,
			CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "c_program")
	private Program program;

	/**
	 * Asset inputs
	 */
	@ManyToMany
	@JoinTable(name = "t_input_asset", joinColumns = @JoinColumn(name = "c_idproject"), inverseJoinColumns = @JoinColumn(name = "c_idasset"))
	private List<Asset> inputs;

	/**
	 * Asset outputs
	 */
	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "c_idorigin")
	private List<Asset> outputs;

	/**
	 * Success ponderation
	 */
	@Column(name = "c_project_ponderation")
	private Float ponderation;

	@Transient
	private Long currentBookingId;

	@Transient
	private String selectedOrderType;

	/**
	 * The theme
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_theme"))
	private Theme theme;

	/**
	 * @return the agency
	 */
	public Customer getAgency() {
		return agency;
	}

	/**
	 * @param agency the agency to set
	 */
	public void setAgency(Customer agency) {
		this.agency = agency;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the director
	 */
	public Director getDirector() {
		return director;
	}

	/**
	 * @param director the director to set
	 */
	public void setDirector(Director director) {
		this.director = director;
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
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the subCategory
	 */
	public String getSubCategory() {
		return subCategory;
	}

	/**
	 * @param subCategory the subCategory to set
	 */
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	/**
	 * @return the program
	 */
	public Program getProgram() {
		return program;
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(Program program) {
		this.program = program;
	}

	/**
	 * @return the prodAddress
	 */
	public String getProdAddress() {
		return prodAddress;
	}

	/**
	 * @param prodAddress the prodAddress to set
	 */
	public void setProdAddress(String prodAddress) {
		this.prodAddress = prodAddress;
	}

	/**
	 * @return the prodOrganisation
	 */
	public String getProdOrganisation() {
		return prodOrganisation;
	}

	/**
	 * @param prodOrganisation the prodOrganisation to set
	 */
	public void setProdOrganisation(String prodOrganisation) {
		this.prodOrganisation = prodOrganisation;
	}

	/**
	 * @return the ponderation
	 */
	public Float getPonderation() {
		return ponderation;
	}

	/**
	 * @param ponderation the ponderation to set
	 */
	public void setPonderation(Float ponderation) {
		this.ponderation = ponderation;
	}

	/**
	 * @return the inputs
	 */
	public List<Asset> getInputs() {
		if (inputs == null)
			inputs = new ArrayList<Asset>();
		return inputs;
	}

	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(List<Asset> inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return the outputs
	 */
	public List<Asset> getOutputs() {
		if (outputs == null)
			outputs = new ArrayList<Asset>();
		return outputs;
	}

	/**
	 * @param outputs the outputs to set
	 */
	public void setOutputs(List<Asset> outputs) {
		this.outputs = outputs;
	}

	/**
	 * Add the given line to the project
	 * 
	 * @param line the line to add
	 * @return true if success, else returns false
	 */
	public boolean addLine(Line line) {
		if (line == null)
			return false;

		return super.addLine(line);
	}

	public Long getCurrentBookingId() {
		return currentBookingId;
	}

	public void setCurrentBookingId(Long currentBookingId) {
		this.currentBookingId = currentBookingId;
	}

	public String getSelectedOrderType() {
		return selectedOrderType;
	}

	public void setSelectedOrderType(String selectedOrderType) {
		this.selectedOrderType = selectedOrderType;
	}

	/**
	 * @return the theme
	 */
	public Theme getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public AbstractProject clone() throws CloneNotSupportedException {
		AbstractProject clone = null;
		Customer agency = this.agency;
		this.agency = null;
		Category category = this.category;
		this.category = null;
		Director director = this.director;
		this.director = null;
		List<Asset> inputs = new ArrayList<Asset>();
		this.inputs = null;
		List<Asset> outputs = new ArrayList<Asset>();
		this.outputs = null;
		try {
			clone = (AbstractProject) super.clone();
			clone.agency = agency;
			clone.category = category;
			clone.director = director;
			clone.inputs = inputs;
			clone.outputs = outputs;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.agency = agency;
			this.category = category;
			this.director = director;
			this.inputs = inputs;
			this.outputs = outputs;
		}
		return clone;
	}

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	public AbstractProject getCloneOfProgram(String title, Program program) throws CloneNotSupportedException {
		AbstractProject clone = null;
		Customer agency = this.agency;
		this.agency = null;
		Category category = this.category;
		this.category = null;
		Director director = this.director;
		this.director = null;
		List<Asset> inputs = new ArrayList<Asset>();
		this.inputs = null;
		List<Asset> outputs = new ArrayList<Asset>();
		this.outputs = null;
		try {
			clone = (AbstractProject) super.getCloneOfRecord(program);
			clone.setId(null);
			clone.agency = agency;
			clone.category = category;
			clone.director = director;
			clone.inputs = inputs;
			clone.outputs = outputs;
			clone.title = title;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.agency = agency;
			this.category = category;
			this.director = director;
			this.inputs = inputs;
			this.outputs = outputs;
		}
		return clone;
	}

	/**
	 * Compare the current object with the given one
	 * 
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given
	 *         is previous
	 */
	@Override
	public int compareTo(AbstractProject o) {
		if (o == null)
			return 1;
		if (this.title == null) {
			if (o.title != null) {
				return -1;
			}
		} else {
			int compare = this.title.compareTo(o.title);
			if (compare != 0)
				return compare;
		}
		if (this.getDate() == null) {
			if (o.getDate() != null) {
				return -1;
			}
		} else {
			if (o.getDate() != null) {
				int compare = this.getDate().compareTo(o.getDate());
				if (compare != 0)
					return compare;
			}
		}
		if (this.getId() == null) {
			if (o.getId() != null) {
				return -1;
			}
		} else {
			int compare = this.getId().compareTo(o.getId());
			if (compare != 0)
				return compare;
		}
		return 0;
	}

	/**
	 * Returns the string expression of a project
	 * 
	 * @return the text
	 */
	@Override
	public String toString() {
		return super.toString() + this.title + " for " + this.getCompany();
	}

}

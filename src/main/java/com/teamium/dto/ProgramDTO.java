package com.teamium.dto;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.constants.Constants;
import com.teamium.domain.Channel;
import com.teamium.domain.Document;
import com.teamium.domain.Theme;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.projects.Project;

/**
 * Wrapper class for Program
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProgramDTO extends AbstractProjectDTO {

	private String reference;
	private String title;
	private String minuteDuration;
	private Integer year;
	private DocumentDTO document;

	private ChannelDTO channel;
	private List<AbstractProjectDTO> linkedRecords;
	private String analyticAccountNumber;
	private Calendar start;
	private Integer nbEpisodes;
	private Integer nbSessions;
	private long pendingProjects;
	private long size;
	private boolean sessioned;
	private long totalPriceOfProjects;

	private String season;
	private String startString;
	private boolean removeDocument = false;

	public ProgramDTO() {

	}

	public ProgramDTO(AbstractProject abstractProject) {
		super(abstractProject);
	}

	public ProgramDTO(Program program, String title, String status, String recordDiscriminator, String theme) {
		if (program != null) {
			this.setId(program.getId());
			this.setTitle(title);
			this.setStatus(status);
			this.setRecordDiscriminator(recordDiscriminator);
			if (program.getDocument() != null) {
				this.setDocument(new DocumentDTO(program.getDocument()));
			}
			this.setTheme(theme);
			this.start = program.getStart();
			if (program.getStart() != null) {
				this.startString = new DateTime(program.getStart()).withZone(DateTimeZone.UTC).toString();
			}
			this.setSeason(program.getSeason());
		}
	}

	public ProgramDTO(Program program, String title, String status, String recordDiscriminator, long pendingProjects) {
		if (program != null) {
			this.setId(program.getId());
			this.setTitle(title);
			this.setStatus(status);
			this.setRecordDiscriminator(recordDiscriminator);
			this.setPendingProjects(pendingProjects);
			if (program.getDocument() != null) {
				this.document = new DocumentDTO(program.getDocument());
			}
			if (program.getTheme() != null) {
				this.setTheme(program.getTheme().getKey());
			}
			this.setSeason(program.getSeason());
			this.start = program.getStart();
			if (program.getStart() != null) {
				this.startString = new DateTime(program.getStart()).withZone(DateTimeZone.UTC).toString();
			}
		}
	}

	public ProgramDTO(String reference, String title, String minuteDuration, Integer year, DocumentDTO document,
			String analyticAccountNumber, Calendar start, Integer nbEpisodes, Integer nbSessions) {
		this.reference = reference;
		this.title = title;
		this.minuteDuration = minuteDuration;
		this.year = year;
		this.document = document;
		this.analyticAccountNumber = analyticAccountNumber;
		this.start = start;
		this.nbEpisodes = nbEpisodes;
		this.nbSessions = nbSessions;
	}

	public ProgramDTO(Program program) {
		super(program);
		this.setId(program.getId());
		this.reference = program.getReference();
		this.title = program.getTitle();
		if (program.getMinuteDuration() != null && program.getMinuteDuration().intValue() != 0) {
			int duration = program.getMinuteDuration().intValue();
			int hour = (duration / 60);
			int min = (duration % 60);
			this.minuteDuration = (hour != 0 ? String.valueOf(hour) : "00") + ":"
					+ (min != 0 ? String.valueOf(min) : "00");
		}
		this.year = program.getYear();
		Document document = program.getDocument();
		if (document != null) {
			this.document = new DocumentDTO(document);
		}
		Channel channel = program.getChannel();
		if (channel != null) {
			this.channel = new ChannelDTO(channel);
		}
		this.analyticAccountNumber = program.getAnalyticAccountNumber();
		this.start = program.getStart();
		this.nbEpisodes = program.getNbEpisodes();
		this.nbSessions = program.getNbSessions();

		List<AbstractProject> linkedRecords = program.getLinkedRecords();
		List<AbstractProjectDTO> linkedRecordsDtos = new ArrayList<>();
		List<Long> listSourceBudgets = new ArrayList<Long>();
		linkedRecords.forEach(r -> {
			if ((r instanceof Project) && r.getSource() != null) {
				listSourceBudgets.add(r.getSource().getId());
			}
		});
		// removing those budget who have bookings
		linkedRecords.forEach(r -> {
			if (r instanceof Project) {
				totalPriceOfProjects += r.getTotalPriceIVAT();
				linkedRecordsDtos.add(new AbstractProjectDTO(r, "For program", Constants.BOOKING_STRING));
			} else if (!listSourceBudgets.contains(r.getId())) {
				totalPriceOfProjects += r.getTotalPriceIVAT();
				linkedRecordsDtos.add(new AbstractProjectDTO(r, "For program", Constants.BUDGET_STRING));
			}

		});

		this.linkedRecords = linkedRecordsDtos;
		this.size = linkedRecords.size();

		if (program.getTheme() != null) {
			this.setTheme(program.getTheme().getKey());
		}

		this.setSessioned(program.isSessioned());
		this.season = program.getSeason();
		this.start = program.getStart();
		if (program.getStart() != null) {
			this.startString = new DateTime(program.getStart()).withZone(DateTimeZone.UTC).toString();
		}
	}

	public ProgramDTO(Program program, Boolean field) {
		if (field.booleanValue() == true) {
			this.setId(program.getId());
			this.setTitle(program.getTitle());
			this.setSessioned(program.isSessioned());
			this.start = program.getStart();
			if (program.getStart() != null) {
				this.startString = new DateTime(program.getStart()).withZone(DateTimeZone.UTC).toString();
			}
		}
	}

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
	public String getMinuteDuration() {
		return minuteDuration;
	}

	/**
	 * @param minuteDuration the minuteDuration to set
	 */
	public void setMinuteDuration(String minuteDuration) {
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
		return nbSessions;
	}

	/**
	 * @param nbSessions the nbSessions to set
	 */
	public void setNbSessions(Integer nbSessions) {
		this.nbSessions = nbSessions;
	}

	/**
	 * @return the document
	 */
	public DocumentDTO getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(DocumentDTO document) {
		this.document = document;
	}

	/**
	 * @return the channel
	 */
	public ChannelDTO getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(ChannelDTO channel) {
		this.channel = channel;
	}

	/**
	 * @return the linkedRecords
	 */
	public List<AbstractProjectDTO> getLinkedRecords() {
		return linkedRecords;
	}

	/**
	 * @param linkedRecords the linkedRecords to set
	 */
	public void setLinkedRecords(List<AbstractProjectDTO> linkedRecords) {
		this.linkedRecords = linkedRecords;
	}

	/**
	 * @return the pendingProjects
	 */
	public long getPendingProjects() {
		return pendingProjects;
	}

	/**
	 * @param pendingProjects the pendingProjects to set
	 */
	public void setPendingProjects(long pendingProjects) {
		this.pendingProjects = pendingProjects;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(long size) {
		this.size = size;
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

	/**
	 * To get Program
	 * 
	 * @param program
	 * 
	 * @return Program entity
	 * 
	 * @throws MalformedURLException
	 */
	public Program getProgram(Program program) throws MalformedURLException {
		setProgramDetail(program, this);
		return program;
	}

	/**
	 * To set program details
	 * 
	 * @param program
	 * 
	 * @param programDTO
	 * 
	 * @throws MalformedURLException
	 */
	private void setProgramDetail(Program program, ProgramDTO programDTO) throws MalformedURLException {
		program.setId(programDTO.getId());
		program.setReference(programDTO.getReference());
		program.setTitle(programDTO.getTitle());
		if (!StringUtils.isBlank(programDTO.getMinuteDuration())) {
			String[] mask = programDTO.getMinuteDuration().split(":");
			int duration = (Integer.parseInt(mask[0]) * 60) + (Integer.parseInt(mask[1]));
			program.setMinuteDuration(duration);
		}
		program.setYear(programDTO.getYear());

		if (programDTO.isRemoveDocument()) {
			program.setDocument(null);
		}

		// DocumentDTO documentDTO = programDTO.getDocument();
		// if (documentDTO != null) {
		// program.setDocument(documentDTO.getDocument());
		// } else {
		// program.setDocument(null);
		// }

		program.setAnalyticAccountNumber(programDTO.getAnalyticAccountNumber());

		if (!StringUtils.isBlank(programDTO.getStartString())) {
			DateTime start = DateTime.parse(programDTO.getStartString()).withZone(DateTimeZone.forID("Asia/Calcutta"));
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(start.getMillis());
			program.setStart(cal);
		}

		program.setNbEpisodes(programDTO.getNbEpisodes());
		program.setNbSessions(programDTO.getNbSessions());

		ChannelDTO channelDTO = programDTO.getChannel();
		if (channelDTO != null) {
			Channel channel = channelDTO.getChannel(new Channel());
			program.setChannel(channel);
		}

		if (!StringUtils.isBlank(programDTO.getTheme())) {
			Theme themeEntity = new Theme();
			themeEntity.setKey(programDTO.getTheme());
			program.setTheme(themeEntity);
		}

		program.setSeason(programDTO.getSeason());
		programDTO.setAbstractProjectDetail(program, programDTO);
	}

	/**
	 * @return the totalPriceOfProjects
	 */
	public long getTotalPriceOfProjects() {
		return totalPriceOfProjects;
	}

	/**
	 * @param totalPriceOfProjects the totalPriceOfProjects to set
	 */
	public void setTotalPriceOfProjects(long totalPriceOfProjects) {
		this.totalPriceOfProjects = totalPriceOfProjects;
	}

	/**
	 * @return the startString
	 */
	public String getStartString() {
		return startString;
	}

	/**
	 * @param startString the startString to set
	 */
	public void setStartString(String startString) {
		this.startString = startString;
	}

	/**
	 * @return the removeDocument
	 */
	public boolean isRemoveDocument() {
		return removeDocument;
	}

	/**
	 * @param removeDocument the removeDocument to set
	 */
	public void setRemoveDocument(boolean removeDocument) {
		this.removeDocument = removeDocument;
	}

}

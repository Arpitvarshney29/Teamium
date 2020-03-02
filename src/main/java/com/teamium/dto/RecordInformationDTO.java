package com.teamium.dto;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Person;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.RecordInformation;
import com.teamium.domain.prod.projects.Brief;

/**
 * Wrapper class for Record-Information
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RecordInformationDTO implements Serializable {

	private static final long serialVersionUID = 5804532494276140505L;

	private String length;
	private String format;
	private String version;
	private String comment;
	private Map<String, Boolean> status;
	private List<BriefDTO> briefs;
	private List<PersonDTO> programMembers;

	public RecordInformationDTO() {
	}

	public RecordInformationDTO(RecordInformation entity) {
		this.length = entity.getLength();
		this.format = entity.getFormat() == null ? null : entity.getFormat().getKey();
		this.version = entity.getVersion();
		this.comment = entity.getComment();
		// this.status = entity.getStatus();
		// if (entity.getBriefs() != null && !entity.getBriefs().isEmpty()) {
		// this.briefs = entity.getBriefs().stream().map(brief -> new
		// BriefDTO(brief)).collect(Collectors.toList());
		// }
		// if (entity.getProgramMembers() != null &&
		// !entity.getProgramMembers().isEmpty()) {
		// List<PersonDTO> personEntity = entity.getProgramMembers().stream().map(person
		// -> new PersonDTO(person))
		// .collect(Collectors.toList());
		// this.programMembers = personEntity;
		// }
	}

	public RecordInformationDTO(String length, String format, String version, String comment,
			Map<String, Boolean> status, List<BriefDTO> briefs, List<PersonDTO> programMembers) {
		super();
		this.length = length;
		this.format = format;
		this.version = version;
		this.comment = comment;
		this.status = status;
		this.briefs = briefs;
		this.programMembers = programMembers;
	}

	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(String length) {
		this.length = length;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the status
	 */
	public Map<String, Boolean> getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Map<String, Boolean> status) {
		this.status = status;
	}

	/**
	 * @return the briefs
	 */
	public List<BriefDTO> getBriefs() {
		return briefs;
	}

	/**
	 * @param briefs
	 *            the briefs to set
	 */
	public void setBriefs(List<BriefDTO> briefs) {
		this.briefs = briefs;
	}

	/**
	 * @return the programMembers
	 */
	public List<PersonDTO> getProgramMembers() {
		return programMembers;
	}

	/**
	 * @param programMembers
	 *            the programMembers to set
	 */
	public void setProgramMembers(List<PersonDTO> programMembers) {
		this.programMembers = programMembers;
	}

	public RecordInformation getRecordInformation(RecordInformation recordInformation,
			RecordInformationDTO recordInformationDTO) {
		setRecordInformationDetail(recordInformation, recordInformationDTO);
		return recordInformation;
	}

	public void setRecordInformationDetail(RecordInformation recordInformation,
			RecordInformationDTO recordInformationDTO) {

		recordInformation.setLength(recordInformationDTO.getLength());
		recordInformation.setVersion(recordInformationDTO.getVersion());
		recordInformation.setComment(recordInformationDTO.getComment());

		if (!StringUtils.isBlank(recordInformationDTO.getFormat())) {
			XmlEntity formatEntity = new XmlEntity();
			formatEntity.setKey(recordInformationDTO.getFormat());
			recordInformation.setFormat(formatEntity);
		}
		// if (recordInformationDTO.getBriefs() != null &&
		// !recordInformationDTO.getBriefs().isEmpty()) {
		// recordInformation.setBriefs(recordInformationDTO.getBriefs().stream()
		// .map(briefDTO -> briefDTO.getResourceInformation(new
		// Brief())).collect(Collectors.toList()));
		// }

		// recordInformation.setStatus(recordInformationDTO.getStatus());

		// if (recordInformationDTO.getProgramMembers() != null &&
		// !recordInformationDTO.getProgramMembers().isEmpty()) {
		// List<Person> personEntity =
		// recordInformationDTO.getProgramMembers().stream().map(personDTO -> {
		// try {
		// return personDTO.getPerson(new Person(), personDTO);
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// }
		// return null;
		// }).collect(Collectors.toList());
		// recordInformation.setProgramMembers(personEntity);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RecordInformationDTO [length=" + length + ", format=" + format + ", version=" + version + ", comment="
				+ comment + ", status=" + status + ", briefs=" + briefs + ", programMembers=" + programMembers + "]";
	}

}

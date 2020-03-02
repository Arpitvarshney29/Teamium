package com.teamium.dto;

import java.net.MalformedURLException;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Document;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.Media;

public class MediaDTO extends AbstractDTO {

	private Long id;
	private String length;
	private String label;
	private XmlEntity format;
	private XmlEntity csa;
	private DocumentDTO file;
	// private Asset asset;
	private XmlEntity localization;
	private String umid;

	public MediaDTO() {

	}

	public MediaDTO(AbstractEntity entity) {
		super(entity);
	}

	public MediaDTO(String length, String label, XmlEntity format, XmlEntity csa, DocumentDTO file,
			XmlEntity localization, String umid) {
		this.length = length;
		this.label = label;
		this.format = format;
		this.csa = csa;
		this.file = file;
		this.localization = localization;
		this.umid = umid;
	}

	public MediaDTO(Media media) {
		this.id = media.getId();
		this.length = media.getLength();
		this.label = media.getLabel();
		this.format = media.getFormat();
		this.csa = media.getCsa();

		Document document = media.getDocument();
		if (document != null) {
			this.file = new DocumentDTO(document);
		}
		this.localization = media.getLocalization();
		this.umid = media.getUmid();
	}

	public Media getMedia(Media media, MediaDTO mediaDTO) throws MalformedURLException {
		setMediaDetail(media, mediaDTO);
		return media;
	}

	public void setMediaDetail(Media media, MediaDTO mediaDTO) throws MalformedURLException {
		media.setId(mediaDTO.getId());
		media.setVersion(mediaDTO.getVersion());
		media.setLength(mediaDTO.getLength());
		media.setLabel(mediaDTO.getLabel());
		media.setFormat(mediaDTO.getFormat());
		media.setCsa(mediaDTO.getCsa());

		DocumentDTO documentDTO = mediaDTO.getFile();
		if (documentDTO != null) {
			media.setDocument(documentDTO.getDocument());
		}
		media.setLocalization(mediaDTO.getLocalization());
		media.setUmid(mediaDTO.getUmid());
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the format
	 */
	public XmlEntity getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(XmlEntity format) {
		this.format = format;
	}

	/**
	 * @return the csa
	 */
	public XmlEntity getCsa() {
		return csa;
	}

	/**
	 * @param csa
	 *            the csa to set
	 */
	public void setCsa(XmlEntity csa) {
		this.csa = csa;
	}

	/**
	 * @return the file
	 */
	public DocumentDTO getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(DocumentDTO file) {
		this.file = file;
	}

	/**
	 * @return the localization
	 */
	public XmlEntity getLocalization() {
		return localization;
	}

	/**
	 * @param localization
	 *            the localization to set
	 */
	public void setLocalization(XmlEntity localization) {
		this.localization = localization;
	}

	/**
	 * @return the umid
	 */
	public String getUmid() {
		return umid;
	}

	/**
	 * @param umid
	 *            the umid to set
	 */
	public void setUmid(String umid) {
		this.umid = umid;
	}

}

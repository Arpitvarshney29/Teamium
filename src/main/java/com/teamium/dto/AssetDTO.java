package com.teamium.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Document;
import com.teamium.domain.prod.Comment;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.resources.Asset;

/**
 * Wrapper class for Asset
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AssetDTO extends AbstractDTO {

	private Long id;
	private String title;
	private DocumentDTO thumbnail;
	private String guid;
	private String description;
	// private List<Comment> comments;

	public AssetDTO() {

	}

	public AssetDTO(Asset asset) {
		this.id = asset.getId();
		this.title = asset.getTitle();
		Document document = asset.getThumbnail();
		if (document != null) {
			this.thumbnail = new DocumentDTO(document);
		}
		this.guid = asset.getGuid();
		this.description = asset.getDescription();
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the thumbnail
	 */
	public DocumentDTO getThumbnail() {
		return thumbnail;
	}

	/**
	 * @param thumbnail
	 *            the thumbnail to set
	 */
	public void setThumbnail(DocumentDTO thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid
	 *            the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * To get asset
	 * 
	 * @param asset
	 *            the asset
	 * 
	 * @param assetDTO
	 *            the assetDTO
	 * 
	 * @return asset object
	 */
	public Asset getAsset(Asset asset, AssetDTO assetDTO) {
		setAssetDetail(asset, assetDTO);
		return asset;
	}

	/**
	 * To set asset details
	 * 
	 * @param asset
	 *            the asset
	 * 
	 * @param assetDTO
	 *            the assetDTO
	 */
	public void setAssetDetail(Asset asset, AssetDTO assetDTO) {
		asset.setId(assetDTO.getId());
		asset.setVersion(assetDTO.getVersion());
		asset.setTitle(assetDTO.getTitle());
		asset.setDescription(assetDTO.getDescription());
		asset.setGuid(assetDTO.getGuid());
		DocumentDTO documentDTO = assetDTO.getThumbnail();
		if (documentDTO != null) {
			Document document = documentDTO.getDocument();
			asset.setThumbnail(document);
		}
	}

}

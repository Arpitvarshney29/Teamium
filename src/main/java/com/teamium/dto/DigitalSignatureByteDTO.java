package com.teamium.dto;

public class DigitalSignatureByteDTO {

	private long staffId;
	private byte[] signatureByte;
	private boolean editable;
	private String absolutefilePath;

	public DigitalSignatureByteDTO() {

	}

	public DigitalSignatureByteDTO(long staffId, byte[] signatureByte, boolean editable, String absolutefilePath) {
		this.staffId = staffId;
		this.signatureByte = signatureByte;
		this.editable = editable;
		this.absolutefilePath = absolutefilePath;
	}

	/**
	 * 
	 * @return
	 */
	public long getStaffId() {
		return staffId;
	}

	/**
	 * 
	 * @param staffId
	 */
	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	/**
	 * 
	 * @return
	 */
	public byte[] getSignatureByte() {
		return signatureByte;
	}

	/**
	 * 
	 * @param signatureByte
	 */
	public void setSignatureByte(byte[] signatureByte) {
		this.signatureByte = signatureByte;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * 
	 * @param editable
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}

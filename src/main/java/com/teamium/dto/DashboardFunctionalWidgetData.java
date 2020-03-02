package com.teamium.dto;

import java.text.DecimalFormat;

public class DashboardFunctionalWidgetData {
	private long totalInventoryCount;
	private long totalExternalUsesCount;
	private long totalInternalUsesCount;
	private float totalExternalUsesCountPercent;
	private float totalInternalUsesCountPercent;
	private Double totalOutsourcedAmount;

	public DashboardFunctionalWidgetData() {

	}

	public DashboardFunctionalWidgetData(long totalInventoryCount, long totalExternalUsesCount,
			long totalInternalUsesCount, Double totalOutsourcedAmount) {
		DecimalFormat dec = new DecimalFormat("##.##");
		this.totalInventoryCount = totalInventoryCount;
		this.totalExternalUsesCount = totalExternalUsesCount;
		this.totalInternalUsesCount = totalInternalUsesCount;
		this.totalOutsourcedAmount = totalOutsourcedAmount == null || totalOutsourcedAmount == 0 ? 0.0
				: Double.parseDouble(dec.format(totalOutsourcedAmount));

		this.totalExternalUsesCountPercent = this.totalExternalUsesCount != 0 && this.totalInventoryCount != 0
				? Float.parseFloat(dec.format(((float) this.totalExternalUsesCount / this.totalInventoryCount) * 100))
				: 0.0F;
		this.totalInternalUsesCountPercent = this.totalInternalUsesCount != 0 && this.totalInventoryCount != 0
				? Float.parseFloat(dec.format(((float) this.totalInternalUsesCount / this.totalInventoryCount) * 100))
				: 0.0F;
	}

	/**
	 * @return the totalInventoryCount
	 */
	public long getTotalInventoryCount() {
		return totalInventoryCount;
	}

	/**
	 * @param totalInventoryCount the totalInventoryCount to set
	 */
	public void setTotalInventoryCount(long totalInventoryCount) {
		this.totalInventoryCount = totalInventoryCount;
	}

	/**
	 * @return the totalExternalUsesCount
	 */
	public long getTotalExternalUsesCount() {
		return totalExternalUsesCount;
	}

	/**
	 * @param totalExternalUsesCount the totalExternalUsesCount to set
	 */
	public void setTotalExternalUsesCount(long totalExternalUsesCount) {
		this.totalExternalUsesCount = totalExternalUsesCount;
	}

	/**
	 * @return the totalInternalUsesCount
	 */
	public long getTotalInternalUsesCount() {
		return totalInternalUsesCount;
	}

	/**
	 * @param totalInternalUsesCount the totalInternalUsesCount to set
	 */
	public void setTotalInternalUsesCount(long totalInternalUsesCount) {
		this.totalInternalUsesCount = totalInternalUsesCount;
	}

	/**
	 * @return the totalOutsourcedAmmount
	 */
	public Double getTotalOutsourcedAmount() {
		return totalOutsourcedAmount;
	}

	/**
	 * @param totalOutsourcedAmount the totalOutsourcedAmmount to set
	 */
	public void setTotalOutsourcedAmount(Double totalOutsourcedAmount) {
		this.totalOutsourcedAmount = totalOutsourcedAmount;
	}

	/**
	 * @return the totalExternalUsesCountPercent
	 */
	public float getTotalExternalUsesCountPercent() {
		return totalExternalUsesCountPercent;
	}

	/**
	 * @param totalExternalUsesCountPercent the totalExternalUsesCountPercent to set
	 */
	public void setTotalExternalUsesCountPercent(float totalExternalUsesCountPercent) {
		this.totalExternalUsesCountPercent = totalExternalUsesCountPercent;
	}

	/**
	 * @return the totalInternalUsesCountPercent
	 */
	public float getTotalInternalUsesCountPercent() {
		return totalInternalUsesCountPercent;
	}

	/**
	 * @param totalInternalUsesCountPercent the totalInternalUsesCountPercent to set
	 */
	public void setTotalInternalUsesCountPercent(float totalInternalUsesCountPercent) {
		this.totalInternalUsesCountPercent = totalInternalUsesCountPercent;
	}

}

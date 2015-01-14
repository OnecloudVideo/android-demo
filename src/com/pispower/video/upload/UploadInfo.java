package com.pispower.video.upload;

public class UploadInfo {
	// 上传状态的枚举UploadStatus对象
	private UploadStatus uploadStatus;

	private int currentValue;

	private int maxValue;

	/**
	 * 无参构造方法
	 */
	public UploadInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 有参构造方法
	 * 
	 * @param uploadStatus
	 * @param currentValue
	 * @param maxValue
	 */
	public UploadInfo(UploadStatus uploadStatus, int currentValue, int maxValue) {
		super();
		this.uploadStatus = uploadStatus;
		this.currentValue = currentValue;
		this.maxValue = maxValue;
	}

	/**
	 * 获取长传状态
	 * 
	 * @return uploadStatus
	 */
	public UploadStatus getUploadStatus() {
		return uploadStatus;
	}

	/**
	 * 设置上传状态
	 * 
	 * @param uploadStatus
	 */
	public void setUploadStatus(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	/**
	 * 获取当前值
	 * 
	 * @return currentValue
	 */
	public int getCurrentValue() {
		return currentValue;
	}

	/**
	 * 设置当前值
	 * 
	 * @param currentValue
	 */
	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * 获取最大值
	 * 
	 * @return maxValue
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * 设置最大值
	 * 
	 * @param maxValue
	 */
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

}

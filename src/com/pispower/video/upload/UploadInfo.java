package com.pispower.video.upload;

public class UploadInfo {
	// 上传状态的枚举UploadStatus对象
	private UploadStatus uploadStatus;

	private long currentValue;

	/**
	 * 无参构造方法
	 */
	public UploadInfo() {
		super();
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
	public long getCurrentValue() {
		return currentValue;
	}

	/**
	 * 设置当前值
	 * 
	 * @param currentValue
	 */
	public void setCurrentValue(long currentValue) {
		this.currentValue = currentValue;
	}

}

package com.pispower.video;

import java.util.Map;

import com.pispower.video.upload.UploadInfo;

public class VideoInfo {

	// 视频id
	private String id;
	// 视频的名字
	private String name;
	// 视频的大小
	private String size;
	// 视频的状态
	private String status;
	
	// 清晰度与对应url的map
	private Map<String, String> clarityUrlMap;

	// 视频上传的信息
	public UploadInfo uploadInfo;

	/**
	 * 有参构造方法，根据uploadInfo来构造对象
	 * 
	 * @param uploadInfo
	 */
	public VideoInfo(UploadInfo uploadInfo) {
		this.uploadInfo = uploadInfo;
	}

	/**
	 * 无参构造方法
	 */
	public VideoInfo() {
		super();
	}


	public VideoInfo(String id, String name, String size, String status,
			Map<String, String> clarityUrlMap) {
		super();
		this.id = id;
		this.name = name;
		this.size = size;
		this.status = status;
		this.clarityUrlMap = clarityUrlMap;
	}

	/**
	 * 获取上传的信息
	 * 
	 * @return uploadInfo
	 */
	public UploadInfo getUploadInfo() {
		return uploadInfo;
	}

	/**
	 * 获取id
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取名字
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名字
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取大小
	 * 
	 * @return size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * 
	 * 设置大小
	 * 
	 * @param size
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * 
	 * 获取状态
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * 设置状态
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * 
	 * 获取清晰度与对应的url的map
	 * 
	 * @return
	 */
	public Map<String, String> getClarityUrlMap() {
		return clarityUrlMap;
	}

	/**
	 * 设置清晰度与对应的url的map
	 * 
	 * @param clarityUrlMap
	 */
	public void setClarityUrlMap(Map<String, String> clarityUrlMap) {
		this.clarityUrlMap = clarityUrlMap;
	}

}

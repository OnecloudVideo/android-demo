package com.pispower.catalog;

import java.io.Serializable;

public class CatalogInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 分类所包含的视频数
	private String holdVideoNums;
	// 分类id
	private String id;
	// 分类的最近修改时间
	private String lastModifiedTime;
	// 分类的名字
	private String name;

	/**
	 * 无参构造方法
	 */
	public CatalogInfo() {
	}

	/**
	 * 有参构造，使用所需参数来构造实例对象
	 * 
	 * @param id
	 * @param name
	 * @param modifiedTime
	 * @param videoNums
	 */
	public CatalogInfo(String id, String name, String modifiedTime,
			String videoNums) {
		this.id = id;
		this.name = name;
		this.lastModifiedTime = modifiedTime;
		this.holdVideoNums = videoNums;
	}

	/**
	 * 获取视频数
	 * 
	 * @return holdVideoNums
	 */
	public String getHoldVideoNums() {
		return holdVideoNums;
	}

	/**
	 * 设置视频数
	 * 
	 * @param holdVideoNums
	 */
	public void setHoldVideoNums(String holdVideoNums) {
		this.holdVideoNums = holdVideoNums;
	}

	/**
	 * 获得id
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
	 * 获取最近的修改时间
	 * 
	 * @return lastModifiedTime
	 */
	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * 设置最近修改时间
	 * 
	 * @param lastModifiedTime
	 */
	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
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
	 * 
	 * 设置名字
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
   
		 
}

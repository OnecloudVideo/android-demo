/*
 * File Name  : VideoTest.java
 * Authors    : Keijack*
 * Stage      : Implementation
 * Created    : Jul 2, 2014 4:28:37 PM
 * Copyright  : Copyright 漏 2009 OneCloud Co., Ltd.  All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * OneCloud Co., Ltd. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with OneCloud.
 */

package com.pispower.network;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.pispower.util.QueryString;

public class VideoClient {

	private static final String Tag = VideoClient.class.getName();

	final private BaseClient baseClient = new BaseClient();

	private BaseClient getBaseClient() {
		return this.baseClient;
	}

	/**
	 * 列出所有的分类
	 * 
	 * @return JSONArray 对象
	 * @throws ParseException
	 * @throws IOException
	 * @throws JSONException
	 */
	public JSONArray listCatalog() throws ParseException, IOException,
			JSONException {
		BaseClient client = this.getBaseClient();
		final QueryString queryString = new QueryString();
		final JSONObject json = client.get("/catalog/list.api", queryString);
		if (json.length() == 0) {
			Log.i(Tag, "JSONObject is empty");
			return null;
		}
		int statusCode = (int) json.getInt("statusCode");
		if (statusCode != 0) {
			Log.i(Tag, "statusCode is not zero");
			return null;
		}
		JSONArray catalogs = json.getJSONArray("catalogs");

		if (catalogs.length() == 0) {
			Log.i(Tag, "catalogs is empty");
			return null;
		}
		return catalogs;
	}

	/**
	 * 获得指定的分类
	 * 
	 * @param catalogId
	 * @return JSONObject 对象
	 * @throws JSONException
	 * @throws ParseException
	 * @throws IOException
	 */
	public JSONObject getCatalog(String catalogId) throws JSONException,
			ParseException, IOException {
		BaseClient client = getBaseClient();
		QueryString queryString = new QueryString();
		queryString.addParam("catalogId", catalogId);
		JSONObject specialCatalogJson;
		specialCatalogJson = client.get("/catalog/get.api", queryString);
		if ((int) specialCatalogJson.getInt("statusCode") != 0) {
			Log.i(Tag, "statusCode is not zero");
			return null;
		}
		if (specialCatalogJson.length() == 0) {
			Log.i(Tag, "return json is empty");
			return null;
		}
		return specialCatalogJson;
	}

	/**
	 * 获得分类下的所有视频信息
	 * 
	 * @param catalogId
	 * @return JSONArray 对象
	 * @throws JSONException
	 * @throws ParseException
	 * @throws IOException
	 */
	public JSONArray listVideo(String catalogId) throws JSONException,
			ParseException, IOException {
		BaseClient client = getBaseClient();
		QueryString qs = new QueryString();
		qs.addParam("catalogId", catalogId);
		JSONObject jo;
		jo = client.get("/video/list.api", qs);
		if ((int) jo.getInt("statusCode") != 0) {
			Log.i(Tag, "/video/list return statusCode is not zero");
			return null;
		}
		if (jo.length() == 0) {
			Log.i(Tag, "/video/list return json is empty");
			return null;
		}
		JSONArray jsonArrayVideos = jo.getJSONArray("videos");
		return jsonArrayVideos;
	}

	/**
	 * 创建指定的分类
	 * 
	 * @param fileName
	 * @return
	 * @throws ParseException
	 * @throws JSONException
	 * @throws IOException
	 */
	public JSONObject createCatalog(String fileName) throws ParseException,
			JSONException, IOException {
		BaseClient client = getBaseClient();
		QueryString qs = new QueryString();
		qs.addParam("name", fileName);
		JSONObject jsonObject = client.post("/catalog/create.api", qs);
		if (jsonObject.getInt("statusCode") == 0) {
			return jsonObject;
		}
		return null;
	}

	/**
	 * 获取指定视频指定的分配率的EmbedCode
	 * 
	 * @param videoId
	 * @param clarity
	 * @return
	 * @throws JSONException
	 * @throws ParseException
	 * @throws IOException
	 */
	public String getVideoEmbedCode(String videoId, String clarity)
			throws JSONException, ParseException, IOException {
		BaseClient client = getBaseClient();
		QueryString qs = new QueryString();
		qs.addParam("videoId", videoId);
		JSONObject jo;

		jo = client.get("/video/get.api", qs);
		if ((int) jo.getInt("statusCode") != 0) {
			Log.i(Tag, "/video/get.api return statusCode is not zero");
			return null;
		}
		if (jo.length() == 0) {
			Log.i(Tag, "/video/get.api return json is empty");
			return null;
		}
		JSONArray jsonArrayVideoCodes = jo.getJSONArray("embedCodes");
		int embedCodesNums = jsonArrayVideoCodes.length();
		if (embedCodesNums == 0) {
			Log.i(Tag, "embedCodes is empty");
			return null;
		}
		JSONObject needVideoCode = null;
		for (int i = 0; i < embedCodesNums; i++) {
			JSONObject js = jsonArrayVideoCodes.getJSONObject(i);
			if (js.getString("clarity").equals(clarity)) {
				needVideoCode = js;
				break;
			}
		}
		if (needVideoCode == null) {
			Log.i(Tag, clarity + " embedCode is empty");
			return null;
		}
		String android_IOS_embedCode = needVideoCode.getString("html5Code");
		return android_IOS_embedCode;
	}

	/**
	 * 从EmbedCode中获得url
	 * 
	 * @param embedCode
	 * @return 字符串
	 */
	public String getUrlFromEmbedCode(String embedCode) {
		if (embedCode == null) {
			return null;
		}
		Pattern p = Pattern.compile("src='http:[\\w/\\.]+'");
		Matcher m = p.matcher(embedCode);
		if (m.find()) {
			String src = m.group();
			src = src.substring(5, src.length() - 1);
			return src;
		}
		return null;
	}

}

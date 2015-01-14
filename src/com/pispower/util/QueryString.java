package com.pispower.util;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class QueryString {

	private final Map<String, String[]> paramMap = new LinkedHashMap<String, String[]>();

	/**
	 * 无参构造方法
	 */
	public QueryString() {
		super();
	}

	/**
	 * 有参构造方法
	 * 
	 * @param queryString
	 */
	public QueryString(final String queryString) {
		super();
		if (StringUtil.isBlank(queryString)) {
			return;
		}
		final String qstr;
		if (queryString.startsWith("?")) {
			qstr = queryString.substring(1);
		} else {
			qstr = queryString;
		}
		initParamMap(qstr);
	}

	private void initParamMap(String queryString) {
		paramMap.clear();
		final String[] keyAndVals = queryString.split("&");
		for (final String keyAndVal : keyAndVals) {

			if (StringUtil.isBlank(keyAndVal)) {
				continue;
			}
			final String key = getKey(keyAndVal);
			final String val = getVal(keyAndVal);
			this.addKeyValToParamMap(key, val);
		}
	}

	private void addKeyValToParamMap(String key, String val) {
		if (!this.paramMap.containsKey(key)) {
			final String[] values = { val };
			this.paramMap.put(key, values);
		} else {
			final String[] values = this.paramMap.get(key);
			this.paramMap.put(key, (String[]) ArraysUtil.add(values, val));
		}

	}

	/**
	 * 有参构造方法
	 * 
	 * @param parameterMap
	 */
	public QueryString(final Map<String, String[]> parameterMap) {
		this.paramMap.putAll(parameterMap);

	}

	private String getVal(String keyAndVal) {

		String val;
		final int index = keyAndVal.indexOf("=");
		if (index < 0) {
			val = "";
		} else {
			val = keyAndVal.substring(index + 1);
		}
		return val;
	}

	private String getKey(String keyAndVal) {

		String key;
		final int index = keyAndVal.indexOf("=");
		if (index < 0) {
			key = keyAndVal;
		} else {
			key = keyAndVal.substring(0, index);
		}

		return key;
	}

	/**
	 * 获取key所对应的parameters中的第一个parameter
	 * 
	 * @param key
	 * @return parameter
	 */
	public String getParam(final String key) {

		final String[] paramVals = this.getParamVal(key);
		if (paramVals == null || paramVals.length == 0) {
			return null;
		} else {
			return paramVals[0];
		}
	}

	/**
	 * 获取key所对应的所有parameters
	 * 
	 * @param key
	 * @return 数组
	 */
	public String[] getParamVal(String key) {

		return this.paramMap.get(key);
	}

	/**
	 * 获取key parameters的map对象
	 * 
	 * @return map对象
	 */
	public Map<String, String[]> getParamMap() {
		return paramMap;
	}

	/**
	 * 获取排序后的key parameters的map对象
	 * 
	 * @return map对象
	 */
	public Map<String, String[]> getSortedParamMap() {
		final String[] ParamNames = this.paramMap.keySet().toArray(
				new String[this.paramMap.keySet().size()]);
		Arrays.sort(ParamNames);
		final Map<String, String[]> sortedParamMap = new LinkedHashMap<String, String[]>();
		for (final String paramName : ParamNames) {
			final String[] valus = this.paramMap.get(paramName).clone();
			Arrays.sort(valus);
			sortedParamMap.put(paramName, valus);
		}
		return sortedParamMap;
	}

	/**
	 * 获取key-parameter对的map对象
	 * 
	 * @return
	 */
	public Map<String, String> getSingleValueParamMap() {
		final Map<String, String> singleValueMap = new LinkedHashMap<String, String>();
		for (final String key : this.paramMap.keySet()) {
			singleValueMap.put(key, this.getParam(key));
		}
		return singleValueMap;
	}

	/**
	 * 获取所有的所有的key值
	 * 
	 * @return
	 */
	public Enumeration<String> getParamNames() {
		return new Vector<String>(this.paramMap.keySet()).elements();
	}

	/**
	 * 添加key -parameter对
	 * 
	 * @param key
	 * @param param
	 */
	public void addParam(final String key, final String param) {
		addKeyValToParamMap(key, param);
	}

	/**
	 * 移除指定key的key parameter对
	 * 
	 * @param key
	 */
	public void removeParam(final String key) {
		if (!this.paramMap.containsKey(key)) {
			return;
		}
		this.paramMap.remove(key);
	}

	private String combineQueryString(final Map<String, String[]> paramMap) {
		String queryString = "";
		for (final String key : paramMap.keySet()) {
			final String[] vals = paramMap.get(key);
			for (final String val : vals) {
				if (!queryString.equals("")) {
					queryString += "&";
				}
				queryString += key + "=" + val;
			}

		}
		return queryString;
	}

	/**
	 * 是否包含输入的QueryString，是返回为true，否则返回为false
	 * 
	 * @param queryString
	 * @return
	 */
	public boolean conains(final QueryString queryString) {
		boolean isContains = true;
		for (final String key : queryString.getParamMap().keySet()) {
			if (!this.paramMap.containsKey(key)) {
				isContains = false;
				break;
			}
			final List<String> valuesInThisObject = Arrays.asList(this.paramMap
					.get(key));
			final List<String> valuesInOtherObject = Arrays.asList(queryString
					.getParamMap().get(key));
			if (!valuesInThisObject.containsAll(valuesInOtherObject)) {
				isContains = false;
				break;
			}
		}
		return isContains;
	}

	@Override
	public String toString() {
		return getQueryString();
	}

	/**
	 * 获取QueryString
	 * 
	 * @return 字符串
	 */
	public String getQueryString() {
		return this.combineQueryString(paramMap);
	}

	@Override
	public QueryString clone() {
		return new QueryString(this.toString());
	}
}

package com.pispower.network;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.pispower.network.SimpleMultipartEntity.ProcessListener;
import com.pispower.util.MD5;
import com.pispower.util.QueryString;
import com.pispower.video.upload.UploadStatus;

public class BaseClient {

	private Handler uploadingHander = null;

	private static final String TAG = BaseClient.class.getName();
	/**
	 * 视频平台的地址。
	 */
	private final String apiHost = "http://video.pispower.com";

	/**
	 * 视频平台提供的 accessKey，该字段会通过公网传输。
	 * 
	 * 请从ovp 开发者支持/Restful API页面获取
	 */
	private final String accessKey = "7h9ft6i3txnc9t";

	/**
	 * 视频平台提供的 accessSecret， 该字段用来生成数字签名，注意保密，并且不要通过公网传输。
	 * 
	 * 请从ovp 开发者支持/Restful API页面获取
	 */
	private final String accessSecret = "8f4a5d833d0e2d31e1eda4c811d2ae32";

	public BaseClient() {

	}

	public BaseClient(Handler uploadingHander) {
         this.uploadingHander=uploadingHander;
	}

	/**
	 * 
	 * 视频平台中要求为 GET 的接口使用的方法。
	 * 
	 * @param apiContext
	 * @param queryString
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws ParseException
	 */
	public JSONObject get(final String apiContext, final QueryString queryString)
			throws IOException, ParseException, JSONException {
		return excute(HttpGet.METHOD_NAME, apiContext, queryString);
	}

	/**
	 * 
	 * 视频平台要求为 POST 的接口使用的方法。
	 * 
	 * @param apiContext
	 * @param queryString
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws ParseException
	 */
	public JSONObject post(final String apiContext,
			final QueryString queryString) throws IOException, ParseException,
			JSONException {
		return excute(HttpPost.METHOD_NAME, apiContext, queryString);
	}

	/**
	 * 
	 * 提交一个方法，并且解析视频平台传回的 JSON 到 JSONObject。
	 * 
	 * 
	 * @param requestBuilder
	 * @param apiContext
	 * @param queryString
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws ParseException
	 */
	private JSONObject excute(final String method, final String apiContext,
			final QueryString queryString) throws IOException, ParseException,
			JSONException {
		final URI context = addRequestParams(apiContext, queryString);
		final HttpClient httpClient = this.getDefaultHttpClient();
		HttpRequestBase httpGet = getHttpRequestBase(method, context);
		httpGet.addHeader("Accept", "application/json");
		httpGet.setHeader("Content-Type", "application/json");
		HttpResponse httpResponse = httpClient.execute(httpGet);
		JSONObject jsonObject = getJSONFromResponse(httpResponse);
		httpClient.getConnectionManager().shutdown();
		return jsonObject;
	}

	private HttpRequestBase getHttpRequestBase(String methodName, final URI uri) {
		HttpRequestBase httpRequestBase = null;
		if (methodName.equals(HttpGet.METHOD_NAME)) {
			httpRequestBase = new HttpGet();
		}
		if (methodName.equals(HttpPost.METHOD_NAME)) {
			httpRequestBase = new HttpPost();
		}
		if (httpRequestBase == null) {
			Log.w(TAG, "httpRequestBase is null");
			throw new NullPointerException("httpRequestBase is null");
			// return null;
		}
		httpRequestBase.setURI(uri);
		Log.d(TAG, "url is: " + httpRequestBase.getURI().toString());
		return httpRequestBase;
	}

	private String formatContext(final String apiContext) {
		if (apiContext.startsWith("/")) {
			return apiContext;
		} else {
			return "/" + apiContext;
		}
	}

	/**
	 * 
	 * 将从视频平台返回的数据转换为JSON对象。
	 * 
	 * @param res
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws ParseException
	 */
	private JSONObject getJSONFromResponse(final HttpResponse res)
			throws IOException, ParseException, JSONException {

		if (res.getStatusLine().getStatusCode() != 200) {
			throw new IOException("error: code is "
					+ res.getStatusLine().getStatusCode());
		}

		HttpEntity httpEntity = res.getEntity();
		String stringJson = EntityUtils.toString(httpEntity, "utf-8");
		JSONObject json = new JSONObject(stringJson);
		return json;

	}

	/**
	 * 
	 * 增加平台规定的必传参数：accessKey、time、sign
	 * 
	 * @param queryString
	 * @return
	 */
	private QueryString addAditionParameters(final QueryString queryString) {
		final QueryString aQueryString = queryString.clone();
		aQueryString.addParam("accessKey", accessKey);
		aQueryString.addParam("time",
				String.valueOf(System.currentTimeMillis()));

		final String sign = getSign(aQueryString);
		aQueryString.addParam("sign", sign);
		return aQueryString;
	}

	/**
	 * 计算数字签名。
	 * 
	 * @param queryString
	 * @return
	 */
	private String getSign(final QueryString queryString) {
		String signOri = this.accessSecret;
		for (final Map.Entry<String, String[]> entry : queryString
				.getSortedParamMap().entrySet()) {
			final String key = entry.getKey();
			for (final String val : entry.getValue()) {
				signOri += key + val;
			}
		}
		signOri += accessSecret;
		try {
			return MD5.getMD5(signOri);
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, e.getMessage());
		}
		return signOri;
	}

	/**
	 * 
	 * 上传文件到视频平台
	 * 
	 * @param apiContext
	 * @param queryString
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws ParseException
	 */
	public JSONObject postFile(final String apiContext,
			final QueryString queryString, final File file) throws IOException,
			ParseException, JSONException {

		final String context = this.apiHost + this.formatContext(apiContext);
		QueryString addQueryString = addAditionParameters(queryString);
		final HttpPost httppost = new HttpPost(context);
		SimpleMultipartEntity simpleMultipartEntity;
		if(uploadingHander!=null){
			 simpleMultipartEntity = new SimpleMultipartEntity(
					new ProcessListener() {
						@Override
						public void currentSendBytes(long bytes) {
	                        	Message message = uploadingHander.obtainMessage();
	                       		message.what = UploadStatus.UPLOADING.ordinal();
	                       		Bundle bundle = new Bundle();
	                       		bundle.putLong("currentValue", bytes);
	                       		message.setData(bundle);
	                       		uploadingHander.sendMessage(message);
						}

					});
		}else{
			 simpleMultipartEntity = new SimpleMultipartEntity();

		}
		
		for (final Map.Entry<String, String[]> entry : addQueryString
				.getParamMap().entrySet()) {
			final String key = entry.getKey();
			for (final String val : entry.getValue()) {
				// 按照平台规定，所有的参数必须进行一次 UTF8 转码。
				simpleMultipartEntity.addPart(key, encodeToUTF8(val));
			}
		}
		simpleMultipartEntity.addPart("uploadFile", file);
		httppost.setEntity(simpleMultipartEntity);
		Log.d(TAG, "file name is " + file.getName());
		final HttpClient httpClient = this.getDefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httppost);
		JSONObject jsonObject = getJSONFromResponse(httpResponse);
		httpClient.getConnectionManager().shutdown();
		return jsonObject;
	}

	public JSONObject postUrlEncodedForm(final String apiContext,
			final QueryString queryString) throws IOException, ParseException,
			JSONException {

		final String context = this.apiHost + this.formatContext(apiContext);
		QueryString addQueryString = addAditionParameters(queryString);
		final HttpPost httppost = new HttpPost(context);
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		for (final Map.Entry<String, String[]> entry : addQueryString
				.getParamMap().entrySet()) {
			final String key = entry.getKey();
			for (final String val : entry.getValue()) {
				formParams.add(new BasicNameValuePair(key, val));
			}
		}
		// 按照平台规定，所有的参数必须进行一次 UTF8 转码。
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
				formParams, "UTF-8");
		httppost.setEntity(urlEncodedFormEntity);
		final HttpClient httpClient = this.getDefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httppost);
		JSONObject jsonObject = getJSONFromResponse(httpResponse);
		httpClient.getConnectionManager().shutdown();
		return jsonObject;
	}

	private String encodeToUTF8(final String val) {
		try {
			return URLEncoder.encode(val, "utf-8");
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
			return val;
		}
	}

	private URI addRequestParams(final String apiContext,
			final QueryString queryString) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 增加每个连接都必须传递的约定参数，包括 accessKey、time、sign
		final QueryString aQueryString = addAditionParameters(queryString);
		for (final Map.Entry<String, String[]> entry : aQueryString
				.getParamMap().entrySet()) {
			final String key = entry.getKey();
			for (final String val : entry.getValue()) {
				params.add(new BasicNameValuePair(key, val));
			}
		}
		final String context = this.formatContext(apiContext);
		String url = this.apiHost + context;
		if (!url.endsWith("?"))
			url += "?";
		// 按照平台规定，所有的参数必须进行一次 UTF8 转码。
		url += URLEncodedUtils.format(params, "utf-8");

		return URI.create(url);
	}

	/**
	 * 获取 DefaultHttpClient对象
	 * 
	 * @return DefaultHttpClient 对象
	 */
	public DefaultHttpClient getDefaultHttpClient() {

		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new SimpleSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return new DefaultHttpClient();
		}
	}
}

package com.pispower.video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pispower.R;
import com.pispower.network.VideoClient;
import com.pispower.util.ContentUriFileConvertion;
import com.pispower.util.FileUtil;
import com.pispower.util.PullRefreshListView;
import com.pispower.util.PullRefreshListView.OnRefreshListener;
import com.pispower.util.StatusTransition;
import com.pispower.video.upload.MultipartUploadHandler;
import com.pispower.video.upload.MultipartUploadThread;

public class VideoActivity extends Activity {

	private static final String TAG = "VideoActivity";
	// activity 结果返回码，用来确定是否正常返回
	private int videoOrAudioSelectCode = 0;
	// 进度对话框
	private ProgressDialog progressDialog;
	// ListView 的Adapter
	private VideoListAdapter videoListAdapter;
	// TextView
	private TextView videoEmpTextView;
	// 资源对象
	private Resources resources;
	
	private PullRefreshListView videoListView;

	private String curCatalogId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_video);
		// 获取资源对象
		resources = getResources();
		// 获取Intent
		Intent intent = getIntent();
		// 获取ActionBar 并设置
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(intent.getStringExtra(resources.getString(R.string.folderName)));
		

		progressDialog = ProgressDialog.show(this,
				resources.getString(R.string.loading),
				resources.getString(R.string.loadDescription));

		curCatalogId = intent.getStringExtra(resources.getString(R.string.catalogId));

	    videoListView = (PullRefreshListView) findViewById(R.id.videoList);
		videoEmpTextView = (TextView) findViewById(R.id.videoEmptyHint);
		videoListView.setEmptyView(videoEmpTextView);
		videoListAdapter = new VideoListAdapter(new ArrayList<VideoInfo>(),
				this);
		VideoListItemClickListener videoListItemClickListener = new VideoListItemClickListener(
				this);
		videoListView.setAdapter(videoListAdapter);
		videoListView.setOnItemClickListener(videoListItemClickListener);

		videoListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				
				List<VideoInfo> videoInfos=	videoListAdapter.getVideoInfoList(); 
				if(videoInfos!=null&&!videoInfos.isEmpty()){
					if(videoInfos.get(0).uploadInfo!=null){
						videoListView.onRefreshComplete();
						return;
					}
				}
				
				new AsyncTask<String, Void, List<VideoInfo>>() {

					@Override
					protected List<VideoInfo> doInBackground(String... params) {
						String catalogId = params[0];
						VideoClient videoClient = new VideoClient();
						List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
						try {
							JSONArray jsonArray = videoClient
									.listVideo(catalogId);
							if (jsonArray == null) {
								return videoInfoList;
							}
							if (jsonArray.length() == 0) {
								return videoInfoList;
							}
							for (int i = 0; i < jsonArray.length(); i++) {
								VideoInfo videoInfo = new VideoInfo();
								JSONObject jsonObject = jsonArray
										.getJSONObject(i);
								videoInfo.setId(jsonObject.getString("id"));
								String fileName = jsonObject.getString("name");
								videoInfo.setName(fileName);
								// 现在restful api 中的返回不包含大小，所以设置为空字符串
								videoInfo.setSize(resources.getString(R.string.emptyString));
								String status = jsonObject.getString("status");
								status = StatusTransition.toChinese(status,resources);
								videoInfo.setStatus(status);
								if (status.equals(resources.getString(R.string.AUDIT_SUCCESS))) {
									Map<String, String> clarityUrlMap = videoClient.getVideoEmbedCode(
											jsonObject.getString("id"),
											resources
													.getString(R.string.audioClarity));
									videoInfo.setClarityUrlMap(clarityUrlMap);
								} else {
									videoInfo.setClarityUrlMap(null);
								}
								videoInfoList.add(videoInfo);
							}
							return videoInfoList;
						} catch (Exception e) {
							Log.e(TAG, e.getMessage());
							return null;
						}
					}
					@Override
					protected void onPostExecute(List<VideoInfo> result) {
						super.onPostExecute(result);
						
						if (!isCancelled() && result != null) {
							videoEmpTextView.setText(R.string.videoListEmpty);
							videoListAdapter.setVideoInfoList(result);
							videoListAdapter.notifyDataSetChanged();
						} else {
							videoEmpTextView.setText(R.string.videoListLoadError);
						}
						
						videoListView.onRefreshComplete();
					}
				}.execute(curCatalogId);

			}

		});

		// 通过异步任务加载视频
		new LoadVideoTask(progressDialog, videoEmpTextView, videoListAdapter,
				resources).execute(curCatalogId);
	}

	/**
	 * 响应ImageButton点击事件的方法,用来上传视频与音频
	 * 
	 * @param view
	 */
	public void uploadVideo(View view) {
		List<VideoInfo> videoInfos = videoListAdapter.getVideoInfoList();
		// 判断是否有视频正在上传
		if (videoInfos != null && !videoInfos.isEmpty()) {
			VideoInfo firstVideoInfo = videoInfos.get(0);
			if (firstVideoInfo.getUploadInfo() != null) {
				Toast.makeText(this,
						resources.getString(R.string.existVideoUploading),
						Toast.LENGTH_LONG).show();
				return;
			}
		}
        if(videoListView.isRefreshing()){
        	Toast.makeText(this,
					resources.getString(R.string.isRefresh),
					Toast.LENGTH_LONG).show();
			return;
        }
		// 新建Intent，通过intent 来使用系统已安装的软件来获取音频与视频文件
		Intent getVideoIntent = new Intent();
		// 设置类型为视频与音频
		getVideoIntent.setType("video/*;audio/*");
		// 设置action
		getVideoIntent.setAction(Intent.ACTION_GET_CONTENT);
		// getVideoIntent.addCategory(Intent.CATEGORY_OPENABLE);
		// 启动第三方应用并获得返回
		startActivityForResult(
				Intent.createChooser(getVideoIntent, "select a video/audio"),
				videoOrAudioSelectCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode != videoOrAudioSelectCode) {
			Log.w(TAG, "request Code " + "=" + requestCode + " , !="
					+ videoOrAudioSelectCode);
			return;
		}
		if (data == null) {
			Log.w(TAG, "no any content return");
			return;
		}
		Uri originalUri = data.getData();
		String filePath = ContentUriFileConvertion
				.convertConentUriToFileString(originalUri, this);
		File file = new File(filePath);
		Log.d(TAG, file.length() + "");
		MultipartUploadHandler multipartUploadHandler = new MultipartUploadHandler(
				videoListAdapter, this);
		String applicationFilesDir = FileUtil
				.getApplicationPrivateFileDir(this);

		String mutilpartUploadDir = applicationFilesDir + File.separator
				+ "MultipartUploadTemp";
		String dir = mutilpartUploadDir + File.separator
				+ file.getName().replace('.', '_') + new Random().nextLong();
		File dirFile = new File(dir);
		Log.d(TAG, "temp file dir is " + dir);
		// 开启线程，用于上传
		MultipartUploadThread multipartUploadThread = new MultipartUploadThread(
				multipartUploadHandler, file, dirFile, this.curCatalogId);
		multipartUploadThread.start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}

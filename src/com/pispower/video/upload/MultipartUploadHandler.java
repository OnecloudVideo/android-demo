package com.pispower.video.upload;

import java.text.Format;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.pispower.R;
import com.pispower.video.VideoInfo;
import com.pispower.video.VideoListAdapter;

public class MultipartUploadHandler extends Handler {

	// ListView的adapter
	private VideoListAdapter videoListAdapter;
	// 资源对象
	private Resources resources;

	/**
	 * 有参构造方法
	 * 
	 * @param adapter
	 * @param context
	 */
	public MultipartUploadHandler(VideoListAdapter adapter, Context context) {
		this.videoListAdapter = adapter;
		this.resources = context.getResources();
	}

	@Override
	public void handleMessage(Message msg) {
		UploadStatus status = UploadStatus.values()[msg.what];
		List<VideoInfo> videoList = this.videoListAdapter.getVideoInfoList();
		Bundle bundle = msg.getData();
		switch (status) {
		case UPLOAD_START:
			addUploadStartInfo(videoList, bundle);
			break;
		case UPLOADING:
			// Bundle bundle=msg.getData();
			addUploadingInfo(videoList, bundle);
			break;
		case UPLOAD_SUCCESS:
			addUploadSuccessInfo(videoList, bundle);
			break;
		case UPLOAD_FAIL:
			addUploadFailInfo(videoList);
			break;
		}
		this.videoListAdapter.notifyDataSetChanged();
		super.handleMessage(msg);
	}

	private void addUploadFailInfo(List<VideoInfo> videoList) {
		videoList.remove(0);
		VideoInfo videoUploadFailInfo = new VideoInfo();
		videoUploadFailInfo.setName("filename");
		videoUploadFailInfo.setStatus(resources
				.getString(R.string.videoUploadFail));
		videoUploadFailInfo.setSize("0");
		videoList.add(0, videoUploadFailInfo);
	}

	private void addUploadSuccessInfo(List<VideoInfo> videoList, Bundle bundle) {
		VideoInfo videoInfo = videoList.remove(0);
		VideoInfo videoUploadSuccessInfo = new VideoInfo();
		videoUploadSuccessInfo.setName(videoInfo.getName());
		videoUploadSuccessInfo.setStatus(resources
				.getString(R.string.videoUploadSuccess));
		videoUploadSuccessInfo.setSize(videoInfo.getSize());
		Map<String,String>maps=new HashMap<String,String>();
		videoUploadSuccessInfo.setClarityUrlMap(maps);
		maps.put("未知", bundle.getString("filePath"));
		videoList.add(0, videoUploadSuccessInfo);
	}

	private void addUploadingInfo(List<VideoInfo> videoList, Bundle bundle) {
		VideoInfo videoInfo = videoList.remove(0);
		UploadInfo uploadingInfo = new UploadInfo();
		uploadingInfo.setUploadStatus(UploadStatus.UPLOADING);
		int maxValue = videoInfo.getUploadInfo().getMaxValue();
		int curValue = bundle.getInt("currentValue");
		Log.i("maxValue", maxValue + "");
		Log.i("curValue", curValue + "");
		uploadingInfo.setCurrentValue(curValue);
		uploadingInfo.setMaxValue(maxValue);
		VideoInfo videoUploadingInfo = new VideoInfo(uploadingInfo);
		videoUploadingInfo.setName(videoInfo.getName());
		videoUploadingInfo.setSize(videoInfo.getSize());
		Format format = NumberFormat.getPercentInstance();
		String curValueString = format.format(curValue / (maxValue * 1.0));
		Log.i("Formate", curValueString);
		videoUploadingInfo.setStatus(curValueString);
		videoList.add(0, videoUploadingInfo);
	}

	private void addUploadStartInfo(List<VideoInfo> videoList, Bundle bundle) {
		UploadInfo uploadInfo = new UploadInfo();
		uploadInfo.setUploadStatus(UploadStatus.UPLOAD_START);
		uploadInfo.setMaxValue(bundle.getInt("partNums"));
		VideoInfo videoInfo = new VideoInfo(uploadInfo);
		videoInfo.setName(bundle.getString("fileName"));
		videoInfo.setSize(bundle.getString("fileSize"));
		videoInfo.setStatus("0%");
		videoList.add(0, videoInfo);
	}

}

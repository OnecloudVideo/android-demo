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

import com.pispower.R;
import com.pispower.video.VideoInfo;
import com.pispower.video.VideoListAdapter;

public class MultipartUploadHandler extends Handler {

	// ListView的adapter
	private VideoListAdapter videoListAdapter;
	// 资源对象
	private Resources resources;
	
	private long currentAlreadySendBytes;

	/**
	 * 有参构造方法
	 * 
	 * @param adapter
	 * @param context
	 */
	public MultipartUploadHandler(VideoListAdapter adapter, Context context) {
		this.videoListAdapter = adapter;
		this.resources = context.getResources();
		this.currentAlreadySendBytes=0;
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
		videoUploadFailInfo.setName(MutilUploadHandlerMessageParams.FILE_NAME);
		videoUploadFailInfo.setStatus(resources
				.getString(R.string.videoUploadFail));
		videoUploadFailInfo.setSize(resources.getString(R.string.zeroSize));
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
		maps.put(resources.getString(R.string.unknownFilePath), bundle.getString(MutilUploadHandlerMessageParams.FILE_PATH));
		videoList.add(0, videoUploadSuccessInfo);
	}

	private void addUploadingInfo(List<VideoInfo> videoList, Bundle bundle) {
		VideoInfo videoInfo = videoList.remove(0);
		UploadInfo uploadingInfo = new UploadInfo();
		uploadingInfo.setUploadStatus(UploadStatus.UPLOADING);
		long curValue = bundle.getLong(MutilUploadHandlerMessageParams.CURRENT_VALUE);
		currentAlreadySendBytes+=curValue;
		uploadingInfo.setCurrentValue(currentAlreadySendBytes);
		VideoInfo videoUploadingInfo = new VideoInfo(uploadingInfo);
		videoUploadingInfo.setName(videoInfo.getName());
		videoUploadingInfo.setSize(videoInfo.getSize());
		Format format = NumberFormat.getPercentInstance();
		String curValueString = format.format(currentAlreadySendBytes/Float.parseFloat(videoInfo.getSize()));
		videoUploadingInfo.setStatus(curValueString);
		videoList.add(0, videoUploadingInfo);
	}

	private void addUploadStartInfo(List<VideoInfo> videoList, Bundle bundle) {
		UploadInfo uploadInfo = new UploadInfo();
		uploadInfo.setUploadStatus(UploadStatus.UPLOAD_START);
		VideoInfo videoInfo = new VideoInfo(uploadInfo);
		videoInfo.setName(bundle.getString(MutilUploadHandlerMessageParams.FILE_NAME));
		videoInfo.setSize(bundle.getString(MutilUploadHandlerMessageParams.FILE_SIZE));
		videoInfo.setStatus(resources.getString(R.string.zeroStatus));
		videoList.add(0, videoInfo);
	}

}

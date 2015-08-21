package com.pispower.video.upload;

import android.os.Handler;

import com.pispower.AppContext;
import com.pispower.video.sdk.VideoSDK;
import com.pispower.video.sdk.multipart.MultipartUploadRequest;

import java.io.File;

public class MultipartUploadThread extends Thread {
	// Handler 对象
	private Handler handler;
	// 临时目录File对象
	private File tempDir;
	// 长传文件File对象
	private File uploadFile;

	private String curCatalogId;
	/**
	 * 有参构造方法
	 * 
	 * @param paramHandler
	 * @param paramFile1
	 * @param paramFile2
	 */
	private MultipartUploadThread(Handler paramHandler, File paramFile1,
			File paramFile2) {
		this.handler = paramHandler;
		this.uploadFile = paramFile1;
		this.tempDir = paramFile2;
	}

	public MultipartUploadThread(Handler paramHandler, File paramFile1,
			File paramFile2,String curCatalogId) {
		this(paramHandler, paramFile1, paramFile2);
		this.curCatalogId=curCatalogId;
	}
	@Override
	public void run() {

		AppContext.getSDK().getMultipartService().upload(new MultipartUploadRequest(this.uploadFile, this.tempDir, this.handler,this.curCatalogId));
		
		super.run();
	}
}

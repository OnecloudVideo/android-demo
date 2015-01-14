package com.pispower.video.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;

import com.pispower.R;
import com.pispower.video.VideoView;

public class PlayActivity extends Activity implements MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener {

	private VideoView videoView;
	private ProgressDialog progressDialog;
    private Resources resources;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("onCreate", "onCreate is called");
		super.onCreate(savedInstanceState);
		resources=getResources();
		// 创建进度对话框
		progressDialog = ProgressDialog.show(this, resources.getString(R.string.loading), resources.getString(R.string.loadDescription));
//		progressDialog.setCanceledOnTouchOutside(false);
//		// 显示进度对话框
//		progressDialog.show();

		// 设置窗口为无标题窗口
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 设置窗口为全屏窗口
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置activity的ContentView
		setContentView(R.layout.play_activity);
		// 获得Intent，及其携带的Uri
		Intent intent = getIntent();
		String playUri = intent.getStringExtra("playUri");

		// 获取VideoView 对象实例
		videoView = (VideoView) findViewById(R.id.palyView);

		// 新建MediaController(媒体控制器)对象实例
		MediaController mediaController = new MediaController(this);

		// 设置MediaController为VideoView对象
		videoView.setMediaController(mediaController);

		// 设置OnPreparedListener为VideoView对象
		videoView.setOnPreparedListener(this);

		// 设置错误监听器，当发生错误时从屏幕上移除进度对话框，并结束本activity，返回到上一个activity。
		videoView.setOnErrorListener(this);

		// 获得http referer,此由用户在亦云视频平台白名单管理中所设定的url
		String referer = this.getResources().getString(R.string.Referer);
		Log.i("HTTP Referer", referer);
		// 设置播放源uri及http Referer字段
		videoView.setVideoURI(Uri.parse(playUri), referer);

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {

		progressDialog.dismiss();
		videoView.stopPlayback();

		AlertDialog.Builder bulider = new AlertDialog.Builder(
				PlayActivity.this);
		bulider.setCancelable(false);
		bulider.setMessage(resources.getString(R.string.playError)).setTitle(resources.getString(R.string.playErrorTitle));

		bulider.setPositiveButton(resources.getString(R.string.playActivityDialogButton),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
						PlayActivity.this.finish();
					}
				});
		bulider.show();
		return true;
	
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// 从屏幕上移除进度对话框
		if(progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		// 开始播放视频
		videoView.start();
	}

	

}

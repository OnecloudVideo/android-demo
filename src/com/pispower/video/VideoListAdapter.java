package com.pispower.video;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pispower.R;
import com.pispower.util.DpPxConvertion;
import com.pispower.util.FileUtil;
import com.pispower.util.MediaUtil;
import com.pispower.util.StringUtil;

public class VideoListAdapter extends BaseAdapter {
    //文件名的最大长度
	private static final int MAX_FILE_NAME_LENGTH=20;
	//progressBar 最大的进度值
	private static final int PROGRESS_MAX_VALUE=100;
	//progressBar 初始化的进度值
	private static final int PROGRESS_INITVALUE=0;
	//控件的padding距离
	private static final int LEFT=5;
	private static final int TOP=0;
	private static final int RIGHT=0;
	private static final int BOTTOM=0;
	
	// 视频信息列表
	private List<VideoInfo> videoInfoList;

	// LayoutInflater 对象
	private LayoutInflater layoutInflater;

	// 场景对象
	private Context context;

	/**
	 * 有参构造方法
	 * 
	 * @param videoInfoList
	 * @param context
	 */
	public VideoListAdapter(List<VideoInfo> videoInfoList, Context context) {
		super();
		this.videoInfoList = videoInfoList;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	@Override
	public int getCount() {
		return videoInfoList.size();
	}

	@Override
	public VideoInfo getItem(int position) {

		return videoInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	/**
	 * 插入指定视频信息添加入视频信息列表的指定位置
	 * 
	 * @param videoInfo
	 * @param index
	 */
	public void add(VideoInfo videoInfo, int index) {
		videoInfoList.add(index, videoInfo);
	}

	/**
	 * 
	 * 插入视频信息集合到视频信息列表的指定位置
	 * 
	 * @param videoInfos
	 * @param index
	 * @return
	 */
	public boolean addAll(Collection<VideoInfo> videoInfos, int index) {
		return videoInfoList.addAll(index, videoInfos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VideoInfo videoInfo = videoInfoList.get(position);
		RelativeLayout view = null;
		if (convertView == null) {
			view = (RelativeLayout) layoutInflater.inflate(
					R.layout.video_list_item, parent, false);
			final ViewHolder viewHolder = new ViewHolder();
			setViewHolder(videoInfo, view, viewHolder);
			view.setTag(viewHolder);
		} else {
			view = (RelativeLayout) convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		if (videoInfo.uploadInfo == null) {
			setTextViewText(position, view, holder);
		} else {
			setProgressBarProcess(videoInfo, view, holder);
		}
		String fileName = videoInfoList.get(position).getName();
		if (fileName.length() > VideoListAdapter.MAX_FILE_NAME_LENGTH) {
			fileName = fileName.substring(fileName.length() - MAX_FILE_NAME_LENGTH);
		}
		if (MediaUtil.isAudioFileType(fileName)) {
			holder.videoImage.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.v_audio));
		} else {
			holder.videoImage.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.v_video));
		}
		holder.name.setText(fileName);
		holder.status.setText(videoInfoList.get(position).getStatus());
		return view;
	}

	/*
	 * 设置ViewHolder
	 */
	private void setViewHolder(VideoInfo videoInfo, RelativeLayout view,
			final ViewHolder viewHolder) {
		viewHolder.videoImage = (ImageView) view.findViewById(R.id.videoImage);
		viewHolder.name = (TextView) view.findViewById(R.id.videoName);
		viewHolder.status = (TextView) view.findViewById(R.id.videoStatus);

		TextView sizeTextView = createTextView(viewHolder);
		viewHolder.size = sizeTextView;

		ProgressBar progressBar = createProgressBar(viewHolder);
		viewHolder.progressBar = progressBar;

		setViewHoldView(videoInfo, view, viewHolder, progressBar);
	}

	/*
	 * 设置viewHolder 中的view
	 */
	private void setViewHoldView(VideoInfo videoInfo, RelativeLayout view,
			final ViewHolder viewHolder, ProgressBar progressBar) {
		if (videoInfo.uploadInfo != null) {
			progressBar.setMax(PROGRESS_MAX_VALUE);
			progressBar.setProgress(PROGRESS_INITVALUE);
			view.addView(viewHolder.progressBar);
			viewHolder.view = viewHolder.progressBar;
		} else {
			view.addView(viewHolder.size);
			viewHolder.view = viewHolder.size;
		}
	}

	/*
	 * 
	 * 设置TextView的text
	 */
	private void setTextViewText(int position, RelativeLayout view,
			ViewHolder holder) {
		String fileLength=videoInfoList.get(position).getSize();
		if(!StringUtil.isBlank(fileLength)){
			holder.size.setText(FileUtil.autoConvertFileLength(Long.parseLong(fileLength)));
		}else{
			holder.size.setText(context.getResources().getString(R.string.emptyString));
		}
		
		if (holder.size != holder.view) {
			view.removeView(holder.view);
			holder.view = holder.size;
			view.addView(holder.size);
		}  
		//现在没有获取视频大小的rest api 接口，所有对其进行隐藏处理。
		//holder.size.setVisibility(View.INVISIBLE);
	}

	/*
	 * 设置ProgressBar的process
	 */
	private void setProgressBarProcess(VideoInfo videoInfo,
			RelativeLayout view, ViewHolder holder) {
		holder.progressBar.setMax(PROGRESS_MAX_VALUE);
	    long currentValue=	videoInfo.uploadInfo.getCurrentValue();
	    float totalSize=	Float.parseFloat(videoInfo.getSize());
		int progress=(int)(currentValue/totalSize*100);
		holder.progressBar.setProgress(progress);

		if (holder.progressBar != holder.view) {
			view.removeView(holder.view);
		   holder.view = holder.progressBar;
			view.addView(holder.progressBar);
		}  
	}

	/*
	 * 创建TextView
	 */
	private TextView createTextView(final ViewHolder viewHolder) {
		TextView sizeTextView = new TextView(this.context);
		sizeTextView.setPadding(DpPxConvertion.dpToPx(LEFT, context), TOP, RIGHT, BOTTOM);
		RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		textViewParams.addRule(RelativeLayout.ALIGN_BOTTOM,
				viewHolder.videoImage.getId());
		textViewParams.addRule(RelativeLayout.RIGHT_OF,
				viewHolder.videoImage.getId());
		sizeTextView.setLayoutParams(textViewParams);
		return sizeTextView;
	}

	/*
	 * 创建ProgressBar
	 */
	private ProgressBar createProgressBar(final ViewHolder viewHolder) {
		ProgressBar progressBar = new ProgressBar(this.context, null,
				android.R.attr.progressBarStyleHorizontal);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.ALIGN_BOTTOM,
				viewHolder.videoImage.getId());
		params.addRule(RelativeLayout.RIGHT_OF, viewHolder.videoImage.getId());
		// params.setMargins(left, top, right, bottom)
		progressBar.setPadding(DpPxConvertion.dpToPx(LEFT, context), TOP, RIGHT, BOTTOM);
		progressBar.setLayoutParams(params);
		return progressBar;
	}

	/**
	 * 获取视频信息列表
	 * 
	 * @return
	 */
	public List<VideoInfo> getVideoInfoList() {
		return videoInfoList;
	}

	/**
	 * 设置视频信息列表
	 * 
	 * @param videoInfoList
	 */
	public void setVideoInfoList(List<VideoInfo> videoInfoList) {
		this.videoInfoList = videoInfoList;
	}

	// 用来保存所需的View
	static class ViewHolder {
		protected ImageView videoImage;
		protected TextView name;
		protected TextView size;
		protected View view;
		protected ProgressBar progressBar;
		protected TextView status;
	}

}

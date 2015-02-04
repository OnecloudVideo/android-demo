package com.pispower.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtil {

	/**
	 * 删除文件夹及其所属的子文件夹或文件
	 * 
	 * @param file
	 */
	public static void deleteAllFiles(File file) {
		if (!file.exists()) {
			Log.w(file.getAbsolutePath(), "file is not exist");
			return;
		}
		if (file.isFile()) {
			if (file.delete()) {
				Log.i(file.getName(), "delete");
			}
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			for (int i = 0; i < childFiles.length; i++) {
				deleteAllFiles(childFiles[i]);
			}
			file.delete();
			Log.i(file.getName(), "delete");
		}
	}

	/**
	 * 获取应用程序私有文件目录的字符串表示，首先会尝试获取外部的文件目录，如果可以获取 则返回，否则会去获取内部的文件目录。
	 * 
	 * @param context
	 * @return
	 */
	public static String getApplicationPrivateFileDir(Context context) {
		String tempDir = null;
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)
				&& !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Log.w("ExternalStorageState", state);
			return null;
		}
		File dir = context.getExternalFilesDir(null);
		if (dir == null) {
			Log.w("dir", "is null");
			tempDir = context.getFilesDir().getAbsolutePath();
		} else {
			Log.d(dir.getAbsolutePath(), " is not null");
			tempDir = dir.getAbsolutePath();
		}
		return tempDir;
	}
    /**
     * 获取缓存目录
     * @param context
     * @return
     */
	public static String getApplicationCacheDir(Context context) {

		String tempDir = null;
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)
				&& !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Log.w("ExternalStorageState", state);
			return null;
		}
		File dir = context.getExternalCacheDir();
		if (dir == null) {
			Log.w("dir", "is null");
			tempDir = context.getCacheDir().getAbsolutePath();
		} else {
			Log.d(dir.getAbsolutePath(), " is not null");
			tempDir = dir.getAbsolutePath();
		}
		return tempDir;

	}

	/**
	 * 写一个对象到缓存中
	 * @param object
	 */
	public static void writeToCache(Object object) {

		String cacheDir = getApplicationCacheDir(null);
		String fileName = cacheDir + File.separator + "catalogInfos";
		File file = new File(fileName);
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					new FileOutputStream(file));
			objectOutputStream.writeObject(object);
			objectOutputStream.close();
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", e.getMessage());
		} catch (IOException e) {
			Log.e("IOException", e.getMessage());
		}
  
	}
	/**
	 * 从缓存中读取一个对象
	 * @return
	 */
	public Object readCacheObject(){
		String cacheDir = getApplicationCacheDir(null);
		String fileName = cacheDir + File.separator + "catalogInfos";
		File file = new File(fileName);
		if(!file.exists()){
			return null;
		}
		Object returnObject=null;
		try {
			ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream(file));
			try {
				returnObject=objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				Log.e("ClassNotFoundException", e.getMessage()); 
			}
			objectInputStream.close();
		} catch (StreamCorruptedException e) {
			Log.e("StreamCorruptedException", e.getMessage()); 
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", e.getMessage()); 
		} catch (IOException e) {
			Log.e("IOException", e.getMessage()); 
		}
		
		return returnObject;
	}
	/**
	 * 把文件的字节数大小转换为相应的大小，转换的范围为 "B", "kB", "MB", "GB", "TB","PB","EB"。
	 * @param size
	 * @return
	 */
	 public static String autoConvertFileLength(long size) {
	     if(size <= 0) return "0";
	     final String[] units = new String[] { "B", "kB", "MB", "GB", "TB","PB","EB"};
	     int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	     return new DecimalFormat("#,##0.##").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	 }
	
}

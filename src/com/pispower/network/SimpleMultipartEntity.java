package com.pispower.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import android.util.Log;

public class SimpleMultipartEntity implements HttpEntity {

	private  ProcessListener processListener;
	
	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();
	private static final String tag = "SimpleMultipartEntity";
	private String boundary = null;
   
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	boolean isSetLast = false;
	boolean isSetFirst = false;

	/**
	 * 构造方法
	 */
	public SimpleMultipartEntity(ProcessListener processListener) {
		final StringBuffer buf = new StringBuffer();
		final Random rand = new Random();
		for (int i = 0; i < 30; i++) {
			buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
		}
		this.boundary = buf.toString();
        this.processListener=processListener;
	}

	
	public SimpleMultipartEntity() {
		final StringBuffer buf = new StringBuffer();
		final Random rand = new Random();
		for (int i = 0; i < 30; i++) {
			buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
		}
		this.boundary = buf.toString();
	}
	
	/**
	 * 根据是否需要写入第一个boundary分割符
	 * 
	 */
	public void writeFirstBoundaryIfNeeds() {
		if (!isSetFirst) {
			try {
				out.write(("--" + boundary + "\r\n").getBytes());
			} catch (final IOException e) {
				Log.e(tag, e.getMessage(), e);
			}
		}
		isSetFirst = true;
	}

	/**
	 * 根据是否需要写入最后一个boundary分割符
	 * 
	 */
	public void writeLastBoundaryIfNeeds() {
		if (isSetLast) {
			return;
		}
		try {
			out.write(("\r\n--" + boundary + "--\r\n").getBytes());
		} catch (final IOException e) {
			Log.e(tag, e.getMessage(), e);
		}
		isSetLast = true;
	}

	/**
	 * 添加一个字符串key-value的part
	 * 
	 * @param key
	 * @param value
	 */
	public void addPart(final String key, final String value) {
		writeFirstBoundaryIfNeeds();
		try {
			out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n")
					.getBytes());
			out.write("Content-Type: text/plain; charset=UTF-8\r\n".getBytes());
			out.write("Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes());
			out.write(value.getBytes());
			out.write(("\r\n--" + boundary + "\r\n").getBytes());
		} catch (final IOException e) {
			Log.e(tag, e.getMessage(), e);
		}
	}

	/**
	 * 添加一个文件流的part
	 * 
	 * @param key
	 * @param fileName
	 * @param fin
	 */
	public void addPart(final String key, final String fileName,
			final InputStream fin) {
		addPart(key, fileName, fin, "application/octet-stream");
	}

	/**
	 * 添加一个file part
	 * 
	 * @param key
	 * @param fileName
	 * @param fin
	 * @param type
	 */
	public void addPart(final String key, final String fileName,
			final InputStream fin, String type) {
		writeFirstBoundaryIfNeeds();
		try {
			type = "Content-Type: " + type + "\r\n";
			out.write(("Content-Disposition: form-data; name=\"" + key
					+ "\"; filename=\"" + fileName + "\"\r\n").getBytes());
			out.write(type.getBytes());
			out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

			final byte[] tmp = new byte[1024];
			int l = 0;
			while ((l = fin.read(tmp)) != -1) {
				out.write(tmp, 0, l);
				if(processListener!=null){
					processListener.currentSendBytes(l);
				}
			}
			out.flush();
		} catch (final IOException e) {
			Log.e(tag, e.getMessage(), e);
		} finally {
			try {
				fin.close();
			} catch (final IOException e) {
				Log.e(tag, e.getMessage(), e);
			}
		}
	}

	/**
	 * 添加一个file part
	 * 
	 * @param key
	 * @param value
	 */
	public void addPart(final String key, final File value) {
		try {
			addPart(key, value.getName(), new FileInputStream(value));
		} catch (final FileNotFoundException e) {
			Log.e(tag, e.getMessage(), e);
		}
	}

	@Override
	public long getContentLength() {
		writeLastBoundaryIfNeeds();
		return out.toByteArray().length;
	}

	@Override
	public Header getContentType() {
		return new BasicHeader("Content-Type", "multipart/form-data; boundary="
				+ boundary);
	}

	@Override
	public boolean isChunked() {
		return false;
	}

	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public boolean isStreaming() {
		return false;
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		outstream.write(out.toByteArray());
	}

	@Override
	public Header getContentEncoding() {
		return null;
	}

	@Override
	public void consumeContent() throws IOException,
			UnsupportedOperationException {
		if (isStreaming()) {
			throw new UnsupportedOperationException(
					"Streaming entity does not implement #consumeContent()");
		}
	}

	@Override
	public InputStream getContent() throws IOException,
			UnsupportedOperationException {
		return new ByteArrayInputStream(out.toByteArray());
	}

	
 public interface ProcessListener{
		
		public abstract void currentSendBytes(long bytes);
	}

}

package com.pispower.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pispower.R;
import com.pispower.catalog.CatalogActivity;
import com.pispower.util.NetworkInspection;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	/**
	 * 响应按钮点击事件的方法，此方法用来启动一个新的Activity
	 * 
	 * @param view
	 */
	public void startDemo(View view) {
		if (NetworkInspection.isExistingAnyNetwork(this)) {
			Intent intent = new Intent(this, CatalogActivity.class);
			this.startActivity(intent);
		} else {
			Toast.makeText(this, "不存在网络连接，请检查", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

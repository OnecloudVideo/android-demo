package com.pispower.catalog;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pispower.R;

public class CatalogActivity extends Activity {

	private LayoutInflater inflater;
	private TextView listEmptyHintTextView;
	private ListView listView;
	private ProgressDialog progressDialog;
	private Resources resources;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_catalog);
		this.inflater = getLayoutInflater();
		ActionBar localActionBar = getActionBar();
		localActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		localActionBar.setDisplayHomeAsUpEnabled(true);
		this.resources = getResources();
		this.progressDialog = ProgressDialog.show(this,
				this.resources.getString(R.string.loading),
				this.resources.getString(R.string.loadDescription));
		this.listView = ((ListView) findViewById(R.id.catalogListView));
		this.listEmptyHintTextView = ((TextView) findViewById(R.id.emptyHint));
		this.listView.setEmptyView(this.listEmptyHintTextView);
		this.listView.setOnItemClickListener(new CatalogListViewItemClickListener(
				this));
		// 开启异步任务，用于从亦云视频下载视频分类
		new LoadCatalogTask(this.progressDialog, this.listView,
				this.listEmptyHintTextView, this).execute(new Void[] { null,
				null });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		getMenuInflater().inflate(R.menu.catalog, paramMenu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case android.R.id.home:
			//回到父activity
			NavUtils.navigateUpFromSameTask(this);
		case R.id.action_settings:
		default:
			break;
		}
		return super.onOptionsItemSelected(paramMenuItem);
	}

	/**
	 * 响应ImageButton点击事件的方法，用来显示dialog并创建视频分类
	 * 
	 * @param paramView
	 */
	@SuppressLint({ "InflateParams" })
	public void createCatalog(View paramView) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		final View localView = this.inflater.inflate(R.layout.add_file, null);
		localBuilder.setView(localView);
		localBuilder.setPositiveButton(R.string.add_file_dialog_add_button,
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						String str = ((EditText) localView
								.findViewById(R.id.addfileName)).getText()
								.toString();
						// 开启异步任务，用于创建视频分类
						new CreateCatalogTask(CatalogActivity.this,
								CatalogActivity.this.listView)
								.execute(new String[] { str });

						paramAnonymousDialogInterface.dismiss();
					}
				});
		localBuilder.setNegativeButton(R.string.add_file_dialog_cancel_button,
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.dismiss();
					}
				});
		localBuilder.create().show();
	}

}

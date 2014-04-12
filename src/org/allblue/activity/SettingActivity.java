package org.allblue.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.allblue.R;
import org.allblue.adapter.SettingAdapter;

public class SettingActivity extends Activity {

	private ListView accountListView;

	private static String accountType;
	
	private static SettingAdapter settingAdapter;
	
	private static Cursor cursor;

	private static void setAccountType(String type) {
		accountType = type;
	}

	public static String getAccountType() {
		return accountType;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.setting_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_account: {
			final String[] accounts = getResources().getStringArray(R.array.accounts);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick one account");

			builder.setSingleChoiceItems(accounts, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int i) {

							SettingActivity.setAccountType(accounts[i]);

							Intent openOAuth = new Intent(
									getApplicationContext(),
									OAuthActivity.class);
							startActivity(openOAuth);
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
			break;
		}
		}
		return true;
	}

    /*
	public void onBackPressed() {
		for (int i = 0; i < accountListView.getChildCount(); i++) {
			RelativeLayout itemLayout = (RelativeLayout) accountListView.getChildAt(i);
			CheckBox cb = (CheckBox) itemLayout.findViewById(R.id.account_chk_box);
			int enabledAccount = cb.isChecked() ? 1 : 0;
			
			TextView screenNameView = (TextView) itemLayout.findViewById(R.id.account_text_view);
			String screenName = screenNameView.getText().toString();
			
			TextView typeView = (TextView) itemLayout.findViewById(R.id.account_type_view);
			String accountType = typeView.getText().toString();
			
			DatabaseHandler.updateUserEnabledOption(screenName, accountType, enabledAccount);
		}
		
		Intent i = new Intent(this, DashBoard.class);
		startActivity(i);
	}
    */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting_screen);

		accountListView = (ListView) findViewById(R.id.accounts);

        //TODO: wth is this..
		//cursor = DatabaseHandler.getSelectAccountCursor(this);

		settingAdapter = new SettingAdapter(this, cursor, false);

		accountListView.setAdapter(settingAdapter);

		accountListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	public void onAccount(final View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Please choose");

		builder.setSingleChoiceItems(new String[] {"Delete"}, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int i) {
						
						RelativeLayout layoutView = (RelativeLayout) v;
						TextView accountNameView = (TextView) layoutView.findViewById(R.id.account_text_view);
						TextView accountTypeView = (TextView) layoutView.findViewById(R.id.account_type_view);
						
						String name = accountNameView.getText().toString();
						String type = accountTypeView.getText().toString();

                        //TODO: delete account
						//DatabaseHandler.deleteAccount(getApplicationContext(), name, type);

						if (SettingActivity.cursor.requery()) {
							SettingActivity.settingAdapter.notifyDataSetChanged();
						}
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

}

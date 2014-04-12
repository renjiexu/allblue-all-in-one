package org.allblue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import org.allblue.activity.HomeActivity;
import org.allblue.activity.SettingActivity;


public class DashBoard extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dashboard);
	}
	
	public void onAccountClick(View v) {
		Intent i = new Intent(this, HomeActivity.class);
		startActivity(i);
	}
	
	public void onSettingClick(View v) {
		Intent i = new Intent(this, SettingActivity.class);
		startActivity(i);
	}
}
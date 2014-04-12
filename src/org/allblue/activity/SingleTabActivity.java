package org.allblue.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.allblue.R;
import org.allblue.metadata.Account;

public class SingleTabActivity extends Activity{
	
	// "tweet" in my app is an generic word for all apps, not Twitter only
	
	private Account account;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_tab_screen);
		
		trickBarTextView();
		
		account = getIntent().getParcelableExtra("account");
		account.refresh(this);
	}
	
	/**
	 * It is use to display account name, set in HomeActivity
	 * Needed to set height as 0 to avoid gap
	 */
	private void trickBarTextView() {
		TextView labelView = (TextView) findViewById(R.id.tab_label_id);
		labelView.setHeight(0);
	}
}

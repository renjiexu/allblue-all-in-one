package org.allblue.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import org.allblue.R;
import org.allblue.metadata.Account;

/**
 * Display all the tabs
 * x
 * @author renjie
 * 
 */
public class HomeActivity extends TabActivity {

	//List<Account> activeAccounts = new ArrayList<Account>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		
		TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabs.setup();

        Dao<Account, Integer> accountDao = GlobalActivity.getAccountDao();
        if (accountDao == null) {
            return;
        }

        try {
            for (Account account : accountDao.queryForAll()) {
                TabHost.TabSpec spec = tabs.newTabSpec(account.getName());

                // save account for future connection pool
                /*
                final Account activeAccount = Account.createAccountByType(account.getType());
                activeAccount.setName(activeAccount.getName());
                activeAccount.setType(activeAccount.getType());
                activeAccounts.add(activeAccount);
                */

                Intent i = new Intent(this, SingleTabActivity.class);
                i.putExtra("account", account);

                LinearLayout articleTab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.single_tab_screen, null);
                TextView labelView = (TextView) articleTab.findViewById(R.id.tab_label_id);
                labelView.setText(account.getName());

                spec.setContent(i);
                spec.setIndicator(articleTab);
                tabs.addTab(spec);
            }
        } catch (SQLException e) {
            Log.e(HomeActivity.class.getName(), "Error", e);
        }
	}

	public void onPost(View v) {
		
		TextView textView = (TextView) findViewById(R.id.tweet_edit);
		String text = textView.getText().toString();
		
		if (text == null || text.length() == 0) {
			return;
		}

        try {
            Dao<Account, Integer> accountDao = GlobalActivity.getAccountDao();
            if (accountDao != null) {
                for (Account account : accountDao.queryForAll()) {
                    account.postText(this, text);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

		textView.setText("");
		Toast.makeText(this, "Post successfully", Toast.LENGTH_LONG).show();
	}
}

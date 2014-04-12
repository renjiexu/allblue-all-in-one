package org.allblue.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.allblue.R;

public class SettingAdapter extends CursorAdapter {

	LayoutInflater inflater;
	
	public SettingAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		int nameCol = c.getColumnIndex("name");
		String name = c.getString(nameCol);
		
		int accountTypeCol = c.getColumnIndex("account_type");
		String accountType = c.getString(accountTypeCol);
		
		int enabledCol = c.getColumnIndex("enabled");
		boolean accountEnabled = (c.getInt(enabledCol) == 1) ? true : false;
		
		TextView accountView = (TextView) v.findViewById(R.id.account_text_view);
		accountView.setText(name);
		
		TextView accountTypeView = (TextView) v.findViewById(R.id.account_type_view);
		accountTypeView.setText(accountType);
		
		ImageView imgView = (ImageView) v.findViewById(R.id.account_icon);
		if (accountType.equalsIgnoreCase("sina")) {
			imgView.setImageResource(R.drawable.sina_logo);
		} else if (accountType.equalsIgnoreCase("twitter")) {
			imgView.setImageResource(R.drawable.twitter_logo);
		}

		CheckBox checkBox = (CheckBox) v.findViewById(R.id.account_chk_box);
		checkBox.setChecked(accountEnabled);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = inflater.inflate(R.layout.account_item, parent, false);
		return v;
	}
}

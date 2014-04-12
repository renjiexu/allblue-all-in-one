package org.allblue.adapter;

import java.io.InputStream;
import java.net.URL;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.allblue.R;

public class SingleTabCursorAdapter extends CursorAdapter {
	
	LayoutInflater inflater;

	public SingleTabCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		inflater = LayoutInflater.from(context);
	}

	public Drawable getImage(String urlText) {
		
		URL url = null;
		try {
			url = new URL(urlText);
			InputStream is = (InputStream) url.getContent();
			Drawable d = Drawable.createFromStream(is, "src");

			return d;
		} catch (Exception ex) {
			Log.e("GET_IMAGE", urlText);
		}
		return null;
	}


	@Override
	public void bindView(View v, Context context, Cursor c) {
		
		int nameCol = c.getColumnIndex("name");
		String name = c.getString(nameCol);

		int textCol = c.getColumnIndex("tweet_text");
		String tweetText = c.getString(textCol);

		int idCol = c.getColumnIndex("_id");
		String id = c.getString(idCol);

		int screenNameCol = c.getColumnIndex("screen_name");
		String screenName = c.getString(screenNameCol);
		
		int imageDataCol = c.getColumnIndex("data");
		byte[] b = c.getBlob(imageDataCol);
		if (b == null || b.length <= 0) {
			ImageView imgView = (ImageView) v.findViewById(R.id.user_icon);
			imgView.setImageResource(R.drawable.sina_logo);
		}
		else {
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			ImageView imgView = (ImageView) v.findViewById(R.id.user_icon);
			imgView.setImageBitmap(bitmap);
		}

		TextView tweetTextView = (TextView) v.findViewById(R.id.ItemText);
		tweetTextView.setText(tweetText);

		TextView userText = (TextView) v.findViewById(R.id.ItemUser);
		userText.setText(name);

		TextView idView = (TextView) v.findViewById(R.id.ItemId);
		idView.setText(id);

		TextView screenNameView = (TextView) v.findViewById(R.id.ItemScreenName);
		screenNameView.setText(screenName);
		
		
//		int inReplyToNameCol = c.getColumnIndex("in_reply_to_screen_name");
//		String inReplyToName = c.getString(inReplyToNameCol);
//		TextView inReplyToView = (TextView) v.findViewById(R.id.ItemInReplyTo);
//		inReplyToView.setText("");
//		if (!inReplyToName.equals("null")) {
//			inReplyToView.setText(inReplyToName);
//		}
	}

	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = inflater.inflate(R.layout.tweet_item, parent, false);
		return v;
	}
}

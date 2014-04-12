package org.allblue.util;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedOAuthToken {

	private static SharedPreferences mSettings;
	private static String SHARED_INFO = "SHARED_INFO";

	private static final String TOKEN = "token";
	private static final String SECRET = "secret";
	
	private SharedOAuthToken() {}
	
	public static void saveRequestInformation(String token, String secret) {
		SharedPreferences.Editor editor = mSettings.edit();

		editor.putString(TOKEN, token);
		
		editor.putString(SECRET, secret);
		
		editor.commit();
	}

	public static void setSettings(Context context, int mode) {
		mSettings = context.getSharedPreferences(SHARED_INFO, mode);
	}

	public static String getRequestToken() {
		return mSettings.getString(TOKEN, null);
	}

	public static String getRequestSecret() {
		return mSettings.getString(SECRET, null);
	}
}

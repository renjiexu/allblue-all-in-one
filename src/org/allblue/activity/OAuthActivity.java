package org.allblue.activity;


import org.allblue.util.SharedOAuthToken;
import org.allblue.metadata.Account;
import org.allblue.util.XMLUtil;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.http.HttpParameters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


public class OAuthActivity extends Activity {
	
	private OAuthConsumer oauthConsumer = null;
	private OAuthProvider mProvider = null;
	
	private String accountType;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Intent oauthIntent = this.getIntent();
		
		accountType = SettingActivity.getAccountType();
		
		String key = XMLUtil.getKey(accountType);
		String value = XMLUtil.getValue(accountType);
		String requestToken = XMLUtil.getRequestToken(accountType);
		String accessToken = XMLUtil.getAccessToken(accountType);
		String authorizeToken = XMLUtil.getAuthorized(accountType);

		oauthConsumer = Account.getOAuthConsumer(key, value, accountType, true);
		mProvider = Account.getOAuthProvider(requestToken, accessToken, authorizeToken, accountType);
		mProvider.setOAuth10a(true);
		
		SharedOAuthToken.setSettings(this, Context.MODE_PRIVATE);
		if (oauthIntent.getData() == null) {
			try {
				String authUrl = mProvider.retrieveRequestToken(oauthConsumer, "myallblue://twroid");
				SharedOAuthToken.saveRequestInformation(oauthConsumer.getToken(), oauthConsumer.getTokenSecret());
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void onResume() {
		super.onResume();

		Uri uri = getIntent().getData();
		
		if (uri != null) {
			Intent i = new Intent(this, SettingActivity.class);

			try {
				oauthConsumer.setTokenWithSecret(SharedOAuthToken.getRequestToken(), SharedOAuthToken.getRequestSecret());
				
				HttpParameters params =  new HttpParameters();
				params.put(OAuth.OAUTH_VERIFIER, uri.getQueryParameter(OAuth.OAUTH_VERIFIER));
				oauthConsumer.setAdditionalParameters(params);
				
				mProvider.retrieveAccessToken(oauthConsumer, uri.getQueryParameter(OAuth.OAUTH_VERIFIER));
				
				final Account newAccount = Account.createAccountByType(accountType);
				newAccount.setType(accountType);
				
				newAccount.updateOAuthToken(this, oauthConsumer);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				startActivity(i);
				finish();
			}
		}
	}
}

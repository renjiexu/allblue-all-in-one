package org.allblue.metadata;

import java.sql.SQLException;
import java.util.LinkedList;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import org.allblue.adapter.SingleTabCursorAdapter;
import org.allblue.util.OAuthUtil;
import org.allblue.util.XMLUtil;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.allblue.activity.GlobalActivity;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ListView;

@DatabaseTable
public class Account implements Parcelable {

    @DatabaseField(generatedId = true)
    protected int id;

    @DatabaseField
	protected String name;

    @DatabaseField
	protected String type;

    @DatabaseField
    protected String token;

    @DatabaseField
    protected String secret;
	
	protected OAuthConsumer oauthConsumer;

	private ListView tweetsListView;
	
	private SingleTabCursorAdapter cursorAdapter;
	
	private Cursor cursor;
	
	protected Activity activity;
	
	
	public Account() {}
	
	public Account(String name, String type, String token, String secret) {
		this.name = name;
        this.type = type;
        this.token = token;
        this.secret = secret;
	}
	
	/*
	 * Parcelable constructor 
	 */
    public Account(Parcel source) {
    	name = source.readString();
    	type = source.readString();
    }
    
    protected OAuthConsumer getOAuthConsumerInstance() {
    	if (oauthConsumer == null) {
    		oauthConsumer = OAuthUtil.getConsumer(getName(), getType());
    	}
    	return oauthConsumer;
    }

    public int getId() {
        return id;
    }
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

    public String getName() {
        return name;
    }

	public void setName(String name) {
		this.name = name;
	}

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }
	
	public static Account createAccountByType(String type) {
		if (type.equalsIgnoreCase(AccountTypes.TWITTER)) {
			return new TwitterAccount();
		}
		
		if (type.equalsIgnoreCase(AccountTypes.SINA)) {
			return new SinaAccount();
		}
		return null;
	}
	
	public void postText(Activity activity, String text) {
		
		OAuthConsumer oauthConsumer = getOAuthConsumerInstance();
		
		DefaultHttpClient mClient = new DefaultHttpClient();
		
		String UPDATE_API = XMLUtil.getUpdate(getType());
		
		HttpPost post = new HttpPost(UPDATE_API);
		LinkedList<BasicNameValuePair> out = new LinkedList<BasicNameValuePair>();
		out.add(new BasicNameValuePair("status", text));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(out, HTTP.UTF_8));
			post.setParams(getParams());
			oauthConsumer.sign(post);
			mClient.execute(post, new BasicResponseHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refresh(Activity activity) {
	/*	 TODO
		this.activity = activity;
		cursor = DatabaseHandler.getSelectTweetCursor(activity, name, getType());
		cursorAdapter = new SingleTabCursorAdapter(activity, cursor, true);
		
		tweetsListView = (ListView) activity.findViewById(R.id.tweetList);
		tweetsListView.setAdapter(cursorAdapter);
		*/
		
		new RefreshTask().execute();
	}
	
	/**
	 * Need to get rid of this semi-builder pattern
	 * @param tweetJSONObj
	 * @return
	 */
	protected TweetItem parseTweetJSONObject(JSONObject tweetJSONObj) {
				
		try {
			String tweetId = tweetJSONObj.getString("id");
			
			String text = tweetJSONObj.getString("text");
	
			JSONObject user = tweetJSONObj.getJSONObject("user");
			
			String name = user.getString("name");
			
			String screenName = user.getString("screen_name");
			
			String userId = user.getString("id");
			
			TweetItem tweetItem = new TweetItem.Builder(Long.valueOf(tweetId), Long.valueOf(userId), name, screenName, text).build();
			
			return tweetItem;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private class RefreshTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			doRefresh();
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if (cursor.requery()) {
				cursorAdapter.notifyDataSetChanged();
			}
		}
	}
	
	public void doRefresh() {
		try {
			OAuthConsumer oauthConsumer = getOAuthConsumerInstance();
			String query = XMLUtil.getHomeline(getType());
			DefaultHttpClient mClient = new DefaultHttpClient();
			HttpGet get = new HttpGet(query);
			oauthConsumer.sign(get);

			String response = mClient.execute(get, new BasicResponseHandler());
			JSONArray array = new JSONArray(response);
			for (int i = 0; i < array.length(); ++i) {
				JSONObject tweetJSONObj = array.getJSONObject(i);
				TweetItem tweetItem = parseTweetJSONObject(tweetJSONObj);
                addTweetItem(tweetItem);
			}

			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private void addTweetItem(TweetItem tweetItem) {
        Dao<TweetItem, Integer> tweetItemDao = GlobalActivity.getTweetItemDao();
        if (tweetItemDao != null) {
            try {
                tweetItem.setAccountId(getId());
                tweetItemDao.create(tweetItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

	public HttpParams getParams() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		return params;
	}

	/*****************************
	 * Following is for Parcelable
	 *****************************/
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(type);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

		@Override
        public Account [] newArray(int size) {
            return new Account[size];
        }
    };


	public void updateOAuthToken(Activity activity, OAuthConsumer oauthConsumer) throws Exception {
        String getUserProfile = XMLUtil.getVerifyCredentials(getType());
        DefaultHttpClient mClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(getUserProfile);
        oauthConsumer.sign(get);

        String response = mClient.execute(get, new BasicResponseHandler());
        JSONObject userProfileJson = new JSONObject(response);
        String screenName = userProfileJson.getString("screen_name");

        Dao<Account, Integer> accountDao = GlobalActivity.getAccountDao();
        Account account = new Account(screenName, getType(), oauthConsumer.getToken(), oauthConsumer.getTokenSecret());
        accountDao.create(account);
	}

	public static OAuthConsumer getOAuthConsumer(String key, String value, String accountType) {
		return getOAuthConsumer(key, value, accountType, false);
	}
	
	public static OAuthConsumer getOAuthConsumer(String key, String value, String accountType, boolean callBack) {
		return new CommonsHttpOAuthConsumer(key, value);
	}

	public static OAuthProvider getOAuthProvider(String requestToken,
			String accessToken, String authorizeToken, String accountType) {
		return new CommonsHttpOAuthProvider(requestToken, accessToken, authorizeToken);
	}
}

package org.allblue.metadata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

@DatabaseTable
public class TweetItem {
	/**
	 *  {
		    "created_at": "Fri Jul 16 16:58:46 +0000 2010",
		    "text": "got a lovely surprise from @craftybeans. She sent me the best tshirt ever. http://www.flickr.com/photos/cindyli/4799054041/ ::giggles::",
		    "id": 18700887835,
		    "in_reply_to_user_id": null,
		    "place": null,
		    "in_reply_to_screen_name": null,
		    "user": {
		      "name": "cindy li",
		      "profile_image_url": "http://a1.twimg.com/profile_images/553508996/43082001_N00_normal.jpg",
		      "location": "San Francisco, CA",
		      "id": 29733,
		      "screen_name": "cindyli"
		    },
		    "source": "web",
		    "in_reply_to_status_id": null
		  },
	 */
    @DatabaseField
	private long tweetId;

    @DatabaseField
	private long userId;

    @DatabaseField
	private String name;

    @DatabaseField
	private String screenName;

    @DatabaseField
	private String text;

    @DatabaseField
    private int accountId;
	
	// Optional parameters
	private String createAt;

	private long inReplyToUserId;

	private String place;

	private String inReplyToScreenName;

	private String source;

	private long inReplyToStatusId;

	private String location;

	private String profileImageUrl;
	
	byte [] imageData;

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

	public TweetItem(Builder builder) {
		this.tweetId = builder.tweetId;
		this.userId = builder.userId;
		this. name = builder.name;
		this.screenName = builder.screenName;
		this.text = builder.text;
		this.createAt = builder.createAt;
		this.inReplyToUserId = builder.inReplyToUserId;
		this.place = builder.place;
		this.inReplyToScreenName = builder.inReplyToScreenName;
		this.source = builder.source;
		this.inReplyToStatusId = builder.inReplyToStatusId;
		this.location = builder.location;
		this.profileImageUrl = builder.profileImageUrl;
		
		if (this.profileImageUrl != null) {
			imageData = downloadProfileImage(this.profileImageUrl);
		}
	}

	
	public static class Builder {
		
		// Required paramters
		private long tweetId;
		
		private long userId;
		
		private String name;
		
		private String screenName;
		
		private String text;
		
		
		// Optional parameters
		private String createAt;
		
		private long inReplyToUserId;
		
		private String place;
		
		private String inReplyToScreenName;
		
		private String source;
		
		private long inReplyToStatusId;
		
		private String location;
		
		private String profileImageUrl;
		
		
		public Builder(long tweetId, long userId, String name, String screenName, String text) {
			this.tweetId = tweetId;
			this.userId = userId;
			this. name = name;
			this.screenName = screenName;
			this.text = text;
		}
		
		public Builder createAt(String createAt) {
			this.createAt = createAt;
			return this;
		}
		
		public Builder inReplyToUserId(long inReplyToUserId){
			this.inReplyToUserId = inReplyToUserId;
			return this;
		}
		
		public Builder place(String place) {
			this.place = place;
			return this;
		}
		
		public Builder inReplyToScreenName(String inReplyToScreenName) {
			this.inReplyToScreenName = inReplyToScreenName;
			return this;
		}
		
		public Builder source(String source) {
			this.source = source;
			return this;
		}
		
		public Builder inReplyToStatusId(long inReplyToStatusId) {
			this.inReplyToStatusId = inReplyToStatusId;
			return this;
		}
		
		public Builder location(String location) {
			this.location = location;
			return this;
		}
		
		public Builder profileImageUrl(String profileImageUrl) {
			this.profileImageUrl = profileImageUrl;
			return this;
		}
		
		public TweetItem build() {
			return new TweetItem(this);
		}
	}
	

	private byte[] downloadProfileImage(String profileImageUrl) {
		DefaultHttpClient mHttpClient = new DefaultHttpClient();

		HttpGet mHttpGet = new HttpGet(profileImageUrl);

		try {
		HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);

		if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

		  HttpEntity entity = mHttpResponse.getEntity();

		    if ( entity != null) {
		    	
		      byte[] result = EntityUtils.toByteArray(entity);

		      return result;
		    }
		}
		} catch (Exception ex) {
			Log.e("GET PROF IMG", "ERROR");
		}
		return null;
	}
}

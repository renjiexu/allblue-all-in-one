package org.allblue.metadata;

import org.json.JSONException;
import org.json.JSONObject;

public class TwitterAccount extends Account {
	public TwitterAccount() {}

	protected TweetItem parseTweetJSONObject(JSONObject tweetJSONObj) {
		try {
			
			String tweetId = tweetJSONObj.getString("id");
			
			String text = tweetJSONObj.getString("text");
	
			JSONObject user = tweetJSONObj.getJSONObject("user");
			
			String name = user.getString("name");
			
			String screenName = user.getString("screen_name");
			
			String userId = user.getString("id");
			
			String profileImageUrl = user.getString("profile_image_url");
			
			String location = user.getString("location");
			
			
			String createAt = tweetJSONObj.getString("created_at");
			
			String inReplyToUserId = tweetJSONObj.getString("in_reply_to_user_id") == null ? tweetJSONObj.getString("in_reply_to_user_id") : "-1";
			
			String inReplyToScreenName = tweetJSONObj.getString("in_reply_to_screen_name");
			
			String source = tweetJSONObj.getString("source");
			
			String inReplyToStatusId = tweetJSONObj.getString("in_reply_to_status_id") == null ? tweetJSONObj.getString("in_reply_to_status_id") : "-1";
			
			
			return new TweetItem.Builder(Long.valueOf(tweetId), Long.valueOf(userId), name, screenName, text)
					.createAt(createAt).inReplyToScreenName(inReplyToScreenName).inReplyToStatusId(Long.valueOf(inReplyToStatusId))
					.inReplyToUserId(Long.valueOf(inReplyToUserId)).location(location).source(source).profileImageUrl(profileImageUrl)
					.build();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}

package org.allblue.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.j256.ormlite.dao.Dao;
import oauth.signpost.OAuthConsumer;
import org.allblue.activity.GlobalActivity;
import org.allblue.metadata.Account;

public class OAuthUtil {
	public static OAuthConsumer getConsumer(final String accountName, final String accountType) {
		String key = XMLUtil.getKey(accountType);
		String value = XMLUtil.getValue(accountType);
		
		OAuthConsumer oauthConsumer = Account.getOAuthConsumer(key, value, accountType);
        Dao<Account, Integer> accountDao = GlobalActivity.getAccountDao();
        try {
            Map keyValue = new HashMap() {{
                put("name", accountName);
                put("type", accountType);
            }};
            List<Account> accounts = accountDao.queryForFieldValues(keyValue);
            if (accounts != null) {
                Account account = accounts.get(0); // it should be only one return value
                oauthConsumer.setTokenWithSecret(account.getToken(), account.getSecret());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return oauthConsumer;
	}
}

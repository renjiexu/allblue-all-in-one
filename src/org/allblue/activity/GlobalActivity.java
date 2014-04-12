package org.allblue.activity;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.*;
import org.allblue.database.DatabaseHelper;
import org.allblue.metadata.*;

import java.sql.SQLException;

public class GlobalActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private static GlobalActivity INSTANCE;
    private static Context context;

    private GlobalActivity() {
        context = getApplicationContext();
    }

    private Context getContext() {
        return context;
    }

    public static Context getGlobalContext() {
        if (INSTANCE == null) {
            synchronized (GlobalActivity.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GlobalActivity();
                }
            }
        }
        return INSTANCE.getContext();
    }

    public static Dao<Account, Integer> getAccountDao() {
        try {
            return INSTANCE.getHelper().getAccountDao();
        } catch (SQLException e) {
            Log.i(GlobalActivity.class.getName(), "Failed to get account dao", e);
            return null;
        }
    }

    public static Dao<TweetItem, Integer> getTweetItemDao() {
        try {
            return INSTANCE.getHelper().getTweetItemDao();
        } catch (SQLException e) {
            Log.i(GlobalActivity.class.getName(), "Failed to get tweetItem dao", e);
            return null;
        }
    }
}

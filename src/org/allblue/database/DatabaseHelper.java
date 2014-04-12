package org.allblue.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.allblue.R;
import org.allblue.metadata.*;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static String DATABASE_NAME = "allblue.db";
    private static int DATABASE_VERSION = 1;

    private Dao<Account, Integer> accountDao;
    private Dao<TweetItem, Integer> tweetItemDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_conf);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "create tables");
            TableUtils.createTable(connectionSource, Account.class);
            TableUtils.createTable(connectionSource, TweetItem.class);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tables");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
    }

    public Dao<Account, Integer> getAccountDao() throws SQLException {
        if (accountDao == null) {
            synchronized (this) {
                if (accountDao == null) {
                    accountDao = getDao(Account.class);
                }
            }
        }
        return accountDao;
    }

    public Dao<TweetItem, Integer> getTweetItemDao() throws SQLException {
        if (tweetItemDao == null) {
            synchronized (this) {
                if (tweetItemDao == null) {
                    tweetItemDao = getDao(TweetItem.class);
                }
            }
        }
        return tweetItemDao;
    }
}

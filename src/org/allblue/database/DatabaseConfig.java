package org.allblue.database;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import org.allblue.metadata.Account;
import org.allblue.metadata.TweetItem;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseConfig extends OrmLiteConfigUtil {
    private static Class [] classes = new Class [] {
            Account.class,
            TweetItem.class
    };

    public static void main(String [] args) throws IOException, SQLException {
        writeConfigFile("ormlite_conf.txt", classes);
    }
}

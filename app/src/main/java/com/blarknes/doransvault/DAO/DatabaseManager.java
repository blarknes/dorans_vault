package com.blarknes.doransvault.DAO;

import android.content.Context;

public class DatabaseManager {

    private static com.blarknes.doransvault.DAO.DatabaseManager instance;
    private DataBaseHelper helper;

    public static void init(Context ctx) {
        if (null == instance) {
            instance = new com.blarknes.doransvault.DAO.DatabaseManager(ctx);
        }
    }

    public static com.blarknes.doransvault.DAO.DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseManager(Context ctx) {
        helper = new DataBaseHelper(ctx);
    }

    public DataBaseHelper getHelper() {
        return helper;
    }
}

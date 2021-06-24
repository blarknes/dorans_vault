package com.blarknes.doransvault.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.blarknes.doransvault.model.Conta;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String databaseName = "AccountsDatabase.db";
    private static final int databaseVersion = 4;

    public DataBaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource src) {
        try{
            TableUtils.createTable(src, Conta.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource src, int oldVersion, int newVersion) {
        try{
            if (oldVersion != newVersion) {
                TableUtils.dropTable(src, Conta.class, true);
                onCreate(db, src);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
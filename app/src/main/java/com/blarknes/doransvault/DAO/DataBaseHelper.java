package com.blarknes.doransvault.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.blarknes.doransvault.model.Usuario;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    public DataBaseHelper(Context context) {
        super(context, "sistema.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource src) {
        try{
            TableUtils.createTable(src, Usuario.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource src, int oldVersion, int newVersion) {
        try{
            TableUtils.dropTable(src, Usuario.class, true);
            onCreate(db, src);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
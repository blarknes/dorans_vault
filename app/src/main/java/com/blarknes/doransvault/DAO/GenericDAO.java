package com.blarknes.doransvault.DAO;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.util.List;

public class GenericDAO {

    protected Dao dao;
    private Class type;

    public GenericDAO(Context context, Class type) {
        com.blarknes.doransvault.DAO.DatabaseManager.init(context);
        this.type = type;
        setDao();
    }

    protected void setDao() {
        try{
            dao = DaoManager.createDao(com.blarknes.doransvault.DAO.DatabaseManager.getInstance().getHelper().getConnectionSource(), type);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List getAll() {
        try{
            List result = dao.queryForAll();
            return result;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Object getById(int id) {
        try{
            return dao.queryForId(id);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void insert(Object obj) {
        try{
            dao.create(obj);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void delete(Object obj) {
        try{
            dao.delete(obj);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void update(Object obj) {
        try{
            dao.update(obj);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

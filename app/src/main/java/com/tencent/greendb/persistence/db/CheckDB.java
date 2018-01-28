package com.tencent.greendb.persistence.db;


import com.tencent.greendb.logic.ContextHolder;
import com.tencent.greendb.persistence.entity.DaoMaster;
import com.tencent.greendb.persistence.entity.DaoSession;
import com.tencent.greendb.persistence.entity.FileItemDao;

import org.greenrobot.greendao.database.Database;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ccccchen on 2018/1/15.
 */

public class CheckDB {

    private Map<String,DaoSession> sessions = new HashMap<>();

    private static class SingletonHolder {
        private static final CheckDB INSTANCE = new CheckDB();
    }

    public static CheckDB getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private CheckDB(){}

    public FileItemDao getFileItemDao(String setID){
        if(sessions.get(setID)!=null)
            return sessions.get(setID).getFileItemDao();
        DaoMaster.OpenHelper helper = new CheckDBOpenHelper(ContextHolder.getAppContext(),getSetDBName(setID));
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        sessions.put(setID, daoSession);
        return daoSession.getFileItemDao();
    }

    public String getSetDBName(String setID){
        return "_"+setID+"_check.db";
    }

}

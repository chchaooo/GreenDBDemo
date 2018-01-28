package com.tencent.greendb.persistence.db;

import android.content.Context;


import com.tencent.greendb.persistence.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ccccchen on 2018/1/15.
 */

public class CheckDBOpenHelper extends DaoMaster.OpenHelper {

    public CheckDBOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}

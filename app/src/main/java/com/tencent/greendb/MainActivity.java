package com.tencent.greendb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tencent.greendb.logic.ContextHolder;
import com.tencent.greendb.persistence.db.CheckDB;
import com.tencent.greendb.persistence.entity.FileItem;
import com.tencent.greendb.persistence.entity.FileItemDao;
import com.tencent.greendb.persistence.repo.FileItemRepo;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.query.Query;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

//    final int[] BASE = {0,1000,2000,5000,10000,20000,50000,100000,150000,200000,360000};
//    final int[] BASE = {1000,2000,5000,10000,20000};
    final int[] BASE = {20010,50000,360000};
//    final int[] BASE = {200};
    final int NUM = 20000;
    String GROUP_ID = "wedifjkdlsdfadfadfccdfsdfsdf";
    private TextView mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextHolder.getInstance().setContext(MainActivity.this);
        initView();
        test();
    }

    private void initView() {
        mInfo = (TextView)findViewById(R.id.info);
    }

    private String prepareDB(int dbIndex) {
        long prepareStart = System.currentTimeMillis();
        List<FileItem> base = new ArrayList<>();
        for(int i=0; i<BASE[dbIndex];i++){
            FileItem f = new FileItem(null, getDBName(dbIndex), GROUP_ID, getPicName(i), false, false, false, 70l);
            base.add(f);
        }
        FileItemRepo.instance().batchInsert(getDBName(dbIndex),base);
        long preparenEnd = System.currentTimeMillis();
        return " prepareDB:"+(preparenEnd-prepareStart)+"ms\t\t";
    }

    private String batchInsert(int dbIndex) {
        final List<FileItem> list1 = new ArrayList<>();
        for (int i = 0; i < NUM; i++) {
            FileItem f = new FileItem(null, getDBName(dbIndex), GROUP_ID, getPicName(i), false, false, false, 70l);
            list1.add(f);
        }
        long insertStart = System.currentTimeMillis();
        FileItemRepo.instance().batchInsert(getDBName(dbIndex),list1);
        long insertEnd = System.currentTimeMillis();
        return " batchInsert:"+(insertEnd-insertStart)+"ms\t\t";
    }

    private String patchOperation(final int dbIndex){
        long queryStart = System.currentTimeMillis();
        CheckDB.getInstance().getFileItemDao(getDBName(dbIndex)).getSession()
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        Query<FileItem> query = CheckDB.getInstance().getFileItemDao(getDBName(dbIndex)).queryBuilder()
                                .where(FileItemDao.Properties.SubSetID.eq(GROUP_ID),
                                        FileItemDao.Properties.FileName.eq("")).build();
                        for(int i=0; i<NUM; i++) {
                            query.setParameter(1,getPicName(BASE[dbIndex]-NUM+i));
                            try {
                                FileItem item = query.unique();
                            }catch (DaoException e){
//                                e.printStackTrace();
                            }
                        }
                    }
                });
        long queryEnd = System.currentTimeMillis();
        return " patchQuery:"+(queryEnd-queryStart)+"ms\t\t";
    }

    private String patchOperationNotExist(final int dbIndex){
        long queryStart = System.currentTimeMillis();
        CheckDB.getInstance().getFileItemDao(getDBName(dbIndex)).getSession()
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        Query<FileItem> query = CheckDB.getInstance().getFileItemDao(getDBName(dbIndex)).queryBuilder()
                                .where(FileItemDao.Properties.SubSetID.eq(GROUP_ID),
                                        FileItemDao.Properties.FileName.eq("")).build();
                        for(int i=0; i<NUM; i++) {
                            query.setParameter(1,getPicName(BASE[dbIndex]-NUM+i)+"notfound");
                            try {
                                FileItem item = query.unique();
                            }catch (DaoException e){
//                                e.printStackTrace();
                            }
//                            if(item!=null) {
//                                FileItemRepo.instance().updateRecordExist(getDBName(dbIndex), GROUP_ID, getPicName(BASE[dbIndex] - NUM + i));
//                            }else{
//                                FileItemRepo.instance().insert();
//                            }
                        }
                    }
                });
        long queryEnd = System.currentTimeMillis();
        return " patchQueryNotExist:"+(queryEnd-queryStart)+"ms\t\t";
    }

    private String patchQueryBack(final int dbIndex){
        long queryStart = System.currentTimeMillis();
        CheckDB.getInstance().getFileItemDao(getDBName(dbIndex)).getSession()
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<NUM; i++) {
                            List<FileItem> out = FileItemRepo.instance().queryByFilename(getDBName(dbIndex), GROUP_ID, getPicName(i));
                        }
                    }
                });
        long queryEnd = System.currentTimeMillis();
        return " patchQuery:"+(queryEnd-queryStart)+"ms\t\t";
    }

    private String patchUpdate(final int dbIndex){
        long updateStart = System.currentTimeMillis();
        CheckDB.getInstance().getFileItemDao(getDBName(dbIndex)).getSession()
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<NUM; i++) {
                            FileItemRepo.instance().updateRecordExist(getDBName(dbIndex), GROUP_ID, getPicName(i));
                        }
                    }
                });
        long updateEnd = System.currentTimeMillis();
        return " patchUpdate:"+(updateEnd-updateStart)+"ms\t\t";
    }

    private void test() {
        Log.v("static","mainThread1");
        Log.v("static","mainThread2");
        Observable.just(1)
                .map(new Func1<Integer, Object>() {
                    @Override
                    public Object call(Integer integer) {
                        Log.v("static","workThread");
                        Log.v("static","workThread");
                        for(int dbIndex=0; dbIndex<BASE.length; dbIndex++) {
                            String out = new String();
                            out += prepareDB(dbIndex);
                            out += prepareDB(dbIndex);
//                            out += batchInsert(dbIndex);
//                            out += oneByoneInsert(dbIndex);
//                            out += oneByoneManualPatchInsert(dbIndex);
//                            out += oneByoneQuery(dbIndex);
//                            out += oneByoneQueryNotExist(dbIndex);
                            out += patchOperation(dbIndex);
                            out += patchOperationNotExist(dbIndex);
//                            out += oneByoneUpdate(dbIndex);
//                            out += oneByoneUpdateNotExist(dbIndex);
//                            out += patchUpdate(dbIndex);
                            Log.v("Static", getDBName(dbIndex)+out);
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private String oneByoneInsert(int dbIndex) {
        final List<FileItem> list1 = new ArrayList<>();
        for (int i = 0; i < NUM; i++) {
            FileItem f = new FileItem(null, getDBName(dbIndex), GROUP_ID, getPicName(i), false, false, false, 70l);
            list1.add(f);
        }
        long insertStart = System.currentTimeMillis();
        for(int i=0; i<NUM;i++){
            FileItemRepo.instance().insert(getDBName(dbIndex),list1.get(i));
        }
        long insertEnd = System.currentTimeMillis();
        return " oneByoneInsert:"+(insertEnd-insertStart)+"ms\t\t";
    }

    private String oneByoneManualPatchInsert(final int dbIndex) {
        final List<FileItem> list1 = new ArrayList<>();
        for (int i = 0; i < NUM; i++) {
            FileItem f = new FileItem(null, getDBName(dbIndex), GROUP_ID, getPicName(i), false, false, false, 70l);
            list1.add(f);
        }
        long insertStart = System.currentTimeMillis();
        CheckDB.getInstance().getFileItemDao(getDBName(dbIndex)).getSession()
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<NUM;i++){
                            FileItemRepo.instance().insert(getDBName(dbIndex),list1.get(i));
                        }
                    }
                });
        long insertEnd = System.currentTimeMillis();
        return " oneByoneManualPatchInsert:"+(insertEnd-insertStart)+"ms\t\t";
    }

    private String oneByoneQuery(int dbIndex){
        long queryStart = System.currentTimeMillis();
        for(int i=0; i<NUM; i++) {
            FileItemRepo.instance().queryByFilename(getDBName(dbIndex), GROUP_ID, getPicName(i));
        }
        long queryEnd = System.currentTimeMillis();
        return " oneByoneQuery:"+(queryEnd-queryStart)+"ms\t\t";
    }

    private String oneByoneQueryNotExist(int dbIndex){
        long queryStart = System.currentTimeMillis();
        for(int i=0; i<NUM; i++) {
            FileItemRepo.instance().queryByFilename(getDBName(dbIndex), GROUP_ID, getPicName(i)+"notfound");
        }
        long queryEnd = System.currentTimeMillis();
        return " oneByoneQueryNotExist:"+(queryEnd-queryStart)+"ms\t\t";
    }

    private String oneByoneUpdate(int dbIndex){
        long updateStart = System.currentTimeMillis();
        for(int i=0; i<NUM; i++) {
            FileItemRepo.instance().updateRecordExist(getDBName(dbIndex), GROUP_ID, getPicName(i));
        }
        long updateEnd = System.currentTimeMillis();
        return " oneByoneUpdate:"+(updateEnd-updateStart)+"ms\t\t";
    }

    private String oneByoneUpdateNotExist(int dbIndex){
        long updateStart = System.currentTimeMillis();
        for(int i=0; i<NUM; i++) {
            FileItemRepo.instance().updateRecordExist(getDBName(dbIndex), GROUP_ID, getPicName(i)+"notfound");
        }
        long updateEnd = System.currentTimeMillis();
        return " oneByoneUpdateNotExist:"+(updateEnd-updateStart)+"ms\t\t";
    }



    private String getPicName(int i){
        return "pic"+i+".jpg";
    }

    private String getDBName(int i){
        return "DB"+BASE[i];
    }
}

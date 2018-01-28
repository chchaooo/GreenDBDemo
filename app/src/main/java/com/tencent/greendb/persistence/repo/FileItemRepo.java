package com.tencent.greendb.persistence.repo;


import com.tencent.greendb.logic.UploadConst;
import com.tencent.greendb.persistence.db.CheckDB;
import com.tencent.greendb.persistence.entity.FileItem;
import com.tencent.greendb.persistence.entity.FileItemDao;
import org.greenrobot.greendao.query.CursorQuery;
import java.util.List;

/**
 * Created by cathy on 2017/10/13.
 */

public class FileItemRepo {

    public static FileItemRepo instance() {
        return Singleton.INSTANCE;
    }

    private static final class Singleton {
        private static FileItemRepo INSTANCE = new FileItemRepo();
    }

    private FileItemRepo() {}

    public void insert(String setID, FileItem item){
        CheckDB.getInstance().getFileItemDao(setID).insert(item);
    }

    public void batchInsert(String setID, List<FileItem> list){
        CheckDB.getInstance().getFileItemDao(setID).insertInTx(list);
    }

    public void batchUpdate(String setID, List<FileItem> list){
        CheckDB.getInstance().getFileItemDao(setID).updateInTx(list);
    }

    public List<FileItem> queryBySubsetID(String setID, String subSetID){
        return CheckDB.getInstance().getFileItemDao(setID).queryBuilder()
                .where(FileItemDao.Properties.SetID.eq(setID),FileItemDao.Properties.SubSetID.eq(subSetID))
                .list();
    }

    public CursorQuery queryCursorBySubsetID(String setID, String subSetID){
        return CheckDB.getInstance().getFileItemDao(setID).queryBuilder()
                .where(FileItemDao.Properties.SetID.eq(setID),FileItemDao.Properties.SubSetID.eq(subSetID))
                .buildCursor();
    }

    public List<FileItem> queryByFilename(String setID, String subSetID, String fileName) {
        return CheckDB.getInstance().getFileItemDao(setID).queryBuilder()
                .where(FileItemDao.Properties.SetID.eq(setID),FileItemDao.Properties.SubSetID.eq(subSetID),
                        FileItemDao.Properties.FileName.eq(fileName))
                .list();
    }

    public long queryRecordTLocalFSize(String setID) {
        return CheckDB.getInstance().getFileItemDao(setID).queryBuilder()
                .where(FileItemDao.Properties.SetID.eq(setID)
                ,FileItemDao.Properties.Record_exist.eq(Boolean.TRUE)
                ,FileItemDao.Properties.LocalExist.eq(Boolean.FALSE))
                .count();
    }

    public long queryLocalTCloudFSize(String setID) {
        return CheckDB.getInstance().getFileItemDao(setID).queryBuilder()
                .where(FileItemDao.Properties.SetID.eq(setID)
                        ,FileItemDao.Properties.LocalExist.eq(Boolean.TRUE)
                        ,FileItemDao.Properties.CloudExist.eq(Boolean.FALSE))
                .count();
    }

    public long getRecordSize(String setID){
        return CheckDB.getInstance().getFileItemDao(setID).queryBuilder()
                .where(FileItemDao.Properties.SetID.eq(setID)
                        ,FileItemDao.Properties.Record_exist.eq(Boolean.TRUE))
                .count();
    }

    public void updateRecordExist(String setID, String subSetID, String fileName){
        CheckDB.getInstance().getFileItemDao(setID).getDatabase()
                .execSQL("UPDATE " + "file_table"
                        + " SET " + "record_exist"+ " = " + UploadConst.UPLOADED
                        + " WHERE " + "file_name" + " = '" + fileName + "'");
    }

    public void updateLocalExist(String setID, String subSetID, String fileName){
        CheckDB.getInstance().getFileItemDao(setID).getDatabase()
                .execSQL("UPDATE " + "file_table"
                        + " SET " + "local_exist"+ " = " + UploadConst.UPLOADED
                        + " WHERE " + "file_name" + " = '" + fileName + "'");
    }

    public void updateCloudExist(String setID, String subSetID, String fileName){
        CheckDB.getInstance().getFileItemDao(setID).getDatabase()
                .execSQL("UPDATE " + "file_table"
                        + " SET " + "cloud_exist"+ " = " + UploadConst.UPLOADED
                        + " WHERE " + "file_name" + " = '" + fileName + "'");
    }
}

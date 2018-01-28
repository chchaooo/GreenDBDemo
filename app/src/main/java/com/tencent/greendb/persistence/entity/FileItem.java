package com.tencent.greendb.persistence.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by ccccchen on 2018/1/15.
 */

@Entity(nameInDb = "file_table")
public class FileItem implements Serializable {

    @Transient
    private static final long serialVersionUID = 5475491552628336406L;

    @Id(autoincrement = true)
    public Long ID;

    @Property(nameInDb = "setid")
    public String setID;

    @Property(nameInDb = "sub_set_id")
    public String subSetID;

    @Property(nameInDb = "file_name")
    @Index
    public String fileName;

    @Property(nameInDb = "record_exist")
    public Boolean record_exist;

    @Property(nameInDb = "local_exist")
    public Boolean localExist;

    @Property(nameInDb = "cloud_exist")
    public Boolean cloudExist;

    @Property(nameInDb = "local_size")
    public Long localSize;

    @Generated(hash = 327245703)
    public FileItem(Long ID, String setID, String subSetID, String fileName,
                    Boolean record_exist, Boolean localExist, Boolean cloudExist,
                    Long localSize) {
        this.ID = ID;
        this.setID = setID;
        this.subSetID = subSetID;
        this.fileName = fileName;
        this.record_exist = record_exist;
        this.localExist = localExist;
        this.cloudExist = cloudExist;
        this.localSize = localSize;
    }

    @Generated(hash = 1388869346)
    public FileItem() {
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getSetID() {
        return this.setID;
    }

    public void setSetID(String setID) {
        this.setID = setID;
    }

    public String getSubSetID() {
        return this.subSetID;
    }

    public void setSubSetID(String subSetID) {
        this.subSetID = subSetID;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getRecord_exist() {
        return this.record_exist;
    }

    public void setRecord_exist(Boolean record_exist) {
        this.record_exist = record_exist;
    }

    public Boolean getLocalExist() {
        return this.localExist;
    }

    public void setLocalExist(Boolean localExist) {
        this.localExist = localExist;
    }

    public Boolean getCloudExist() {
        return this.cloudExist;
    }

    public void setCloudExist(Boolean cloudExist) {
        this.cloudExist = cloudExist;
    }

    public Long getLocalSize() {
        return this.localSize;
    }

    public void setLocalSize(Long localSize) {
        this.localSize = localSize;
    }

    @Override
    public String toString() {
        return "FileItem{" +
                "ID=" + ID +
                ", setID='" + setID + '\'' +
                ", subSetID='" + subSetID + '\'' +
                ", fileName='" + fileName + '\'' +
                ", record_exist=" + record_exist +
                ", localExist=" + localExist +
                ", cloudExist=" + cloudExist +
                ", localSize=" + localSize +
                '}';
    }
}

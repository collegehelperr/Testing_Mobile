package com.co.coller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class task {

    @SerializedName("id_task")
    @Expose
    private String idTask;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("detail_task")
    @Expose
    private String detailTask;
    @SerializedName("tgl_ddline")
    @Expose
    private String tglDdline;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id_jenis")
    @Expose
    private String idJenis;

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDetailTask() {
        return detailTask;
    }

    public void setDetailTask(String detailTask) {
        this.detailTask = detailTask;
    }

    public String getTglDdline() {
        return tglDdline;
    }

    public void setTglDdline(String tglDdline) {
        this.tglDdline = tglDdline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdJenis() {
        return idJenis;
    }

    public void setIdJenis(String idJenis) {
        this.idJenis = idJenis;
    }
}
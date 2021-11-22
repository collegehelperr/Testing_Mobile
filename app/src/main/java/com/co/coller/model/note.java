package com.co.coller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class note {

    @SerializedName("id_note")
    @Expose
    private String idNote;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("judul_note")
    @Expose
    private String judulNote;
    @SerializedName("tgl_note")
    @Expose
    private String tglNote;
    @SerializedName("isi_note")
    @Expose
    private String isiNote;

    public String getIdNote() {
        return idNote;
    }

    public void setIdNote(String idNote) {
        this.idNote = idNote;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJudulNote() {
        return judulNote;
    }

    public void setJudulNote(String judulNote) {
        this.judulNote = judulNote;
    }

    public String getTglNote() {
        return tglNote;
    }

    public void setTglNote(String tglNote) {
        this.tglNote = tglNote;
    }

    public String getIsiNote() {
        return isiNote;
    }

    public void setIsiNote(String isiNote) {
        this.isiNote = isiNote;
    }
}

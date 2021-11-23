package com.co.coller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class schedule {

    @SerializedName("id_schedule")
    @Expose
    private String idSchedule;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("hari")
    @Expose
    private String hari;
    @SerializedName("waktu_mulai")
    @Expose
    private String waktuMulai;
    @SerializedName("waktu_berakhir")
    @Expose
    private String waktuBerakhir;
    @SerializedName("nama_schedule")
    @Expose
    private String namaSchedule;

    public String getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(String idSchedule) {
        this.idSchedule = idSchedule;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(String waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public String getWaktuBerakhir() {
        return waktuBerakhir;
    }

    public void setWaktuBerakhir(String waktuBerakhir) {
        this.waktuBerakhir = waktuBerakhir;
    }

    public String getNamaSchedule() {
        return namaSchedule;
    }

    public void setNamaSchedule(String namaSchedule) {
        this.namaSchedule = namaSchedule;
    }
}

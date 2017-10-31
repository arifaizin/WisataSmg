package org.sandec.wisatasmg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by idn on 10/30/2017.
 */

public class WisataModel {
    @SerializedName("id_wisata")
    @Expose
    private String idWisata;
    @SerializedName("nama_wisata")
    @Expose
    private String namaWisata;
    @SerializedName("gambar_wisata")
    @Expose
    private String gambarWisata;
    @SerializedName("deksripsi_wisata")
    @Expose
    private String deksripsiWisata;
    @SerializedName("alamat_wisata")
    @Expose
    private String alamatWisata;
    @SerializedName("event_wisata")
    @Expose
    private String eventWisata;
    @SerializedName("latitude_wisata")
    @Expose
    private String latitudeWisata;
    @SerializedName("longitude_wisata")
    @Expose
    private String longitudeWisata;


    public String getIdWisata() {
        return idWisata;
    }

    public void setIdWisata(String idWisata) {
        this.idWisata = idWisata;
    }

    public String getNamaWisata() {
        return namaWisata;
    }

    public void setNamaWisata(String namaWisata) {
        this.namaWisata = namaWisata;
    }

    public String getGambarWisata() {
        return gambarWisata;
    }

    public void setGambarWisata(String gambarWisata) {
        this.gambarWisata = gambarWisata;
    }

    public String getDeksripsiWisata() {
        return deksripsiWisata;
    }

    public void setDeksripsiWisata(String deksripsiWisata) {
        this.deksripsiWisata = deksripsiWisata;
    }

    public String getAlamatWisata() {
        return alamatWisata;
    }

    public void setAlamatWisata(String alamatWisata) {
        this.alamatWisata = alamatWisata;
    }

    public String getEventWisata() {
        return eventWisata;
    }

    public void setEventWisata(String eventWisata) {
        this.eventWisata = eventWisata;
    }

    public String getLatitudeWisata() {
        return latitudeWisata;
    }

    public void setLatitudeWisata(String latitudeWisata) {
        this.latitudeWisata = latitudeWisata;
    }

    public String getLongitudeWisata() {
        return longitudeWisata;
    }

    public void setLongitudeWisata(String longitudeWisata) {
        this.longitudeWisata = longitudeWisata;
    }
}

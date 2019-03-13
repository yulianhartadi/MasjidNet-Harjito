package com.rawzadigital.masjidnet.model;

public class ModelEvent {
    public String name;
    private String address;
    private String deskripsi;
    private String ustadz;
    private String materi;
    private String thumbnail;
    private String brochure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getaddress() {
        return address;
    }

    public String getdeskripsi() {
        return deskripsi;
    }

    public String getustadz() {
        return ustadz;
    }

    public String getmateri() {
        return materi;
    }

    public String getImage() {
        return thumbnail;
    }

    public void setImage(String thumbnail) {
        if (thumbnail.equals("")) {
            this.thumbnail = "";
        } else {
            this.thumbnail = "http://masjid.fkam.or.id/upload/masjid/thumbnail/" + thumbnail;
        }
    }


    public String getbrochure() {
        return brochure;
    }

    public void setbrochure(String brochure) {
        if (brochure.equals("")) {
            this.brochure = "";
        } else {
            this.brochure = "http://masjid.fkam.or.id/upload/kajian/" + brochure;
        }
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public void setustadz(String ustadz) {
        this.ustadz = ustadz;
    }

    public void setmateri(String materi) {
        this.materi = materi;
    }
    public void setdeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

}

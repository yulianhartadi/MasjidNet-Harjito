package com.rawzadigital.masjidnet.model;

public class ModelMasjid {
    public String name;
    private String address;
    private String latitude;
    private String longitude;
    private String thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getaddress() {
        return address;
    }

    public String getlatitude() {
        return latitude;
    }

    public String getlongitude() {
        return longitude;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public void setlatitude(String latitude) {
        if (latitude.isEmpty()) {
            this.latitude = "";
        } else {
            this.latitude = latitude;
        }
    }

    public void setlongitude(String longitude) {
        if (longitude.isEmpty()) {
            this.longitude = "";
        } else {
            this.longitude = longitude;
        }
    }

    public String getImage() {
        return thumbnail;
    }

    public void setImage(String thumbnail) {
        if (thumbnail.equals("")) {
            this.thumbnail = "";
        } else if (thumbnail.startsWith("https://i.ytimg.com/vi/")) {
            this.thumbnail = thumbnail;
        } else {
            this.thumbnail = "http://masjid.fkam.or.id/upload/masjid/thumbnail/" + thumbnail;
        }
    }

}

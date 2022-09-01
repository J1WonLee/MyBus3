package com.example.mybus.apisearch.itemList;

import com.google.gson.annotations.SerializedName;

public class StopSchList {
    @SerializedName("stId")
    public String stId;
    @SerializedName("stNm")
    public String stNm;
    @SerializedName("tmX")
    public String tmX;
    @SerializedName("tmY")
    public String tmY;
    @SerializedName("posX")
    public String posX;
    @SerializedName("posY")
    public String posY;
    @SerializedName("arsId")
    public String arsId;

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getStNm() {
        return stNm;
    }

    public void setStNm(String stNm) {
        this.stNm = stNm;
    }

    public String getTmX() {
        return tmX;
    }

    public void setTmX(String tmX) {
        this.tmX = tmX;
    }

    public String getTmY() {
        return tmY;
    }

    public void setTmY(String tmY) {
        this.tmY = tmY;
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
    }

    public String getArsId() {
        return arsId;
    }

    public void setArsId(String arsId) {
        this.arsId = arsId;
    }
}

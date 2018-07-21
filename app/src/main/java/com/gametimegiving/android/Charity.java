package com.gametimegiving.android;

import android.graphics.Bitmap;

import java.util.List;

public class Charity {
    List<Charity> listOfCharities;
    private int CharityId;
    private String CharityName;
    private String Detail;
    private String Mission;
    private String Purpose;
    private String ContactEmail;
    private String ContactPhone;
    private String Logo;
    private Bitmap bitmap;

    public int getCharityId() {
        return CharityId;
    }

    public void setCharityId(int charityId) {
        this.CharityId = charityId;
    }

    public String getCharityName() {
        return CharityName;
    }

    public void setCharityName(String teamName) {
        this.CharityName = teamName;
    }


    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getMission() {
        return Mission;
    }

    public void setMission(String mission) {
        Mission = mission;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getContactEmail() {
        return ContactEmail;
    }

    public void setContactEmail(String contactEmail) {
        ContactEmail = contactEmail;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String contactPhone) {
        ContactPhone = contactPhone;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
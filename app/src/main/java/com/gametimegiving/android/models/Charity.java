package com.gametimegiving.android.models;

public class Charity {
    private String id;
    private String Name;
    private String detail;
    private String Mission;
    private String Purpose;
    private String ContactEmail;
    private String ContactPhone;
    private String logo;
    private int TotalAmountRaised;


    public Charity() {
    }

    public Charity(String Name, String detail, String Mission, String Purpose, String ContactEmail,
                   String ContactPhone, String logo, int totalamountraised) {
        this.Name = Name;
        this.detail = detail;
        this.Mission = Mission;
        this.Purpose = Purpose;
        this.ContactEmail = ContactEmail;
        this.ContactPhone = ContactPhone;
        this.logo = logo;
        this.TotalAmountRaised = totalamountraised;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalAmountRaised() {
        return TotalAmountRaised;
    }

    public void setTotalAmountRaised(int totalAmountRaised) {
        TotalAmountRaised = totalAmountRaised;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
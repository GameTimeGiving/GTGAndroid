package com.gametimegiving.android;

public class Charity {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private String Name;
    private String Detail;
    private String Mission;
    private String Purpose;
    private String ContactEmail;
    private String ContactPhone;
    private String logo;
    private int TotalAmountRaised;


    public Charity() {
    }

    public Charity(String Name, String Detail, String Mission, String Purpose, String ContactEmail,
                   String ContactPhone, String logo, int totalamountraised) {
        this.Name = Name;
        this.Detail = Detail;
        this.Mission = Mission;
        this.Purpose = Purpose;
        this.ContactEmail = ContactEmail;
        this.ContactPhone = ContactPhone;
        this.logo = logo;
        this.TotalAmountRaised = totalamountraised;
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
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
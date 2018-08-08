package com.gametimegiving.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Team implements Parcelable {

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
    private String teamname;
    private String mascot;
    private Charity charity;
    private String logo;

    public Team() {


    }

    public Team(String teamname, String mascot, Charity charity, String logo) {
        this.teamname = teamname;
        this.mascot = mascot;
        this.charity = charity;
        this.logo = logo;

    }

    protected Team(Parcel in) {
        teamname = in.readString();
        mascot = in.readString();
    }

    public static Creator<Team> getCREATOR() {
        return CREATOR;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public Charity getCharity() {
        return charity;
    }

    public void setCharity(Charity charity) {
        this.charity = charity;
    }

    public String getMascot() {
        return mascot;
    }

    public void setMascot(String mascot) {
        this.mascot = mascot;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(teamname);
        parcel.writeString(mascot);
    }


}
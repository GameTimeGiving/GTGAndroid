package com.gametimegiving.android;

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

    public Team() {


    }

    public Team(String teamname, String mascot, Charity charity) {
        this.teamname = teamname;
        this.mascot = mascot;
        this.charity = charity;

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
package com.gametimegiving.android.models;

import android.app.Activity;

import com.gametimegiving.android.Helpers.Utilities;

import java.util.Date;

public class Pledge {
    private final static String TAG = "PLEDGE";
    private String gameid;
    private String teamid;
    private String charityid;
    private int amount;
    private String user;
    private Date timeOfPledge;
    private int preferredCharity_id;
    private Utilities utilities = new Utilities();
    private Activity mContext;

    public Pledge() {

    }

    public Pledge(String gameid, String teamid, String charityid) {
        this.gameid = gameid;
        this.teamid = teamid;
        this.charityid = charityid;
    }

    public Pledge(Activity context) {
        mContext = context;
    }

    //Adding to the file
    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

    public String getCharityid() {
        return charityid;
    }

    public void setCharityid(String charityid) {
        this.charityid = charityid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getTimeOfPledge() {
        return timeOfPledge;
    }

    public void setTimeOfPledge(Date timeOfPledge) {
        this.timeOfPledge = timeOfPledge;
    }

    public int getPreferredCharity_id() {
        return preferredCharity_id;
    }

    public void setPreferredCharity_id() {
        this.preferredCharity_id = 1;
    }

    public int SubmitPledge() {
        int rtnVal = 0;
        String method = "pledge";
        // Write a message to the database
        return rtnVal;
    }


}

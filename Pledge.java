package com.gametimegiving.android;

import android.app.Activity;

import java.util.Date;

public class Pledge {
    private final static String TAG = "PLEDGE";
    private int game_id;
    private int team_id;
    private int charity_id;
    private int amount;
    private int user;
    private Date timeOfPledge;
    private int preferredCharity_id;
    private Utilities utilities = new Utilities();
    private Activity mContext;


    public Pledge(Activity context) {
        mContext = context;
    }

    //Adding to the file
    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public int getCharity_id() {
        return charity_id;
    }

    public void setCharity_id(int charity_id) {
        this.charity_id = charity_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
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

package com.gametimegiving.android;

import android.content.Context;

public class Player {
    private static final String TAG = "Player";
    public Utilities utilities = new Utilities();
    private String player_id;
    private int myteam_id;
    private int pledgetotal;


    private String mylastpledgeid;
    private int mylastpledgeamount;
    private Charity[] myCharities;
    private Team[] myTeams;

    Player() {

    }

    Player(String player_id) {
        this.player_id = player_id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public Player(int pledgetotal) {
        this.pledgetotal = pledgetotal;
    }

    public static boolean isRegisteredPlayer(String userName, String pwd, Context context) {
        Boolean registeredPlayer = false;
        String method = "login";
        return registeredPlayer;
    }

    public int getPledgetotal() {
        return pledgetotal;
    }

    public void setPledgetotal(int pledgetotal) {
        this.pledgetotal = pledgetotal;
    }


    public int getMyteam_id() {
        return myteam_id;
    }

    public void setMyteam_id(int myteam_id) {
        this.myteam_id = myteam_id;
    }


    public Charity[] getMyCharities() {
        return myCharities;
    }

    public void setMyCharities(Charity[] myCharities) {
        this.myCharities = myCharities;
    }

    public Team[] getMyTeams() {
        return myTeams;
    }

    public void setMyTeams(Team[] myTeams) {
        this.myTeams = myTeams;
    }

    public String getMylastpledgeid() {
        return mylastpledgeid;
    }

    public void setMylastpledgeid(String mylastpledgeid) {
        this.mylastpledgeid = mylastpledgeid;
    }

    public int getMylastpledgeamount() {
        return mylastpledgeamount;
    }

    public void setMylastpledgeamount(int mylastpledgeamount) {
        this.mylastpledgeamount = mylastpledgeamount;
    }

}

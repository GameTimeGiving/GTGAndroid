package com.gametimegiving.android;

import android.content.Context;

public class Player {
    private static final String TAG = "Player";
    public Utilities utilities = new Utilities();
    private String user;
    private String myteam;
    private String game;
    private int pledgetotal;
    private String mylastpledgeid;
    private int mylastpledgeamount;
    private Charity[] myCharities;
    private Team[] myTeams;

    Player() {

    }

    Player(String game, int pledgetotal, String user) {
        this.game = game;
        this.pledgetotal = pledgetotal;
        this.user = user;
    }


    public static boolean isRegisteredPlayer(String userName, String pwd, Context context) {
        Boolean registeredPlayer = false;
        String method = "login";
        return registeredPlayer;
    }

    public String getPlayer() {
        return user;
    }

    public void setPlayer(String user) {
        this.user = user;
    }

    public int getPledgetotal() {
        return pledgetotal;
    }

    public void setPledgetotal(int pledgetotal) {
        this.pledgetotal = pledgetotal;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMyteam() {
        return myteam;
    }

    public void setMyteam(String myteam) {
        this.myteam = myteam;
    }


    public void setMylastpledgeid(String mylastpledgeid) {
        this.mylastpledgeid = mylastpledgeid;
    }



}

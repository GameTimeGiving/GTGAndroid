package com.gametimegiving.android.models;

import com.gametimegiving.android.Helpers.Utilities;

public class Player {
    private static final String TAG = "Player";
    public Utilities utilities = new Utilities();
    private String id;
    private String user;
    private String myteam;
    private String game;
    private int pledgetotal;
    private String mylastpledgeid;
    private int mylastpledgeamount;
    private Charity[] myCharities;
    private Team[] myTeams;

    public Player() {

    }

    public Player(String game, int pledgetotal, String user, String id) {
        this.game = game;
        this.pledgetotal = pledgetotal;
        this.user = user;
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

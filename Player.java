package com.gametimegiving.android;

import android.content.Context;

public class Player {
    private static final String TAG = "Player";
    public Utilities utilities = new Utilities();
    private int player_id;
    private int myteam_id;
    private int pledgetotal;
    private int myLastPledgeAmount;
    private Charity[] myCharities;
    private Team[] myTeams;

    Player() {

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

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id() {
        this.player_id = 2;
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

    public int getMyLastPledgeAmount() {
        return myLastPledgeAmount;
    }

    public void setMyLastPledgeAmount(int myLastPledgeAmount) {
        this.myLastPledgeAmount = myLastPledgeAmount;
    }
}

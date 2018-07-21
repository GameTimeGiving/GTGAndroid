package com.gametimegiving.android;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Game implements Parcelable {

    private final String TAG = getClass().getSimpleName();
    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
    private String gamestatus;
    private String period;
    private String timeleft;
    private int hometeamscore;
    private int awayteamscore;
    private int hometeampledgetotal;
    private int awayteampledgetotal;
    private Player player;
    private Team hometeam;
    private Team awayteam;

    public Game() {

    }

    private String gameid;

    protected Game(Parcel in) {
        gamestatus = in.readString();
        period = in.readString();
        timeleft = in.readString();
        hometeamscore = in.readInt();
        awayteamscore = in.readInt();
        hometeampledgetotal = in.readInt();
        awayteampledgetotal = in.readInt();
        // hometeam=in.readTypedObject(Team);
        //      gameid = in.readInt();

        //        hometeamlogo = in.readString();
//        awayteamlogo = in.readString();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getGamestatus() {
        return gamestatus;
    }

    public void setGamestatus(String gamestatus) {
        this.gamestatus = gamestatus;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTimeleft() {
        return timeleft;
    }

    public void setTimeleft(String timeleft) {
        this.timeleft = timeleft;
    }

    public int getHometeamscore() {
        return hometeamscore;
    }

    public void setHometeamscore(int hometeamscore) {
        this.hometeamscore = hometeamscore;
    }

    public int getAwayteamscore() {
        return awayteamscore;
    }

    public void setAwayteamscore(int awayteamscore) {
        this.awayteamscore = awayteamscore;
    }

    public int getHometeampledgetotal() {
        return hometeampledgetotal;
    }

    public void setHometeampledgetotal(int hometeampledgetotal) {
        this.hometeampledgetotal = hometeampledgetotal;
    }

    public int getAwayteampledgetotal() {
        return awayteampledgetotal;
    }

    public void setAwayteampledgetotal(int awayteampledgetotal) {
        this.awayteampledgetotal = awayteampledgetotal;
    }

    public Team getHometeam() {
        return hometeam;
    }

    public void setHometeam(Team hometeam) {
        this.hometeam = hometeam;
    }

    public Game(
            String gamestatus,
            String period,
            String timeleft,
            int hometeamscore,
            int awayteamscore,
            int hometeampledgetotal,
            int awayteampledgetotal,
            Team hometeam,
            Team awayteam,
            Player player) {
        this.gamestatus = gamestatus;
        this.period = period;
        this.timeleft = timeleft;
        this.hometeamscore = hometeamscore;
        this.awayteamscore = awayteamscore;
        this.hometeampledgetotal = hometeampledgetotal;
        this.awayteampledgetotal = awayteampledgetotal;
        this.hometeam = hometeam;
        this.awayteam = awayteam;
        this.player = player;
        //        this.gameid=gameid;
//        this.myteam=myteam;
//        this.startdate=startdate;
//        this.hometeamlogo=hometeamlogo;
//        this.awayteamlogo=awayteamlogo;

    }

    public String getGameid() {
        return gameid;
    }


//    public Team getMyteam() {
//        return myteam;
//    }
//
//    public void setMyteam(Team myteam) {
//        this.myteam = myteam;
//    }


//
//    public Date getStartdate() {
//        return startdate;
//    }
//
//    public void setStartdate(Date startdate) {
//        this.startdate = startdate;
//    }
//
//    public String getHometeamlogo() {
//        return hometeamlogo;
//    }
//
//    public void setHometeamlogo(String hometeamlogo) {
//        this.hometeamlogo = hometeamlogo;
//    }
//
//    public String getAwayteamlogo() {
//        return awayteamlogo;
//    }
//
//    public void setAwayteamlogo(String awayteamlogo) {
//        this.awayteamlogo = awayteamlogo;
//    }

    public Team getAwayteam() {
        return awayteam;
    }

    public void setAwayteam(Team awayteam) {
        this.awayteam = awayteam;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gamestatus);
        parcel.writeString(period);
        parcel.writeString(timeleft);
        parcel.writeInt(hometeamscore);
        parcel.writeInt(awayteamscore);
        parcel.writeInt(hometeampledgetotal);
        parcel.writeInt(awayteampledgetotal);

    }
}



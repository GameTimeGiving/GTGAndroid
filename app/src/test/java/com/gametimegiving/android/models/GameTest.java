package com.gametimegiving.android.models;

import com.gametimegiving.android.Activities.GameBoardActivity;

import junit.framework.Assert;

import org.junit.Test;

public class GameTest {

    @Test
    public void WhenGivenAGameIDTheGameIsDeterminedCorrectly() {
        //Arrange
        Game game = new Game();
        GameBoardActivity activity = new GameBoardActivity();
        String expectedgameid = "xxxxxxxxxx";
        //Act
        game.setGameid(expectedgameid);
        String actualgameid = game.DetermineCurrentGame(activity);
        //Assert
        Assert.assertEquals(expectedgameid, actualgameid);
    }
}
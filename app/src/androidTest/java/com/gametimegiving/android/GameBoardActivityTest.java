package com.gametimegiving.android;

import com.gametimegiving.android.Activities.GameBoardActivity;
import com.gametimegiving.android.models.Player;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.fail;

public class GameBoardActivityTest {
    @Before
    public void setUp() {

    }

    @Test
    public void WhenThePlayerIdIsSetDeterminePlayerReturnsTheCorrectID(){
        //Arrange
        String expectedPlayerId ="xxxxxxxxxxxx";
        Player mPlayer = new Player();
        mPlayer.setId(expectedPlayerId);
        GameBoardActivity activity = new GameBoardActivity();
        //Act
        String actualPlayerId=activity.DeterminePlayer();
        //Assert
        Assert.assertEquals(expectedPlayerId,actualPlayerId);

    }

    @Test
    public void addPledgesTest() {
        //Arrange
        GameBoardActivity activity = new GameBoardActivity();
        String TestPlayerId = "SIHEoIIhvSwX5LwCgY6G";
        int pledgeAmount = 7;

        //  GetPlayerFromDB(TestPlayerId);


        //Act

        //activity.addPledges(pledgeAmount);

        //Assert
        fail("Not Implemented");

    }

//    private Player GetPlayerFromDB(String testPlayerId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference playerRef = db.collection("players").document(testPlayerId);
//        playerRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                Player player = document.toObject(Player.class);
//                return player;
//                }
//            }
//        });
//    }



        @Test
    public void getGames() {
        fail("Not Implemented");
    }

    @Test
    public void onBraintreeSubmit() {
        fail("Not Implemented");
    }

    @Test
    public void sendPaymentMethod() {
        fail("Not Implemented");
    }

    @Test
    public void showdialog() {
        fail("Not Implemented");
    }

    @Test
    public void openDailogPledgesAdd() {
        fail("Not Implemented");
    }

    @Test
    public void updateGameBoard() {
        fail("Not Implemented");
    }


    @Test
    public void GetPersonalPledge() {
        //Arrange

        //Act
        GetPersonalPledge();
        //Assert



    }

}
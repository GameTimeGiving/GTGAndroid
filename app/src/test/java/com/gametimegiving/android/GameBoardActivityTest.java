package com.gametimegiving.android;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.fail;

public class GameBoardActivityTest {
    @Before
    public void setUp() {

    }

    @Test
    public void addPledgesTest() {
        //Arrange
        GameBoardActivity activity = new GameBoardActivity();
        String TestPlayerId = "SIHEoIIhvSwX5LwCgY6G";
        int pledgeAmount = 7;

        //  GetPlayerFromDB(TestPlayerId);


        //Act

        activity.addPledges(pledgeAmount);

        //Assert

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

    private void setPlayer() {
    }

    @Test
    public void getAGame() {
        fail("Not Implemented");
    }

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
    public void onCreate() {
    }
}
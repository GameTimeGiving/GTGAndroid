
package com.gametimegiving.android.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.gametimegiving.android.Helpers.Constant;
import com.gametimegiving.android.Helpers.CustomizeDialog;
import com.gametimegiving.android.Helpers.GlideApp;
import com.gametimegiving.android.Helpers.TeamSelectorDialog;
import com.gametimegiving.android.Helpers.Utilities;
import com.gametimegiving.android.R;
import com.gametimegiving.android.models.Game;
import com.gametimegiving.android.models.Player;
import com.gametimegiving.android.models.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

import cz.msebera.android.httpclient.Header;

public class GameBoardActivity extends GTGBaseActivity implements View.OnClickListener {
    private final static int SUBMIT_PAYMENT_REQUEST_CODE = 100;
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = getClass().getSimpleName();
    final String API_GET_TOKEN = "http://x1.gametimegiving.com/experiment/GenerateToken";
    final String API_CHECK_OUT = "http://x1.gametimegiving.com/experiment/CreateTransaction";
    final int REQUEST_CODE = 999;
    public Game mGame = new Game();
    public Player mPlayer = new Player();
    Utilities utilities = new Utilities();
    Group pledgeButtons;
    ImageView ivHomeTeamLogo, ivAwayTeamLogo;
    private String mClientToken;
    private Button mUndoLastPledge,
            pledgeBtn1,
            pledgeBtn2,
            pledgeBtn3,
            btnPayNow;
    private Integer mMyLastPledge = 0;
    private TextView tv_VisitorTeamName,
            tv_HomeTeamName,
            tv_homeTeamMascot,
            tv_visitorTeamMascot,
            tv_AwayTeamScore,
            tv_homeTeamScore,
            tv_MyTotalPledgeTotals,
            tv_HomeTeamPledgeTotals,
            tv_AwayTeamPledgeTotals,
            tv_GamePeriod,
            tvGameNotStarted;
    private AsyncHttpClient client = new AsyncHttpClient();
    private double TransactionAmt = 0;
    String photoUrl;
    String user;
    String myteam = "away";
    String playerID;
    int iMyPledgeTotal;
    ArrayList<Team> TeamsFromGameArray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerID = ReadSharedPref("player", this);
        Log.d(TAG, "Flow Step#1");
        mPlayer.setId(playerID);
        setContentView(R.layout.gameboard);
        //     GTGSnackBar(findViewById(R.id.GameBoardLayout), "On Create");

        SetNavDrawer();
        tvGameNotStarted = findViewById(R.id.gamenotstarted);

        //Find and set all the textviews on the view
        tv_HomeTeamName = findViewById(R.id.HomeTeamName);
        tv_homeTeamMascot = findViewById(R.id.HomeTeamMascot);
        tv_VisitorTeamName = findViewById(R.id.VisitorTeamName);
        tv_visitorTeamMascot = findViewById(R.id.VisitorTeamMascot);
        tv_homeTeamScore = findViewById(R.id.tv_HomeTeamScore);
        tv_AwayTeamScore = findViewById(R.id.tv_AwayTeamScore);
        tv_MyTotalPledgeTotals = findViewById(R.id.pledge);
        tv_HomeTeamPledgeTotals = findViewById(R.id.tv_HomeTeamPledges);
        tv_AwayTeamPledgeTotals = findViewById(R.id.tv_AwayTeamPledges);
        tv_GamePeriod = findViewById(R.id.tv_GamePeriod);
        ivHomeTeamLogo = findViewById(R.id.hometeamlogo);
        ivAwayTeamLogo = findViewById(R.id.awayteamlogo);
        //Find and set all the buttons on the view
        mUndoLastPledge = findViewById(R.id.btnundolastpledge);
        mUndoLastPledge.setOnClickListener(this);
        mUndoLastPledge.setEnabled(false);
        pledgeBtn1 = findViewById(R.id.PledgeButton1);
        pledgeBtn1.setOnClickListener(this);
        pledgeBtn2 = findViewById(R.id.PledgeButton2);
        pledgeBtn2.setOnClickListener(this);
        pledgeBtn3 = findViewById(R.id.PledgeButton3);
        pledgeBtn3.setOnClickListener(this);
        btnPayNow = findViewById(R.id.btnpaynow);
        pledgeButtons = findViewById(R.id.pledgeButtons);
        GetAGame();

    }

    @Override
    protected void onResume() {
        super.onResume();
        playerID = ReadSharedPref("player", this);
        mPlayer.setId(playerID);
    }
    public void GetAGame() {
        Log.d(TAG, "Flow Step#2");
        mGame.DetermineCurrentGame(this);
        if (mGame.getGameid() != null) {
            DocumentReference gameRef = db.collection("games").document(mGame.getGameid());
            gameRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        // Log.d(TAG, "Current data: " + snapshot.getData());
                        mGame = snapshot.toObject(Game.class);
                        mGame.setGameid(gameRef.getId());
                        GetTeamLogos(ivHomeTeamLogo, ivAwayTeamLogo);
                        GetPersonalPledge();
                        SetGameBoardMode(mGame, mPlayer);
                        DetermineHomeOrAway();
                        isFirstTimeIn(mGame);


                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        } else {
            Toast.makeText(this, "Oops! There is no Game to find. Try Again!", Toast.LENGTH_SHORT).show();
            mGame.DetermineCurrentGame(this);
        }
    }

    public void ShowTeamSelectorDialog() {
        DialogFragment newFragment = new TeamSelectorDialog();
        FragmentManager fm = getSupportFragmentManager();
        newFragment.show(fm, "team");
    }


    public void GetPersonalPledge() {
        Log.d(TAG, "Flow Step#4");
        playerID = DeterminePlayer();
        if (playerID != "" && playerID != null) {
            DocumentReference playerRef = db.collection("players").document(playerID);
            playerRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mPlayer = document.toObject(Player.class);
                        WriteIntSharedPref("mypledgetotal", mPlayer.getPledgetotal());
//                        SetGameBoardMode(mGame,mPlayer);
//                        isFirstTimeIn(mGame);
//                        UpDatePersonalPledgeTotal();
                    }
                }

            });
        } else {
            if (isFirstTimeIn(mGame) == false) {
                String message = "Oops! We couldn't find a player id";
                GTGSnackBar(findViewById(R.id.GameBoardLayout), message);

            }
        }
    }

    public ArrayList<Game> GetGames() {
        // int gameId = 001;
        ArrayList<Game> listOfGames = new ArrayList<Game>();
        db.collection("games")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Game game = document.toObject(Game.class);
                            listOfGames.add(game);
                        }
                    }
                });
        return listOfGames;
    }

    private void ClearGameBoard() {
        mGame.setHometeamscore(0);
        mGame.setAwayteamscore(0);
        mGame.setTimeleft("00:00");
        mGame.setHometeampledgetotal(0);
        mGame.setAwayteampledgetotal(0);
        mPlayer.setPledgetotal(0);
    }
    private void DetermineHomeOrAway() {
        String homeoraway = "";
        String MyTeams = ReadSharedPref("myteams", this);
        String HomeTeam = mGame.getHometeam().getTeamname();
        String AwayTeam = mGame.getAwayteam().getTeamname();
        if (MyTeams.contains(HomeTeam)) homeoraway = "home";
        if (MyTeams.contains(AwayTeam)) homeoraway = "away";
        if (homeoraway == "") {
            openDailogTeamChooser(mGame);
            GTGSnackBar(findViewById(R.id.GameBoardLayout), "Pick Your Team");
        } else {
            mPlayer.setMyteam(homeoraway);
            Utilities.WriteStringSharedPref("myteam", homeoraway, this);
        }
    }

    private void GetClientToken() {

        client.get(API_GET_TOKEN, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String token) {
                mClientToken = token.substring(1, token.length() - 1);
                Log.d(TAG, String.format("mClientToken:%s", mClientToken));
                btnPayNow.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                Log.d(TAG, String.format("Error getting the client token: %s", errorResponse));

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    private void GetTeamLogos(ImageView homelogo, ImageView awaylogo) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String homeTeamLogo = mGame.getHometeam().getLogo();
        String awayTeamLogo = mGame.getAwayteam().getLogo();
        StorageReference homeTeamLogoReference = storage.getReferenceFromUrl(homeTeamLogo);
        StorageReference awayTeamLogoReference = storage.getReferenceFromUrl(awayTeamLogo);
        if (!this.isFinishing()) {
        GlideApp.with(this /* context */)
                .load(homeTeamLogoReference)
                .into(homelogo);
            //          .into(ivHomeTeamLogo);
        GlideApp.with(this /* context */)
                .load(awayTeamLogoReference)
                .into(awaylogo);
            //        .into(ivAwayTeamLogo);
        }
    }

    private void UpDatePersonalPledgeTotal() {
        Log.d(TAG, String.format("Updating the Personal Pledge Total with %s", mPlayer.getPledgetotal()));
        iMyPledgeTotal = mPlayer.getPledgetotal();
        WriteIntSharedPref("mypledgetotal", iMyPledgeTotal);
        String formattedPledgeAmount = Utilities.FormatCurrency(iMyPledgeTotal);
        tv_MyTotalPledgeTotals.setText(formattedPledgeAmount);
    }

    private void SetGameBoardMode(Game mGame, Player mPlayer) {
        Log.d(TAG, "Flow Step#5");
        Group pledgeButtons = findViewById(R.id.pledgeButtons);
        switch (mGame.getGamestatus()) {
            case Constant.GAMENOTSTARTED:
                tv_GamePeriod.setText("00:00");
                pledgeButtons.setVisibility(View.GONE);
                btnPayNow.setVisibility(View.GONE);
                tvGameNotStarted.setText("Game Not Started");
                tvGameNotStarted.setVisibility(View.VISIBLE);
                ClearGameBoard();
                break;
            case Constant.GAMEINPROGRESS:
                tv_GamePeriod.setText(String.format("%s in the %s", mGame.getTimeleft(), mGame.getPeriod()));
                pledgeButtons.setVisibility(View.VISIBLE);
                btnPayNow.setVisibility(View.GONE);
                tvGameNotStarted.setVisibility(View.GONE);
                break;
            case Constant.GAMEOVER:
                tv_GamePeriod.setText("Game Over");
                GetClientToken();
                pledgeButtons.setVisibility(View.GONE);
                tvGameNotStarted.setVisibility(View.GONE);
                Log.d(TAG, String.format("Game Over Personal Pledge total is %s", String.valueOf(mPlayer.getPledgetotal())));
                iMyPledgeTotal = ReadIntSharedPref("mypledgetotal", this);
                if (iMyPledgeTotal > 0) {
                    btnPayNow.setText(String.format("Donate %s ", tv_MyTotalPledgeTotals.getText()));
                    btnPayNow.setVisibility(View.VISIBLE);
                    btnPayNow.setEnabled(false);
                }
                break;
        }
        UpdateGameBoard(mGame);
    }

    public void onBraintreeSubmit(View v) {
        btnPayNow.setEnabled(false);
        TransactionAmt = Double.parseDouble(tv_MyTotalPledgeTotals.getText().toString().replaceAll("(?<=\\d),(?=\\d)|\\$", ""));
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(mClientToken); //"sandbox_mdys6zxr_jstvnq9hgzfgrt79"
        startActivityForResult(dropInRequest.getIntent(GameBoardActivity.this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                String PaymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                SendPaymentMethod(PaymentMethodNonce, TransactionAmt);
                Log.d(TAG, String.format("PaymentMethodNonce:%s", PaymentMethodNonce));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Log.d(TAG, String.format("Result Cancelled:%s", Activity.RESULT_CANCELED));
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d(TAG, String.format("Activity OK: %s Exception:%s",
                        Activity.RESULT_OK,
                        error.toString()));
            }
        }
    }


    public void SendPaymentMethod(String nonce, double amt) {
        RequestParams params = new RequestParams();
        params.put("nonce", nonce);
        params.put("amt", amt);
        client.post(API_CHECK_OUT, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d(TAG, String.format("Payment Successful:%s", responseBody.toString()));
                        btnPayNow.setEnabled(false);
                        btnPayNow.setText("Payment Successful");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d(TAG, String.format("Payment Not Successful:%s", error.toString()));
                        btnPayNow.setText("Try Again.. Payment NOT Successful");

                    }
                }
        );
    }

    public void addPledges(int amount) {
        String gameid = ReadSharedPref("gameid", this);
        String user = ReadSharedPref("user", this);
        String player = ReadSharedPref("player", this);
        String myteam = ReadSharedPref("myteam", this);
        if (gameid != "" && user != "" && player != "" && myteam != "") {
            Map<String, Object> pledge = new HashMap<>();
            pledge.put("game", gameid);
            pledge.put("user", user);
            pledge.put("player", player);
            pledge.put("amount", amount);
            pledge.put("myteam", myteam);
            UpdateGameBoardLocal(myteam, amount);
            db.collection("pledges")
                    .add(pledge)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference pledgeRef) {
                            mPlayer.setMylastpledgeid(pledgeRef.getId());
                            mMyLastPledge = amount;
                            if (amount > 0) {
                                mUndoLastPledge.setEnabled(true);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

            openDailogPledgesAdd(amount);
        } else {
            String missingItem = "";
            if (gameid == "") {
                missingItem = "GameId";
                mGame.DetermineCurrentGame(this);
            }
            if (user == "") {
                missingItem = "UserId";
            }
            if (player == "") {
                missingItem = "PlayerId";
                DeterminePlayer();
            }
            if (myteam == "") {
                missingItem = "Your Team";
                DetermineHomeOrAway();
            }
            String message = String.format("Oops! Invalid Pledge... We can't find %s Try Again!", missingItem);
            GTGSnackBar(findViewById(R.id.GameBoardLayout), message);
        }

    }

    public String DeterminePlayer() {
        playerID = mPlayer.getId();
        if (playerID == "") playerID = GetPlayerFromSharedPrefs();
        if (playerID == "") {
            userId = GetUserIdFromSharedPrefs();
            CollectionReference playersCollection = db.collection("players");
            Query qPlayerRef = playersCollection.whereEqualTo("user", userId);
            qPlayerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                mPlayer = document.toObject(Player.class);
                                mPlayer.setId(document.getId());
                                playerID = document.getId();
                                Utilities.WriteStringSharedPref("player", document.getId(), GameBoardActivity.this);
                                WriteIntSharedPref("mypledgetotal", mPlayer.getPledgetotal());
                            }

                        }
                    }
                }
            });
        }
        return playerID;
    }

    private void UpdateGameBoardLocal(String myTeam, int pledgeAmount) {
        int hometeampledgetotal = utilities.RemoveCurrency(tv_HomeTeamPledgeTotals.getText().toString());
        int awayteampledgetotal = utilities.RemoveCurrency(tv_AwayTeamPledgeTotals.getText().toString());
        int playerpledgetotal = utilities.RemoveCurrency(tv_MyTotalPledgeTotals.getText().toString());

        if (myTeam == "home") {
            tv_HomeTeamPledgeTotals.setText(Utilities.FormatCurrency(hometeampledgetotal + pledgeAmount));
        } else {
            tv_AwayTeamPledgeTotals.setText(Utilities.FormatCurrency(awayteampledgetotal + pledgeAmount));
        }
        tv_MyTotalPledgeTotals.setText(Utilities.FormatCurrency(playerpledgetotal + pledgeAmount));

    }

    private void undoLastPledge() {
        addPledges(mMyLastPledge * -1);
        mUndoLastPledge.setEnabled(false);
    }

    public void CreatePlayer(int firstPledgeAmount) {
        String gameid = mGame.getGameid();
        String user = mPlayer.getUser();
        if (user == null || user == "") user = GetUserIdFromSharedPrefs();
        Map<String, Object> data = new HashMap<>();
        data.put("game", gameid);
        data.put("pledgetotal", 0);
        data.put("user", user);
        db.collection("players")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Player created " + documentReference.getId());
                        mPlayer.setId(documentReference.getId());
                        Utilities.WriteStringSharedPref("player", documentReference.getId(), GameBoardActivity.this);
                        addPledges(firstPledgeAmount);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getParent(), "Error Creating Player:", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        boolean DemoMode = false;
        int btnClicked = v.getId();
        switch (btnClicked) {
            case R.id.PledgeButton1:
                if (playerID == "") {
                    CreatePlayer(Constant.FIRSTPLEDGEAMT);
                } else {
                    addPledges(Constant.FIRSTPLEDGEAMT);
                }
                break;
            case R.id.PledgeButton2:
                if (playerID == "") {
                    CreatePlayer(Constant.SECONDPLEDGEAMT);
                } else {
                    addPledges(Constant.SECONDPLEDGEAMT);
                }
                break;
            case R.id.PledgeButton3:
                if (playerID == "") {
                    CreatePlayer(Constant.THIRDPLEDGEAMT);
                } else {
                    addPledges(5);
                }
                break;
            case R.id.btnundolastpledge:
                undoLastPledge();
                break;
        }

        DemoMode();
    }

    private void DemoMode() {
        boolean DemoMode;
        DemoMode = ReadBoolSharedPref("demo", this);
        if (DemoMode == true) {
            int democlickcount = 3;
            int remainingpledges = 0;
            int pledgecount = GetPledgeCount();
            if (pledgecount > democlickcount - 1) {
                UpdateGameStatus(Constant.GAMEOVER);
                WriteBoolSharedPref("demo", false);
                WriteIntSharedPref("pledgecount", 0);
            } else {
                remainingpledges = democlickcount - pledgecount;
                pledgecount++;
                WriteIntSharedPref("pledgecount", pledgecount);
                GTGSnackBar(findViewById(R.id.GameBoardLayout), String.format("Pledge %s more times, to continue demo", String.valueOf(remainingpledges)));
            }
        }
    }


    public void openDailogPledgesAdd(int pledge_value) {
        String sConfirmation;
        final CustomizeDialog pledgeDialog = new CustomizeDialog(this);
        pledgeDialog.setContentView(R.layout.dialogpledges);
        TextView tv_pledge_donation = pledgeDialog.findViewById(R.id.tv_pledge_donation);
        if (pledge_value < 0) {
            sConfirmation = String.format("<center>Oops! Your <b>%s Pledge</b><br />has been undone.</center>", Utilities.FormatCurrency(pledge_value).replace("-", ""));

        } else {
            sConfirmation = String.format("<center>Great Job! Your pledge of <br /><b>%s</b><br /> is confirmed.</center>", Utilities.FormatCurrency(pledge_value));
        }
        tv_pledge_donation.setText(Html.fromHtml(sConfirmation));
        pledgeDialog.show();
        pledgeDialog.setCancelable(false);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                pledgeDialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 2000);
    }

    public void openDailogTeamChooser(Game game) {
        CustomizeDialog teamchooserDialog = new CustomizeDialog(this);
        teamchooserDialog.setContentView(R.layout.chooseteamdialog);
        TextView tvHomeTeamName = teamchooserDialog.findViewById(R.id.tvhometeamname);
        TextView tvAwayTeamName = teamchooserDialog.findViewById(R.id.tvawayteamname);
        TextView tvHomeTeamMascot = teamchooserDialog.findViewById(R.id.tvhometeammascot);
        TextView tvAwayTeamMascot = teamchooserDialog.findViewById(R.id.tvawayteammascot);
        ImageButton ibHomeTeamLogo = teamchooserDialog.findViewById(R.id.ivhometeamlogo);
        ImageButton ibAwayTeamLogo = teamchooserDialog.findViewById(R.id.ivawayteamlogo);
        //  teamchooserDialog.setCancelable(false);
        tvHomeTeamName.setText(game.getHometeam().getTeamname());
        tvAwayTeamName.setText(game.getAwayteam().getTeamname());
        tvHomeTeamMascot.setText(game.getHometeam().getMascot());
        tvAwayTeamMascot.setText(game.getAwayteam().getMascot());
        GetTeamLogos(ibHomeTeamLogo, ibAwayTeamLogo);
        teamchooserDialog.show();
//
        Button btnCancel = teamchooserDialog.findViewById(R.id.btncancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamchooserDialog.dismiss();
            }
        });
        ibHomeTeamLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = String.format("You have picked %s %s as your team. Let's Play!",
                        game.getHometeam().getTeamname(), game.getHometeam().getMascot());
                GTGSnackBar(findViewById(R.id.GameBoardLayout), message);
                teamchooserDialog.dismiss();
            }
        });
        ibAwayTeamLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = String.format("You have picked %s %s as your team. Let's Play!",
                        game.getAwayteam().getTeamname(), game.getAwayteam().getMascot());
                GTGSnackBar(findViewById(R.id.GameBoardLayout), message);
                teamchooserDialog.dismiss();
            }
        });

    }
    public void UpdateGameBoard(Game mGame) {
//        String timeleft = "Not Started";
//        Log.d(TAG,String.format("The game status is %s",mGame.getGamestatus()));
//       if (mGame.getGamestatus().contentEquals(Constant.GAMEOVER)) {
//            timeleft = "Game Over";
//        } else {
//            timeleft = String.format("%s in the %s", mGame.getTimeleft(), mGame.getPeriod());
//        }
        // tv_GamePeriod.setText(timeleft);
        tv_homeTeamScore.setText(Integer.toString(mGame.getHometeamscore()));
        tv_AwayTeamScore.setText(Integer.toString(mGame.getAwayteamscore()));
        tv_HomeTeamName.setText(mGame.getHometeam().getTeamname());
        tv_homeTeamMascot.setText(mGame.getHometeam().getMascot());
        tv_VisitorTeamName.setText(mGame.getAwayteam().getTeamname());
        tv_visitorTeamMascot.setText(mGame.getAwayteam().getMascot());
        tv_HomeTeamPledgeTotals.setText(Utilities.FormatCurrency(mGame.getHometeampledgetotal()));
        tv_AwayTeamPledgeTotals.setText(Utilities.FormatCurrency(mGame.getAwayteampledgetotal()));
    }


}






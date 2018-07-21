
package com.gametimegiving.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int SUBMIT_PAYMENT_REQUEST_CODE = 100;
    private static final int RC_SIGN_IN = 777;
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    final int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = MaxMemory / 8;
    final Handler handler = new Handler();
    private final String TAG = getClass().getSimpleName();
    private final Player player = new Player();
    public Integer ActiveGameID,
            mUserTeamID;
    public String ClientToken;
    public Payment payment;
    public String MyPledgeAmount;
    public Boolean PreferredCharityNoticeShown;
    public boolean bFirstTimeIn = true;
    public Game mGame;
    public Player mPlayer = new Player();
    //  public Game mGame = null;
    Utilities utilities = new Utilities();
    Timer timer;
    TimerTask timerTask;
    Group pledgeButtons;
    private ImageView mHomeLogo,
            mAwayLogo;
    private Context context;
    private String[] arr = null;
    private Integer mMyPledgeTotals = 0,
            mMyLastPledge = 0;
    private TextView tv_VisitorTeamName,
            tv_HomeTeamName,
            tv_homeTeamMascot,
            tv_visitorTeamMascot,
            tv_AwayTeamScore,
            tv_homeTeamScore,
            tv_pledges,
            tv_MyTotalPledgeTotals,
            tv_HomeTeamPledgeTotals,
            tv_AwayTeamPledgeTotals,
            tv_GamePeriod,
            tv_PreferredCharityNotice;
    private Button mUndoLastPledge,
            pledgeBtn1,
            pledgeBtn2,
            pledgeBtn3,
            btnPayNow;
    private String mToken;
    private LruCache<Integer, Bitmap> imageMemCache;

    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                                    new AuthUI.IdpConfig.TwitterBuilder().build(),
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.PhoneBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }
        String userId = auth.getUid();
        mPlayer.setPlayer_id(userId);
        mGame = new Game();
        mGame.setGameid("suYroi6ZuratHkBDuyF7");
        setContentView(R.layout.gameboard);
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
        //Find all the imageviews
        mHomeLogo = findViewById(R.id.hometeamlogo);
        mAwayLogo = findViewById(R.id.awayteamlogo);
        //Find the groups on the view
        pledgeButtons = findViewById(R.id.pledgeButtons);
        GetAGame();
        if (getIntent().getExtras() != null) {
            try {
                Bundle extras = getIntent().getExtras();
                bFirstTimeIn = extras.getBoolean(Constant.ISFIRSTTIMEIN);
                ActiveGameID = extras.getInt("selectedgameid");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        imageMemCache = new LruCache<>(cacheSize);
        Integer ag = utilities.ReadSharedPref("activegame", this);
        context = this;
        showdialog();

    }

    public void GetAGame() {
        //  FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference gameRef = db.collection("games").document(mGame.getGameid());
        gameRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    mGame = document.toObject(Game.class);
                    mGame.setGameid(gameRef.getId());
                    Log.d(TAG, String.format("GameData - Player Pledge: %s",
                            Integer.toString(mGame.getPlayer().getPledgetotal())));
                    SetGameBoardMode(mGame);
                    UpdateGameBoard(mGame);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }

        });
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
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
        return listOfGames;
    }

    private void SetGameBoardMode(Game mGame) {
        View l = findViewById(R.id.pledgeButtons);
        View ppl = null; //TODO: Replace this with a group
        switch (mGame.getGamestatus()) {
            case Constant.GAMENOTSTARTED:
                pledgeButtons.setVisibility(View.GONE);
                // mGame.ClearBoard();
                break;
            case Constant.GAMEINPROGRESS:
                pledgeButtons.setVisibility(View.VISIBLE);
                l.setVisibility(View.VISIBLE);
                btnPayNow.setVisibility(View.GONE);
                break;
            case Constant.GAMEOVER:
                if (tv_pledges.getText() != "") {
                    MyPledgeAmount = tv_pledges.getText().toString();
                    l.setVisibility(View.GONE);
                    pledgeButtons.setVisibility(View.GONE);
                    btnPayNow.setVisibility(View.VISIBLE);
                    btnPayNow.setOnClickListener(v -> MakeBrainTreePayment());
                } else {
                    utilities.ShowMsg("You have no pledges", this);

                }
                break;
        }
    }

    private void MakeBrainTreePayment() {
//        Customization customization = new Customization.CustomizationBuilder()
//                .primaryDescription("My Pledge  ")
//                .amount(MyPledgeAmount)
//                .submitButtonText("Donate Now")
//                .build();
//        Intent intent = new Intent(context, BraintreePaymentActivity.class)
//                .putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN,
//                        ClientToken);
//        intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
//        startActivityForResult(intent, SUBMIT_PAYMENT_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
//TODO:Add the Braintree functionality back into the project
//        if (requestCode == SUBMIT_PAYMENT_REQUEST_CODE) {
//            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
//                String nonce = data.getStringExtra(
//                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
//                );


        //               SendNonceToBraintree(nonce, MyPledgeAmount);
        //           }
        //      }
    }

    private void TurnOffPledgeMechanisms(View l, View ppl, Button btn, Button btnPay) {
        ppl.setVisibility(View.GONE);
        l.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        btnPay.setVisibility(View.GONE);
    }


    private void getGameName() {
        if (getIntent().getExtras() != null) {
            String games = getIntent().getExtras().getString("selectedgame");

        }
    }


    /*
     * Add Pledges
     *
     * */
    private void addPledges(int value) {
        mUndoLastPledge.setEnabled(true);
        mMyLastPledge = value;
        //Write to plegde table
        Map<String, Object> pledge = new HashMap<>();
        pledge.put("game", mGame.getGameid());
        pledge.put("user", mPlayer.getPlayer_id());
        pledge.put("amount", value);
        db.collection("pledges")
                .add(pledge)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference pledgeRef) {
                        //Log.d(TAG,String.format("Document %s successfully written!",pledgeRef.getId()));
                        mPlayer.setMylastpledgeid(pledgeRef.getId());
                        mPlayer.setMylastpledgeamount(value);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        openDailogPledgesAdd(value);
    }

    /*
     *
     * Undo Pledges
     *
     * */
    private void undoLastPledge() {
        mUndoLastPledge.setEnabled(false);
        int mylastpledgeamount = mPlayer.getMylastpledgeamount() * -1;
        addPledges(mylastpledgeamount);

    }


    public void showdialog() {
        final CustomizeDialog customizeDialog = new CustomizeDialog(context);
        customizeDialog.setContentView(R.layout.preferredcharitynotice);
        tv_PreferredCharityNotice = customizeDialog.findViewById(R.id.tv_preferredCharityNotice);
        Button button = customizeDialog.findViewById(R.id.btnok);
        button.setOnClickListener(v -> customizeDialog.dismiss());
        //Check if this is my first time in
        bFirstTimeIn = utilities.ReadBoolSharedPref(Constant.ISFIRSTTIMEIN, this);
        if (bFirstTimeIn) {
            String teamName = "THe Team"; //mGame.getMyteam().getTeamName();
            String charityName = "The Charity";//mGame.getMyteam().getPreferredCharity().getCharityName();
            String preferredCharityMessage =
                    String.format("NOTICE: Your team, %s, has chosen to support %s as a Preferred Charity. Your pledges will be split equally between the" +
                            "charities you and %s have chosen.", teamName, charityName, teamName);
            tv_PreferredCharityNotice.setText(preferredCharityMessage);
            customizeDialog.show();
            customizeDialog.setCancelable(false);
            utilities.WriteSharedPref(Constant.ISFIRSTTIMEIN, "false", this, "b");
        }
    }


    @Override
    public void onClick(View v) {
        int btnClicked = v.getId();
        if (v == null) btnClicked = 0;
        switch (btnClicked) {
            case R.id.PledgeButton1:
                addPledges(1);
                break;
            case R.id.PledgeButton2:
                addPledges(3);
                break;
            case R.id.PledgeButton3:
                addPledges(5);
                break;
            case R.id.btnundolastpledge:
                undoLastPledge();
                break;
        }
    }

    public void openDailogPledgesAdd(int pledge_value) {
        String sConfirmation;
        final CustomizeDialog pledgeDialog = new CustomizeDialog(context);
        pledgeDialog.setContentView(R.layout.dialogpledges);
        TextView tv_pledge_donation = pledgeDialog.findViewById(R.id.tv_pledge_donation);
        if (pledge_value < 0) {
            sConfirmation = String.format("<center>Oops! Your last pledge of <br /><b>%s</b><br /> has been undone.</center>", utilities.FormatCurrency(pledge_value).replace("-", ""));

        } else {
            sConfirmation = String.format("<center>Great Job! Your pledge of <br /><b>%s</b><br /> is confirmed.</center>", utilities.FormatCurrency(pledge_value));
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


    public void UpdateGameBoard(Game mGame) {
        tv_GamePeriod.setText(String.format("%s in the %s", mGame.getTimeleft(), mGame.getPeriod()));
        tv_homeTeamScore.setText(Integer.toString(mGame.getHometeamscore()));
        tv_AwayTeamScore.setText(Integer.toString(mGame.getAwayteamscore()));
        tv_HomeTeamName.setText(mGame.getHometeam().getTeamname());
        tv_homeTeamMascot.setText(mGame.getHometeam().getMascot());
        tv_VisitorTeamName.setText(mGame.getAwayteam().getTeamname());
        tv_visitorTeamMascot.setText(mGame.getAwayteam().getMascot());
        //       tv_pledges.setText(String.format("%s", utilities.FormatCurrency(player.getMyTotalPledgeAmount(this))));

        tv_HomeTeamPledgeTotals.setText(utilities.FormatCurrency(mGame.getHometeampledgetotal()));
        tv_AwayTeamPledgeTotals.setText(utilities.FormatCurrency(mGame.getAwayteampledgetotal()));
        tv_MyTotalPledgeTotals.setText(utilities.FormatCurrency(mGame.getPlayer().getPledgetotal()));
    }


}






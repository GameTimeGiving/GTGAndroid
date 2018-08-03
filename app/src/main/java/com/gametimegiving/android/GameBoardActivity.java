
package com.gametimegiving.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

public class GameBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int SUBMIT_PAYMENT_REQUEST_CODE = 100;

    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    final int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = MaxMemory / 8;
    final Handler handler = new Handler();
    private final String TAG = getClass().getSimpleName();
    private final Player player = new Player();
    public Integer ActiveGameID,
            mUserTeamID;
    final String API_GET_TOKEN = "http://x1.gametimegiving.com/experiment/GenerateToken";
    final String API_CHECK_OUT = "http://x1.gametimegiving.com/experiment/CreateTransaction";
    // public String tokenizationKey = "sandbox_mdys6zxr_jstvnq9hgzfgrt79";
    final int REQUEST_CODE = 999;
    public Game mGame = new Game();
    public Payment payment;
    public String MyPledgeAmount;
    public Boolean PreferredCharityNoticeShown;
    public boolean bFirstTimeIn = true;
    HashMap<String, String> paramsHash;
    public Player mPlayer = new Player();
    //  public Game mGame = null;
    Utilities utilities = new Utilities();
    Group pledgeButtons;
    private ImageView mHomeLogo,
            mAwayLogo;
    private Context context;
    private String[] arr = null;
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
            tv_pledges,
            tv_MyTotalPledgeTotals,
            tv_HomeTeamPledgeTotals,
            tv_AwayTeamPledgeTotals,
            tv_GamePeriod,
            tv_PreferredCharityNotice,
            tvGameNotStarted;
    private AsyncHttpClient client = new AsyncHttpClient();
    private double TransactionAmt = 0;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("user");
        String gameId = bundle.getString("game");
        mPlayer.setPlayer(userId);
        //TODO:(1)As a User I can pick a game and that game will be pulled up on the game board. Shared preeferences maybe?
        mGame.setGameid(gameId);
        Log.d(TAG, String.format("Game ID set:%s", gameId));
        //TODO:(2) As a user I can pick my team. Either I follow the team already or I can pick one to follow then.
        mPlayer.setMyteam("away");
        setContentView(R.layout.gameboard);
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
        Integer ag = utilities.ReadSharedPref("activegame", this);
        context = this;
        showdialog();

    }

    private void SetNavDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mContext = this;
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        Intent intent = null;
                        switch (id) {
                            case R.id.nav_gameboard:
                                intent = new Intent(mContext, GameBoardActivity.class);
                                //    mContext.startActivity(intent);
                                break;
                            case R.id.nav_charities:
                                intent = new Intent(mContext, CharitySelection.class);
                                //      mContext.startActivity(intent);
                                break;
                            case R.id.nav_games:
                                intent = new Intent(mContext, GameSelection.class);
                                //        mContext.startActivity(intent);
                                break;
                            case R.id.nav_teams:
                                intent = new Intent(mContext, TeamSelection.class);
                                break;
                            case R.id.nav_profile:
                                intent = new Intent(mContext, Profile.class);
                                break;

                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        mContext.startActivity(intent);
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Utilities.ShowVersion(this);
                break;
            case R.id.action_signout:
                FirebaseAuth.getInstance().signOut();
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void onBackPressed() {
//        // call super.onBackPressed();  at last.
//        Intent intent = new Intent(this, GameBoardActivity.class);
//        startActivity(intent);
//        super.onBackPressed();
//    }

    public void GetAGame() {
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
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        mGame = snapshot.toObject(Game.class);
                        mGame.setGameid(gameRef.getId());
                        Log.d(TAG, String.format("GameData - Player Pledge: %s",
                                Integer.toString(mGame.getPlayer().getPledgetotal())));
                        GetTeamLogos();
                        SetGameBoardMode(mGame);

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        } else {
            Toast.makeText(this, "Oops! There is no Game to find. ", Toast.LENGTH_SHORT).show();
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

    private void GetTeamLogos() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String homeTeamLogo = mGame.getHometeam().getLogo();
        String awayTeamLogo = mGame.getAwayteam().getLogo();
        StorageReference homeTeamLogoReference = storage.getReferenceFromUrl(homeTeamLogo);
        StorageReference awayTeamLogoReference = storage.getReferenceFromUrl(awayTeamLogo);
        if (!this.isFinishing()) {
        GlideApp.with(this /* context */)
                .load(homeTeamLogoReference)
                .into(ivHomeTeamLogo);
        GlideApp.with(this /* context */)
                .load(awayTeamLogoReference)
                .into(ivAwayTeamLogo);
        }
    }

    private void GetPersonalPledge() {
        DocumentReference playerRef = db.collection("players").document("d5EASA7VcjebVFaPuSIA");
        playerRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    mPlayer = document.toObject(Player.class);
                    Log.d(TAG, String.format("Pl%s has pledged $ %s in game %s", mPlayer.getPlayer(),
                            Integer.toString(mPlayer.getPledgetotal()),
                            mPlayer.getGame()));
                    UpDatePersonalPledgeTotal(mPlayer);
                }
            }

        });
    }

    private void UpDatePersonalPledgeTotal(Player player) {
        tv_MyTotalPledgeTotals.setText(utilities.FormatCurrency(player.getPledgetotal()));
        btnPayNow.setText(String.format("Donate %s ", tv_MyTotalPledgeTotals.getText()));

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

    private void SetGameBoardMode(Game mGame) {
        Group pledgeButtons = findViewById(R.id.pledgeButtons);
        switch (mGame.getGamestatus()) {
            case Constant.GAMENOTSTARTED:
                pledgeButtons.setVisibility(View.GONE);
                tvGameNotStarted.setText(String.format("Game Not Started"));
                tvGameNotStarted.setVisibility(View.VISIBLE);
                ClearGameBoard();
                break;
            case Constant.GAMEINPROGRESS:
                pledgeButtons.setVisibility(View.VISIBLE);
                btnPayNow.setVisibility(View.GONE);
                tvGameNotStarted.setVisibility(View.GONE);
                GetPersonalPledge();
                break;
            case Constant.GAMEOVER:
                GetClientToken();
                GetPersonalPledge();
                    pledgeButtons.setVisibility(View.GONE);
                tvGameNotStarted.setVisibility(View.GONE);
                    btnPayNow.setVisibility(View.VISIBLE);
                btnPayNow.setEnabled(false);
                //  btnPayNow.setOnClickListener(v -> MakeBrainTreePayment());
                break;
        }
        UpdateGameBoard(mGame);
    }

    private void ClearGameBoard() {
        mGame.setHometeamscore(0);
        mGame.setAwayteamscore(0);
        mGame.setTimeleft("00:00");
        mGame.setHometeampledgetotal(0);
        mGame.setAwayteampledgetotal(0);
        mPlayer.setPledgetotal(0);
        UpDatePersonalPledgeTotal(mPlayer);
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


    void SendPaymentMethod(String nonce, double amt) {
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

    private void addPledges(int amount) {
        Map<String, Object> pledge = new HashMap<>();
        pledge.put("game", mGame.getGameid());
        pledge.put("user", mPlayer.getPlayer());
        pledge.put("amount", amount);
        pledge.put("myteam", mPlayer.getMyteam());
        UpdateGameBoardLocal(mPlayer.getMyteam(), amount);
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
    }

    private void UpdateGameBoardLocal(String myTeam, int pledgeAmount) {
        int hometeampledgetotal = utilities.RemoveCurrency(tv_HomeTeamPledgeTotals.getText().toString());
        int awayteampledgetotal = utilities.RemoveCurrency(tv_AwayTeamPledgeTotals.getText().toString());
        int playerpledgetotal = utilities.RemoveCurrency(tv_MyTotalPledgeTotals.getText().toString());

        if (myTeam == "home") {
            tv_HomeTeamPledgeTotals.setText(utilities.FormatCurrency(hometeampledgetotal + pledgeAmount));
        } else {
            tv_AwayTeamPledgeTotals.setText(utilities.FormatCurrency(awayteampledgetotal + pledgeAmount));
        }
        tv_MyTotalPledgeTotals.setText(utilities.FormatCurrency(playerpledgetotal + pledgeAmount));

    }

    private void undoLastPledge() {
        addPledges(mMyLastPledge * -1);
        mUndoLastPledge.setEnabled(false);
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
            String teamName = "The Team"; //mGame.getMyteam().getTeamName();
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
            sConfirmation = String.format("<center>Oops! Your <b>%s Pledge</b><br />has been undone.</center>", utilities.FormatCurrency(pledge_value).replace("-", ""));

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
        tv_HomeTeamPledgeTotals.setText(utilities.FormatCurrency(mGame.getHometeampledgetotal()));
        tv_AwayTeamPledgeTotals.setText(utilities.FormatCurrency(mGame.getAwayteampledgetotal()));
    }


}






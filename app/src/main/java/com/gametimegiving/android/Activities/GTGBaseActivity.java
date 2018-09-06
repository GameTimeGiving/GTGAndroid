package com.gametimegiving.android.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gametimegiving.android.Helpers.Constant;
import com.gametimegiving.android.Helpers.FancyGifDialog;
import com.gametimegiving.android.Helpers.FancyGifDialogListener;
import com.gametimegiving.android.Helpers.GlideApp;
import com.gametimegiving.android.Helpers.Utilities;
import com.gametimegiving.android.R;
import com.gametimegiving.android.models.Game;
import com.gametimegiving.android.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public abstract class GTGBaseActivity extends AppCompatActivity {
    // final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "GTGBaseActivity";
    public User gtguser;
    public Uri photoUrl;
    String userId;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;
    private String username;
    String name;
    String email;
    private TextView tv_PreferredCharityNotice;
    private Handler mHandler = new Handler();
    boolean firstTimer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();
            gtguser = new User(userId, name, email, photoUrl);

        }
    }

    public String ReadSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public int ReadIntSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    public boolean ReadBoolSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }

    public void WriteBoolSharedPref(String key, Boolean bVal) {
        SharedPreferences sharedPref = this.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, bVal);
        editor.commit();
    }

    public void WriteIntSharedPref(String key, int iVal) {
        SharedPreferences sharedPref = this.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, iVal);
        editor.commit();

    }



    public void SetNavDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
                                intent = new Intent(navigationView.getContext(), GameBoardActivity.class);
                                //    mContext.startActivity(intent);
                                break;
                            case R.id.nav_charities:
                                intent = new Intent(navigationView.getContext(), CharitySelection.class);
                                //      mContext.startActivity(intent);
                                break;
                            case R.id.nav_games:
                                intent = new Intent(navigationView.getContext(), GameSelection.class);
                                //        mContext.startActivity(intent);
                                break;
                            case R.id.nav_teams:
                                intent = new Intent(navigationView.getContext(), TeamSelection.class);
                                break;
                            case R.id.nav_profile:
                                intent = new Intent(navigationView.getContext(), Profile.class);
                                break;

                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        navigationView.getContext().startActivity(intent);
                        return true;
                    }
                });

        View headerLayout = navigationView.getHeaderView(0);
        SetUserProfileInfo(headerLayout, gtguser);

    }

    public void SetUserProfileInfo(View view, User user) {
        TextView tvUserName = view.findViewById(R.id.tvusername);
        ImageView userProfileImage = view.findViewById(R.id.userprofileimage);
        tvUserName.setText(String.format("Logged In As: %s", user.getName()));
        String sPhotoUrl = "";
        try {
            sPhotoUrl = photoUrl.toString();
        } catch (NullPointerException ex) {

        }
        if (sPhotoUrl != ""){
            GlideApp.with(getApplicationContext() /* context */)
                    .load(sPhotoUrl)
                    .into(userProfileImage);
            }

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
            case R.id.action_demo:
                RunDemo();
                break;
            case R.id.action_reset:
                ClearSharedPrefs();
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

    public int GetPledgeCount() {
        int Count = ReadIntSharedPref(getString(R.string.pledgeCountSharedPrefName), this);
        return Count;
    }

    public void SaveUserLocal(FirebaseUser user) {
        Utilities.WriteStringSharedPref("user", user.getUid(), this);
        Utilities.WriteStringSharedPref("name", user.getDisplayName(), this);
        Utilities.WriteStringSharedPref("photoURL", user.getPhotoUrl().toString(), this);
    }
    public void ShowWelcomeDialog(String status) {
        new FancyGifDialog.Builder(this)
                .setTitle(String.format("Game %s", status))
                .setMessage("<style text-align='left'>To Play Game Time Giving:<br>(1)Choose Your Teams<br>(2)Choose Your Charities<br>(3)Click a pledge button to record your pledge.<br>(4)When the game is over pay your pledge</style>")
                .setNegativeBtnText("Got it!")
                //.setPositiveBtnBackground("#FF4081")
                // .setPositiveBtnText("Got it!")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.letsplay)   //Pass your Gif here
                .isCancellable(true)
//                .OnPositiveClicked(new FancyGifDialogListener() {
//                    @Override
//                    public void OnClick() {
//                        Toast.makeText(GTGBaseActivity.this,"Ok",Toast.LENGTH_SHORT).show();
//                    }
//                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        TurnOffFirstTimeInSwith();
                    }
                })
                .build();
    }

    private void RunDemo() {
        //Set the Game status to Not Started
        //   String demoplayerid="sS0p0a631HWTcZUaIATy";
        String message = "Operating in Demo Mode... Please wait 5 seconds";
        GTGSnackBar(findViewById(R.id.GameBoardLayout), message);
        // GTGToast(message);
        WriteBoolSharedPref("demo", true);
        UpdateGameStatus(Constant.GAMENOTSTARTED);
        // Utilities.WriteStringSharedPref("player", demoplayerid,this);
        // mPlayer.setId(demoplayerid);
        mHandler.postDelayed(new Runnable() {
            public void run() {
                UpdateGameStatus(Constant.GAMEINPROGRESS);
            }
        }, 5000);
        //Count 5 pledges
        //Set the Game Status to Over
    }

    private void GTGToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    public void UpdateGameStatus(String status) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> game = new HashMap<>();
        game.put("gamestatus", status);
        db.collection("games").document("suYroi6ZuratHkBDuyF7")
                .update(game)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Game Status Updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void ClearSharedPrefs() {
        final SharedPreferences sharedpreferences = this.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();
    }

    public void GTGSnackBar(View v, String Message) {
        try {
            Snackbar.make(v, Message, Snackbar.LENGTH_LONG).show();
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "Error showing Snack Bar: " + ex.toString());

        }

    }
    public String GetUserIdFromSharedPrefs() {
        return  ReadSharedPref("user", this);
    }

    public String GetPlayerFromSharedPrefs() {
        return  ReadSharedPref("player", this);
    }
    public boolean isFirstTimeIn(Game game) {
        String status = "";
        firstTimer = true;
        boolean val = ReadBoolSharedPref("firsttimein", this);
        Log.d(TAG, String.format("Inside isFirstTime firstTimer is %s and val is %s",
                String.valueOf(firstTimer), String.valueOf(val)));
        if (val == true) {
            if (game.getGamestatus() == Constant.GAMENOTSTARTED) {
                status = "Not Started";
            }
            if (game.getGamestatus() == Constant.GAMEINPROGRESS) {
                status = "In Progress";
            }
            if (game.getGamestatus() == Constant.GAMEOVER) {
                status = "Over";
            }
            ShowWelcomeDialog(status);
            TurnOffFirstTimeInSwith();
            firstTimer = true;
        }
        return firstTimer;
    }

    //    public void showdialog() {
//        final CustomizeDialog customizeDialog = new CustomizeDialog(this);
//        customizeDialog.setContentView(R.layout.preferredcharitynotice);
//        tv_PreferredCharityNotice = customizeDialog.findViewById(R.id.tv_preferredCharityNotice);
//        Button button = customizeDialog.findViewById(R.id.btnok);
//        button.setOnClickListener(v -> customizeDialog.dismiss());
//        Log.d(TAG,"Showing Dialog");
//            String teamName = "The Team"; //mGame.getMyteam().getTeamName();
//            String charityName = "The Charity";//mGame.getMyteam().getPreferredCharity().getCharityName();
//            String preferredCharityMessage =
//                    String.format("NOTICE: Your team, %s, has chosen to support %s as a Preferred Charity. Your pledges will be split equally between the" +
//                            "charities you and %s have chosen.", teamName, charityName, teamName);
//            tv_PreferredCharityNotice.setText(preferredCharityMessage);
//            customizeDialog.show();
//            customizeDialog.setCancelable(false);
//    }
    public void TurnOffFirstTimeInSwith() {
        WriteBoolSharedPref("firsttimein", false);
    }

    public void TurnOnFirstTimeInSwith() {
        WriteBoolSharedPref("firsttimein", true);
    }

    public boolean isFirstTimeUser() {
        boolean firstTimer = false;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();
        if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
            Toast.makeText(this, "This appears to be your first time here.", Toast.LENGTH_LONG).show();
            firstTimer = true;
        } else {
            Toast.makeText(this, String.format("Welcome Back %s", auth.getCurrentUser().getDisplayName()), Toast.LENGTH_LONG).show();
            firstTimer = false;
        }
        return firstTimer;
    }
}

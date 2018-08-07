package com.gametimegiving.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;

public abstract class GTGBaseActivity extends AppCompatActivity {
    public User gtguser;
    public Uri photoUrl;
    String userId;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;
    private String username;
    private String name;
    private String email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();
            gtguser = new User(userId, name, email, photoUrl.toString());
        }
    }

    public String ReadSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public void WriteSharedPref(String key, String val, String type) {
        SharedPreferences sharedPref = this.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (type) {
            case "s":
                editor.putString(key, val);
                break;
            case "b":
                boolean bVal = false;
                if (val.equals("true")) {
                    bVal = true;
                }
                editor.putBoolean(key, bVal);
                break;
            case "i":
                Integer iVal = Integer.parseInt(val);
                editor.putInt(key, iVal);
                break;

        }

        editor.apply();
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
        tvUserName.setText(String.format("Logged In As: %s", gtguser.getName()));
        if (photoUrl.toString() != "") {
            GlideApp.with(getApplicationContext() /* context */)
                    .load(photoUrl)
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
                Toast.makeText(this, "Demo N/A", Toast.LENGTH_SHORT).show();
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

    public void ClearSharedPrefs() {
        final SharedPreferences sharedpreferences = this.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();
    }

    public boolean isFirstTimeUser() {
        boolean firstTimer = false;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();
        if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
            Toast.makeText(this, "This appears to be your first time here.", Toast.LENGTH_LONG).show();
            firstTimer = true;
        } else {
            Toast.makeText(this, String.format("Welcome Back,", auth.getCurrentUser().getDisplayName()), Toast.LENGTH_LONG).show();
            firstTimer = false;
        }
        return firstTimer;
    }
}

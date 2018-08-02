package com.gametimegiving.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeamSelection extends AppCompatActivity {
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    final public String TAG = "TeamSelection";
    List<Team> teamList;
    private RecyclerView mRecyclerView;
    private TeamAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);
        SetNavDrawer();
        GetTeam("ALL");
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_signout:
                FirebaseAuth.getInstance().signOut();
                this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void SetAdapter() {
        mRecyclerView = findViewById(R.id.listofteams);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TeamAdapter(this, teamList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void GetTeam(String all) {
        // int gameId = 001;
        teamList = new ArrayList<Team>();
        db.collection("teams")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Team team = document.toObject(Team.class);
                            teamList.add(team);
                        }
                        SetAdapter();
                    } else {
                        Log.d(TAG, "Getting Teams is failing");
                    }
                });

    }

    private void SetNavDrawer() {
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
                                intent = new Intent(mContext, GameBoardActivity.class);
                                break;
                            case R.id.nav_charities:
                                intent = new Intent(mContext, CharitySelection.class);
                                break;
                            case R.id.nav_games:
                                intent = new Intent(mContext, GameSelection.class);
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
}

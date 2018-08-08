package com.gametimegiving.android.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.gametimegiving.android.Adapters.GameAdapter;
import com.gametimegiving.android.R;
import com.gametimegiving.android.models.Game;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GameSelection extends GTGBaseActivity {
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    final public String TAG = "GameSelection";
    List<Game> gameList;
    private RecyclerView mRecyclerView;
    private GameAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //  private DrawerLayout mDrawerLayout;
    //  private ActionBarDrawerToggle mDrawerToggle;
    //   private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);
        SetNavDrawer();

        GetGame("ALL");
//        mDrawerLayout = findViewById(R.id.drawer_layout);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.mainmenu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_settings:
//                Utilities.ShowVersion(this);
//                break;
//            case R.id.action_signout:
//                FirebaseAuth.getInstance().signOut();
//                Intent startMain = new Intent(Intent.ACTION_MAIN);
//                startMain.addCategory(Intent.CATEGORY_HOME);
//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(startMain);
//
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void SetAdapter() {
        mRecyclerView = findViewById(R.id.listofgames);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GameAdapter(this, gameList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void GetGame(String all) {
        // int gameId = 001;
        gameList = new ArrayList<Game>();
        db.collection("games")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Game game = document.toObject(Game.class);
                            gameList.add(game);
                            Log.d(TAG, String.format("Home Team Name: %s", game.getHometeam().getTeamname()));
                        }
                        SetAdapter();
                    } else {
                        Log.d(TAG, "Getting Games is failing");
                    }
                });

    }

//    private void SetNavDrawer() {
//        mDrawerLayout = findViewById(R.id.drawer_layout);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        // set item as selected to persist highlight
//                        menuItem.setChecked(true);
//                        // close drawer when item is tapped
//                        mDrawerLayout.closeDrawers();
//                        int id = menuItem.getItemId();
//                        Intent intent = null;
//                        switch (id) {
//                            case R.id.nav_gameboard:
//                                intent = new Intent(mContext, GameBoardActivity.class);
//                                break;
//                            case R.id.nav_charities:
//                                intent = new Intent(mContext, CharitySelection.class);
//                                break;
//                            case R.id.nav_games:
//                                intent = new Intent(mContext, GameSelection.class);
//                                break;
//                            case R.id.nav_teams:
//                                intent = new Intent(mContext, TeamSelection.class);
//                                break;
//                            case R.id.nav_profile:
//                                intent = new Intent(mContext, Profile.class);
//                                break;
//
//                        }
//                        // Add code here to update the UI based on the item selected
//                        // For example, swap UI fragments here
//                        mContext.startActivity(intent);
//                        return true;
//                    }
//                });
//    }
}

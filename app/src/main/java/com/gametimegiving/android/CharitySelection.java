package com.gametimegiving.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CharitySelection extends AppCompatActivity {
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    final public String TAG = "CharitySelection";
    List<Charity> charityList;
    private RecyclerView mRecyclerView;
    private CharityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_selection);
        GetCharity("ALL");
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigationdrawermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.nav_gameboard) {
            Intent intent = new Intent(this, GameBoardActivity.class);
            this.startActivity(intent);
            return true;
        }

        if (id == R.id.nav_charities) {
            Intent intent = new Intent(this, CharitySelection.class);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // call super.onBackPressed();  at last.
        Intent intent = new Intent(this, CharitySelection.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private void SetAdapter() {
        mRecyclerView = findViewById(R.id.listofcharities);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CharityAdapter(this, charityList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void GetCharity(String all) {
        // int gameId = 001;
        charityList = new ArrayList<Charity>();
        db.collection("charity")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Charity charity = document.toObject(Charity.class);
                            charityList.add(charity);
                        }
                        SetAdapter();
                    } else {
                        Log.d(TAG, "Getting Charities is failing");
                    }
                });

    }
}

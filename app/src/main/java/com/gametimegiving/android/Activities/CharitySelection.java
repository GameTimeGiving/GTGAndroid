package com.gametimegiving.android.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.gametimegiving.android.Adapters.CharityAdapter;
import com.gametimegiving.android.R;
import com.gametimegiving.android.models.Charity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CharitySelection extends GTGBaseActivity {
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    final public String TAG = "CharitySelection";
    List<Charity> charityList;
    private RecyclerView mRecyclerView;
    private CharityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_selection);
        SetNavDrawer();
        GetCharity("ALL");

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
                            charity.setId(document.getId());
                            charityList.add(charity);
                        }
                        SetAdapter();
                    } else {
                        Log.d(TAG, "Getting Charities is failing");
                    }
                });

    }


}

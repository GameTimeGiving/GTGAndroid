package com.gametimegiving.android.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.gametimegiving.android.Adapters.TeamAdapter;
import com.gametimegiving.android.R;
import com.gametimegiving.android.models.Team;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeamSelection extends GTGBaseActivity {
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    final public String TAG = "TeamSelection";
    List<Team> teamList;
    private RecyclerView mRecyclerView;
    private TeamAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);
        SetNavDrawer();
        GetTeam("ALL");
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

}

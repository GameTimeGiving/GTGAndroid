package com.gametimegiving.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CharityDetail extends AppCompatActivity {
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    final public String TAG = "CharityDetail";
    List<Charity> charityList;
    private RecyclerView mRecyclerView;
    private CharityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext = this;
    String charityid;
    String lastviewedcharityid;
    ImageView ivCharity;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    Button btnSaveCharity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        charityid = bundle.getString("charityid");
        Log.d(TAG, String.format("Opening the detail for charity %s", charityid));
        setContentView(R.layout.activity_charity_detail);
        SetNavDrawer();
        GetCharity(charityid);
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

    private void SetAdapter() {
        mRecyclerView = findViewById(R.id.listofcharities);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CharityAdapter(this, charityList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void GetCharity(String charityid) {
        // int gameId = 001;
        charityList = new ArrayList<Charity>();
        db.collection("charity").document(charityid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Charity charity = document.toObject(Charity.class);
                        if (charity != null) {
                            Log.d(TAG, String.format("The charity name is %s", charity.getName()));
                            SetCharityDetail(charity);
                        }
                    } else {
                        Log.d(TAG, "Getting Charities is failing");
                    }
                });

    }

    private void SetCharityDetail(Charity charity) {
        Typeface varsity_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/varsity_regular.ttf");
        TextView tvcharityname = findViewById(R.id.tvcharityname);
        tvcharityname.setTypeface(varsity_font);
        tvcharityname.setText(charity.getName());
        TextView tvcharitydetails = findViewById(R.id.tvcharitydetails);
        tvcharitydetails.setText(charity.getDetail());
        TextView tvamountraised = findViewById(R.id.tvamountraised);
        String totalAmountRaisedByCharity = Utilities.FormatCurrency(charity.getTotalAmountRaised());
        tvamountraised.setText(String.format("Amount Raised via GTG; %s", totalAmountRaisedByCharity));
        ivCharity = findViewById(R.id.charitylogo);
        String charityLogo = charity.getLogo();
        StorageReference charityLogoReference;
        try {
            charityLogoReference = storage.getReferenceFromUrl(charityLogo);
        } catch (Exception ex) {
            charityLogoReference = storage.getReferenceFromUrl(String.valueOf(R.string.defaultcharitylogo));

        }
        GlideApp.with(mContext)
                .load(charityLogoReference)
                .into(ivCharity);
        btnSaveCharity = findViewById(R.id.btnCharityDetailSave);
        btnSaveCharity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) (v.getContext());
                Toast.makeText(v.getContext(), String.format("Saving %s to your profile.", charity.getName()), Toast.LENGTH_SHORT).show();
                Utilities.WriteSharedPref("SCharity1", charityid, activity, "s");
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
                menuItem -> {
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
                });
    }
}

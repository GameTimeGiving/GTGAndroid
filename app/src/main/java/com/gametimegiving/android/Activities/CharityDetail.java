package com.gametimegiving.android.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gametimegiving.android.Adapters.CharityAdapter;
import com.gametimegiving.android.Helpers.GlideApp;
import com.gametimegiving.android.Helpers.Utilities;
import com.gametimegiving.android.R;
import com.gametimegiving.android.models.Charity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CharityDetail extends GTGBaseActivity {
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
                Utilities.WriteStringSharedPref("SCharity1", charityid, CharityDetail.this);
            }
        });

    }


}

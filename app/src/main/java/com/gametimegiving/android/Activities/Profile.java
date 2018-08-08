package com.gametimegiving.android.Activities;

import android.os.Bundle;

import com.gametimegiving.android.R;

public class Profile extends GTGBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SetNavDrawer();
    }
}

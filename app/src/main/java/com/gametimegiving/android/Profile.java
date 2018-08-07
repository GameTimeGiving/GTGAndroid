package com.gametimegiving.android;

import android.os.Bundle;

public class Profile extends GTGBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SetNavDrawer();
    }
}

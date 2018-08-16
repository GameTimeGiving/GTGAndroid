package com.gametimegiving.android.Activities;

import android.os.Bundle;
import android.widget.TextView;

import com.gametimegiving.android.Helpers.Constant;
import com.gametimegiving.android.R;

public class Profile extends GTGBaseActivity {
    TextView tv_teamsifollowinprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tv_teamsifollowinprofile = findViewById(R.id.tv_teamsifollowinprofile);
        String teamsIfollow = ReadSharedPref(Constant.TEAMSIFOLLOW, this);

        tv_teamsifollowinprofile.setText("My Teams: \n" + teamsIfollow.replaceAll("\\p{P}", ""));
        SetNavDrawer();
    }
}

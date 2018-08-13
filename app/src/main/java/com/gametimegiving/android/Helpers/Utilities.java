package com.gametimegiving.android.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gametimegiving.android.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;

public class Utilities {

    public void ShowMsg(String message, Context ctx) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }

    public void NotYetImplemented(Context ctx) {
        Toast.makeText(ctx, "Not Yet Implemented", Toast.LENGTH_LONG).show();
    }

    public static String FormatCurrency(double num) {
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        return defaultFormat.format(num);

    }



    public Integer ReadSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    public Boolean ReadBoolSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }

    public static void GenerateHashKey(Context ctx) {
        String TAG = "GenerateHashKey";
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(
                    "com.gametimegiving.android",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(TAG, Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, String.format("PackageManager Name Not Found Exception:%s", e.toString()));

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, String.format("No Such Algorithm Exception:%s", e.toString()));
        }
    }

    public static void SetUserProfileInfo(View view, String username, String photoUrl, Context context) {
        TextView tvUserName = view.findViewById(R.id.tvusername);
        ImageView userProfileImage = view.findViewById(R.id.userprofileimage);
        tvUserName.setText(String.format("Logged In As: %s", username));
        if (photoUrl != "") {
            GlideApp.with(context /* context */)
                    .load(photoUrl)
                    .into(userProfileImage);
        }
    }


    public static void ShowVersion(Context ctx) {
        String versionName = "N/A";
        try {
            versionName = ctx.getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(ctx, String.format("Version:%s", versionName), Toast.LENGTH_SHORT).show();
    }

    public static void WriteStringSharedPref(String key, String val, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public int RemoveCurrency(String dollars) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        Number number;
        int finalnumber;
        try {
            number = format.parse(dollars);
            finalnumber = (int) (long) number;
        } catch (ParseException e) {
            finalnumber = 0;
        }

        return finalnumber;
    }


}
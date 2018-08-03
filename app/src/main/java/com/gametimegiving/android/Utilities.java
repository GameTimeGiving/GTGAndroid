package com.gametimegiving.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;

public class Utilities {
    public void ShowMsg(String message, Context ctx) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }

    public void NotYetImplemented(Context ctx) {
        Toast.makeText(ctx, "Not Yet Implemented", Toast.LENGTH_LONG).show();
    }

    public void WriteSharedPref(String key, String val, Activity activity, String type) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (type) {
            case "s":
                editor.putString(key, val);
                break;
            case "b":
                boolean bVal = false;
                if (val.equals("true")) {
                    bVal = true;
                }
                editor.putBoolean(key, bVal);
                break;
            case "i":
                Integer iVal = Integer.parseInt(val);
                editor.putInt(key, iVal);
                break;

        }

        editor.commit();
    }

    public String ReadSharedPref(String key, Activity activity, String type) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public Integer ReadSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    public Boolean ReadBoolSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }


    public String FormatCurrency(double num) {
        String value;
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        return defaultFormat.format(num);

    }

    public int RemoveCurrency(String dollars) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        Number number = 0;
        int finalnumber = 0;
        try {
            number = format.parse(dollars);
            finalnumber = (int) (long) number;
        } catch (ParseException e) {
            finalnumber = 0;
        }

        return finalnumber;
    }

    public void ClearSharedPrefs(Activity activity) {
        final SharedPreferences sharedpreferences = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();
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
}
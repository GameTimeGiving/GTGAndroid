
package com.gametimegiving.android;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;


class CustomizeDialog extends Dialog {

    private Context context;

    CustomizeDialog(Context context) {
        super(context, R.style.Theme_Dialog_Translucent);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            this.context = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

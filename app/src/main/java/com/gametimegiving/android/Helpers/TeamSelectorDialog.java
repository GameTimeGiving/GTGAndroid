package com.gametimegiving.android.Helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.gametimegiving.android.R;

public class TeamSelectorDialog extends DialogFragment {
    String[] TeamsFromGameArray;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TeamsFromGameArray[0] = "Neville";
        TeamsFromGameArray[1] = "Ouachita";
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pickyourteam)
                .setItems(TeamsFromGameArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Toast.makeText(getActivity(), "Picked item 1", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getActivity(), "Picked item 2", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                        }
                    }
                });


        return builder.create();
    }
}

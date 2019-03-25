package com.example.boris.financialstatement.Managers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.example.boris.financialstatement.Adapters.RecyclerAdapter;
import com.example.boris.financialstatement.MainActivity;

public class DialigAlert extends DialogFragment {

    private MainActivity mainActivity;
    private int id;
    private int typCase;
    private String text;

    public void setData(MainActivity mainActivity, int id, int typCase, String name) {
        this.mainActivity = mainActivity;
        this.id = id;
        this.typCase = typCase;
        text = name;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  // create builder
        builder.setMessage("do you want to delete " + text); // text

        // if yes
        builder.setPositiveButton("yes", (dialog, which) -> {
            switch (typCase){
                case 1:
                    mainActivity.deleteIn(id);
                    break;
                case 2:
                    mainActivity.deleteOut(id);
                    break;
                case 3:
                    mainActivity.deleteAss(id);
                    break;
                case 4:
                    mainActivity.deleteLiab(id);
                    break;
            }
        });
        // if no
        builder.setNegativeButton("no", (dialog, which) -> {

        });
        return builder.create();
    }


}

package com.example.boris.financialstatement.Models;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.boris.financialstatement.MainActivity;
import com.example.boris.financialstatement.R;

public class CustomAlert {
    private Dialog dialog;
    private boolean isActive = false;
    public void showDialog(MainActivity activity, int typeCase){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert);

        ImageButton imageButton = dialog.findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        ImageButton imageButton2 = dialog.findViewById(R.id.imageButton3);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = dialog.findViewById(R.id.alertTitle);
                EditText cost = dialog.findViewById(R.id.alertCost);

                String name = title.getText().toString();
                int price = Integer.parseInt(cost.getText().toString());


                switch (typeCase){
                    case 1:
                        activity.addIn(name, price);
                        break;
                }

                dismissDialog();
            }
        });

        isActive = true;
        dialog.show();

    }

    public void dismissDialog(){
        isActive = false;
        dialog.dismiss();
    }

    public boolean isActive(){
        return isActive;
    }

}

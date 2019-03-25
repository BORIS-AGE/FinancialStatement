package com.example.boris.financialstatement.Managers;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.boris.financialstatement.MainActivity;
import com.example.boris.financialstatement.R;

public class CustomAlert {
    private Dialog dialog;
    private boolean editMode = false;
    private String defaultName = "";
    private String defaultCost = "";
    private int id;

    public CustomAlert() {
    }

    public CustomAlert(boolean editMode, String defaultName, String defaultCost, int id) {
        this.editMode = editMode;
        this.defaultName = defaultName;
        this.defaultCost = defaultCost;
        this.id = id;
    }

    public void showDialog(MainActivity activity, int typeCase){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert);

        EditText title = dialog.findViewById(R.id.alertTitle);
        EditText cost = dialog.findViewById(R.id.alertCost);
        title.setText(defaultName);
        cost.setText(defaultCost);

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
                String name = title.getText().toString();
                int price = Integer.parseInt(cost.getText().toString());
                if (name.trim().equals("") || cost.getText().toString().trim().equals("")){
                    Toast.makeText(activity.getApplicationContext(), "incorrect value", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!editMode){
                    switch (typeCase){
                        case 1:
                            activity.addIn(name, price);
                            break;
                        case 2:
                            activity.addOut(name, price);
                            break;
                        case 3:
                            activity.addAss(name, price);
                            break;
                        case 4:
                            activity.addLiab(name, price);
                            break;
                    }
                }else{
                    switch (typeCase){
                        case 1:
                            activity.updateIn(name, price, id);
                            break;
                        case 2:
                            activity.updateOut(name, price, id);
                            break;
                        case 3:
                            activity.updateAss(name, price, id);
                            break;
                        case 4:
                            activity.updateLiab(name, price, id);
                            break;
                    }
                }

                dismissDialog();
            }
        });
        dialog.show();

    }

    public void dismissDialog(){
        dialog.dismiss();
    }

}

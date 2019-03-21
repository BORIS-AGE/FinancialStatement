package com.example.boris.financialstatement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boris.financialstatement.Adapters.RecyclerAdapter;
import com.example.boris.financialstatement.Models.CashModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recIn, recOut, recAss, recLiab;
    private TextView cash, textIn, textOut, textPayday, textExpenses;
    private List<CashModel> listIn, listOut, listAss, listLiab;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() == null){
                startActivity(new Intent(this, RegistrationActiviry.class));
                finish();
            }
        };
        auth.addAuthStateListener(authStateListener);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_input_black_24dp);

        setDefaults();
        setLists();
        setRecyclers();
        countCash();

        Toast.makeText(getApplicationContext(), auth.getCurrentUser().getEmail() + "", Toast.LENGTH_LONG).show();
    }

    private void countCash() {
        int income = 0;
        int outcome = 0;
        for (int i = 0; i < listIn.size(); i++) {
            income += listIn.get(i).getCash();
        }

        for (int i = 0; i < listOut.size(); i++) {
            outcome += listOut.get(i).getCash();
        }


        textIn.setText("$" + splitNumberWithTeams(income + ""));
        textOut.setText("$-" + splitNumberWithTeams(outcome + ""));
        textPayday.setText("$" + splitNumberWithTeams((income - outcome) + ""));
    }

    private void setRecyclers() {
        RecyclerAdapter adapterIn = new RecyclerAdapter(listIn, this);
        recIn.setLayoutManager(new LinearLayoutManager(this));
        recIn.setHasFixedSize(true);
        recIn.setAdapter(adapterIn);

        RecyclerAdapter adapterOut = new RecyclerAdapter(listOut, this);
        recOut.setLayoutManager(new LinearLayoutManager(this));
        recOut.setHasFixedSize(true);
        recOut.setAdapter(adapterOut);

        RecyclerAdapter adapterAss = new RecyclerAdapter(listAss, this);
        recAss.setLayoutManager(new LinearLayoutManager(this));
        recAss.setHasFixedSize(true);
        recAss.setAdapter(adapterAss);

        RecyclerAdapter adapterLiab = new RecyclerAdapter(listLiab, this);
        recLiab.setLayoutManager(new LinearLayoutManager(this));
        recLiab.setHasFixedSize(true);
        recLiab.setAdapter(adapterLiab);
    }

    private void setLists() {
        listIn = new ArrayList<>();
        listOut = new ArrayList<>();
        listAss = new ArrayList<>();
        listLiab = new ArrayList<>();

        listIn.add(new CashModel("aaaaaaa: ", 123123));
        listIn.add(new CashModel("aaaaaaa: ", 123123));
        listIn.add(new CashModel("aaaaaaa: ", 123123));
        listIn.add(new CashModel("aaaaaaa: ", 123123));
        listIn.add(new CashModel("aaaaaaa: ", 123123));
        listIn.add(new CashModel("aaaaaaa: ", 123123));
        listIn.add(new CashModel("aaaaaaa: ", 123123));
        listIn.add(new CashModel("aaaaaaa: ", 123123));

        listOut.add(new CashModel("aaaaaaa: ", 123123));
        listOut.add(new CashModel("aaaaaaa: ", 123123));
        listOut.add(new CashModel("aaaaaaa: ", 123123));
        listOut.add(new CashModel("aaaaaaa: ", 123123));
        listOut.add(new CashModel("aaaaaaa: ", 123123));
        listOut.add(new CashModel("aaaaaaa: ", 123123));
        listOut.add(new CashModel("aaaaaaa: ", 123123));
        listOut.add(new CashModel("aaaaaaa: ", 123123));

        listAss.add(new CashModel("aaaaaaa: ", 123123));
        listAss.add(new CashModel("aaaaaaa: ", 123123));
        listAss.add(new CashModel("aaaaaaa: ", 123123));
        listAss.add(new CashModel("aaaaaaa: ", 123123));
        listAss.add(new CashModel("aaaaaaa: ", 123123));
        listAss.add(new CashModel("aaaaaaa: ", 123123));
        listAss.add(new CashModel("aaaaaaa: ", 123123));
        listAss.add(new CashModel("aaaaaaa: ", 123123));

        listLiab.add(new CashModel("aaaaaaa: ", 123123));
        listLiab.add(new CashModel("aaaaaaa: ", 123123));
        listLiab.add(new CashModel("aaaaaaa: ", 123123));
        listLiab.add(new CashModel("aaaaaaa: ", 123123));
        listLiab.add(new CashModel("aaaaaaa: ", 123123));
        listLiab.add(new CashModel("aaaaaaa: ", 123123));
        listLiab.add(new CashModel("aaaaaaa: ", 123123));
        listLiab.add(new CashModel("aaaaaaa: ", 123123));


    }

    private void setDefaults() {
        recIn = findViewById(R.id.recyclerIncome);
        recOut = findViewById(R.id.recyclerOutcome);
        recAss = findViewById(R.id.recyclerAssets);
        recLiab = findViewById(R.id.recyclerLiabilities);

        cash = findViewById(R.id.cash);
        textIn = findViewById(R.id.cashIn);
        textOut = findViewById(R.id.cashOut);
        textPayday = findViewById(R.id.payday);
        textExpenses = findViewById(R.id.textExpenses);

    }

    public static String splitNumberWithTeams(String str){
        if (str.length() > 3){
            String temp = "";
            for (int i = 0; i < str.length(); i++) {
                if (i % 3 == 0 && i != 0){
                    temp += ",";
                }
                temp += str.charAt(i);
            }
            return temp;
        }
        else {
            return str;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                FirebaseAuth.getInstance().signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

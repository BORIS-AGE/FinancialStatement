package com.example.boris.financialstatement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boris.financialstatement.Adapters.RecyclerAdapter;
import com.example.boris.financialstatement.Models.ArrayModel;
import com.example.boris.financialstatement.Models.CashModel;
import com.example.boris.financialstatement.Models.CustomAlert;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference modelRef;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private CustomAlert customAlert = new CustomAlert();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validateRegistration();

        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_input_black_24dp);

        setDefaults();
        setLists();
        }

    private void countCash() {
        int income = 0;
        int outcome = 0;
        if (listIn != null){
            for (int i = 0; i < listIn.size(); i++) {
                income += listIn.get(i).getCash();
            }
        }else{
            Toast.makeText(getApplicationContext(),"income is null", Toast.LENGTH_LONG).show();
        }

        if (listOut != null){
            for (int i = 0; i < listOut.size(); i++) {
                outcome += listOut.get(i).getCash();
            }
        }else{
            Toast.makeText(getApplicationContext(),"outcome is null", Toast.LENGTH_LONG).show();
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
        progressDialog.setMessage("Wait...");
        progressDialog.show();
      myRef.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayModel arrayModel = dataSnapshot.getValue(ArrayModel.class);

                if (arrayModel == null){
                    myRef.child(auth.getUid()).setValue(new ArrayModel(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listIn = new ArrayList<>();
                            listOut = new ArrayList<>();
                            listAss = new ArrayList<>();
                            listLiab = new ArrayList<>();
                        }
                    });
                }else{
                    listIn = new ArrayList<>(arrayModel.getListIn() != null ? arrayModel.getListIn() : new ArrayList<>());
                    listOut = new ArrayList<>(arrayModel.getListOut() != null ? arrayModel.getListOut() : new ArrayList<>());
                    listAss = new ArrayList<>(arrayModel.getListAss() != null ? arrayModel.getListAss() : new ArrayList<>());
                    listLiab = new ArrayList<>(arrayModel.getListLiab() != null ? arrayModel.getListLiab() : new ArrayList<>());
                }
                setRecyclers();
                countCash();

                if (progressDialog.isShowing())
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

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

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Data");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        progressDialog = new ProgressDialog(this);
    }

    public static String splitNumberWithTeams(String str){
        if (str.length() > 3){
            String temp = "";
            for (int i = 1; i < str.length(); i++) {
                if (i % 3 == 0 && i != 0){
                    temp += ",";
                }
                temp += str.charAt(str.length() - i);
            }

            str = "";

            for (int i = 1; i < temp.length(); i++) {
                str += temp.charAt(temp.length() - i);
            }

            return str;
        }
        else {
            return str;
        }
    }

    private void validateRegistration(){
        auth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() == null){
                startActivity(new Intent(this, RegistrationActiviry.class));
                finish();
            }
        };
        auth.addAuthStateListener(authStateListener);
        if (auth.getCurrentUser() == null){
            startActivity(new Intent(this, RegistrationActiviry.class));
            finish();
        }else{
            user = auth.getCurrentUser();
        }
    }

    public void addIncome(View view) {
        customAlert.showDialog(this, 1);
    }

    public void addIn(String name, int cost){
        listIn.add(new CashModel(name, cost));
        myRef.child(auth.getUid()).setValue(new ArrayModel(listIn,listOut,listAss,listLiab)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setLists();
            }
        });
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

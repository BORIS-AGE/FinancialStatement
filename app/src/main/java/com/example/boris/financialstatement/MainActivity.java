package com.example.boris.financialstatement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boris.financialstatement.Adapters.RecyclerAdapter;
import com.example.boris.financialstatement.Managers.DialigAlert;
import com.example.boris.financialstatement.Models.ArrayModel;
import com.example.boris.financialstatement.Models.CashModel;
import com.example.boris.financialstatement.Managers.CustomAlert;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recIn, recOut, recAss, recLiab;
    private TextView cash, textIn, textOut, textPayday, textExpenses;
    private List<CashModel> listIn, listOut, listAss, listLiab;
    private int totalCash = 0;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private CustomAlert customAlert = new CustomAlert();
    private boolean changedIn = false, changedOut = false, changedAss = false, changedLiab = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validateRegistration();

        setContentView(R.layout.activity_main);

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
        cash.setText(totalCash + "");
    }

    private void setRecyclers() {
        RecyclerAdapter adapterIn = new RecyclerAdapter(listIn, this, 1);
        recIn.setLayoutManager(new LinearLayoutManager(this));
        recIn.setHasFixedSize(true);
        recIn.setAdapter(adapterIn);

        RecyclerAdapter adapterOut = new RecyclerAdapter(listOut, this, 2);
        recOut.setLayoutManager(new LinearLayoutManager(this));
        recOut.setHasFixedSize(true);
        recOut.setAdapter(adapterOut);

        RecyclerAdapter adapterAss = new RecyclerAdapter(listAss, this, 3);
        recAss.setLayoutManager(new LinearLayoutManager(this));
        recAss.setHasFixedSize(true);
        recAss.setAdapter(adapterAss);

        RecyclerAdapter adapterLiab = new RecyclerAdapter(listLiab, this, 4);
        recLiab.setLayoutManager(new LinearLayoutManager(this));
        recLiab.setHasFixedSize(true);
        recLiab.setAdapter(adapterLiab);
    }

    private void setLists() {
        progressDialog.setMessage("Wait...");
        progressDialog.show();
      myRef.child(auth.getUid()).orderByChild("cash").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayModel arrayModel = dataSnapshot.getValue(ArrayModel.class);

                if (arrayModel == null){
                    myRef.child(auth.getUid()).setValue(new ArrayModel(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), totalCash)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listIn = new ArrayList<>();
                            listOut = new ArrayList<>();
                            listAss = new ArrayList<>();
                            listLiab = new ArrayList<>();
                            totalCash = 0;
                        }
                    });
                }else{
                    listIn = new ArrayList<>(arrayModel.getListIn() != null ? arrayModel.getListIn() : new ArrayList<>());
                    listOut = new ArrayList<>(arrayModel.getListOut() != null ? arrayModel.getListOut() : new ArrayList<>());
                    listAss = new ArrayList<>(arrayModel.getListAss() != null ? arrayModel.getListAss() : new ArrayList<>());
                    listLiab = new ArrayList<>(arrayModel.getListLiab() != null ? arrayModel.getListLiab() : new ArrayList<>());
                    totalCash = arrayModel.getCash();
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_input_black_24dp);

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

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCash += 100;
                saveAndUpdatValue("cash", totalCash);
            }
        });

        progressDialog = new ProgressDialog(this);
    }

    public static String splitNumberWithTeams(String str){
         str = str.trim();
         boolean lowerThanZero = Integer.parseInt(str) < 0;
        if (str.length() > 3){
            StringBuilder temp = new StringBuilder();
            StringBuilder temp2 = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {
                if (i % 3 == 0 && i != 0){
                    if (!lowerThanZero || i != str.length() - 1)
                    temp.append(",");
                }
                temp.append(str.charAt(str.length() - i - 1));
            }

            for (int i = 0; i < temp.length(); i++) {
                temp2.append(temp.charAt(temp.length() - i - 1));
            }

            return temp2.toString();
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

    private void saveAndUpdatValue(String name, Object list){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put(name, list);

        myRef.child(auth.getUid()).updateChildren(modelMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                setLists();
            }
        });

        /*myRef.child(auth.getUid()).setValue(new ArrayModel(listIn,listOut,listAss,listLiab, totalCash)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setLists();
            }
        });*/
    }

    public void addIncome(View view) {
        customAlert.showDialog(this, 1);
    }
    public void addOutcome(View view) {
        customAlert.showDialog(this, 2);
    }
    public void addAssets(View view) {
        customAlert.showDialog(this, 3);
    }
    public void addLiab(View view) {
        customAlert.showDialog(this, 4);
    }

    public void addIn(String name, int cost){
        listIn.add(new CashModel(name, cost));
        saveAndUpdatValue("listIn", listIn);
    }
    public void addOut(String name, int cost){
        listOut.add(new CashModel(name, cost));
        saveAndUpdatValue("listOut",listOut);
    }
    public void addAss(String name, int cost){
        listAss.add(new CashModel(name, cost));
        saveAndUpdatValue("listAss",listAss);
    }
    public void addLiab(String name, int cost){
        listLiab.add(new CashModel(name, cost));
        saveAndUpdatValue("listLiab",listLiab);
    }

    public void updateIn(String name, int cost, int id){
        listIn.remove(id);
        listIn.add(new CashModel(name, cost));
        saveAndUpdatValue("listIn", listIn);
    }
    public void updateOut(String name, int cost, int id){
        listOut.remove(id);
        listOut.add(new CashModel(name, cost));
        saveAndUpdatValue("listOut",listOut);
    }
    public void updateAss(String name, int cost, int id){
        listAss.remove(id);
        listAss.add(new CashModel(name, cost));
        saveAndUpdatValue("listAss",listAss);
    }
    public void updateLiab(String name, int cost, int id){
        listLiab.remove(id);
        listLiab.add(new CashModel(name, cost));
        saveAndUpdatValue("listLiab",listLiab);
    }

    public void deleteIn(int id){
        listIn.remove(id);
        saveAndUpdatValue("listIn", listIn);
    }
    public void deleteOut(int id){
        listOut.remove(id);
        saveAndUpdatValue("listOut",listOut);
    }
    public void deleteAss(int id){
        listAss.remove(id);
        saveAndUpdatValue("listAss",listAss);
    }
    public void deleteLiab(int id){
        listLiab.remove(id);
        saveAndUpdatValue("listLiab",listLiab);

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

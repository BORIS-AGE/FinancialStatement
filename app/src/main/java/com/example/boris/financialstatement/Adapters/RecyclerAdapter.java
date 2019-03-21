package com.example.boris.financialstatement.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.boris.financialstatement.MainActivity;
import com.example.boris.financialstatement.Models.CashModel;
import com.example.boris.financialstatement.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {

    private List<CashModel> items;
    private Context context;

    public RecyclerAdapter(List<CashModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.name.setText(items.get(i).getName());
        myHolder.cash.setText("$" + MainActivity.splitNumberWithTeams(items.get(i).getCash() + ""));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView cash;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.recyclerName);
            cash = itemView.findViewById(R.id.recyclerCash);

        }
    }
}

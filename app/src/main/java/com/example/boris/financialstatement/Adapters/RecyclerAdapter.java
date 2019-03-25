package com.example.boris.financialstatement.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.boris.financialstatement.MainActivity;
import com.example.boris.financialstatement.Managers.CustomAlert;
import com.example.boris.financialstatement.Managers.DialigAlert;
import com.example.boris.financialstatement.Models.CashModel;
import com.example.boris.financialstatement.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {

    private List<CashModel> items;
    private int typeCase;
    private MainActivity mainActivity;
    private DialigAlert dialigAlert;


    public RecyclerAdapter(List<CashModel> items, MainActivity mainActivity, int typeCase) {
        this.items = items;
        this.typeCase = typeCase;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        if (items != null){
            myHolder.name.setText(items.get(i).getName());
            myHolder.cash.setText("$" + MainActivity.splitNumberWithTeams(items.get(i).getCash() + ""));
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView cash;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.recyclerName);
            cash = itemView.findViewById(R.id.recyclerCash);

            itemView.setOnLongClickListener(v -> {
                // turn color to hover mode
                itemView.setBackgroundColor(mainActivity.getResources().getColor(R.color.hoverColor));
                openMenu(itemView, getAdapterPosition());

                return true;
            });

        }
    }

    public void openMenu(View itemView, int id){
        PopupMenu popupMenu = new PopupMenu(mainActivity.getApplicationContext(), itemView);
        popupMenu.setOnMenuItemClickListener(item -> {
            // turn color back to white
            itemView.setBackgroundColor(mainActivity.getResources().getColor(R.color.white));
            switch (item.getItemId()){
                case R.id.deleteRecycler:

                    dialigAlert = new DialigAlert();
                    dialigAlert.setData(mainActivity, id, typeCase, items.get(id).getName());
                    dialigAlert.show(mainActivity.getSupportFragmentManager(), "tag");
                    return true;

                case R.id.editRecycler:
                    CustomAlert customAlert = new CustomAlert(true,items.get(id).getName(),items.get(id).getCash() + "", id);
                    customAlert.showDialog(mainActivity, typeCase);
                    return true;
            }
            return false;
        });

        popupMenu.setOnDismissListener(menu -> {  // turn color back to white
            itemView.setBackgroundColor(mainActivity.getResources().getColor(R.color.white));
        });
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.recycler_menu, popupMenu.getMenu());
        popupMenu.show();
    }
}

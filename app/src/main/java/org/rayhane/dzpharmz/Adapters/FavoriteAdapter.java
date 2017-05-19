package org.rayhane.dzpharmz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.rayhane.dzpharmz.Model.Favorite;
import org.rayhane.dzpharmz.R;

import java.util.List;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Favorite> favsList;
    static Context context;



    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView favName;
        TextView favState;
        TextView favAddress;
        TextView options;



        public FavoriteViewHolder(View v) {
            super(v);
            favName = (TextView) v.findViewById(R.id.nameF);
            favState = (TextView) v.findViewById(R.id.hourF);
            favAddress = (TextView) v.findViewById(R.id.addressF);
            options = (TextView) v.findViewById(R.id.textViewOptionsF);
        }
    }

    public FavoriteAdapter(List<Favorite> favsList, Context context) {
        this.favsList = favsList;
        this.context = context;
    }


    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_card, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Favorite favorite = favsList.get(position);
        holder.favName.setText(favorite.getName());
        holder.favAddress.setText(favorite.getAddress());
        holder.favState.setText(favorite.getTime());

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.delete_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i;
                        if ((item.getTitle().toString()).equalsIgnoreCase("Supprimer")) {
                            Toast.makeText(PharmacyAdapter.context, "Supprimée", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return favsList.size();
    }
}

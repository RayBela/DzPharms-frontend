package org.rayhane.dzpharmz.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.rayhane.dzpharmz.Model.Favorite;
import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.R;

import java.util.List;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Favorite> favsList;



    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView favName;
        TextView favState;
        TextView favAddress;



        public FavoriteViewHolder(View v) {
            super(v);
            favName = (TextView) v.findViewById(R.id.name);
            favState = (TextView) v.findViewById(R.id.state);
            favAddress = (TextView) v.findViewById(R.id.address);


        }
    }

    public FavoriteAdapter(List<Favorite> favsList) {
        this.favsList = favsList;
    }


    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favs_list_row, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Favorite favorite = favsList.get(position);
        holder.favName.setText(favorite.getName());
        holder.favAddress.setText(favorite.getAddress());
        holder.favState.setText(favorite.getState());
    }


    @Override
    public int getItemCount() {
        return favsList.size();
    }
}

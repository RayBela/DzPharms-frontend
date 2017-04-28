package org.rayhane.dzpharmz.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.R;

import java.util.List;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder> {

    private List<Pharmacy> pharmsList;



    public class PharmacyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;
        TextView pharmacyState;
        TextView pharmacyAddress;



        public PharmacyViewHolder(View v) {
            super(v);
            pharmacyName = (TextView) v.findViewById(R.id.name);
            pharmacyState = (TextView) v.findViewById(R.id.state);
            pharmacyAddress = (TextView) v.findViewById(R.id.address);


        }
    }

    public PharmacyAdapter(List<Pharmacy> pharmsList) {
        this.pharmsList = pharmsList;
    }


    @Override
    public PharmacyAdapter.PharmacyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharms_list_row, parent, false);
        return new PharmacyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PharmacyAdapter.PharmacyViewHolder holder, int position) {
        Pharmacy pharm = pharmsList.get(position);
        holder.pharmacyName.setText(pharm.getName());
        holder.pharmacyAddress.setText(pharm.getAddress());
        holder.pharmacyState.setText(pharm.getState());


    }

    @Override
    public int getItemCount() {
        return pharmsList.size();
    }
}

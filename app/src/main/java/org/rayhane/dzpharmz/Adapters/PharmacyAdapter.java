package org.rayhane.dzpharmz.Adapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.rayhane.dzpharmz.Interfaces.OnItemClickListener;
import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.R;

import java.util.List;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder> {

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    private List<Pharmacy> pharmsList;
    private final OnItemClickListener listener;
    Fragment mFragment ;


    public class PharmacyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;
        TextView pharmacyAddress;
        public View v;






        public PharmacyViewHolder(View v) {
            super(v);
            this.v = v;
            pharmacyName = (TextView) v.findViewById(R.id.nameC);
            pharmacyAddress = (TextView) v.findViewById(R.id.addressC);
        }

        public void bind(final Pharmacy item, final OnItemClickListener listener) {


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }

            });

        }



    }

    public PharmacyAdapter(List<Pharmacy> pharmsList, OnItemClickListener listener) {
        this.pharmsList = pharmsList;
        this.listener = listener;

    }


    @Override
    public PharmacyAdapter.PharmacyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacy_card, parent, false);

        return new PharmacyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PharmacyAdapter.PharmacyViewHolder holder, int position) {
        Pharmacy pharm = pharmsList.get(position);
        holder.bind(pharmsList.get(position), listener);
        holder.pharmacyName.setText("Pharmacie " + pharm.getName());
        holder.pharmacyAddress.setText(pharm.getPharmacy_address());

        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

    }




    @Override
    public int getItemCount() {
        return pharmsList.size();
    }
}

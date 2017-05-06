package org.rayhane.dzpharmz.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.rayhane.dzpharmz.Interfaces.OnItemClickListener;
import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.R;

import java.util.List;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder> {

    private List<Pharmacy> pharmsList;
    private final OnItemClickListener listener;



    public class PharmacyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;
        TextView pharmacyState;
        TextView pharmacyAddress;





        public PharmacyViewHolder(View v) {
            super(v);
            pharmacyName = (TextView) v.findViewById(R.id.nameC);
            pharmacyState = (TextView) v.findViewById(R.id.stateC);
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

    public PharmacyAdapter(List<Pharmacy> pharmsList , OnItemClickListener listener) {
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
        holder.pharmacyName.setText(pharm.getName());
        holder.pharmacyAddress.setText(pharm.getPharmacy_address());
        holder.pharmacyState.setText(pharm.getHref());


    }




    @Override
    public int getItemCount() {
        return pharmsList.size();
    }
}

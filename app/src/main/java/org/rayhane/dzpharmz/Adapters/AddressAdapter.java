package org.rayhane.dzpharmz.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.rayhane.dzpharmz.Model.Address;
import org.rayhane.dzpharmz.R;

import java.util.List;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Address> addressesList;



    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView addressName;
        TextView addressLocation;



        public AddressViewHolder(View v) {
            super(v);
            addressName = (TextView) v.findViewById(R.id.nameA);
            addressLocation = (TextView) v.findViewById(R.id.locationA);



        }
    }

    public AddressAdapter(List<Address> addressesList) {
        this.addressesList = addressesList;
    }


    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_card, parent, false);
        return new AddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {

        Address address = addressesList.get(position);
        holder.addressName.setText(address.getName());
        holder.addressLocation.setText(address.getLocation());

    }



    @Override
    public int getItemCount() {
        return addressesList.size();
    }
}


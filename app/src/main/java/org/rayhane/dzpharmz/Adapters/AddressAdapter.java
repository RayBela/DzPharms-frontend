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

import org.rayhane.dzpharmz.Model.Address;
import org.rayhane.dzpharmz.R;

import java.util.List;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Address> addressesList;
    static Context context;



    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView addressName;
        TextView addressLocation;
        TextView options;



        public AddressViewHolder(View v) {
            super(v);
            addressName = (TextView) v.findViewById(R.id.nameA);
            addressLocation = (TextView) v.findViewById(R.id.locationA);
            options = (TextView) v.findViewById(R.id.textViewOptionsA);

        }
    }

    public AddressAdapter(List<Address> addressesList, Context context) {
        this.addressesList = addressesList;
        this.context = context;

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
        return addressesList.size();
    }
}


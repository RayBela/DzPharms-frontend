package org.rayhane.dzpharmz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;

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
    static Context context;


    public class PharmacyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;
        TextView pharmacyAddress;
        TextView options;






        public PharmacyViewHolder(View v) {
            super(v);
            pharmacyName = (TextView) v.findViewById(R.id.nameC);
            pharmacyAddress = (TextView) v.findViewById(R.id.addressC);
            options = (TextView) v.findViewById(R.id.textViewOptionsP);
        }

        public void bind(final Pharmacy item, final OnItemClickListener listener) {


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }

            });

        }



    }

    public PharmacyAdapter(List<Pharmacy> pharmsList, OnItemClickListener listener,Context context) {
        this.pharmsList = pharmsList;
        this.listener = listener;
        this.context = context;
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

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.pharmacy_manu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i;
                        if ((item.getTitle().toString()).equalsIgnoreCase("Ajouter aux Favoris")) {
                            Toast.makeText(PharmacyAdapter.context, "Pharmacie ajouté aux Favoris", Toast.LENGTH_SHORT).show();
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
        return pharmsList.size();
    }
}

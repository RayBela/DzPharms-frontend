package org.rayhane.dzpharmz.View.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rayhane.dzpharmz.Adapters.AddressAdapter;
import org.rayhane.dzpharmz.Model.Address;
import org.rayhane.dzpharmz.R;
import java.util.ArrayList;
import java.util.List;


public class AddressesListFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private List<Address> addressesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AddressAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addAddressFab;

    public AddressesListFragment() {
    }

    public static AddressesListFragment newInstance(String param1, String param2) {
        AddressesListFragment fragment = new AddressesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_addresses_list, container, false);


        final LayoutInflater dialogInflater = getActivity().getLayoutInflater();


        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.addresses_recycler_view);

        addAddressFab = (FloatingActionButton) view.findViewById(R.id.addAddressFab);

        addAddressFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogInflater.inflate(R.layout.add_address_dialog,null))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //add address
                                //update ui
                                //toast that address has been added
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Log.d("debugMode", "The application stopped after this");
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new AddressAdapter(addressesList, getActivity());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        prepareAddressesData();

        return view;

    }


    private void prepareAddressesData() {

        Address address = new Address("Domicile","Hai Essabah , Bir El-Djir  , Oran");
        addressesList.add(address);

        address = new Address("Travail","Avenue Hammu Mokhtar , Oran");
        addressesList.add(address);

        mAdapter.notifyDataSetChanged();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

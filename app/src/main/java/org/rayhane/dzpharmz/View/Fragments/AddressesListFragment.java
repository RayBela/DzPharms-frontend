package org.rayhane.dzpharmz.View.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rayhane.dzpharmz.Adapters.AddressAdapter;
import org.rayhane.dzpharmz.Model.Address;
import org.rayhane.dzpharmz.R;
import org.rayhane.dzpharmz.View.Decoration.DeviderItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class AddressesListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddressesListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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


    private List<Address> addressesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AddressAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_addresses_list, container, false);

        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.addresses_recycler_view);
        layoutManager = new LinearLayoutManager(this.getActivity());
        Log.d("debugMode", "The application stopped after this");
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new AddressAdapter(addressesList);
        recyclerView.setAdapter(mAdapter);

        prepareAddressesData();

        return view;

    }


    private void prepareAddressesData() {

        Address address = new Address("Domicile","Hai Essabah , Bir El-Djir  , Oran");
        addressesList.add(address);

        address = new Address("Domicile","Hai Essabah , Bir El-Djir  , Oran");
        addressesList.add(address);

        address = new Address("Domicile","Hai Essabah , Bir El-Djir  , Oran");
        addressesList.add(address);

        address = new Address("Domicile","Hai Essabah , Bir El-Djir  , Oran");
        addressesList.add(address);


        mAdapter.notifyDataSetChanged();
    }


    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

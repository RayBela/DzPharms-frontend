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
import org.rayhane.dzpharmz.Adapters.PharmacyAdapter;
import org.rayhane.dzpharmz.Model.Address;
import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.R;
import org.rayhane.dzpharmz.View.Activities.DeviderItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressesListFragment.
     */
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
        recyclerView.addItemDecoration(new DeviderItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

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

import org.rayhane.dzpharmz.Adapters.FavoriteAdapter;
import org.rayhane.dzpharmz.Model.Favorite;
import org.rayhane.dzpharmz.R;
import org.rayhane.dzpharmz.View.Decoration.DeviderItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FavorisFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FavorisFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavorisFragment newInstance(String param1, String param2) {
        FavorisFragment fragment = new FavorisFragment();
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



    private List<Favorite> favsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FavoriteAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoris, container, false);

        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.favs_recycler_view);
        layoutManager = new LinearLayoutManager(this.getActivity());
        Log.d("debugMode", "The application stopped after this");
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new FavoriteAdapter(favsList);
        recyclerView.setAdapter(mAdapter);

        prepareFavsData();

        return view;
    }


    private void prepareFavsData() {

        Favorite favorite = new Favorite("Pharmacie 1","Rue Larbi Men Mhidi , Oran","Ouverte");
        favsList.add(favorite);

        favorite = new Favorite("Pharmacie 1","Rue Larbi Men Mhidi , Oran","Ouverte");
        favsList.add(favorite);

        favorite = new Favorite("Pharmacie 1","Rue Larbi Men Mhidi , Oran","Ouverte");
        favsList.add(favorite);


        favorite = new Favorite("Pharmacie 1","Rue Larbi Men Mhidi , Oran","Ouverte");
        favsList.add(favorite);


        favorite = new Favorite("Pharmacie 1","Rue Larbi Men Mhidi , Oran","Ouverte");
        favsList.add(favorite);


        favorite = new Favorite("Pharmacie 1","Rue Larbi Men Mhidi , Oran","Ouverte");
        favsList.add(favorite);



        mAdapter.notifyDataSetChanged();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFavorisFragmentInteraction(uri);
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
        void onFavorisFragmentInteraction(Uri uri);
    }
}

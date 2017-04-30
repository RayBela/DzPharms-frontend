package org.rayhane.dzpharmz.View.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rayhane.dzpharmz.Adapters.PharmacyAdapter;
import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.R;
import org.rayhane.dzpharmz.View.Activities.DeviderItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PharmsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PharmsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PharmsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PharmsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PharmsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PharmsListFragment newInstance(String param1, String param2) {
        PharmsListFragment fragment = new PharmsListFragment();
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

    private List<Pharmacy> pharmsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PharmacyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pharms_list, container, false);

        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this.getActivity());
        Log.d("debugMode", "The application stopped after this");


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DeviderItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PharmacyAdapter(pharmsList);
        recyclerView.setAdapter(mAdapter);

        preparePharmsData();

        return view;
    }


    private void preparePharmsData() {

        Pharmacy pharm = new Pharmacy("Pharmacie 1","Rue Larbi Men Mhidi , Oran","Ouverte");
        pharmsList.add(pharm);

        pharm = new Pharmacy("Pharmacie 1","Rue Larbi Men Mhidi , Oran","Ouverte");
        pharmsList.add(pharm);

        pharm = new Pharmacy("Pharmacie 2","Rue Larbi Men Mhidi , Oran","Fermé");
        pharmsList.add(pharm);

        pharm = new Pharmacy("Pharmacie 3","Rue Larbi Men Mhidi , Oran","Ouverte");
        pharmsList.add(pharm);

        pharm = new Pharmacy("Pharmacie 4","Rue Larbi Men Mhidi , Oran","Ouverte");
        pharmsList.add(pharm);

        pharm = new Pharmacy("Pharmacie 5","Rue Larbi Men Mhidi , Oran","Fermé");
        pharmsList.add(pharm);

        pharm = new Pharmacy("Pharmacie 6","Rue Larbi Men Mhidi , Oran","Ouverte");
        pharmsList.add(pharm);

        pharm = new Pharmacy("Pharmacie 7","Rue Larbi Men Mhidi , Oran","Fermé");
        pharmsList.add(pharm);

        mAdapter.notifyDataSetChanged();
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onListFragmentInteraction(uri);
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
        void onListFragmentInteraction(Uri uri);
    }
}

package org.rayhane.dzpharmz.View.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.rayhane.dzpharmz.Interfaces.OnItemClickListener;
import org.rayhane.dzpharmz.Adapters.PharmacyAdapter;
import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.Model.PostBody;
import org.rayhane.dzpharmz.R;
import org.rayhane.dzpharmz.Services.DzpharmsClient;
import org.rayhane.dzpharmz.View.Activities.PharmacyMapsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.rayhane.dzpharmz.Services.ApiClient.getClient;

public class PharmsListFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    //Location variables
    public Location mLastLocation;
    protected LocationRequest mLocationRequest;
    //location settings variables
    public final static int REQUEST_CHECK_SETTINGS = 100;

    // google play services variables
    protected GoogleApiClient mGoogleApiClient;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;




    //tags
    private static final String TAG = "HomeFragment";

    //visual components variables
    private ProgressDialog mProgressDialog;

    private List<Pharmacy> pharmsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PharmacyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public PharmsListFragment() {
        // Required empty public constructor Failed to connect to localhost/127.0.0.1:80
    }


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
        if (mGoogleApiClient == null) buildGoogleApiClient();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //if (mGoogleApiClient == null) buildGoogleApiClient();
        //mGoogleApiClient.connect();
/*
        Retrofit retrofit = getClient();
        // Create a very simple REST adapter which points the dzpharms API endpoint.
        DzpharmsClient client =  retrofit.create(DzpharmsClient.class);

        // Fetch a list of pharmacies
        Call<List<Pharmacy>> call = client.getPharms();

        call.enqueue(new Callback<List<Pharmacy>>() {
            @Override
            public void onResponse(Call<List<Pharmacy>> call, Response<List<Pharmacy>> response) {
                Log.e("success", response.toString());
                List<Pharmacy> pharms = response.body();
                Log.e("success", "Number of pharms received: " + pharms.size());
                Log.e("pharms list", pharms.toString());
                mAdapter = new PharmacyAdapter(pharms, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Pharmacy item) {
                        Intent pharmMapIntent = new Intent(getActivity(), PharmacyMapsActivity.class);
                        startActivity(pharmMapIntent);
                    }
                });
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<List<Pharmacy>> call, Throwable t) {
                Log.e("failure",t.getMessage());
            }
        });*/


    }


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_pharms_list, container, false);


        if (mGoogleApiClient == null)
            buildGoogleApiClient();
        mGoogleApiClient.connect();


        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this.getActivity());
        Log.d("debugMode", "The application stopped after this");

        /*mAdapter = new PharmacyAdapter(pharmsList, new OnItemClickListener() {
            @Override
            public void onItemClick(Pharmacy item) {
                Intent pharmMapIntent = new Intent(getActivity(), PharmacyMapsActivity.class);
                startActivity(pharmMapIntent);
                mAdapter.notifyDataSetChanged();
            }});*/

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



       // preparePharmsData();

        return view;
    }


    protected void requestLocationUpdates(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(5 * 1000);
    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private void getNearestPharms(){

        PostBody postLocation = new PostBody(mLastLocation.getLatitude(),mLastLocation.getLongitude());

        Retrofit retrofit = getClient();
        // Create a very simple REST adapter which points the dzpharms API endpoint.
        DzpharmsClient client =  retrofit.create(DzpharmsClient.class);
        Call<List<Pharmacy>> call = client.getNearestPharms
                (postLocation);

        showProgressDialog();
        call.enqueue(new Callback<List<Pharmacy>>() {
            @Override
            public void onResponse(Call<List<Pharmacy>> call, Response<List<Pharmacy>> response) {
                Log.e("success", response.toString());
                List<Pharmacy> pharms = response.body();
                Log.e("success", "Number of pharms received: " + pharms.size());
                Log.e("pharms list", pharms.toString());

                mAdapter = new PharmacyAdapter(pharms, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Pharmacy item) {
                        Intent pharmMapIntent = new Intent(getActivity(), PharmacyMapsActivity.class);
                        pharmMapIntent.putExtra("pharmacy", item);
                        startActivity(pharmMapIntent);
                    }
                },getActivity());
                recyclerView.setAdapter(mAdapter);
                hideProgressDialog();

            }
            @Override
            public void onFailure(Call<List<Pharmacy>> call, Throwable t) {
                Log.e("failure",t.getMessage());
            }
        });


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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(haveNetworkConnection()) {
            getCurrentLocation();
        }
    }

    private void showConnectionDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Cette Application nécessite une connection Internet")
                .setCancelable(false)
                .setPositiveButton("Activer 3G ou WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })

                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Récuperation des données");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void getCurrentLocation(){

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            final LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Log.i("location :"," "+latLng.toString());
            getNearestPharms();
        } else {
            requestLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Uri uri);
    }
}

package org.rayhane.dzpharmz.View.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.rayhane.dzpharmz.Adapters.PharmacyAdapter;
import org.rayhane.dzpharmz.Interfaces.OnItemClickListener;
import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.Model.PostBody;
import org.rayhane.dzpharmz.R;
import org.rayhane.dzpharmz.Services.DzpharmsClient;
import org.rayhane.dzpharmz.View.Activities.MainActivity;
import org.rayhane.dzpharmz.View.Activities.PharmacyMapsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.rayhane.dzpharmz.Services.ApiClient.getClient;


public class HomeFragment extends Fragment implements OnMapReadyCallback ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult>{


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //fragment variables
    private OnFragmentInteractionListener mListener;

    // map variables
    MapView mapView;
    private GoogleMap googleMap;

    // google play services variables
    protected GoogleApiClient mGoogleApiClient;

    //Location variables
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    protected LocationRequest mLocationRequest;

    //location settings variables
    public final static int REQUEST_CHECK_SETTINGS = 100;

    //tags
    private static final String TAG = "HomeFragment";

    //visual components variables
    private ProgressDialog mProgressDialog;
    private FloatingActionButton sFab;


    /**
     * start of home fragment methods
     */

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        if(!haveNetworkConnection())
            showConnectionDialog();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        View mainView = inflater.inflate(R.layout.app_bar_main, container, false);
        sFab = (FloatingActionButton)mainView.findViewById(R.id.searchFab);

        // adding map to fragment
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // For showing a move to my location button
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                // mMap.setMyLocationEnabled(true);

            }
        });

        if (mGoogleApiClient == null)
            buildGoogleApiClient();
        requestLocationUpdates();



        return rootView;
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

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        requestLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
      //  hideProgressDialog();


    }

    @Override
    public void onPause() {
        super.onPause();
       // mapView.onResume();
      //  hideProgressDialog();
       // stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
       // mapView.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    /**
     * start of location methods
     */

    protected void requestLocationUpdates(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(5*1000);
    }

    public void addMarker(LatLng markerLocation, String posName, String description, String color){

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(markerLocation);
        markerOptions.title(posName);
        markerOptions.snippet(description);
        if(color == "green"){
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));}
        else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        }
        /*if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }*/
        mCurrLocationMarker = googleMap.addMarker(markerOptions);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent markerIntent = new Intent(getActivity(), PharmacyMapsActivity.class);
                markerIntent.putExtra("name", marker.getTitle());
                markerIntent.putExtra("address", marker.getSnippet());
                Bundle args = new Bundle();
                args.putParcelable("position", marker.getPosition());
                markerIntent.putExtra("bundle",args);
                startActivity(markerIntent);
            }
        });
      /*  googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(20, 20, 20, 60);
                sFab.setLayoutParams(lp);
                return true;
            }
        });*/
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

       checkLocationSettings();
        getCurrentLocation();

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private void checkLocationSettings(){

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }


    private void getCurrentLocation(){


        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            final LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Log.i("location :"," "+latLng.toString());
            displayCurrentLocation(latLng);
            getNearestPharms(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            moveCamera(14,latLng);

        } else {
            requestLocationUpdates();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    private void displayCurrentLocation(LatLng latLng){
      //  showProgressDialog();
        addMarker(latLng,"Votre Position","Vous etes Ici","red");
    }

    private void moveCamera(float zoom,LatLng position){
      //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        mLastLocation = location;
        final LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        displayCurrentLocation(latLng);
        getNearestPharms(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        moveCamera(14,latLng);
        stopLocationUpdates();

    }


    private void locationSettingsResults(Status status){

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    /**
     * getting nearest pharms
     */

    private void getNearestPharms(double lat, double lng){

        PostBody postLocation = new PostBody(lat,lng);
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

                for (Pharmacy element : pharms) {
                    LatLng pharmLocation = new LatLng(element.getLatitude(),element.getLongitude());
                    addMarker(pharmLocation,"Pharmacie " + element.getName(),element.getPharmacy_address() ,"green");
                }
               hideProgressDialog();
            }
            @Override
            public void onFailure(Call<List<Pharmacy>> call, Throwable t) {
                Log.e("failure",t.getMessage());
            }
        });
    }


    /**
     * other methods
     */


    private void showConnectionDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Cette Application nécessite une connection Internet")
                .setCancelable(false)
                .setPositiveButton("Activer Les données", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })

                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
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
            mProgressDialog.setMessage(getString(R.string.loadingLocation));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    /**
     * fragments methods
     */


    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        locationSettingsResults(status);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onHomeFragmentInteraction(Uri uri);
    }


}

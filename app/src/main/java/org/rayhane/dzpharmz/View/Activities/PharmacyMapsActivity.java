package org.rayhane.dzpharmz.View.Activities;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.R;

public class PharmacyMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Pharmacy mPharmacy;

    private TextView name;
    private TextView address;
    private String phoneNumber;
    private FloatingActionButton phoneNumberFab;
    private LatLng pharmPosition;

    String addressP;
    String nameP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_maps);

        // Enable the Up button

        name = (TextView) findViewById(R.id.pharmacyName);
        address = (TextView) findViewById(R.id.addressText);
        phoneNumberFab = (FloatingActionButton) findViewById(R.id.phoneFab);

        Intent pharmIntent = getIntent();
        mPharmacy = (Pharmacy) pharmIntent.getSerializableExtra("pharmacy");
        if (mPharmacy != null) {

            nameP = "Pharmacie " + mPharmacy.getName();
            addressP = mPharmacy.getPharmacy_address();
            name.setText(nameP);
            address.setText(addressP);
            phoneNumber = mPharmacy.getPhone_number();

        } else {

            nameP = pharmIntent.getStringExtra("name");
            addressP = pharmIntent.getStringExtra("address");
            name.setText(nameP);
            address.setText(addressP);
            Bundle bundle = getIntent().getParcelableExtra("bundle");
            pharmPosition = bundle.getParcelable("position");

        }
        phoneNumberFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber != null) {
                    Intent callPharmIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                    startActivity(callPharmIntent);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(PharmacyMapsActivity.this).create();
                    alertDialog.setMessage("Numéro de Téléphone non disponible");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng pharmacyPosition;
        // Add a marker in Sydney and move the camera
        if (mPharmacy != null) {
            pharmacyPosition = new LatLng(mPharmacy.getLatitude(), mPharmacy.getLongitude());
        } else {
            pharmacyPosition = pharmPosition;
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pharmacyPosition);
        markerOptions.title(nameP);
        markerOptions.snippet(addressP);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pharmacyPosition, 14));



    }

}
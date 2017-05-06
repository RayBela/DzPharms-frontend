package org.rayhane.dzpharmz.View.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.rayhane.dzpharmz.R;
import org.rayhane.dzpharmz.View.Decoration.CircleTransform;
import org.rayhane.dzpharmz.View.Fragments.AddPharmacyFragment;
import org.rayhane.dzpharmz.View.Fragments.AddressesListFragment;
import org.rayhane.dzpharmz.View.Fragments.FavorisFragment;
import org.rayhane.dzpharmz.View.Fragments.HomeFragment;
import org.rayhane.dzpharmz.View.Fragments.PharmsListFragment;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        FavorisFragment.OnFragmentInteractionListener,
        PharmsListFragment.OnFragmentInteractionListener,
        AddPharmacyFragment.OnFragmentInteractionListener,
        AddressesListFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener
{

    // Ui Elements
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    public TextView txtName, txtWebsite;
    public ImageView imgProfile;

    //Tags
    private static final String TAG = MainActivity.class.getSimpleName();

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_FAVORIS = "favoris";
    private static final String TAG_ADDRESSES = "addresses";
    private static final String TAG_PHARMS_LIST = "pharms list";
    private static final String TAG_SUGGEST_PHARM = "suggest pharm";
    public static String CURRENT_TAG = TAG_HOME;

    // activity names
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;

    private Handler mHandler;

    private String urlProfileImg ;
    private GoogleApiClient mGoogleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        // Navigation view header

        //drawer activities names
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ajouter la recherche ici", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

     /*   if(!haveNetworkConnection()){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Cette Application nécessite une connection Internet")
                    .setCancelable(false)
                    .setPositiveButton("Activer 3G ou WIFI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })

                    .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }*/

        // build google api client to sign out

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }




    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {

        Intent introIntent = getIntent();

        // name, website
        txtName.setText(introIntent.getStringExtra("userName"));
        txtWebsite.setText(introIntent.getStringExtra("userEmail"));
        urlProfileImg = introIntent.getStringExtra("imgUrl");
        // loading header background image

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);// showing dot next to notifications label

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            toggleFab();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        toggleFab();

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    /**
     *
     * @return
     */

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                FavorisFragment favorisFragment = new FavorisFragment();
                return favorisFragment;
            case 2:
                PharmsListFragment pharmsListFragment = new PharmsListFragment();
                return pharmsListFragment;

            case 3:
                AddressesListFragment addressesListFragment = new AddressesListFragment();
                return addressesListFragment;

            case 4:

            AddPharmacyFragment addPharmacyFragment = new AddPharmacyFragment();
            return addPharmacyFragment;

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_favoris:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_FAVORIS;
                        break;
                    case R.id.nav_pharms_list:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PHARMS_LIST;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SUGGEST_PHARM;
                        break;
                    case R.id.nav_addresses:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_ADDRESSES;
                        break;
                    case R.id.nav_about_us:
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
            }

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (shouldLoadHomeFragOnBackPress) {

            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
        loadNavHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_logout) {
            signOut();
            return true;
        }
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    /***
     * method show fab
     */

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //  finish();
                        Intent introIntent = new Intent(MainActivity.this, SigninActivity.class);
                        startActivity(introIntent);
                        Toast.makeText(getApplicationContext(), "Vous etes déconnécté!", Toast.LENGTH_LONG).show();
                    }
                });
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }

    @Override
    public void onHomeFragmentInteraction(Uri uri) {
        return;
    }

    @Override
    public void onFavorisFragmentInteraction(Uri uri) {
        return;
    }


    @Override
    public void onListFragmentInteraction(Uri uri) {
        return;
    }

    @Override
    public void onSetFragmentInteraction(Uri uri) {
        return;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}

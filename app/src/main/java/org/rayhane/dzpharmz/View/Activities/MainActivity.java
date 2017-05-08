package org.rayhane.dzpharmz.View.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import org.rayhane.dzpharmz.Adapters.PharmacyAdapter;
import org.rayhane.dzpharmz.Interfaces.OnItemClickListener;
import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.R;
import org.rayhane.dzpharmz.Services.DzpharmsClient;
import org.rayhane.dzpharmz.View.Decoration.CircleTransform;
import org.rayhane.dzpharmz.View.Fragments.AddPharmacyFragment;
import org.rayhane.dzpharmz.View.Fragments.AddressesListFragment;
import org.rayhane.dzpharmz.View.Fragments.FavorisFragment;
import org.rayhane.dzpharmz.View.Fragments.HomeFragment;
import org.rayhane.dzpharmz.View.Fragments.PharmsListFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.rayhane.dzpharmz.Services.ApiClient.getClient;

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


    //visual components variables
    private ProgressDialog mProgressDialog;
    /**
     * load navigation drawer with fragments
     * signout
     * recherche par nom
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        mHandler = new Handler();
        navHeader = navigationView.getHeaderView(0);

        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        //drawer activities names
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        final LayoutInflater dialogInflater = this.getLayoutInflater();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResearchDialog(dialogInflater);
            }

        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

       buildGoogleApiClient();

    }

    private void showResearchDialog(final LayoutInflater inflaterL){

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = inflaterL.inflate(R.layout.search_dialog, null);

        builder.setView(view)
                .setCancelable(false)
                .setPositiveButton("Recherche", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        EditText searchfield = (EditText) view.findViewById(R.id.searchField);
                        String searchQuery = searchfield.getText().toString();


                        Retrofit retrofit = getClient();
                        // Create a very simple REST adapter which points the dzpharms API endpoint.
                        DzpharmsClient client =  retrofit.create(DzpharmsClient.class);
                        Call<List<Pharmacy>> call = client.getPharmacy(searchQuery);

                        showProgressDialog();
                        call.enqueue(new Callback<List<Pharmacy>>() {
                            @Override
                            public void onResponse(Call<List<Pharmacy>> call, Response<List<Pharmacy>> response) {

                                Log.e("success", response.toString());
                                List<Pharmacy> pharms = response.body();
                                Pharmacy pharm = pharms.get(0);
                                Log.e("success", "Number of pharms received: " + pharms.size());
                                Log.e("pharms list", pharms.toString());

                                if (pharm != null) {
                                    Intent pharmMapIntent = new Intent(MainActivity.this, PharmacyMapsActivity.class);
                                    pharmMapIntent.putExtra("pharmacy", pharm);
                                    hideProgressDialog();
                                    startActivity(pharmMapIntent);
                                } else {
                                    hideProgressDialog();
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.makeText(getApplicationContext(), "Pharmacie introuvable", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Pharmacy>> call, Throwable t) {
                                hideProgressDialog();
                                Toast toast = new Toast(getApplicationContext());
                                toast.makeText(getApplicationContext(), "Pharmacie introuvable", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    });
        AlertDialog alert = builder.create();
        alert.show();
    }


    synchronized private void buildGoogleApiClient(){
        // build google api client to sign out
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
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
    protected void onResume() {
        super.onResume();
        loadNavHeader();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
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

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Recherche...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}

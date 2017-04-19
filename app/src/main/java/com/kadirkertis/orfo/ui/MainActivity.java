package com.kadirkertis.orfo.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.data.DbTaskParams;
import com.kadirkertis.orfo.data.DbTasks;
import com.kadirkertis.orfo.databinding.ActivityMainBinding;
import com.kadirkertis.orfo.model.CheckInPlace;
import com.kadirkertis.orfo.model.CheckInUser;
import com.kadirkertis.orfo.model.PlaceInfo;
import com.kadirkertis.orfo.model.User;
import com.kadirkertis.orfo.services.UserTrackingService;
import com.kadirkertis.orfo.utilities.Constants;
import com.kadirkertis.orfo.utilities.DepthPageTransformer;
import com.kadirkertis.orfo.utilities.LocationUtilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_LOCATION = 100;
    private ActivityMainBinding mBinding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;


    private ActionBarDrawerToggle mToggle;
    private MyPagerAdapter myPagerAdapter;
    private FirebaseUser mUser;
    private SharedPreferences mPrefs;

    private String mTableNumber;
    private String mStoreId;
    private ProgressDialog mProgressDialog;
    boolean mBound = false;
    UserTrackingService.UserTrackingServiceBinder binder;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            mBound = true;
            binder = (UserTrackingService.UserTrackingServiceBinder) iBinder;
            binder.setOnLocationDataArrivedListener(new UserTrackingService.OnLocationDataArrivedListener() {
                @Override
                public void onLocationDataArrived() {
                    if (binder.checkUserAtPlace()) {
                        String lastCheckedInPlace = mPrefs.getString(Constants.PREFS_CHECKED_IN_PLACE_ID, null);
                        long lastCheckInTime = mPrefs.getLong(Constants.PREFS_LAST_CHECK_IN_TIME, 0);
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }

                        if (!mStoreId.equals(lastCheckedInPlace)) {
                            //New Place

                            //Clear Cart
                            new DbTasks(MainActivity.this).execute(new DbTaskParams(DbTasks.TASK_EMPTY_CART));

                            unbindService(mConnection);
                            mBound = false;
                            //Check-In User
                            checkUserIn(mStoreId, mTableNumber);

                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putLong(Constants.PREFS_ORDER_TIME, Long.MAX_VALUE);
                            editor.apply();

                        } else {
                            unbindService(mConnection);
                            mBound = false;
                            //User Still at the same place
                            checkUserIn(mStoreId, mTableNumber);

                        }
                    } else {
                        showGenericDialog(R.string.not_at_place);
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                    binder.removeOnLocationDataArrivedListener();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mPrefs = getSharedPreferences(Constants.PREFS_CHECKED_IN_PLACE, MODE_PRIVATE);

        //Set up auth
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser == null) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setTheme(R.style.My_FirebaseUI)
                                    .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                                    .setLogo(R.drawable.orfolognobg)
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //Setup toolbar
        mBinding.mainAcToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mBinding.mainAcToolbar);

        //SetUp Navigation Drawer
        mBinding.mainAcNavView.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this,
                mBinding.mainAcDrawerLayout,
                mBinding.mainAcToolbar,
                R.string.drawer_open,
                R.string.drawer_closed);
        mBinding.mainAcDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        //Set up pager
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mBinding.mainAcPager.setAdapter(myPagerAdapter);
        mBinding.mainAcPager.setPageTransformer(true, new DepthPageTransformer());
        mBinding.mainAcTabLayout.setupWithViewPager(mBinding.mainAcPager);


        //Set up scan
        mBinding.mainAcScanFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To get the Menu location must be enabled
                if (LocationUtilities.isLocationEnabled(MainActivity.this)) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestLocationpermission();
                    } else {
                        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setPrompt(getString(R.string.scan_qr_code_prompt));
                        integrator.setCameraId(0);
                        integrator.setBarcodeImageEnabled(true);
                        integrator.initiateScan();
                    }

                } else {
                    showEnableLocationDialog();
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                showSnackbar(R.string.sign_in_successful);
                mUser = mAuth.getCurrentUser();
                String userId = mPrefs.getString(Constants.PREFS_USER_ID, null);
                if (userId == null) {
                    checkUserRegistered();
                }
                return;

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_sign_in_response);
        }

        //Qr Code Scanning
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                showSnackbar(R.string.scan_cancelled);
            } else {

                String results = result.getContents();
                String[] sa = results.split("@");
                String qrPrefix = sa[0];
                mTableNumber = sa[1];
                mStoreId = sa[2];

                if (!results.contains(Constants.ORFO_PREFIX)) {
                    showSnackbar(R.string.unknow_qr_code);
                    return;
                }
                if (!qrPrefix.equals(Constants.ORFO_PREFIX)) {
                    showSnackbar(R.string.unknow_qr_code);
                    return;
                }
                showProgressDialog(R.string.getting_location);
                mFirebaseDatabase.getReference()
                        .child(Constants.DB_PLACES)
                        .child(mStoreId)
                        .child(Constants.TABLE_COMPANY_INFO)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                PlaceInfo placeInfo = dataSnapshot.getValue(PlaceInfo.class);
                                if (placeInfo != null) {
                                    if (placeInfo.getLongitude() == Double.MAX_VALUE ||
                                            placeInfo.getLatitude() == Double.MAX_VALUE) {
                                        if (mProgressDialog.isShowing()) {
                                            mProgressDialog.dismiss();
                                        }
                                        showGenericDialog(R.string.palce_location_info_error);
                                        return;
                                    }
                                    Intent intent = new Intent(getApplicationContext(), UserTrackingService.class);
                                    intent.putExtra(Constants.EXTRA_PLACE_LAT_LONG, new double[]{placeInfo.getLatitude(), placeInfo.getLongitude()});
                                    intent.putExtra(Constants.EXTRA_PLACE_ID, placeInfo.getId());
                                    startService(intent);
                                    bindService(intent, mConnection, BIND_AUTO_CREATE);
                                } else {
                                    Toast.makeText(MainActivity.this, getString(R.string.place_not_registered),
                                            Toast.LENGTH_SHORT).show();
                                    if (mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                            }
                        });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkUserRegistered() {
        Query q = mFirebaseDatabase.getReference()
                .child(Constants.DB_USERS)
                .orderByChild("email")
                .equalTo(mAuth.getCurrentUser().getEmail());
        showProgressDialog(R.string.check_user_exists);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        String userKey = user.getKey();
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString(Constants.PREFS_USER_ID, userKey);
                        editor.apply();
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }

                    }
                } else {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    registerNewUser();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    private void registerNewUser() {
        final String key = mFirebaseDatabase.getReference()
                .child(Constants.DB_USERS)
                .push()
                .getKey();

        String userName = null ;
        if(mUser != null){
            userName = mUser.getDisplayName();

            if(userName == null){
                for(UserInfo userInfo : mUser.getProviderData()){
                    userName = userInfo.getDisplayName();
                }
            }
        }

        HashMap<String, Object> regDate = new HashMap<>();
        regDate.put("date", ServerValue.TIMESTAMP);
        User user = new User(key, userName, mUser.getEmail(), regDate);

        showProgressDialog(R.string.user_initialization_message);
        mFirebaseDatabase.getReference()
                .child(Constants.DB_USERS)
                .child(key)
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString(Constants.PREFS_USER_ID, key);
                        editor.apply();
                        mProgressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackbar(R.string.registeration_failure_msg);
                        mProgressDialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            //TODO Debug Only
            case R.id.sign_out_menu:
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString(Constants.PREFS_USER_ID, null);
                editor.apply();
                AuthUI.getInstance().signOut(this);
                return true;
            //TODO Debug Only
            case R.id.check_out_menu:
                if(binder != null){
                    binder.checkUserOut();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_item_order_history:
                mBinding.mainAcDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (mBinding.mainAcDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.mainAcDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new PlacesFragment();
            } else {
                return new FavoritePlacesFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.pager_places_title);
            } else return getString(R.string.pager_fav_places_title);
        }
    }


    private void requestLocationpermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                //if denied result array empty
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showGenericDialog(R.string.location_permission_not_granted);

                }
            }

        }
    }


    private void showSnackbar(int resourceId) {
        Snackbar.make(mBinding.mainAcCoordinatorLayout, getString(resourceId), Snackbar.LENGTH_SHORT)
                .show();
    }


    private void showProgressDialog(int messageResource) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(messageResource));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void showGenericDialog(int resourceId) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(resourceId));
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });
        dialog.show();
    }

    private void showEnableLocationDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getResources().getString(R.string.location_not_enabled));
        dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);

            }
        });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });
        dialog.show();
    }

    private void checkUserIn(final String storeId, final String tableNumber) {

        String userKey = mPrefs.getString(Constants.PREFS_USER_ID, null);
        String userName = mAuth.getCurrentUser().getDisplayName();
        if(userName == null){
            for(UserInfo userInfo:mUser.getProviderData()){
                userName = userInfo.getDisplayName();
            }
        }
        HashMap<String, Object> timeAdded = new HashMap<>();
        timeAdded.put(Constants.PROPERTY_TIME_ADDED, ServerValue.TIMESTAMP);

        Map<String, Object> checkIns = new HashMap<>();

        Map<String, Object> userCheckIn = new CheckInUser(storeId, timeAdded).toMap();
        Map<String, Object> placeCheckIn = new CheckInPlace(userKey, userName, timeAdded).toMap();
        Map<String, Object> placeCuurentlyIn = new CheckInPlace(userKey, userName, timeAdded).toMap();

        String checkInId = mFirebaseDatabase.getReference()
                .child(Constants.DB_PLACES)
                .child(storeId)
                .child(Constants.TABLE_PLACE_CHECKED_INS)
                .push()
                .getKey();

        String placeReference = "/" + Constants.DB_PLACES + "/" + storeId + "/" + Constants.TABLE_PLACE_CHECKED_INS + "/" + checkInId;
        String currentlyInReference = "/" + Constants.DB_PLACES + "/" + storeId + "/" + Constants.TABLE_PLACE_CURRENT_CHECKED_INS + "/" + checkInId;
        String userReference = "/" + Constants.DB_USERS + "/" + userKey + "/" + Constants.TABLE_USER_CHECKED_IN_PLACES + "/" + checkInId;
        checkIns.put(placeReference,
                placeCheckIn);
        checkIns.put(currentlyInReference,
                placeCuurentlyIn);
        checkIns.put(userReference,
                userCheckIn);

        showProgressDialog(R.string.check_in_progress_message);
        mFirebaseDatabase.getReference().updateChildren(checkIns)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(mBinding.getRoot(), getString(R.string.checked_in_succesfully),
                                Snackbar.LENGTH_SHORT).show();
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();

                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString(Constants.PREFS_CHECKED_IN_PLACE, storeId);
                        editor.putString(Constants.PREFS_CHECKED_IN_TABLE_NUMBER, tableNumber);
                        editor.putLong(Constants.PREFS_LAST_CHECK_IN_TIME, System.currentTimeMillis());
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                        intent.putExtra(Constants.CHECKED_IN_STORE_ID, storeId);
                        intent.putExtra(Constants.CHECKED_IN_TABLE_NUMBER, tableNumber);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(mBinding.getRoot(), getString(R.string.checked_in_failure),
                                Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, e.getMessage());
                        if (mProgressDialog != null)
                            mProgressDialog.dismiss();
                    }
                });

    }

}

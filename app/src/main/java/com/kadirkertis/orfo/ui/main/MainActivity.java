package com.kadirkertis.orfo.ui.main;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kadirkertis.domain.interactor.auth.repository.AuthResponse;
import com.kadirkertis.domain.interactor.checkIn.model.CheckInRequest;
import com.kadirkertis.domain.utils.Constants;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.databinding.ActivityMainBinding;
import com.kadirkertis.orfo.ui.main.fragments.places.PlacesFragment;
import com.kadirkertis.orfo.ui.orderhistory.OrderHistoryActivity;
import com.kadirkertis.orfo.ui.products.ProductsActivity;
import com.kadirkertis.orfo.utils.DepthPageTransformer;
import com.kadirkertis.orfo.utils.Response;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @Inject
    MainViewModelFactory viewModelFactory;

    private MainViewModel viewModel;

    private ActivityMainBinding mBinding;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Assign view model and bind to it
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        //Setup toolbar
        mBinding.mainAcToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mBinding.mainAcToolbar);

        //SetUp Navigation Drawer
        setUpNavigationDrawer();

        //Set up pager
        setUpPager();

        //Observe Data
        viewModel.getAuthResponse().observe(this, this::parseAuthResponse);
        viewModel.getQrResponse().observe(this, this::parseQrResponse);
        mBinding.mainAcScanFab.setOnClickListener(view -> viewModel.loadPlace());
        viewModel.observeAuthStatus();

    }

    private void parseQrResponse(Response<CheckInRequest> qrResponse) {
        switch (qrResponse.getStatus()) {
            case LOADING:
                // showProgressDialog(getString(R.string.place_messages_place_loading));
                break;
            case SUCCESS:
                mProgressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                intent.putExtra(Constants.CHECKED_IN_STORE_ID, qrResponse.getData().getPlaceId());
                intent.putExtra(Constants.CHECKED_IN_TABLE_NUMBER, qrResponse.getData().getPlaceId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case ERROR:
                mProgressDialog.dismiss();
                showSnackbar(getString(R.string.place_messages_place_loading_error));
                break;
        }
    }

    private void parseAuthResponse(Response<AuthResponse> authStatusResponse) {
        switch (authStatusResponse.getStatus()) {
            case LOADING:
                showProgressDialog(getString(R.string.auth_messages_loading));
                break;
            case SUCCESS:
                mProgressDialog.dismiss();
                break;
            case ERROR:
                mProgressDialog.dismiss();
                showSnackbar(getString(R.string.auth_messages_not_authorized));
                viewModel.signInUser();
                break;

        }
    }


    /*
    **********View Helpers***************
    */

    private void setUpNavigationDrawer() {
        mBinding.mainAcNavView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this,
                mBinding.mainAcDrawerLayout,
                mBinding.mainAcToolbar,
                R.string.drawer_open,
                R.string.drawer_closed);
        mBinding.mainAcDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    private void setUpPager() {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mBinding.mainAcPager.setAdapter(myPagerAdapter);
        mBinding.mainAcPager.setPageTransformer(true, new DepthPageTransformer());
        mBinding.mainAcTabLayout.setupWithViewPager(mBinding.mainAcPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
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


    private void showSnackbar(String message) {
        Snackbar.make(mBinding.mainAcCoordinatorLayout, message, Snackbar.LENGTH_SHORT)
                .show();
    }


    private void showProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }


}

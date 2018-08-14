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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.IdpResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kadirkertis.domain.utils.Constants;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.databinding.ActivityMainBinding;
import com.kadirkertis.orfo.ui.Router.Router;
import com.kadirkertis.orfo.ui.base.activity.DaggerAppCompatActivity;
import com.kadirkertis.orfo.ui.orderhistory.OrderHistoryActivity;
import com.kadirkertis.orfo.utils.DepthPageTransformer;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends DaggerAppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @Inject
    MainViewModelFactory viewModelFactory;

    @Inject
    Router router;


    private MainViewModel viewModel;

    private ActivityMainBinding mBinding;


    private ActionBarDrawerToggle mToggle;
    private MyPagerAdapter myPagerAdapter;

    private ProgressDialog mProgressDialog;

    private CompositeDisposable disposables = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Assign view model and bind to it
        viewModel = createViewModel();

        //Redirect User To Sign Up if not auth
        viewModel.getAuthObservable().observe(this, auth -> {
            if (auth != null && auth.getCurrentUser() == null) {
                router.showAuthScreen(this);
            }
        });
        //Setup toolbar
        mBinding.mainAcToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mBinding.mainAcToolbar);

        //SetUp Navigation Drawer
        setUpNavigationDrawer();

        //Set up pager
        setUpPager();


        mBinding.mainAcScanFab.setOnClickListener(view -> {
            disposables.add(
                    viewModel.initiateScan().subscribe());
        }
        );

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            disposables.add(viewModel.parseSignResult(resultCode, response)
                    .doOnError((e) -> {
                        showSnackbar(e.getMessage());
                        mProgressDialog.dismiss();
                    })
                    .doOnComplete(() -> mProgressDialog.dismiss())
                    .subscribe());
            return;
        }

        //Qr Code Scanning
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        disposables.add(

                viewModel.parseQrResult(result)
                        .doOnSubscribe((subs)-> showProgressDialog())
                        .subscribe((success) -> {
                                    mProgressDialog.dismiss();
                                },
                                (error) -> {
                                    showSnackbar(error.getMessage());
                                    mProgressDialog.dismiss();
                                })
        );


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    /*
    Helper Methods
    * */

    private MainViewModel createViewModel() {
        return ViewModelProviders
                .of(this, viewModelFactory)
                .get(MainViewModel.class);
    }

    private void setUpNavigationDrawer() {
        mBinding.mainAcNavView.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this,
                mBinding.mainAcDrawerLayout,
                mBinding.mainAcToolbar,
                R.string.drawer_open,
                R.string.drawer_closed);
        mBinding.mainAcDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    private void setUpPager() {
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
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


    private void showSnackbar(int resourceId) {
        Snackbar.make(mBinding.mainAcCoordinatorLayout, getString(resourceId), Snackbar.LENGTH_SHORT)
                .show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(mBinding.mainAcCoordinatorLayout, message, Snackbar.LENGTH_SHORT)
                .show();
    }


    private void showProgressDialog(int messageResource) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(messageResource));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Getting Products");
        mProgressDialog.show();
    }


}

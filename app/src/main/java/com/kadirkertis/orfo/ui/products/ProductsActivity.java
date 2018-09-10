package com.kadirkertis.orfo.ui.products;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kadirkertis.domain.utils.Constants;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.databinding.ActivityProductsBinding;
import com.kadirkertis.orfo.ui.Router.Router;
import com.kadirkertis.orfo.ui.main.AddToCartDialogFragment;
import com.kadirkertis.orfo.ui.products.fragments.ProductsFragment;
import com.kadirkertis.orfo.utils.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public class ProductsActivity extends DaggerAppCompatActivity
        implements AddToCartDialogFragment.AddToCartDialogListener {

    @Inject
    FragmentManager fragmentManager;

    @Inject
    ActivityUtils activityUtils;

    @Inject
    Router router;

    @Inject
    ProductsViewModel viewModel;


    private static final String PRODUCTS_FRAGMENT = "products_fr";
    private String storeId;
    private ProductsFragment productsFragment;
    private TextView cartCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProductsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_products);

        //Get store and table Info
        storeId = getIntent().getStringExtra(Constants.CHECKED_IN_STORE_ID);
        String tableNumber = getIntent().getStringExtra(Constants.CHECKED_IN_TABLE_NUMBER);

        setupProductsFragment(tableNumber);

        //Set up Toolbar
        Toolbar toolbar = binding.productsActivityToolbar;
        setupToolbar(toolbar, "Menu", R.drawable.ic_arrow_back);

        activityUtils.addFragmentWithTagToActivity(fragmentManager, productsFragment, binding.productsFragmentContainer.getId(), PRODUCTS_FRAGMENT);


    }

    private void setupProductsFragment(String tableNumber) {
        productsFragment = (ProductsFragment) fragmentManager.findFragmentByTag(PRODUCTS_FRAGMENT);
        if (productsFragment == null) {
            Bundle args = new Bundle();
            args.putString(Constants.CHECKED_IN_STORE_ID, storeId);
            args.putString(Constants.CHECKED_IN_TABLE_NUMBER, tableNumber);

            productsFragment = new ProductsFragment();
            productsFragment.setArguments(args);
        }

    }

    private void setupToolbar(Toolbar toolbar, String title, int resourceId) {
        toolbar.setTitle(getString(R.string.products_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });
    }

    @Override
    public void onAddToCart(String category, String subCategory, int position, int quantity, View root) {
        if (productsFragment != null)
            productsFragment.onAddToCart(category, subCategory, position, quantity, root);
        viewModel.updateCartCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.updateCartCount();
    }

    @Override
    public void onCancel() {
        if (productsFragment != null)
            productsFragment.onCancel();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products_menu, menu);
        final View cartMenu = menu.findItem(R.id.cart_menu_item).getActionView();
        cartMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                router.showCartScreen(ProductsActivity.this);
            }
        });
        cartCountTextView = cartMenu.findViewById(R.id.txtCount);
        viewModel.updateCartCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_menu_item:
                router.showCartScreen(this);
                return true;
            case R.id.chat_menu_item:
                router.showChatScreen(this, storeId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

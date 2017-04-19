package com.kadirkertis.orfo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.data.DbTaskParams;
import com.kadirkertis.orfo.data.DbTasks;
import com.kadirkertis.orfo.databinding.ActivityProductsBinding;
import com.kadirkertis.orfo.utilities.Constants;


public class ProductsActivity extends AppCompatActivity implements AddToCartDialogFragment.AddToCartDialogListener {

    private static final String PRODUCTS_FRAGMENT = "products_fr";
    private ActivityProductsBinding mBinding;
    private String mStoreId;
    private ProductsFragment mProductsFragment;
    private TextView cartCountTextView;
    private int numOfItemsInCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_products);

        mStoreId = getIntent().getStringExtra(Constants.CHECKED_IN_STORE_ID);
        String tableNumber = getIntent().getStringExtra(Constants.CHECKED_IN_TABLE_NUMBER);

        Toolbar toolbar = mBinding.productsActivityToolbar;
        toolbar.setTitle(getString(R.string.products_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });


        FragmentManager fm = getSupportFragmentManager();
        mProductsFragment = (ProductsFragment) fm.findFragmentByTag(PRODUCTS_FRAGMENT);


        if (mProductsFragment == null) {
            Bundle args = new Bundle();
            args.putString(Constants.CHECKED_IN_STORE_ID, mStoreId);
            args.putString(Constants.CHECKED_IN_TABLE_NUMBER, tableNumber);

            mProductsFragment = new ProductsFragment();
            mProductsFragment.setArguments(args);
            fm.beginTransaction()
                    .replace(mBinding.productsFragmentContainer.getId(), mProductsFragment, PRODUCTS_FRAGMENT)
                    .commit();
        }


    }

    @Override
    public void onAddToCart(String category,String subCategory,int position,int quantity,View root) {
        if (mProductsFragment != null)
            mProductsFragment.onAddToCart(category,subCategory,position,quantity,root);
        updateCartCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    @Override
    public void onCancel() {
        if (mProductsFragment != null)
            mProductsFragment.onCancel();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products_menu,menu);
        final View cartMenu = menu.findItem(R.id.cart_menu_item).getActionView();
        cartMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsActivity.this,CartActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(ProductsActivity.this,null)
                            .toBundle());
                }
                else {
                    startActivity(intent);
                }
            }
        });
        cartCountTextView = (TextView) cartMenu.findViewById(R.id.txtCount);
        updateCartCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart_menu_item:
                Intent intent = new Intent(this,CartActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this,null)
                            .toBundle());
                }
                else {
                    startActivity(intent);
                }
                return true;
            case R.id.chat_menu_item:
                Intent intentCh = new Intent(this,ChatActivity.class);
                intentCh.putExtra(Constants.EXTRA_PLACE_ID,mStoreId);
                intentCh.putExtra(Constants.EXTRA_USER_ID,getSharedPreferences(Constants.PREFS_CHECKED_IN_PLACE,MODE_PRIVATE)
                .getString(Constants.PREFS_USER_ID,null));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intentCh, ActivityOptionsCompat.makeSceneTransitionAnimation(this,null)
                            .toBundle());
                }
                else {
                    startActivity(intentCh);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateCartCount() {
        numOfItemsInCart = DbTasks.numOfItems(getContentResolver());
        if (cartCountTextView == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (numOfItemsInCart == 0)
                    cartCountTextView.setVisibility(View.INVISIBLE);
                else {
                    cartCountTextView.setVisibility(View.VISIBLE);
                    cartCountTextView.setText(Integer.toString(numOfItemsInCart));
                }
            }
        });
    }
}

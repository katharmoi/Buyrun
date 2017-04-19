package com.kadirkertis.orfo.ui;


import android.animation.Animator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.adapters.ProductAdapter;
import com.kadirkertis.orfo.data.DbTaskParams;
import com.kadirkertis.orfo.data.DbTasks;
import com.kadirkertis.orfo.databinding.FragmentProductsBinding;
import com.kadirkertis.orfo.model.Item;
import com.kadirkertis.orfo.model.OrderItem;
import com.kadirkertis.orfo.utilities.Constants;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = ProductsFragment.class.getSimpleName();
    private FragmentProductsBinding mBinding;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDb;
    private DatabaseReference mProductsDbReference;
    private ChildEventListener mProductsDbListener;
    private String mStoreId;
    private String mTableNumber;


    private ProductAdapter mStarterAdapter;
    private ProductAdapter mMainCourseAdapter;
    private ProductAdapter mSpecialsAdapter;
    private ProductAdapter mDessertAdapter;
    private ProductAdapter mAlcoholicAdapter;
    private ProductAdapter mNonAlcoholicAdapter;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        Bundle args = getArguments();


        if (args != null) {
            mStoreId = args.getString(Constants.CHECKED_IN_STORE_ID);
            mTableNumber = args.getString(Constants.CHECKED_IN_TABLE_NUMBER);
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        /*
        *User not auth return back to Main
        * which in turn returns back to login
         */
        if (mUser == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

        mDb = FirebaseDatabase.getInstance();
        mProductsDbReference = mDb.getReference().
                child(Constants.DB_PLACES).
                child(mStoreId).
                child(Constants.TABLE_PRODUCTS);


        List<Item> starters = new ArrayList<>();
        List<Item> mainCourse = new ArrayList<>();
        List<Item> specials = new ArrayList<>();
        List<Item> desserts = new ArrayList<>();

        List<Item> alcoholic = new ArrayList<>();
        List<Item> nonAlcoholic = new ArrayList<>();


        mStarterAdapter = new ProductAdapter(getContext(), starters, getChildFragmentManager());
        mMainCourseAdapter = new ProductAdapter(getContext(), mainCourse, getChildFragmentManager());
        mSpecialsAdapter = new ProductAdapter(getContext(), specials, getChildFragmentManager());
        mDessertAdapter = new ProductAdapter(getContext(), desserts, getChildFragmentManager());
        mAlcoholicAdapter = new ProductAdapter(getContext(), alcoholic, getChildFragmentManager());
        mNonAlcoholicAdapter = new ProductAdapter(getContext(), nonAlcoholic, getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false);
        View root = mBinding.getRoot();
        LinearLayoutManager layoutManagerStarter =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManagerMainCourse =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager layoutManagerSpecials =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager layoutManagerDesserts =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager layoutManagerAlcoholic =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager layoutManagerNonAlcoholic =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mBinding.productsFoodsStartersRecyclerView.setAdapter(mStarterAdapter);
        mBinding.productsFoodsStartersRecyclerView.setLayoutManager(layoutManagerStarter);

        mBinding.productsFoodsMainCourseRecyclerView.setAdapter(mMainCourseAdapter);
        mBinding.productsFoodsMainCourseRecyclerView.setLayoutManager(layoutManagerMainCourse);

        mBinding.productsFoodsSpecialsRecyclerView.setAdapter(mSpecialsAdapter);
        mBinding.productsFoodsSpecialsRecyclerView.setLayoutManager(layoutManagerSpecials);

        mBinding.productsFoodsDessertsRecyclerView.setAdapter(mDessertAdapter);
        mBinding.productsFoodsDessertsRecyclerView.setLayoutManager(layoutManagerDesserts);

        mBinding.productsBeveragesAlcoholicRecyclerView.setAdapter(mAlcoholicAdapter);
        mBinding.productsBeveragesAlcoholicRecyclerView.setLayoutManager(layoutManagerAlcoholic);

        mBinding.productsBeveragesNonAlcoholicRecyclerView.setAdapter(mNonAlcoholicAdapter);
        mBinding.productsBeveragesNonAlcoholicRecyclerView.setLayoutManager(layoutManagerNonAlcoholic);

        mBinding.foodsHead.setOnClickListener(this);
        mBinding.startersHead.setOnClickListener(this);
        mBinding.mainCourseHead.setOnClickListener(this);
        mBinding.specialsHead.setOnClickListener(this);
        mBinding.beveragesHead.setOnClickListener(this);
        mBinding.alcoholicHead.setOnClickListener(this);
        mBinding.nonAlcoholicHead.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        attachProductsListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachProductsListener();
        clearAdapters();
    }

    private void clearAdapters() {
        mStarterAdapter.clear();
        mMainCourseAdapter.clear();
        mSpecialsAdapter.clear();
        mDessertAdapter.clear();
        mAlcoholicAdapter.clear();
        mNonAlcoholicAdapter.clear();
    }

    private void categorizeProduct(Item item) {
        String category = item.getCategory();
        String subCategory = item.getSubCategory();

        if (category.equals(Constants.MAIN_CATEGORY[Constants.INDEX_MAIN_FOODS])) {
            if (subCategory.equals(Constants.SUB_CATEGORY_FOODS[Constants.INDEX_FOODS_STARTERS])) {
                mStarterAdapter.addProduct(item);
            } else if (subCategory.equals(Constants.SUB_CATEGORY_FOODS[Constants.INDEX_FOODS_MAIN_COURSE])) {
                mMainCourseAdapter.addProduct(item);
            } else if (subCategory.equals(Constants.SUB_CATEGORY_FOODS[Constants.INDEX_FOODS_SPECIALS])) {
                mSpecialsAdapter.addProduct(item);
            } else if (subCategory.equals(Constants.SUB_CATEGORY_FOODS[Constants.INDEX_FOODS_DESSERTS])) {
                mDessertAdapter.addProduct(item);
            }
        } else if (category.equals(Constants.MAIN_CATEGORY[Constants.INDEX_MAIN_BEVERAGES])) {
            if (subCategory.equals(Constants.SUB_CATEGORY_BEVERAGES[Constants.INDEX_BVRGS_ALCOHOLIC])) {
                mAlcoholicAdapter.addProduct(item);
            } else if (subCategory.equals(Constants.SUB_CATEGORY_BEVERAGES[Constants.INDEX_BVRGS_NONALCOHOLIC])) {
                mNonAlcoholicAdapter.addProduct(item);
            }
        }

    }

    private void attachProductsListener() {
        if (mProductsDbListener == null) {
            mProductsDbListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Item item = dataSnapshot.getValue(Item.class);
                    categorizeProduct(item);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mProductsDbReference.addChildEventListener(mProductsDbListener);
        }
    }

    private void detachProductsListener() {
        if (mProductsDbListener != null) {
            mProductsDbReference.removeEventListener(mProductsDbListener);
            mProductsDbListener = null;
        }
    }


    public void onAddToCart(String category, String subCategory, int position, int quantity, View root) {
        Item item = null;
        if (category.equals(Constants.MAIN_CATEGORY[Constants.INDEX_MAIN_FOODS])) {
            if (subCategory.equals(Constants.SUB_CATEGORY_FOODS[Constants.INDEX_FOODS_STARTERS])) {
                item = mStarterAdapter.getItem(position);
            } else if (subCategory.equals(Constants.SUB_CATEGORY_FOODS[Constants.INDEX_FOODS_MAIN_COURSE])) {
                item = mMainCourseAdapter.getItem(position);
            } else if (subCategory.equals(Constants.SUB_CATEGORY_FOODS[Constants.INDEX_FOODS_SPECIALS])) {
                item = mSpecialsAdapter.getItem(position);
            } else if (subCategory.equals(Constants.SUB_CATEGORY_FOODS[Constants.INDEX_FOODS_DESSERTS])) {
                item = mDessertAdapter.getItem(position);
            }
        } else if (category.equals(Constants.MAIN_CATEGORY[Constants.INDEX_MAIN_BEVERAGES])) {
            if (subCategory.equals(Constants.SUB_CATEGORY_BEVERAGES[Constants.INDEX_BVRGS_ALCOHOLIC])) {
                item = mAlcoholicAdapter.getItem(position);
            } else if (subCategory.equals(Constants.SUB_CATEGORY_BEVERAGES[Constants.INDEX_BVRGS_NONALCOHOLIC])) {
                item = mNonAlcoholicAdapter.getItem(position);
            }
        }

        if (item != null) {
            OrderItem orderItem = new OrderItem(item.getKey(), item.getName(), item.getPrice(), quantity, item.getUrl());
            new DbTasks(getActivity()).execute(new DbTaskParams(DbTasks.TASK_INSERT_TO_CART, orderItem));
        } else {
            Log.d(LOG_TAG, "Invalid Product");
        }


        //Animate Fragment to cart icon
        int[] cartPos = new int[2];
        View cartMenuItemView = getActivity().findViewById(R.id.cart_menu_item);
        if (cartMenuItemView != null) {
            cartMenuItemView.getLocationInWindow(cartPos);
        }

        int[] rootPos = new int[2];
        root.getLocationInWindow(rootPos);

        final ImageView img = new ImageView(getActivity());
        img.setBackgroundColor(getResources().getColor(android.R.color.white));



        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = root.getWidth();
        layoutParams.height = root.getHeight();
        getActivity().addContentView(img,layoutParams);

        img.setX(getResources().getDisplayMetrics().widthPixels/2 - root.getWidth()/2);
        img.setY(getResources().getDisplayMetrics().heightPixels/2 - root.getHeight()/2);

        img.animate()
                .setDuration(500)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .x(cartPos[0] - root.getWidth()/2 +cartMenuItemView.getWidth()/2)
                .y(cartPos[1] -root.getHeight()/2)
                .scaleX(0)
                .scaleY(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        ((ViewGroup)img.getParent()).removeView(img);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();

        Fragment dialogFragment = getChildFragmentManager()
                .findFragmentByTag(getString(R.string.add_to_cart_dialog_transaction_name));
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (dialogFragment != null) {
            ft.remove(dialogFragment);
        }
        ft.commit();


    }


    public void onCancel() {
        Fragment dialogFragment = getChildFragmentManager()
                .findFragmentByTag(getString(R.string.add_to_cart_dialog_transaction_name));
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (dialogFragment != null) {
            ft.remove(dialogFragment);
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        TransitionManager.beginDelayedTransition(mBinding.productsRoot,new Fade());
        int id = view.getId();
        switch (id){
            case R.id.foodsHead:
                toggleVisibility(mBinding.productsFoodsStartersRecyclerView,
                        mBinding.productsFoodsSpecialsRecyclerView,
                        mBinding.productsFoodsMainCourseRecyclerView,
                        mBinding.productsFoodsDessertsRecyclerView
                        );
                break;
            case R.id.startersHead:
                toggleVisibility(mBinding.productsFoodsStartersRecyclerView);
                break;
            case R.id.specialsHead:
                toggleVisibility(mBinding.productsFoodsSpecialsRecyclerView);
                break;
            case R.id.mainCourseHead:
                toggleVisibility(mBinding.productsFoodsMainCourseRecyclerView);
                break;
            case R.id.dessertsHead:
                toggleVisibility(mBinding.productsFoodsDessertsRecyclerView);
                break;
            case R.id.beveragesHead:
                toggleVisibility(mBinding.productsBeveragesAlcoholicRecyclerView,
                        mBinding.productsBeveragesNonAlcoholicRecyclerView);
                break;
            case R.id.alcoholicHead:
                toggleVisibility(mBinding.productsBeveragesAlcoholicRecyclerView);
                break;
            case R.id.nonAlcoholicHead:
                toggleVisibility(mBinding.productsBeveragesNonAlcoholicRecyclerView);
                break;

        }
    }

    private void toggleVisibility(View view){
        view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
    private void toggleVisibility(View... views){
        boolean visible =false;
        for(View view : views){
            if(view.getVisibility() == View.VISIBLE){
                visible = true;
            }
        }

        for(View view : views){
            view.setVisibility(visible ? View.GONE : View.VISIBLE);
        }
    }
}

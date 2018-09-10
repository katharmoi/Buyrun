package com.kadirkertis.orfo.ui.products.fragments;


import android.animation.Animator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import com.annimon.stream.Stream;
import com.kadirkertis.domain.interactor.product.model.Item;
import com.kadirkertis.domain.interactor.product.model.OrderItem;
import com.kadirkertis.domain.utils.Constants;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.databinding.FragmentProductsBinding;
import com.kadirkertis.orfo.ui.products.ProductAdapter;
import com.kadirkertis.orfo.ui.products.ProductsViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment implements View.OnClickListener {

    @Inject
    ProductsViewModel viewModel;
    @Inject
    Picasso picasso;
    private FragmentProductsBinding mBinding;


    private String placeId;
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

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            placeId = args.getString(Constants.CHECKED_IN_STORE_ID);
            mTableNumber = args.getString(Constants.CHECKED_IN_TABLE_NUMBER);
        }

        viewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        observeCurrentPlace();
        observeProducts();
    }

    public void observeCurrentPlace() {
        viewModel.getCurrentPlaceObservable().observe(this, currentPlace -> {
            //Do some
        });
    }

    public void observeProducts() {
        viewModel.getProductsObservable(placeId).observe(this, products -> {
            Stream.of(products)
                    .forEach(this::categorizeProduct);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerViews();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        clearAdapters();
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
        TransitionManager.beginDelayedTransition(mBinding.productsRoot, new Fade());
        int id = view.getId();
        switch (id) {
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
            viewModel.addToCart(orderItem);
        } else {
            Timber.d("Invalid Product");
        }


        //Animate Fragment
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
        getActivity().addContentView(img, layoutParams);

        img.setX(getResources().getDisplayMetrics().widthPixels / 2 - root.getWidth() / 2);
        img.setY(getResources().getDisplayMetrics().heightPixels / 2 - root.getHeight() / 2);

        img.animate()
                .setDuration(500)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .x(cartPos[0] - root.getWidth() / 2 + cartMenuItemView.getWidth() / 2)
                .y(cartPos[1] - root.getHeight() / 2)
                .scaleX(0)
                .scaleY(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        ((ViewGroup) img.getParent()).removeView(img);
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


    //Helper Methods
    private void initializeRecyclerViews() {


        List<Item> starters = new ArrayList<>();
        List<Item> mainCourse = new ArrayList<>();
        List<Item> specials = new ArrayList<>();
        List<Item> desserts = new ArrayList<>();

        List<Item> alcoholic = new ArrayList<>();
        List<Item> nonAlcoholic = new ArrayList<>();


        mStarterAdapter = new ProductAdapter(picasso, starters, getChildFragmentManager());
        mMainCourseAdapter = new ProductAdapter(picasso, mainCourse, getChildFragmentManager());
        mSpecialsAdapter = new ProductAdapter(picasso, specials, getChildFragmentManager());
        mDessertAdapter = new ProductAdapter(picasso, desserts, getChildFragmentManager());
        mAlcoholicAdapter = new ProductAdapter(picasso, alcoholic, getChildFragmentManager());
        mNonAlcoholicAdapter = new ProductAdapter(picasso, nonAlcoholic, getChildFragmentManager());

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


    private void toggleVisibility(View view) {
        view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private void toggleVisibility(View... views) {
        boolean visible = false;
        for (View view : views) {
            if (view.getVisibility() == View.VISIBLE) {
                visible = true;
            }
        }

        for (View view : views) {
            view.setVisibility(visible ? View.GONE : View.VISIBLE);
        }
    }
}

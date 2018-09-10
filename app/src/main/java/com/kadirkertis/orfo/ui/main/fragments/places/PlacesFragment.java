package com.kadirkertis.orfo.ui.main.fragments.places;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.domain.utils.Constants;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.databinding.FragmentPlacesBinding;
import com.kadirkertis.orfo.ui.main.fragments.places.adapter.AllPlacesAdapter;
import com.kadirkertis.orfo.ui.main.fragments.places.adapter.PlacesAdapter;
import com.kadirkertis.orfo.ui.placeprofile.PlaceProfileActivity;
import com.kadirkertis.orfo.ui.placeprofile.PlaceProfileFragment;
import com.kadirkertis.orfo.utils.GridItemDecoration;
import com.kadirkertis.orfo.utils.Response;
import com.kadirkertis.orfo.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFragment extends DaggerFragment implements PlacesAdapter.OnPlaceClickedListener {

    @Inject
    PlacesFragmentViewModelFactory viewModelFactory;

    @Inject
    AllPlacesAdapter adapter;

    private PlacesFragmentViewModel viewModel;

    private FragmentPlacesBinding mBinding;
    private FirebaseDatabase mDb;
    private DatabaseReference mPlaceListReference;
    private ChildEventListener mPlaceListEventListener;
    private boolean mTwoPane;

    private PlacesAdapter mAdapter;

    public PlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlacesFragmentViewModel.class);
        viewModel.getPlaceResponse().observe(this, this::parseResponse);
        viewModel.loadAllPlaces();
        mDb = FirebaseDatabase.getInstance();
        mPlaceListReference = mDb.getReference()
                .child(Constants.DB_PLACE_LIST);
    }


    private void parseResponse(Response<List<Place>> response) {
        switch (response.getStatus()) {
            case LOADING:
                displayLoadingState();
                break;
            case SUCCESS:
                displaySuccessState(response.data);
                break;
            case ERROR:
                displayErrorState(response.error);
                break;
        }
    }

    private void displayErrorState(Throwable error) {
        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        Timber.d(error);
    }

    private void displaySuccessState(List<Place> places) {
        adapter.onPlacesUpdate(places);
    }

    private void displayLoadingState() {
        Toast.makeText(getContext(), "loading", Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_places, container, false);
        View root = mBinding.getRoot();
        ButterKnife.bind(this, root);
        List<Place> mPlaceList = new ArrayList<>();
        mAdapter = new PlacesAdapter(mPlaceList, getActivity(), R.layout.list_item_places, this);
        mTwoPane = getActivity().findViewById(R.id.mainAcDetailsContainer) != null;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), Utility.getColumCount(getActivity(), mTwoPane));
        mBinding.placesFrRecyclerView.setLayoutManager(layoutManager);
        mBinding.placesFrRecyclerView.setAdapter(adapter);
        int recyclerItemSpacing = Utility.getSpacing(getActivity(), mTwoPane);
        mBinding.placesFrRecyclerView.addItemDecoration(new GridItemDecoration(recyclerItemSpacing, Utility.getColumCount(getActivity(), mTwoPane)));
        mBinding.placesFrProgress.setVisibility(View.VISIBLE);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDbListener();
        mBinding.placesFrProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDbListener();
        mBinding.placesFrProgress.setVisibility(View.GONE);
    }

    private void attachDbListener() {
        if (mPlaceListEventListener == null) {

            mPlaceListEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mAdapter.addItem(dataSnapshot.getValue(Place.class));
                    if (mBinding.placesFrProgress.isShown()) {
                        mBinding.placesFrProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    mAdapter.notifyItemChanged(mAdapter.getItemPosition(dataSnapshot.getValue(Place.class)));
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
            mPlaceListReference.addChildEventListener(mPlaceListEventListener);

            mPlaceListReference.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //OrfoAnimations.revealEffectShow(getContext(),mBinding.placesFrRecyclerView);
                    } else {
                        mBinding.placesFrRecyclerView.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }
    }

    private void detachDbListener() {
        if (mPlaceListEventListener != null) {
            mPlaceListReference.removeEventListener(mPlaceListEventListener);
            mPlaceListEventListener = null;
            mAdapter.clear();
        }
    }

    @Override
    public void onPlaceClicked(String placeKey, String placeName, double[] placeLatLong, int position, View sharedElement) {
        boolean twoPane = getActivity().findViewById(R.id.mainAcDetailsContainer) != null;
        if (twoPane) {
            Fragment fr = PlaceProfileFragment.newInstance(placeKey, placeName, placeLatLong);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainAcDetailsContainer, fr)
                    .commit();

        } else {
            Intent intent = new Intent(getActivity(), PlaceProfileActivity.class);
            intent.putExtra(Constants.PLACE_ID_EXTRA, placeKey);
            intent.putExtra(Constants.PLACE_NAME_EXTRA, placeName);
            intent.putExtra(Constants.PLACE_LAT_LONG_EXTRA, placeLatLong);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sharedElement.setTransitionName(getString(R.string.shared_thumb_img));
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        sharedElement, getString(R.string.shared_thumb_img))
                        .toBundle());
            } else {
                startActivity(intent);
            }

        }
    }
}

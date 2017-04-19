package com.kadirkertis.orfo.ui;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.adapters.PlacesAdapter;
import com.kadirkertis.orfo.databinding.FragmentPlacesBinding;
import com.kadirkertis.orfo.model.PlaceInfo;
import com.kadirkertis.orfo.utilities.Constants;
import com.kadirkertis.orfo.utilities.GridItemDecoration;
import com.kadirkertis.orfo.utilities.OrfoAnimations;
import com.kadirkertis.orfo.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFragment extends Fragment implements PlacesAdapter.OnPlaceClickedListener{

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
        mDb = FirebaseDatabase.getInstance();
        mPlaceListReference = mDb.getReference()
                .child(Constants.DB_PLACE_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_places, container, false);
        View root = mBinding.getRoot();
        List<PlaceInfo> mPlaceList = new ArrayList<>();
        mAdapter = new PlacesAdapter(mPlaceList,getActivity(),R.layout.list_item_places,this);
        mTwoPane = getActivity().findViewById(R.id.mainAcDetailsContainer) != null;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),Utility.getColumCount(getActivity(),mTwoPane));
        mBinding.placesFrRecyclerView.setLayoutManager(layoutManager);
        mBinding.placesFrRecyclerView.setAdapter(mAdapter);
        int recyclerItemSpacing =Utility.getSpacing(getActivity(),mTwoPane);
        mBinding.placesFrRecyclerView.addItemDecoration(new GridItemDecoration(recyclerItemSpacing, Utility.getColumCount(getActivity(),mTwoPane)));
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

    private void attachDbListener(){
        if(mPlaceListEventListener == null){

            mPlaceListEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mAdapter.addItem(dataSnapshot.getValue(PlaceInfo.class));
                    if( mBinding.placesFrProgress.isShown()){
                        mBinding.placesFrProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    mAdapter.notifyItemChanged(mAdapter.getItemPosition(dataSnapshot.getValue(PlaceInfo.class)));
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
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        OrfoAnimations.revealEffectShow(getContext(),mBinding.placesFrRecyclerView);
                    }else{
                        mBinding.placesFrRecyclerView.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }
    }

    private void detachDbListener(){
        if(mPlaceListEventListener != null){
            mPlaceListReference.removeEventListener(mPlaceListEventListener);
            mPlaceListEventListener = null;
            mAdapter.clear();
        }
    }

    @Override
    public void onPlaceClicked(String placeKey,String placeName,double[] placeLatLong,int position,View sharedElement) {
        boolean twoPane = getActivity().findViewById(R.id.mainAcDetailsContainer) != null;
        if(twoPane){
            Fragment fr = PlaceProfileFragment.newInstance(placeKey,placeName,placeLatLong);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainAcDetailsContainer,fr)
                    .commit();

        }else{
            Intent intent = new Intent(getActivity(),PlaceProfileActivity.class);
            intent.putExtra(Constants.PLACE_ID_EXTRA,placeKey);
            intent.putExtra(Constants.PLACE_NAME_EXTRA,placeName);
            intent.putExtra(Constants.PLACE_LAT_LONG_EXTRA,placeLatLong);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sharedElement.setTransitionName(getString(R.string.shared_thumb_img));
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        sharedElement,getString(R.string.shared_thumb_img))
                        .toBundle());
            }
            else {
                startActivity(intent);
            }

        }
    }
}

package com.kadirkertis.orfo.ui.placeprofile;


import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.domain.utils.Constants;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.adapters.ReviewsAdapter;
import com.kadirkertis.orfo.databinding.FragmentPlaceProfileBinding;
import com.kadirkertis.orfo.model.Review;
import com.kadirkertis.orfo.utils.OrfoAnimations;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceProfileFragment extends Fragment implements OnMapReadyCallback {

    private FragmentPlaceProfileBinding mBinding;

    private String mPlaceId;
    private String mPlaceName;
    private double[] mPlaceLatLng;
    private FirebaseDatabase mDb;
    private DatabaseReference mPlaceInfoDatabaseReference;
    private DatabaseReference mPlaceReviewsDatabaseReference;
    private Place mPlace;
    private ReviewsAdapter mReviewsAdapter;
    private List<Review> mReviewList;
    private Typeface mFont;
    private Typeface mFontBold;
    private Boolean mPlaceFaved;
    private Typeface mFontItalic;


    public PlaceProfileFragment() {
        // Required empty public constructor
    }


    public static PlaceProfileFragment newInstance(String placeId, String placeName, double[] placeLatLng) {
        PlaceProfileFragment fragment = new PlaceProfileFragment();
        Bundle args = new Bundle();
        args.putString(Constants.PLACE_ID_EXTRA, placeId);
        args.putString(Constants.PLACE_NAME_EXTRA, placeName);
        args.putDoubleArray(Constants.PLACE_LAT_LONG_EXTRA, placeLatLng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlaceId = getArguments().getString(Constants.PLACE_ID_EXTRA);
            mPlaceName = getArguments().getString(Constants.PLACE_NAME_EXTRA);
            mPlaceLatLng = getArguments().getDoubleArray(Constants.PLACE_LAT_LONG_EXTRA);
        }

        mDb = FirebaseDatabase.getInstance();
        mPlaceInfoDatabaseReference = mDb.getReference()
                .child(Constants.DB_PLACES)
                .child(mPlaceId)
                .child(Constants.TABLE_COMPANY_INFO);
        mPlaceInfoDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPlace = dataSnapshot.getValue(Place.class);
                Picasso.get()
                        .load(mPlace.getImageUrl())
                        .placeholder(R.drawable.no_img_placeholder)
                        .error(R.drawable.no_img_placeholder)
                        .into(mBinding.placeProfileAcPlaceImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                mBinding.placeProfileAcPlaceText.setText(mPlace.getPlaceName());
                mBinding.placeProfileAcAddressText.setText(mPlace.getAddress());
                mBinding.placeProfileAcPhoneText.setText(mPlace.getPhone());
                mBinding.placeProfileAcPlaceTypeText.setText(mPlace.getPlaceType().toUpperCase());
                mBinding.placeProfileAcToolbar.setTitle(mPlace.getPlaceName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPlaceReviewsDatabaseReference = mDb.getReference()
                .child(Constants.DB_PLACES)
                .child(mPlaceId)
                .child(Constants.TABLE_PLACE_REVIEWS);
        mPlaceReviewsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ss : dataSnapshot.getChildren()) {
                    Review review = ss.getValue(Review.class);
                    mReviewsAdapter.addItem(review);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.cant_get_review, Toast.LENGTH_SHORT).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTransitionEnd(Transition transition) {
                    OrfoAnimations.revealEffectShow(getContext(), mBinding.placeProfileAcPlaceImage);
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_profile, container, false);
        if (getActivity().findViewById(R.id.mainAcDetailsContainer) == null) {
            Toolbar toolbar = mBinding.placeProfileAcToolbar;
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_back));
            mBinding.placeProfileAcCollapsingLayout.setTitle(mPlaceName);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AppCompatActivity) getActivity()).onSupportNavigateUp();
                }
            });
        }

        //TODO correct this
        mPlaceFaved = false;//new DbTasks(getActivity()).isPlaceInDb(mPlaceId);
        if (mPlaceFaved) {
            mBinding.placeProfileAcFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)));
        }

        mBinding.placeProfileAcFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO correct this
//                if (!(new DbTasks(getActivity()).isPlaceInDb(mPlaceId))) {
//                    new DbTasks(getActivity()).execute(new DbTaskParams(DbTasks.TASK_FAV_PLACE, mPlace));
//                    Snackbar.make(mBinding.getRoot(), getString(R.string.added_to_fav), Snackbar.LENGTH_SHORT).show();
//                    mBinding.placeProfileAcFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)));
//                } else {
//                    new DbTasks(getActivity()).execute(new DbTaskParams(DbTasks.TASK_DELETE_FROM_FAV_PLACE, mPlace));
//                    Snackbar.make(mBinding.getRoot(), getString(R.string.removed_from_fav), Snackbar.LENGTH_SHORT).show();
//                    mBinding.placeProfileAcFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.colorAccent)));
//                }

            }
        });

        mBinding.placeProfileAcPlaceText.setTypeface(mFontBold);
        mBinding.placeProfileAcAddressText.setTypeface(mFont);
        mBinding.placeProfileAcPhoneText.setTypeface(mFont);
        mBinding.placeProfileAcPlaceTypeText.setTypeface(mFontBold);
        mBinding.placeProfileAcReviewsHeader.setTypeface(mFontBold);


        mReviewList = new ArrayList<>();
        Map<String, Typeface> typefaces = new HashMap<>();
        typefaces.put(Constants.TYPEFACE_COND_BOLD, mFontBold);
        typefaces.put(Constants.TYPEFACE_COND_ITALIC, mFontItalic);
        typefaces.put(Constants.TYPEFACE_COND_LIGHT, mFont);
        mReviewsAdapter = new ReviewsAdapter(mReviewList, getActivity(), R.layout.list_item_review, typefaces);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.reviewsRecycler.setLayoutManager(lm);
        mBinding.reviewsRecycler.setAdapter(mReviewsAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return mBinding.getRoot();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO add null lat lng check
        LatLng latLng = new LatLng(mPlaceLatLng[0], mPlaceLatLng[1]);
        CameraPosition target = CameraPosition.builder()
                .target(latLng)
                .zoom(17)
                .tilt(65)
                .bearing(90)
                .build();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(mPlaceName)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.orfomarker));
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf");
        mFontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondBold.ttf");
        mFontItalic = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLightItalic.ttf");
    }

}

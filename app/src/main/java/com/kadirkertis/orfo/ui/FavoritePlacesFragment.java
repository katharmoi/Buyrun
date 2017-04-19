package com.kadirkertis.orfo.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.adapters.FavoritePlacesAdapter;
import com.kadirkertis.orfo.data.OrfoDbContract;
import com.kadirkertis.orfo.utilities.Constants;
import com.kadirkertis.orfo.utilities.GridItemDecoration;
import com.kadirkertis.orfo.utilities.Utility;
import com.kadirkertis.orfo.views.EmptyRecyclerView;

public class FavoritePlacesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
FavoritePlacesAdapter.OnPlaceClickedListener{
    private static final int FAV_LOADER = 101;
    private FavoritePlacesAdapter mAdapter;
    private boolean mTwoPane;


    public FavoritePlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_favorite_places, container, false);
        TextView emptyView = (TextView) root.findViewById(R.id.emptyView);
        mAdapter = new FavoritePlacesAdapter(getActivity(),null,this);
        mTwoPane = getActivity().findViewById(R.id.mainAcDetailsContainer) != null;
        EmptyRecyclerView recyclerView = (EmptyRecyclerView) root.findViewById(R.id.fav_places_recycler_view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setEmptyView(emptyView);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), Utility.getColumCount(getActivity(),mTwoPane));
        recyclerView.setLayoutManager(layoutManager);
        int recyclerItemSpacing =Utility.getSpacing(getActivity(),mTwoPane);
        recyclerView.addItemDecoration(new GridItemDecoration(recyclerItemSpacing, Utility.getColumCount(getActivity(),mTwoPane)));

        return  root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAV_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_URI,
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onPlaceClicked(String placeKey,String placeName,double[] placeLatLng,View sharedElement) {
        boolean twoPane = getActivity().findViewById(R.id.mainAcDetailsContainer) != null;
        if(twoPane){
            Fragment fr = PlaceProfileFragment.newInstance(placeKey,placeName,placeLatLng);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainAcDetailsContainer,fr)
                    .commit();

        }else{
            Intent intent = new Intent(getActivity(),PlaceProfileActivity.class);
            intent.putExtra(Constants.PLACE_ID_EXTRA,placeKey);
            intent.putExtra(Constants.PLACE_NAME_EXTRA,placeName);
            intent.putExtra(Constants.PLACE_LAT_LONG_EXTRA,placeLatLng);
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

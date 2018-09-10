package com.kadirkertis.orfo.ui.main.fragments.places.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.Relay;
import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.orfo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kadir Kertis on 9/7/2018.
 */

public class AllPlacesAdapter extends RecyclerView.Adapter<AllPlacesAdapter.AllPlacesViewHolder> {

    private List<Place> allPlaces = new ArrayList<>();
    private final Relay<Place> onClickRelay = BehaviorRelay.create();


    @NonNull
    @Override
    public AllPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_places, parent, false);
        return new AllPlacesViewHolder(root, onClickRelay);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPlacesViewHolder holder, int position) {
        holder.setItem(allPlaces.get(position));

    }

    @Override
    public int getItemCount() {
        return allPlaces.size();
    }

    public void onPlacesUpdate(final List<Place> places) {
        this.allPlaces = places;
    }


    static final class AllPlacesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_place_img)
        ImageView placeImage;

        @BindView(R.id.list_item_place_name)
        TextView placeName;

        @BindView(R.id.list_item_place_type)
        TextView placeType;

        @BindView(R.id.list_item_place_rating_bar)
        RatingBar ratingBar;

        private Place place;
        private Relay<Place> onClickRelay;

        AllPlacesViewHolder(View itemView, Relay<Place> onClickRelay) {
            super(itemView);
            this.onClickRelay = onClickRelay;
            ButterKnife.bind(this, itemView);
        }

        void setItem(final Place place) {
            this.place = place;
            placeName.setText(place.getPlaceName());
            placeType.setText(place.getPlaceType());
            if (place.getImageUrl() != null) {
                Picasso.get()
                        .load(place.getImageUrl())
                        .placeholder(R.drawable.no_img_placeholder)
                        .error(R.drawable.no_img_placeholder)
                        .into(placeImage);
            }
        }

        @OnClick(R.id.place_card_view)
        void onPlaceClicked() {
            onClickRelay.accept(place);
        }
    }
}

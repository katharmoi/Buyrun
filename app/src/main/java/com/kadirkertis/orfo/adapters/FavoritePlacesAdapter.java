package com.kadirkertis.orfo.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.data.OrfoDbContract;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.TwitterCollection;

/**
 * Created by Kadir Kertis on 29.3.2017.
 */

public class FavoritePlacesAdapter extends RecyclerView.Adapter<FavoritePlacesAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private OnPlaceClickedListener mListener;
    private int mSelectedPosition = -1;

    public FavoritePlacesAdapter(Context context, Cursor cursor, OnPlaceClickedListener listener) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public FavoritePlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoritePlacesAdapter.ViewHolder holder, int position) {
        String placeName;
        if (mCursor.moveToPosition(position)) {
            placeName = mCursor.getString(
                    mCursor.getColumnIndex(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_NAME));
            holder.placeName.setText(placeName);

            holder.placeType.setText(mCursor.getString(
                    mCursor.getColumnIndex(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_TYPE)));
            Picasso.with(mContext)
                    .load(mCursor.getString(
                            mCursor.getColumnIndex(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_IMG_URL)
                    ))
                    .placeholder(R.drawable.no_img_placeholder)
                    .error(R.drawable.no_img_placeholder)
                    .into(holder.placeImage);
            holder.placeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(mSelectedPosition);
                    mSelectedPosition = holder.getAdapterPosition();
                    notifyItemChanged(mSelectedPosition);
                    mListener.onPlaceClicked(mCursor.getString(mCursor.getColumnIndex(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ID)),
                            mCursor.getString(mCursor.getColumnIndex(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_NAME)),
                            new double[]{mCursor.getDouble(mCursor.getColumnIndex(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_LAT)),
                            mCursor.getDouble(mCursor.getColumnIndex(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_LNG))},
                    holder.placeImage
                    );
                }
            });
            holder.itemView.setSelected(position == mSelectedPosition);
            holder.placeImage.setContentDescription(placeName);
        }

    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        this.mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView placeImage;
        private TextView placeName;
        private TextView placeType;
        private RatingBar ratingBar;


        public ViewHolder(View itemView) {
            super(itemView);

            placeImage = (ImageView) itemView.findViewById(R.id.list_item_place_img);
            placeName = (TextView) itemView.findViewById(R.id.list_item_place_name);
            placeType = (TextView) itemView.findViewById(R.id.list_item_place_type);
            ratingBar = (RatingBar) itemView.findViewById(R.id.list_item_place_rating_bar);
        }
    }

    public interface OnPlaceClickedListener {
        void onPlaceClicked(String placeKey, String placeName, double[] placeLatLng, View sharedView);
    }
}

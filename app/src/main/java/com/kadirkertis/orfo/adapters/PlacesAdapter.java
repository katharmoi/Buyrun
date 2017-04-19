package com.kadirkertis.orfo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.PlaceInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kadir Kertis on 17.2.2017.
 */

public class PlacesAdapter extends GenericAdapter<PlaceInfo,PlacesAdapter.ViewHolder> {

    private List<PlaceInfo> mData;
    private Context mContext;
    private OnPlaceClickedListener mListener;
    private int mSelectedPosition = -1;

    public PlacesAdapter(List<PlaceInfo> data, Context context, int resourceId,OnPlaceClickedListener listener) {
        super(data, context, resourceId);
        mData = data;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_places,parent,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PlaceInfo placeInfo = mData.get(position);
        holder.placeName.setText(placeInfo.getPlaceName());
        holder.placeType.setText(placeInfo.getPlaceType());
        if(placeInfo.getImageUrl() != null){
            Picasso.with(mContext)
                    .load(placeInfo.getImageUrl())
                    .placeholder(R.drawable.no_img_placeholder)
                    .error(R.drawable.no_img_placeholder)
                    .into(holder.placeImage);
        }
        holder.placeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(mSelectedPosition);
                mSelectedPosition = holder.getAdapterPosition();
                notifyItemChanged(mSelectedPosition);
                mListener.onPlaceClicked(placeInfo.getId(),
                                    placeInfo.getPlaceName(),
                        new double[]{placeInfo.getLatitude(),placeInfo.getLongitude()},
                        mSelectedPosition,holder.placeImage);
            }
        });
        holder.itemView.setSelected(position == mSelectedPosition);
        holder.placeImage.setContentDescription(placeInfo.getPlaceName());

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

    public interface OnPlaceClickedListener{
        void onPlaceClicked(String placeKey,
                            String placeName,
                            double[] placeLatLong,
                            int selectedPosition,View sharedElement);
    }
}

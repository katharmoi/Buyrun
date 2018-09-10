package com.kadirkertis.orfo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.CheckInPlace;

import java.util.List;

/**
 * Created by Kadir Kertis on 14.4.2017.
 */

public class ChatterAdapter extends GenericAdapter<CheckInPlace,ChatterAdapter.ViewHolder> {
    public ChatterAdapter(List<CheckInPlace> data, Context context, int resourceId) {
        super(data, context, resourceId);
    }

    @Override
    public ChatterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chatters,
                parent,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ChatterAdapter.ViewHolder holder, int position) {
        CheckInPlace user = mData.get(position);
        if(user.getUid() != null){
            holder.mChatter.setText(user.getUid());
        }else{
            holder.mChatter.setText(mContext.getString(R.string.anonymous_user));
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mChatter;
        public ViewHolder(View itemView) {
            super(itemView);
            mChatter = itemView.findViewById(R.id.listItemChatterId);
        }
    }
}

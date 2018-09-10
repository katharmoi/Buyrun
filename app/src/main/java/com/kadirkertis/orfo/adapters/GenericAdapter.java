package com.kadirkertis.orfo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Kadir Kertis on 17.2.2017.
 */

public abstract class GenericAdapter<T,VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    final List<T> mData;
    final int mResourceId;
    final Context mContext;

    public GenericAdapter(List<T> data, Context context, int resourceId){
        mData = data;
        mContext = context;
        mResourceId = resourceId;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public T getItem(int position){
        try{
            return mData.get(position);
        }catch (IndexOutOfBoundsException e){
            return null;
        }

    }

    public int getItemPosition(T item){
        int index = mData.indexOf(item);
        return index;
    }

    public void addItem(T item){
        mData.add(item);
        notifyItemInserted(mData.size() -1);
    }

    public void addAll(List<T> items){
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public void removeItem(T item){
        int index = mData.indexOf(item);
        mData.remove(item);
        notifyItemRemoved(index);
    }

    public void removeLastItem(){
        mData.remove(mData.size() -1);
        notifyItemRemoved(mData.size());
    }

    public void clear(){
        int size = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0,size);
    }
}

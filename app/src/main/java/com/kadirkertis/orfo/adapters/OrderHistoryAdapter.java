package com.kadirkertis.orfo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.Order;
import com.kadirkertis.orfo.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kadir Kertis on 28.3.2017.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private final List<Order> orderList;
    private final Context mContext;
    private final String mTotalPrefix;
    private final String mPlacePrefix;
    private final String mDatePrefix;

    public OrderHistoryAdapter(Context context, ArrayList<Order> data) {
        mContext = context;
        this.orderList = data;
        mTotalPrefix = mContext.getString(R.string.total_prefix);
        mPlacePrefix = mContext.getString(R.string.order_details_place);
        mDatePrefix = mContext.getString(R.string.order_details_date);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.closed_order_list_item, parent, false
                );
        return new ViewHolder(root);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<OrderItem> items = orderList.get(position).getOrderedItems();
        double total = 0;
        for(OrderItem item : items){
            total += item.getTotalPrice();
        }
        OrderSingleItemAdapter adapter = new OrderSingleItemAdapter(mContext, items);
        holder.orderList.setAdapter(adapter);
        holder.orderTotal.setText(mTotalPrefix + total);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public void addOrder(Order order) {
        orderList.add(order);
        notifyItemInserted(orderList.size() - 1);
    }

    public void removeOrder(Order order){
        int position = orderList.indexOf(order);
        if(position > -1){
            orderList.remove(position);
            notifyItemRemoved(position);
        }


    }

    public void clear() {
        int size = orderList.size();
        orderList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final ListView orderList;
        final TextView orderTotal;
        final TextView orderPlace;
        final TextView orderDate;

        public ViewHolder(View itemView) {
            super(itemView);
            orderList = itemView.findViewById(R.id.closed_order_item_list_view);
            orderTotal = itemView.findViewById(R.id.closed_order_item_total_text);
            orderPlace = itemView.findViewById(R.id.closed_order_item_place_name);
            orderDate = itemView.findViewById(R.id.closed_order_item_order_date);
        }
    }
}

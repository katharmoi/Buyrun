package com.kadirkertis.orfo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.OrderItem;

import java.util.List;

/**
 * Created by Kadir Kertis on 11.2.2017.
 */

public class OrderSingleItemAdapter extends ArrayAdapter<OrderItem> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_LIST = 1;

    private List<OrderItem> mItems;
    private static class ViewHolder {
        TextView itemName;
        TextView itemPrice;
        TextView quantity;
        TextView totalPrice;
    }

    public OrderSingleItemAdapter(Context context, List<OrderItem> items) {
        super(context, R.layout.ordered_item, items);
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());

        if (type == VIEW_TYPE_HEADER) {
            if (convertView == null) {

                convertView = inflator.inflate(R.layout.ordered_item_header, parent, false);
            }

        } else {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = inflator.inflate(R.layout.ordered_item, parent, false);
                viewHolder.itemName = (TextView) convertView.findViewById(R.id.ordered_item_name);
                viewHolder.quantity = (TextView) convertView.findViewById(R.id.ordered_item_quantity);
                viewHolder.itemPrice = (TextView) convertView.findViewById(R.id.ordered_item_price);
                viewHolder.totalPrice = (TextView) convertView.findViewById(R.id.ordered_item_total_price);

            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            OrderItem item = getItem(position-1);
            viewHolder.itemName.setText(item.getItemName());
            viewHolder.itemPrice.setText(Double.toString(item.getPrice()));
            viewHolder.quantity.setText(Integer.toString(item.getQuantity()));
            viewHolder.totalPrice.setText(Double.toString(item.getTotalPrice()));


            convertView.setTag(viewHolder);
        }

        return convertView;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mItems.size() +1;
    }

    @Override
    public int getItemViewType(int position) {
        return position > 0 ? VIEW_TYPE_LIST:VIEW_TYPE_HEADER;
    }
}

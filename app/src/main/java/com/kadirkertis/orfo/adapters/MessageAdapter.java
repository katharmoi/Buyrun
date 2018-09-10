package com.kadirkertis.orfo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.Message;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kadir Kertis on 14.4.2017.
 */

public class MessageAdapter extends GenericAdapter<Message, MessageAdapter.ViewHolder> {

    private static final int VIEW_USER = 0;
    private static final int VIEW_OTHERS = 1;
    private final String muserId;

    public MessageAdapter(List<Message> data, Context context, int resourceId, String userId) {
        super(data, context, resourceId);
        muserId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = null;
        switch (viewType) {
            case VIEW_USER:
                root = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_user, parent, false);
                break;
            case VIEW_OTHERS:
                root = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_others, parent, false);
        }
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message msg = mData.get(position);
        if (msg != null) {
            holder.message.setText(msg.getMessage());
            holder.name.setText(msg.getName());
            holder.date.setText(new SimpleDateFormat("hh:mm").format(msg.getTimeAddedLong()));
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView message;
        private final TextView name;
        private final TextView date;


        public ViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageTextView);
            name = itemView.findViewById(R.id.nameTextView);
            date = itemView.findViewById(R.id.dateTextView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mData.get(position);
        if (muserId.equals(message.getUserId() )) {
            return VIEW_USER;
        } else {
            return VIEW_OTHERS;
        }

    }
}

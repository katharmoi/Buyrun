package com.kadirkertis.orfo.ui.orderhistory.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.adapters.OrderHistoryAdapter;
import com.kadirkertis.orfo.model.Order;
import com.kadirkertis.domain.utils.Constants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryFragment extends Fragment {


    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    public static final String ORDER_DAY_BACK = "ORDER_DAY_BACK";


    private int mDayBack;

    private FirebaseDatabase mDb;
    private DatabaseReference mOrdersReference;
    private Query mClosedOrdersDateQuery;
    private ChildEventListener mOrdersListener;
    private RecyclerView mOrdersRecyclerView;
    private OrderHistoryAdapter mOrderAdapter;
    private SharedPreferences mPrefs;
    private String mUserId;



    public static OrderHistoryFragment newInstance(int dayBack) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ORDER_DAY_BACK, dayBack);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDayBack = getArguments().getInt(ORDER_DAY_BACK);
        }

        //TODO Auth check

        mPrefs = getActivity().getSharedPreferences(Constants.PREFS_CHECKED_IN_PLACE, Context.MODE_PRIVATE);
        mUserId = mPrefs.getString(Constants.PREFS_USER_ID,null);


        mDb = FirebaseDatabase.getInstance();
        mOrdersReference = mDb.getReference()
                .child(Constants.DB_USERS)
                .child(mUserId)
                .child(Constants.TABLE_USER_ORDERS);
        mClosedOrdersDateQuery = mOrdersReference
                .orderByChild("timeAdded")
                .equalTo(mDayBack);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_order_history, container, false);
        mOrdersRecyclerView = root.findViewById(R.id.order_history_recycler_view);
        LinearLayoutManager layoutManager = new
                LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        ArrayList<Order> mOrdersList = new ArrayList<>();
        mOrderAdapter = new OrderHistoryAdapter(getContext(),mOrdersList);
        mOrdersRecyclerView.setAdapter(mOrderAdapter);
        mOrdersRecyclerView.setLayoutManager(layoutManager);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        attachOrderListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachOrderListener();
    }

    private void attachOrderListener() {
        if(mOrdersListener == null){
            mOrdersListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Order order = dataSnapshot.getValue(Order.class);
                    mOrderAdapter.addOrder(order);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mOrdersReference.addChildEventListener(mOrdersListener);
        }
    }

    private void detachOrderListener() {
        if(mOrdersListener != null){
            mOrdersReference.removeEventListener(mOrdersListener);
            mOrdersListener = null;
        }
    }


}

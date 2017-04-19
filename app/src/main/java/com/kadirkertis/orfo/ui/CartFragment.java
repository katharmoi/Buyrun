package com.kadirkertis.orfo.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.adapters.CartAdapter;
import com.kadirkertis.orfo.data.DbTaskParams;
import com.kadirkertis.orfo.data.DbTasks;
import com.kadirkertis.orfo.data.OrfoDbContract;
import com.kadirkertis.orfo.model.Order;
import com.kadirkertis.orfo.model.OrderItem;
import com.kadirkertis.orfo.utilities.Constants;
import com.kadirkertis.orfo.views.EmptyRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener {
    private static final int CART_LOADER = 100;

    private CartAdapter mAdapter;
    private TextView mTotalAmountText;
    private CardView mTotalCard;
    private Button mOrderBtn;
    private SharedPreferences mPrefs;
    private FirebaseDatabase mFirebaseDatabase;
    private ProgressDialog mProgressDialog;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getActivity().getSharedPreferences(Constants.PREFS_CHECKED_IN_PLACE, Context.MODE_PRIVATE);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        mAdapter = new CartAdapter(getActivity(),null);
        mTotalAmountText = (TextView) root.findViewById(R.id.cart_general_total_text);
        mTotalCard = (CardView) root.findViewById(R.id.cart_total_card);
        mOrderBtn = (Button) root.findViewById(R.id.cart_order_btn);
        mOrderBtn.setOnClickListener(this);
        TextView emptyView = (TextView) root.findViewById(R.id.emptyView);
        EmptyRecyclerView cartRecycler = (EmptyRecyclerView) root.findViewById(R.id.cart_recycler);
        cartRecycler.setEmptyView(emptyView);
        cartRecycler.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        cartRecycler.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CART_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                OrfoDbContract.OrfoCartTable.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if(mAdapter.getItemCount() > 0){
            mTotalCard.setVisibility(View.VISIBLE);
            mOrderBtn.setVisibility(View.VISIBLE);
            mTotalAmountText.setText(getString(R.string.total_amount_prefix)
                    +Double.toString(mAdapter.getTotal()));
        }
        else{
            mTotalCard.setVisibility(View.GONE);
            mOrderBtn.setVisibility(View.GONE);
        }
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {
        if(view == mOrderBtn){
            order();
        }
    }

    private void order() {
        String userKey = mPrefs.getString(Constants.PREFS_USER_ID, null);
        String storeId = mPrefs.getString(Constants.PREFS_CHECKED_IN_PLACE,null);
        String table = mPrefs.getString(Constants.PREFS_CHECKED_IN_TABLE_NUMBER,null);
        String[] tb = table.split("_");
        int tableNumber = Integer.parseInt(tb[1]);

        HashMap<String, Object> orderTime = new HashMap<>();
        orderTime.put(Constants.PROPERTY_TIME_ADDED, ServerValue.TIMESTAMP);

        String orderId = mFirebaseDatabase.getReference()
                .child(Constants.DB_PLACES)
                .child(storeId)
                .child(Constants.TABLE_PLACE_PENDING_ORDERS)
                .push()
                .getKey();

        Map<String, Object> orders = new HashMap<>();

        List<OrderItem> order= mAdapter.getOrder();



        Map<String, Object> userOrder = (new Order(orderId,storeId,orderTime,userKey,tableNumber,order)).toMap();

        String placeReference = "/"
                + Constants.DB_PLACES
                + "/"
                + storeId
                + "/"
                + Constants.TABLE_PLACE_PENDING_ORDERS
                + "/"
                + orderId;


        String userReference = "/"
                + Constants.DB_USERS
                + "/"
                + userKey
                + "/"
                + Constants.TABLE_USER_ORDERS
                + "/"
                + orderId;
        orders.put(placeReference,
                userOrder);
        orders.put(userReference,
                userOrder);

        showProgressDialog(R.string.processing_order);
        mFirebaseDatabase.getReference().updateChildren(orders)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), getString(R.string.ordered_succesfully),
                                Toast.LENGTH_SHORT).show();
                        if (mProgressDialog != null)
                            mProgressDialog.dismiss();
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putLong(Constants.PREFS_ORDER_TIME,System.currentTimeMillis());
                        editor.apply();

                        new DbTasks(getActivity()).execute(new DbTaskParams(DbTasks.TASK_EMPTY_CART));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), getString(R.string.order_failure),
                                Toast.LENGTH_SHORT).show();
                        if (mProgressDialog != null)
                            mProgressDialog.dismiss();
                    }
                });

    }

    private void showProgressDialog(int messageResource) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(messageResource));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

}

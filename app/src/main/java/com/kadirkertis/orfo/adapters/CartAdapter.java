package com.kadirkertis.orfo.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kadirkertis.domain.interactor.product.model.Item;
import com.kadirkertis.orfo.R;
import com.kadirkertis.data.database.OrfoDbContract;
import com.kadirkertis.orfo.model.OrderItem;
import com.kadirkertis.orfo.utils.NumberSpinner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kadir Kertis on 7.3.2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Context mContext;
    private Cursor mCursor;

    public CartAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            int quantity =  mCursor.getInt(mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_QUANTITY));
            String productName = mCursor.getString(
                    mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_NAME));
            double price =  mCursor.getDouble(
                    mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_PRICE));

            holder.productName.setText(productName);

            holder.productQuantity.setText(mContext.getString(R.string.quantity_prefix)
                    +"" +quantity);
            holder.productPrice.setText(mContext.getString(R.string.price_prefix)
                    +"" +price);
            holder.productSubTotal.setText(mContext.getString(R.string.subtotal_prefix)
                   +"" +quantity*price);
            holder.quantitySpinner.setValue(quantity);
            holder.updateCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    new DbTasks(mContext).execute(
//                            new DbTaskParams(DbTasks.TASK_UPDATE_CART,
//                                    mCursor.getInt(mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable._ID)),
//                                    holder.quantitySpinner.getValue())
//                    );
                }
            });

            Picasso.get()
                    .load(mCursor.getString(mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_IMAGE_URL)))
                    .placeholder(R.drawable.no_img_placeholder)
                    .error(R.drawable.no_img_placeholder)
                    .into(holder.productImage);
            holder.clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                            .content(R.string.dialog_sure_to_delete)
                            .positiveText(R.string.yes)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                    new DbTasks(mContext).execute(
//                                            new DbTaskParams(DbTasks.TASK_DELETE_FROM_CART,
//                                                    mCursor.getInt(mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable._ID))));
                                }
                            })
                            .negativeText(R.string.cancel)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });

            holder.productImage.setContentDescription(productName);
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView productImage;
        final TextView productName;
        final TextView productQuantity;
        final TextView productPrice;
        final TextView productSubTotal;
        final ImageButton clearBtn;
        final NumberSpinner quantitySpinner;
        final ImageButton updateCart;

        public ViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.cart_product_image);
            productName = itemView.findViewById(R.id.cart_prdoduct_name);
            productQuantity = itemView.findViewById(R.id.cart_prdoduct_quantity);
            productPrice = itemView.findViewById(R.id.cart_prdoduct_price);
            productSubTotal = itemView.findViewById(R.id.cart_prdoduct_sub_total);
            clearBtn = itemView.findViewById(R.id.cart_clear);
            quantitySpinner = itemView.findViewById(R.id.cart_quantity_spinner);
            updateCart = itemView.findViewById(R.id.cart_refresh);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public int getItem(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getInt(0);
    }

    public double getTotal(){
        double total = 0;

        if(mCursor.moveToFirst()){
           do {
                int quantity =  mCursor.getInt(mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_QUANTITY));
                double price =  mCursor.getDouble(
                        mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_PRICE));
                total += quantity * price;
            }while (mCursor.moveToNext());
        }

        return total;
    }

    public List<Item> getOrder(){
       List<Item> order = new ArrayList<>();
        if(mCursor.moveToFirst()){
            do{
                String productId =mCursor.getString(mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_ID));
                String name = mCursor.getString(mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_NAME));;
                double price =  mCursor.getDouble(
                        mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_PRICE));
                int quantity =  mCursor.getInt(mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_QUANTITY));
                String imageUrl = mCursor.getString(mCursor.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_IMAGE_URL));
                Item item = new Item(productId,name,price,quantity,imageUrl);
                order.add(item);
            }while (mCursor.moveToNext());

        }
        return order;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
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

}

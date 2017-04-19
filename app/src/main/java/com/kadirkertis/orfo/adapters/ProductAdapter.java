package com.kadirkertis.orfo.adapters;


import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.Item;
import com.kadirkertis.orfo.ui.AddToCartDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kadir Kertis on 6.2.2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private static final String TAG = ProductAdapter.class.getSimpleName() ;
    private List<Item> mProducts;
    private Context mContext;
    private FragmentManager mFm;
    private int[] cartIconPosition;

    public ProductAdapter(Context context, List<Item> products, FragmentManager fm) {
        mContext = context;
        mProducts = products;
        mFm = fm;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = mProducts.get(position);
        holder.productNameTextView.setText(item.getName());
        holder.productDescTextView.setText(item.getDescription());
        if (item.getUrl() != null) {
            Picasso.with(mContext)
                    .load(item.getUrl())
                    .placeholder(R.drawable.no_img_placeholder)
                    .error(R.drawable.no_img_placeholder)
                    .into(holder.productImageView);
        }
        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = mFm.beginTransaction();
                Fragment prevDialog = mFm.findFragmentByTag(
                        mContext.getString(R.string.add_to_cart_dialog_transaction_name));

                if (prevDialog != null) {
                    ft.remove(prevDialog);
                }

                DialogFragment fr = AddToCartDialogFragment.newInstance(item.getKey(),
                        item.getName(), item.getUrl(), item.getPrice(),
                        item.getCategory(),
                        item.getSubCategory(),
                        holder.getAdapterPosition(),
                        cartIconPosition
                        );
                fr.setShowsDialog(true);
                try{
                    fr.show(ft, mContext.getString(R.string.add_to_cart_dialog_transaction_name));
                }catch (IllegalStateException e){
                    Log.e(TAG,e.getMessage());
                }

            }
        });
        holder.productImageView.setContentDescription(item.getName());


    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTextView;
        private TextView productDescTextView;
        private ImageView productImageView;
        private ImageButton addToCartBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            productNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_name_text_view);
            productDescTextView = (TextView) itemView.findViewById(R.id.list_item_product_desc_text_view);
            productImageView = (ImageView) itemView.findViewById(R.id.list_item_product_img_view);
            addToCartBtn = (ImageButton) itemView.findViewById(R.id.list_item_product_add_to_cart_btn);
        }
    }

    public void addProduct(Item item) {
        this.mProducts.add(item);
        this.notifyItemInserted(mProducts.size() - 1);
    }

    public void clear() {
        int size = mProducts.size();
        this.mProducts.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    public Item getItem(int position) {
        try {
            return mProducts.get(position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}




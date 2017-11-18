package com.kadirkertis.orfo.ui.products;


import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.kadirkertis.domain.model.Item;
import com.kadirkertis.orfo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by Kadir Kertis on 6.2.2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Item> products;
    private Picasso picasso;
    private FragmentManager fragmentManager;
    private int[] cartIconPosition;

    public Relay<Item> onAddToCartClickedRelay = PublishRelay.create();
    public Relay<Item> onItemClickedRelay = PublishRelay.create();

    public ProductAdapter(Picasso picasso, List<Item> products, FragmentManager fm) {
        this.picasso = picasso;
        this.products = products;
        fragmentManager = fm;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product, parent, false);
        return new ViewHolder(root, picasso, onItemClickedRelay, onAddToCartClickedRelay);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = products.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void addProduct(Item item) {
        this.products.add(item);
        this.notifyItemInserted(products.size() - 1);
    }

    public void clear() {
        int size = products.size();
        this.products.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    public Item getItem(int position) {
        try {
            return products.get(position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Flowable<Item> onItemClicked() {
        return onItemClickedRelay.toFlowable(BackpressureStrategy.LATEST);
    }

    public Flowable<Item> onAddToCartRelayClicked() {
        return onAddToCartClickedRelay.toFlowable(BackpressureStrategy.LATEST);
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTextView;
        private TextView productDescTextView;
        private ImageView productImageView;
        private ImageButton addToCartBtn;
        private Picasso picasso;

        private final Relay<Item> addToCartClickRelay;
        private final Relay<Item> itemClickedRelay;

        //TODO map this to view item
        private Item item;

        public ViewHolder(View itemView, Picasso picasso, Relay<Item> itemClickedRelay, Relay<Item> addToCartClickedRelay) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.list_item_product_name_text_view);
            productDescTextView = itemView.findViewById(R.id.list_item_product_desc_text_view);
            productImageView = itemView.findViewById(R.id.list_item_product_img_view);
            addToCartBtn = itemView.findViewById(R.id.list_item_product_add_to_cart_btn);
            this.picasso = picasso;
            this.itemClickedRelay = itemClickedRelay;
            this.addToCartClickRelay = addToCartClickedRelay;
        }

        public void setItem(Item item) {
            this.item = item;
            productNameTextView.setText(item.getName());
            productDescTextView.setText(item.getDescription());
            if (item.getUrl() != null) {
                picasso.load(item.getUrl())
                        .placeholder(R.drawable.no_img_placeholder)
                        .error(R.drawable.no_img_placeholder)
                        .into(productImageView);
            }
            addToCartBtn.setOnClickListener(view -> addToCartClickRelay.accept(item));
            productImageView.setContentDescription(item.getName());
            itemView.setOnClickListener(view -> itemClickedRelay.accept(item));
        }

    }
}





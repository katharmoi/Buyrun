package com.kadirkertis.orfo.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.utils.NumberSpinner;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * Created by Kadir Kertis on 1.3.2017.
 */

public class AddToCartDialogFragment extends DialogFragment {

    AddToCartDialogListener mListener;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String IMAGE_URL = "url";
    private static final String PRICE = "price";
    private static final String CAT = "cat";
    private static final String SUB_CAT = "sub_cat";
    private static final String POS ="pos";
    private static final String ICON_POS ="icon_pos";

    private String mProductId;
    private String mImageUrl;
    private String mProductName;
    private double mPrice;
    private String mCategory;
    private String mSubCategory;
    private int mPosition;
    private int[] mCartIconPosition;
    private int mQuantity = 1;

    public static AddToCartDialogFragment newInstance(String id, String name, String imageUrl, double price,
    String category, String subCategory, int position, int[] cartIconPosition) {
        AddToCartDialogFragment f = new AddToCartDialogFragment();

        Bundle args = new Bundle();
        args.putString(ID, id);
        args.putString(NAME, name);
        args.putString(IMAGE_URL, imageUrl);
        args.putDouble(PRICE, price);
        args.putString(CAT, category);
        args.putString(SUB_CAT,subCategory);
        args.putInt(POS,position);
        args.putIntArray(ICON_POS,cartIconPosition);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle args = getArguments();
        if (args != null) {
            mProductId = args.getString(ID);
            mProductName = args.getString(NAME);
            mImageUrl = args.getString(IMAGE_URL);
            mPrice = args.getDouble(PRICE);
            mCategory = args.getString(CAT);
            mSubCategory = args.getString(SUB_CAT);
            mPosition = args.getInt(POS);
            mCartIconPosition = args.getIntArray(ICON_POS);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.add_to_cart_dialog, container, false);

        //Check if product exists in com.kadirkertis.orfo.com.kadirkertis.com.kadirkertis.data.data.cart
        //if exists update mQuantity accordingly else set
        //mQuantity to one

        ImageView productImage = root.findViewById(R.id.add_to_cart_product_img_view);
        TextView productName = root.findViewById(R.id.add_to_cart_product_name_text_view);
        TextView priceText = root.findViewById(R.id.add_to_cart_single_item_price_text);
        final TextView quantityText = root.findViewById(R.id.add_to_cart_quantity_text);
        final TextView totalText = root.findViewById(R.id.add_to_cart_total_text);
        NumberSpinner numberSpinner = root.findViewById(R.id.add_to_cart_quantity_spinner);
        Button addToCartBtn = root.findViewById(R.id.add_to_cart_product_add_to_cart_btn);
        Button cancelBtn = root.findViewById(R.id.add_to_cart_product_cancel_btn);
        final View decorView = getDialog().getWindow().getDecorView();
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddToCart(mCategory, mSubCategory, mPosition, mQuantity, decorView);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCancel();
            }
        });

        numberSpinner.setOnValueChangeListener(new NumberSpinner.OnValueChangeListener() {
            @Override
            public void onChange(int value) {
                mQuantity = value;
                quantityText.setText(getString(R.string.quantity_prefix)
                        + Integer.toString(mQuantity));
                totalText.setText(getString(R.string.total_prefix)
                        + String.format(Locale.getDefault(), getString(R.string.money_format), mPrice * mQuantity));
            }
        });

        Picasso.get()
                .load(mImageUrl)
                .error(R.drawable.no_img_placeholder)
                .placeholder(R.drawable.no_img_placeholder)
                .into(productImage);
        productName.setText(mProductName);
        priceText.setText(getString(R.string.price_prefix)
                + String.format(Locale.getDefault(), getString(R.string.money_format), mPrice));
        quantityText.setText(getString(R.string.quantity_prefix)
                + Integer.toString(mQuantity));
        totalText.setText(getString(R.string.total_prefix)
                + String.format(Locale.getDefault(), getString(R.string.money_format), mPrice * mQuantity));

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (AddToCartDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement AddToCartDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public interface AddToCartDialogListener {
        void onAddToCart(String category, String subCategory, int position, int quantity,View root);

        void onCancel();
    }
}

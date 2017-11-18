package com.kadirkertis.orfo.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.Item;
import com.kadirkertis.orfo.ui.main.AddToCartDialogFragment;

import timber.log.Timber;

/**
 * Created by Kadir Kertis on 11/6/2017.
 */

public class ActivityUtilsImpl implements ActivityUtils {
    @Override
    public void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        if (!fragment.isAdded()) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment);
            transaction.commit();
        }

    }

    @Override
    public void addFragmentWithTagToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, String tag) {
        if (!fragment.isAdded()) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment, tag);
            transaction.commit();
        }
    }

    @Override
    public void addDialogFragmentWithTagToActivity(@NonNull FragmentManager fragmentManager,
                                                   @NonNull Fragment fragment, @NonNull Item item,
                                                   int frameId, String tag,
                                                   int adapterPosition,
                                                   int[] cartIconPosition) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prevDialog = fragmentManager.findFragmentByTag(
                fragment.getActivity().getString(R.string.add_to_cart_dialog_transaction_name));

        if (prevDialog != null) {
            ft.remove(prevDialog);
        }

        DialogFragment fr = AddToCartDialogFragment.newInstance(item.getKey(),
                item.getName(), item.getUrl(), item.getPrice(),
                item.getCategory(),
                item.getSubCategory(),
                adapterPosition,
                cartIconPosition
        );
        fr.setShowsDialog(true);
        try {
            fr.show(ft, fragment.getActivity().getString(R.string.add_to_cart_dialog_transaction_name));
        } catch (IllegalStateException e) {
            Timber.d(e.getMessage());
        }
    }


    @Override
    public void setFragmentWithTagToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, String tag, int frameId, String backStackName, boolean animate) {
        if (!fragment.isAdded()) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment, tag);
            transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    @Override
    public boolean propagateBackToTopFragment(@NonNull FragmentManager fragmentManager) {
        return false;
    }

}

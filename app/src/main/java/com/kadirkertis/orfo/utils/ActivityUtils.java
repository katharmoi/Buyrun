package com.kadirkertis.orfo.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.kadirkertis.orfo.model.Item;


/**
 * Created by Kadir Kertis on 11/6/2017.
 */

public interface ActivityUtils {

    void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId);

    void addFragmentWithTagToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, String tag);

    void addDialogFragmentWithTagToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment,
                                            @NonNull Item item, int frameId, String tag, int adapterPosition,
                                            int[] cartIconPosition);

    void setFragmentWithTagToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, String tag, int frameId, String backStackName, boolean animate);

    boolean propagateBackToTopFragment(@NonNull FragmentManager fragmentManager);

}

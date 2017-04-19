package com.kadirkertis.orfo.ui;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;


import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.utilities.Constants;

public class PlaceProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_profile);
        String placeKey = getIntent().getStringExtra(Constants.PLACE_ID_EXTRA);
        String placeName = getIntent().getStringExtra(Constants.PLACE_NAME_EXTRA);
        double[] placeLatLng = getIntent().getDoubleArrayExtra(Constants.PLACE_LAT_LONG_EXTRA);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition tr = new Slide(Gravity.BOTTOM);
            tr.addTarget(R.id.placeProfileAcUserDataContainer);
            getWindow().setEnterTransition(tr);

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        Fragment fr = PlaceProfileFragment.newInstance(placeKey,placeName,placeLatLng);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.placeProfileContainer, fr)
                .commit();

    }

}

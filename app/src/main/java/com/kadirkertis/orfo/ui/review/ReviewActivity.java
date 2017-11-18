package com.kadirkertis.orfo.ui.review;

import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;

import com.kadirkertis.orfo.R;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.review_toolbar);
        toolbar.setTitle(getString(R.string.review_activity_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition trEnter = new Slide(Gravity.END);
            trEnter.setDuration(350);
            getWindow().setEnterTransition(trEnter);
        }
        Bundle args = getIntent().getExtras();

        if (args != null) {
            String placeId = args.getString(ReviewDialogFragment.ID);
            DialogFragment fr = ReviewDialogFragment.newInstance(placeId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.review_fragment_container, fr)
                    .commit();
        }
    }


}

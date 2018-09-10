package com.kadirkertis.orfo.ui.orderhistory;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.ui.orderhistory.fragments.OrderHistoryFragment;

public class OrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        Toolbar tb = findViewById(R.id.order_history_toolbar);
        setSupportActionBar(tb);
        tb.setTitle(getString(R.string.order_history_ac_title));
        tb.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });

        Fragment fr = OrderHistoryFragment.newInstance(30);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.order_history_fragment_container,fr)
                .commit();
    }
}

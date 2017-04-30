package com.clintonmedbery.rajawalibasicproject;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;


public class SightActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> {
    private DiscreteScrollView itemPicker;
    ArrayList<Sight> sights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
        initArray();
        initDiscreteScroll();
    }

    private void initArray() {
        sights = new ArrayList<>();
        sights.add(new Sight("Abbaye de fontaine André", "kekker", R.drawable.abbay));
        sights.add(new Sight("Musée d‘ethnographie", "dsfsdfsd", R.drawable.lalall));
        sights.add(new Sight("Collegiate Church", "dsfasdf", R.drawable.neuch));
    }

    private void initDiscreteScroll() {
        itemPicker = (DiscreteScrollView) findViewById(R.id.picker);
        itemPicker.setOrientation(Orientation.HORIZONTAL);
        itemPicker.setOnItemChangedListener(this);
        itemPicker.setAdapter(new PhotoAdapter(sights));
//        itemPicker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        onItemChanged(sights.get(0));
    }

    private void onItemChanged(Sight sight) {

    }

    @Override
    public void onCurrentItemChanged(@NonNull RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        onItemChanged(sights.get(adapterPosition));

    }
}

package br.ufc.quixada.backontrack.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.ufc.quixada.backontrack.Fragments.GridSpacingItemDecoration;
import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.adapter.ExerciseAdapter;
import br.ufc.quixada.backontrack.model.Exercise;
import br.ufc.quixada.backontrack.model.Section;

/**
 * Created by samue on 14/07/2017.
 */

public class ExerciseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private List<Exercise> exerciseList;
    private List<Section> secList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.fragment_main);
        initCollapsingToolbar();
        //initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //secList = getIntent().getStringExtra("EXTRA_SESSION_ID");
        exerciseList = new ArrayList<>();
        adapter = new ExerciseAdapter(this, exerciseList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        try{
            Glide.with(this).load(
                    R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e){
            e.printStackTrace();
            Log.v("Backdrop Glide catch", "Gliding Error");
        }
        prepareAtivity();



    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            boolean isShow = false;
            int scroolRange = -1;

            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scroolRange == -1){
                    scroolRange = appBarLayout.getTotalScrollRange();
                }
                if(scroolRange + verticalOffset == 0){
                    collapsingToolbar.setTitle("Flexões");
                    isShow = true;
                } else if(isShow){
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //Just for testing purpose
    private void prepareAtivity(){
        int[] thumbnails = new int[]{
                R.drawable.exercise,
                R.drawable.images
        };

        Exercise a = new Exercise(1, "Flexão", getString(R.string.description_test), thumbnails[0]);
        exerciseList.add(a);

        a = new Exercise(1, "Flexão2", getString(R.string.description_test), thumbnails[1]);
        exerciseList.add(a);

        a = new Exercise(1, "Flexão3", getString(R.string.description_test), thumbnails[0]);
        exerciseList.add(a);

        a = new Exercise(1, "Flexão4", getString(R.string.description_test), thumbnails[1]);
        exerciseList.add(a);

        adapter.notifyDataSetChanged();
    }
}


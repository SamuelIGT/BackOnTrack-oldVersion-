package br.ufc.quixada.backontrack.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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
    private Section sec;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_main);

        //gets the section that was chosen
        sec = (Section) getIntent().getSerializableExtra(getString(R.string.sectionToExercise_extraTAG));

        //Initializes the collapsing toolbar
        initCollapsingToolbar(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new ExerciseAdapter(this, sec.getExerciseList());
        prepareAtivity();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true)); //Defines how the cards will be shown
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);





    }

    private void initCollapsingToolbar(final Context context){
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle(sec.getTitle());
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(context,R.color.primary_text));

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
                //if its collapsed
                if(scroolRange + verticalOffset == 0){

                    collapsingToolbar.setTitle(sec.getTitle());
                    isShow = true;

                    //if its expanded
                } else if(isShow){

                    collapsingToolbar.setTitle(sec.getTitle());
                    isShow = false;
                }
            }
        });
    }

/*    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }*/

    //Just for testing purpose
    private void prepareAtivity(){

        try{
            Glide.with(this).load(
                    R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e){
            e.printStackTrace();
            Log.v("Backdrop Glide catch", "Gliding Error");
        }

        /*int[] thumbnails = new int[]{
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
*/
        adapter.notifyDataSetChanged();
    }
}


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
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.ufc.quixada.backontrack.Fragments.GridSpacingItemDecoration;
import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.StorageController;
import br.ufc.quixada.backontrack.adapter.ExerciseAdapter;
import br.ufc.quixada.backontrack.model.Exercise;
import br.ufc.quixada.backontrack.model.Section;

/**
 * Created by samue on 14/07/2017.
 */

public class ExerciseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private Section sec;
    private boolean hasDataChanges = false;
    private StorageController storage = new StorageController();

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
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle(sec.getTitle());
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(context,R.color.newPrimaryText));

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

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        notifyPreviousActivity();
        super.onBackPressed();
    }

    private void notifyPreviousActivity() {
        Intent intent = new Intent();
        boolean hasChange = false;
        Log.d("notifyPreviousActivity", "hasDataChanges: " + hasDataChanges);
        if(hasDataChanges){
            hasChange = true;
            intent.putExtra("HAS_CHANGE", hasChange);
            setResult(RESULT_OK, intent);
            Log.d("notifyPreviousActivity", "section saved");
            storage.saveSectionChanges(getString(R.string.LEVELS_KEY), sec, this);

            hasDataChanges = false;
        }
        Log.d("notifyPreviousActivity", "ShoulderShurg Status: "+ sec.getExerciseList().get(1).getStatus());

        intent.putExtra("HAS_CHANGE", hasChange);
        setResult(RESULT_OK, intent);
    }

    //----------GETTING THE DATA BACK FROM A CALLED ACTIVITY---------------------|
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                String strEditText[] = data.getStringArrayExtra("EXERCISE_RESULT");
                Log.d("onActivityResult", "EXERCISE ID"+ Integer.parseInt(strEditText[0]));
                if(strEditText[1].equals("DONE")){
                    hasDataChanges = true;
                    for(int i = 0; i < sec.getExerciseList().size(); i++){
                        if(sec.getExerciseList().get(i).getId() == Integer.parseInt(strEditText[0])){
                            Log.d("onActivityResult", "Exercise Status setted "+sec.getExerciseList().get(i).getTitle());
                            sec.getExerciseList().get(i).setStatus("DONE");
                            unlockExercise();
                        }
                    }
                }else{
                    Log.d("ON_ActivityResult", "INCOMPLETE");
                }
            }
        }
    }

    //--------------------------------------------------------------------------------|

    private void unlockExercise(){
        for(int i = 1; i < sec.getExerciseList().size(); i++){
            if(sec.getExerciseList().get(i-1).getStatus().equals("DONE")){
                Log.d("unlockExercise", "Next Exercise Unlocked");
                Log.d("unlockExercise", "ShoulderShurg Status: "+ sec.getExerciseList().get(1).getStatus());
                sec.getExerciseList().get(i).setUnlocked(true);
                recyclerView.getAdapter().notifyItemChanged(i);
            }
        }
    }

}
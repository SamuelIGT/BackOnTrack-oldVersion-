package br.ufc.quixada.backontrack.Fragments;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.adapter.AtividadeAdapter;
import br.ufc.quixada.backontrack.model.Atividade;

/**
 * Created by samue on 14/07/2017.
 */

public class ExerciseFragment extends Fragment{

    public ExerciseFragment(){

    }

    private RecyclerView recyclerView;
    private AtividadeAdapter adapter;
    private List<Atividade> atividadeList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //initCollapsingToolbar();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        atividadeList = new ArrayList<>();
        adapter = new AtividadeAdapter(rootView.getContext(), atividadeList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(rootView.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAtivity();

        return rootView;
    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) recyclerView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) recyclerView.findViewById(R.id.appbar);
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
                    collapsingToolbar.setTitle("Card View");
                    isShow = true;
                } else if(isShow){
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge){
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
            int position = parent.getChildAdapterPosition(view); //item position
            int column = position % spanCount; //item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
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

        Atividade a = new Atividade(1, "Flexão", 10, getString(R.string.description_test), thumbnails[0]);
        atividadeList.add(a);

        a = new Atividade(1, "Flexão2", 10, getString(R.string.description_test), thumbnails[1]);
        atividadeList.add(a);

        a = new Atividade(1, "Flexão3", 10, getString(R.string.description_test), thumbnails[0]);
        atividadeList.add(a);

        a = new Atividade(1, "Flexão4", 10, getString(R.string.description_test), thumbnails[1]);
        atividadeList.add(a);

        adapter.notifyDataSetChanged();
    }
}


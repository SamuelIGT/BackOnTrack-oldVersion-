package br.ufc.quixada.backontrack.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.activities.ExerciseExecutionActivity;
import br.ufc.quixada.backontrack.model.Exercise;

/**
 * Created by samue on 17/07/2017.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyViewHolder> {

    private Context mContext;
    private List<Exercise> exerciseList;
    private Intent exerciseIntent;
    private View itemView;
    private List<Integer> itemsPosition;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView thumbnail;
        public Button start;
        public ImageView lock;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_text);
            description = (TextView) view.findViewById(R.id.description_text);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_view);
            start = (Button) view.findViewById(R.id.btn_start);
            lock = (ImageView) view.findViewById(R.id.view_card_lock);
        }
    }

    public ExerciseAdapter(Context mContext, List<Exercise> exerciseList) {
        this.itemsPosition = new ArrayList<>();
        this.mContext = mContext;
        this.exerciseList = exerciseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_card, parent, false);
        //fixes the card corner not transparent bug
        // setupLayout(itemView);

        ImageView thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail_view);
        exerciseIntent = new Intent(mContext, ExerciseExecutionActivity.class);

        Button start = (Button) itemView.findViewById(R.id.btn_start);

        TextView t = (TextView) itemView.findViewById(R.id.title_text);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Exercise exercise = exerciseList.get(position);
        holder.title.setText(exercise.getNome());
        holder.description.setText(exercise.getDescricao());
        itemsPosition.add(position);

        if(!exercise.isUnlocked()){
            holder.lock.setVisibility(View.VISIBLE);
        }

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    exerciseIntent.putExtra(mContext.getString(R.string.exercise_execution_extra), exercise);
                    mContext.startActivity(exerciseIntent);
            }

        });
        // loading album cover using Glide library
        Glide.with(mContext).load(exercise.getThumbnail()).into(holder.thumbnail);

        //popup menu
//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
    }

    private void setupLayout(View view) {
        CardView cv = (CardView) view.findViewById(R.id.card_view);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cv.getBackground().setAlpha(0);
        } else {
            cv.setBackgroundColor(ContextCompat.getColor(view.getContext(), android.R.color.transparent));
        }
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
//    private void showPopupMenu(View view) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//    }
//
//    /**
//     * Click listener for popup menu items
//     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.action_add_favourite:
//                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }
    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

}


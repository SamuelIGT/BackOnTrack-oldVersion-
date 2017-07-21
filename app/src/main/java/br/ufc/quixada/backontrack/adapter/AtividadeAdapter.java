package br.ufc.quixada.backontrack.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.model.Atividade;

/**
 * Created by samue on 17/07/2017.
 */

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Atividade> atividadeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_text);
            description = (TextView) view.findViewById(R.id.description_text);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_view);
        }
    }

    public AtividadeAdapter(Context mContext, List<Atividade> atividadeList) {
        this.mContext = mContext;
        this.atividadeList = atividadeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_card, parent, false);
        ImageView thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail_view);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Atividade atividade = atividadeList.get(position);
        holder.title.setText(atividade.getNome());
        holder.description.setText(atividade.getDescricao());

        // loading album cover using Glide library
        Glide.with(mContext).load(atividade.getThumbnail()).into(holder.thumbnail);

        //popup menu
//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
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
        return atividadeList.size();
    }

}


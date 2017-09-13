package br.ufc.quixada.backontrack;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import br.ufc.quixada.backontrack.activities.ExerciseExecutionActivity;

/**
 * Created by samue on 03/08/2017.
 */

public class EffortButton {
    private int level;
    private TextView title;
    private ImageButton btn;
    private boolean isSelected;

    public EffortButton(int level, TextView title, ImageButton btn) {
        this.btn = btn;
        this.level = level;
        this.title = title;
        this.isSelected = false;
    }

    public void select(Context context, List<EffortButton> btnList, TextView motivation) {
        if (!isSelected) {
            showAll(btnList, context);
            setDefaultButton(btnList, context);
            isSelected = true;
            DrawableCompat.setTint(btn.getDrawable(), ContextCompat.getColor(context, R.color.newColorAccent));
            title.setTextColor(ContextCompat.getColor(context, R.color.primaryText));
            motivation.setVisibility(View.VISIBLE);

        } else {
            showAll(btnList, context);
            DrawableCompat.setTint(btn.getDrawable(), ContextCompat.getColor(context, R.color.icons_black));
            getTitle().setTextColor(ContextCompat.getColor(context, R.color.secondaryText));
            setSelected(false);
            motivation.setVisibility(View.INVISIBLE);

        }
    }

    public void setDefaultButton(List<EffortButton> btnList, Context context) {
        for (EffortButton eBtn : btnList) {
            if (eBtn.getLevel() != getLevel()) {
                eBtn.deselectAll(eBtn, context);
            }
        }
    }

    public void deselectAll(EffortButton eBtn, Context context) {
        DrawableCompat.setTint(eBtn.getBtn().getDrawable(), ContextCompat.getColor(context, R.color.icons_black));
        eBtn.getTitle().setTextColor(ContextCompat.getColor(context, R.color.secondaryText));
        eBtn.setSelected(false);
        eBtn.getTitle().setVisibility(View.INVISIBLE);

    }

    public void showAll(List<EffortButton> btnList, Context context) {
        for (EffortButton eBtn : btnList) {
            eBtn.getTitle().setVisibility(View.VISIBLE);
        }
    }



    public int getLevel() {
        return level;
    }

    public ImageButton getBtn() {
        return btn;
    }

    public boolean isSelected() {
        return isSelected;
    }

    private void setSelected(boolean a) {
        isSelected = a;
    }

    public TextView getTitle() {
        return title;
    }
}

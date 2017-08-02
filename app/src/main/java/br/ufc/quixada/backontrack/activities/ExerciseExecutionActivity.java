package br.ufc.quixada.backontrack.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Vector;

import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.chronometer.Chronometer;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by samue on 28/07/2017.
 */

public class ExerciseExecutionActivity extends AppCompatActivity {

    //private List<String> steps;
    private TextView timer;
    private TextView txtViewSteps;
    private Button startStop;
    private Button finish;
    private CircularProgressBar pgBar;
    private ImageView staticPgBar;
    private CircularProgressDrawable pgBarDrawable;
    private Chronometer chronometer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_execution);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exercise_execution);
        //toolbar.setTitle(exercise.getTitle());
        setSupportActionBar(toolbar);

        // txtViewSteps = (TextView) findViewById(R.id.txt_steps);
        timer = (TextView) findViewById(R.id.timer);
        startStop = (Button) findViewById(R.id.btn_start_stop);
        finish = (Button) findViewById(R.id.btn_finish);
        pgBar = (CircularProgressBar) findViewById(R.id.pgBar);
        staticPgBar = (ImageView) findViewById(R.id.pgBarStatic);
        pgBarDrawable = (CircularProgressDrawable) pgBar.getIndeterminateDrawable();
        chronometer = new Chronometer(timer, this);

        finish.setVisibility(View.INVISIBLE);
//        setupSteps();

        setupScreen(this);

    }

    private void setupScreen(Context context) {
        setupButtons(context);
    }

    //override the function of the android back button
    @Override
    public void onBackPressed() {
        stopTimer();

        finishExerciseAlert();
    }


/*    private void setupSteps() {
        steps = new ArrayList<String>();
        steps.add("Sente-se em uma cadeira.");
        steps.add("Coloque suas mãos na mesa.");
        steps.add("Empurre seu corpo para trás.");

        for (String step : steps) {
            txtViewSteps.setText(txtViewSteps.getText() + step + "\n");
        }
    }*/

    private void setupButtons(final Context context) {
        pgBar.setVisibility(View.INVISIBLE);

        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chronometer.isRunning()) {
                    startTimer();
                } else {
                   stopTimer();
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishExerciseConfirmation();
            }
        });
    }
    private void startTimer(){
        finish.setVisibility(View.INVISIBLE);

        startStop.setText(R.string.button_stop);

        pgBar.setVisibility(View.VISIBLE);
        pgBarDrawable.start();

        chronometer.startTimer(SystemClock.uptimeMillis());
    }
    private void stopTimer(){
        finish.setVisibility(View.VISIBLE);

        startStop.setText(R.string.button_continue);

        pgBar.setVisibility(View.INVISIBLE);
        pgBarDrawable.stop();

        chronometer.stopTimer();
    }

    private void finishExerciseConfirmation(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExerciseExecutionActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.exercise_finish_confirmation, null);

        boolean madeChoice = false;
        Button btnConfirm = (Button) mView.findViewById(R.id.btn_confirm);
        ImageButton btnClose = (ImageButton) mView.findViewById(R.id.btn_close_confirmation);
        final ImageButton emoticon1 = (ImageButton) mView.findViewById(R.id.ic_emotion_1);
        final ImageButton emoticon2 = (ImageButton) mView.findViewById(R.id.ic_emotion_2);
        final ImageButton emoticon3 = (ImageButton) mView.findViewById(R.id.ic_emotion_3);
        final ImageButton emoticon4 = (ImageButton) mView.findViewById(R.id.ic_emotion_4);
        final ImageButton emoticon5 = (ImageButton) mView.findViewById(R.id.ic_emotion_5);
        TextView motivation = (TextView) mView.findViewById(R.id.txt_motivation);

        final ImageButton btnlist [] = {(ImageButton)mView.findViewById(R.id.ic_emotion_1),
                (ImageButton) mView.findViewById(R.id.ic_emotion_2),
                (ImageButton) mView.findViewById(R.id.ic_emotion_3),
                (ImageButton) mView.findViewById(R.id.ic_emotion_4),
                (ImageButton) mView.findViewById(R.id.ic_emotion_5)};


        mBuilder.setView(mView);
        final AlertDialog finishAlert = mBuilder.create();
        finishAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        finishAlert.show();

        //closes the dialog window when touched outside.
        finishAlert.setCanceledOnTouchOutside(true);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(ExerciseExecutionActivity.this, "Confirmado!", Toast.LENGTH_SHORT).show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finishAlert.hide();
            }
        });

        emoticon1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedItem(emoticon1, btnlist);
            }
        });

        emoticon2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedItem(emoticon2, btnlist);
            }
        });

        emoticon3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedItem(emoticon3, btnlist);
            }
        });

        emoticon4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedItem(emoticon4, btnlist);
            }
        });

        emoticon5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedItem(emoticon5, btnlist);
            }
        });


    }

    private void selectedItem(ImageButton btn, ImageButton btnList[]){
        setDefaultButton(btnList);
        DrawableCompat.setTint(btn.getDrawable(), ContextCompat.getColor(ExerciseExecutionActivity.this, R.color.colorAccent));
    }
    public void setDefaultButton(ImageButton btnList[]) {
        for (int i = 0; i <= btnList.length-1; i++ ) {
            DrawableCompat.setTint(btnList[i].getDrawable(), ContextCompat.getColor(ExerciseExecutionActivity.this, R.color.icons_black));
        }
    }
    private void finishExerciseAlert() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExerciseExecutionActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_alert_finish, null);

        Button mAccept = (Button) mView.findViewById(R.id.btn_accept);
        Button mDecline = (Button) mView.findViewById(R.id.btn_decline);

        mBuilder.setView(mView);
        final AlertDialog finishAlert = mBuilder.create();
        finishAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        finishAlert.show();

        //closes the dialog window when touched outside.
        finishAlert.setCanceledOnTouchOutside(true);

        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ExerciseExecutionActivity.this, "Dados salvos", Toast.LENGTH_SHORT).show();
                ExerciseExecutionActivity.super.onBackPressed();
            }
        });

        mDecline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finishAlert.hide();
            }
        });
    }

}

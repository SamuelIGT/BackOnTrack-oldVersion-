package br.ufc.quixada.backontrack.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

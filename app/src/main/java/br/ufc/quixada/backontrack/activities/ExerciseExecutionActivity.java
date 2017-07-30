package br.ufc.quixada.backontrack.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        setupTimer(this);

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

    private void setupTimer(final Context context) {
        pgBar.setVisibility(View.INVISIBLE);

        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chronometer.isRunning()) {
                    finish.setVisibility(View.INVISIBLE);

                    startStop.setText(R.string.button_stop);

                    pgBar.setVisibility(View.VISIBLE);
                    pgBarDrawable.start();

                    chronometer.startTimer(SystemClock.uptimeMillis());
                } else {
                    finish.setVisibility(View.VISIBLE);

                    startStop.setText(R.string.button_continue);

                    pgBar.setVisibility(View.INVISIBLE);
                    pgBarDrawable.stop();

                    chronometer.stopTimer();

                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "TESTE", Toast.LENGTH_SHORT).show();
            }
        });

    }



}

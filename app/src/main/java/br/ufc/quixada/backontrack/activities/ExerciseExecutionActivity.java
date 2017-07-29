package br.ufc.quixada.backontrack.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.progressBar.ProgressBarAnimation;

/**
 * Created by samue on 28/07/2017.
 */

public class ExerciseExecutionActivity extends AppCompatActivity {

    private List<String> steps;
    private TextView timer;
    private TextView txtViewSteps;
    private Button startStop;
    private ProgressBar pgBar;
    private ProgressBar pgBarFilled;

    int min, s, ms;
    private boolean isRunning;
    private long startTime, timeMS, timeSB, update;
    Handler h = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_execution);

        txtViewSteps = (TextView) findViewById(R.id.txt_steps);
        timer = (TextView) findViewById(R.id.timer);
        startStop = (Button) findViewById(R.id.start_stop);
       // pgBar = (ProgressBar) findViewById(R.id.pgBar);
       // pgBarFilled = (ProgressBar) findViewById(R.id.pgBar_filled);



 /*       ProgressBarAnimation anim = new ProgressBarAnimation(pgBar, 0, 100);
        anim.setDuration(1000);
        pgBar.startAnimation(anim);
*/
        setupSteps();

        setupTimer(this);

    }

    private void setupSteps() {
        steps = new ArrayList<String>();
        steps.add("Sente-se em uma cadeira.");
        steps.add("Coloque suas mãos na mesa.");
        steps.add("Empurre seu corpo para trás.");

        for (String step : steps) {
            txtViewSteps.setText(txtViewSteps.getText() + step + "\n");
        }
    }

    private void setupTimer(final Context context) {


        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    //pgBar.setVisibility(View.VISIBLE);
                    //pgBarFilled.setVisibility(View.INVISIBLE);

                    startTime = SystemClock.uptimeMillis();
                    h.postDelayed(r, 0);
                    isRunning = true;
                } else {
                   // pgBar.setVisibility(View.INVISIBLE);
                  //  pgBarFilled.setVisibility(View.VISIBLE);
                    timeSB += timeMS;
                    h.removeCallbacks(r);
                    isRunning = false;
                }
            }
        });

/*        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0;
                timeMS = 0;
                timeSB = 0;
                s = 0;
                min = 0;
                ms = 0;
                h.removeCallbacks(r);
                tv.setText("0:00:000");
            }
        });*/

    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            timeMS = SystemClock.uptimeMillis() - startTime;
            update = timeSB + timeMS;
            s = (int) (update / 1000);
            min = s / 60;
            s = s % 60;
            ms = (int)(((int)update % 1000) / 10);
            timer.setText("" + String.format("%02d", min) + ":" + String.format("%02d", s) + ":" + ms);
            h.postDelayed(this, 0);
        }
    };

}

package br.ufc.quixada.backontrack.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import br.ufc.quixada.backontrack.EffortButton;
import br.ufc.quixada.backontrack.ProgressBarAnimation;
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

    private Button startStop;
    private Button finish;
    private Button playWatch;

    private CircularProgressBar pgBar;
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
        playWatch = (Button) findViewById(R.id.btn_play);
        pgBar = (CircularProgressBar) findViewById(R.id.pgBar);
        pgBarDrawable = (CircularProgressDrawable) pgBar.getIndeterminateDrawable();
        chronometer = new Chronometer(timer, this);

        finish.setVisibility(View.INVISIBLE);
        setupSteps();

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

    public void setupSteps() {
        LinearLayout stepsLayout2 = (LinearLayout) findViewById(R.id.layout_steps_2);
        LinearLayout stepsLayout1 = (LinearLayout) findViewById(R.id.layout_steps_1);

        final List<Button> btnSteps = new ArrayList<>();
        final List<ProgressBar> stepsProgress = new ArrayList<>();

        //travel the entire stepsLayout1 children.
        for (int i = 0; i < stepsLayout1.getChildCount(); i++) {
            //if the child is a button
            if (stepsLayout1.getChildAt(i) instanceof Button) {
                btnSteps.add((Button) stepsLayout1.getChildAt(i));
            } else if (stepsLayout1.getChildAt(i) instanceof ProgressBar) {
                stepsProgress.add((ProgressBar) stepsLayout1.getChildAt(i));
            }
        }
        //travel the entire stepsLayout2 children.
        for (int i = 0; i < stepsLayout2.getChildCount(); i++) {
            //if the child is a button
            if (stepsLayout2.getChildAt(i) instanceof Button) {
                btnSteps.add((Button) stepsLayout2.getChildAt(i));
            } else if (stepsLayout2.getChildAt(i) instanceof ProgressBar) {
                stepsProgress.add((ProgressBar) stepsLayout2.getChildAt(i));
            }
        }

        //sets the OnClickListener
        for (int i = 0; i < btnSteps.size(); i++) {
            final int j = i;
            btnSteps.get(i).setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    btnSteps.get(j).setSelected(!btnSteps.get(j).isSelected());

                    //Handle selected state change
                    if (btnSteps.get(j).isSelected()) {
                        if (j + 1 == btnSteps.size()) {
                            for (int k = j; k >= 0; k--) {
                                if (!btnSteps.get(k).isSelected()) {
                                    btnSteps.get(k).setSelected(!btnSteps.get(k).isSelected());
                                }
                                ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(k), 0, 100);
                                anim.setDuration(1000);
                                stepsProgress.get(k).startAnimation(anim);
                            }
                            return;
                        }

                        if (j + 1 == (btnSteps.size() / 2)) {
                            ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(j + 1), 0, 100);
                            anim.setDuration(1000);
                            stepsProgress.get(j + 1).startAnimation(anim);
                        }

                        if (j + 1 > (btnSteps.size() / 2)) {
                            for (int k = j; k >= 0; k--) {
                                if (!btnSteps.get(k).isSelected()) {
                                    btnSteps.get(k).setSelected(!btnSteps.get(k).isSelected());
                                }
                                ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(k), 0, 100);
                                anim.setDuration(1000);
                                stepsProgress.get(k).startAnimation(anim);
                            }

                            ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(j + 1), 0, 100);
                            anim.setDuration(1000);
                            stepsProgress.get(j + 1).startAnimation(anim);
                        } else {
                            for (int k = j; k >= 0; k--) {
                                if (!btnSteps.get(k).isSelected()) {
                                    btnSteps.get(k).setSelected(!btnSteps.get(k).isSelected());
                                }
                                ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(k), 0, 100);
                                anim.setDuration(1000);
                                stepsProgress.get(k).startAnimation(anim);
                            }
                            ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(j), 0, 100);
                            anim.setDuration(1000);
                            stepsProgress.get(j).startAnimation(anim);
                        }

                        //Handle de-select state
                    } else {
                        if (j + 1 == btnSteps.size()) {

                            return;
                        }

                        if (j + 1 == (btnSteps.size() / 2)) {
                            ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(j + 1), 100, 0);
                            anim.setDuration(1000);
                            stepsProgress.get(j + 1).startAnimation(anim);
                        }

                        if (j + 1 > (btnSteps.size() / 2)) {
                            for (int k = j; k < btnSteps.size(); k++) {
                                if (btnSteps.get(k).isSelected()) {
                                    if (k == btnSteps.size() - 1) {
                                        btnSteps.get(k).setSelected(false);
                                        ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(k), 100, 0);
                                        anim.setDuration(1000);
                                        stepsProgress.get(k).startAnimation(anim);
                                    } else {
                                        btnSteps.get(k).setSelected(false);
                                        ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(k + 1), 100, 0);
                                        anim.setDuration(1000);
                                        stepsProgress.get(k + 1).startAnimation(anim);
                                    }
                                }
                            }

                            ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(j + 1), 100, 0);
                            anim.setDuration(1000);
                            stepsProgress.get(j + 1).startAnimation(anim);
                        } else {
                            for (int k = j; k < btnSteps.size(); k++) {
                                if (btnSteps.get(k).isSelected()) {
                                    if (k + 1 >= btnSteps.size() / 2) {
                                        if (k == btnSteps.size() - 1){
                                            btnSteps.get(k).setSelected(false);
                                        }else{
                                            ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(k + 1), 100, 0);
                                            anim.setDuration(1000);
                                            stepsProgress.get(k + 1).startAnimation(anim);
                                        }

                                    }

                                    btnSteps.get(k).setSelected(false);
                                    ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(k), 100, 0);
                                    anim.setDuration(1000);
                                    stepsProgress.get(k).startAnimation(anim);
                                }

                            }

                            ProgressBarAnimation anim = new ProgressBarAnimation(stepsProgress.get(j), 100, 0);
                            anim.setDuration(1000);
                            stepsProgress.get(j).startAnimation(anim);
                        }


                    }
                }
            });
        }



        /*btnStep1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btnStep1.setSelected(!btnStep1.isSelected());
                if (btnStep1.isSelected()) {
                    //Handle selected state change
                } else {
                    //Handle de-select state change
                }
            }
        });*/

    }
        /*Log.v("StepsLayout Children", ""+stepsLayout1.getChildCount());
        for( int i = 0; i < stepsLayout.getChildCount(); i++ ) {
            if (stepsLayout.getChildAt(i) instanceof Button) {
                Log.v("StepsLayout TextView", "" + i);
            }
        }*/

    //List<TextView> stepsView = new ArrayList<>();
    //stepsView.add((TextView)findViewById(R.));


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

        playWatch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                watchVideo();
            }
        });
    }

    private void watchVideo() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExerciseExecutionActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_video_lesson, null);

        VideoView mVideoView = (VideoView) mView.findViewById(R.id.video_view);
        String uriPath = "android.resource://br.ufc.quixada.backontrack/" + R.raw.test;

        mBuilder.setView(mView);
        final AlertDialog finishAlert = mBuilder.create();
        finishAlert.show();

        Uri uri2 = Uri.parse(uriPath);
        mVideoView.setVideoURI(uri2);
        mVideoView.requestFocus();
        mVideoView.start();

    }

    private void startTimer() {
        finish.setVisibility(View.INVISIBLE);

        startStop.setText(R.string.button_stop);

        pgBar.setVisibility(View.VISIBLE);
        pgBarDrawable.start();

        chronometer.startTimer(SystemClock.uptimeMillis());
    }

    private void stopTimer() {
        finish.setVisibility(View.VISIBLE);

        startStop.setText(R.string.button_continue);

        pgBar.setVisibility(View.INVISIBLE);
        pgBarDrawable.stop();

        chronometer.stopTimer();
    }

    private void finishExerciseConfirmation() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExerciseExecutionActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.exercise_finish_confirmation, null);

        Button btnConfirm = (Button) mView.findViewById(R.id.btn_confirm);
        ImageButton btnClose = (ImageButton) mView.findViewById(R.id.btn_close_confirmation);

        final List<EffortButton> btnList = new ArrayList<>();
        btnList.add(new EffortButton(1, (TextView) mView.findViewById(R.id.txt_emoticon_1), (ImageButton) mView.findViewById(R.id.ic_emotion_1)));
        btnList.add(new EffortButton(2, (TextView) mView.findViewById(R.id.txt_emoticon_2), (ImageButton) mView.findViewById(R.id.ic_emotion_2)));
        btnList.add(new EffortButton(3, (TextView) mView.findViewById(R.id.txt_emoticon_3), (ImageButton) mView.findViewById(R.id.ic_emotion_3)));
        btnList.add(new EffortButton(4, (TextView) mView.findViewById(R.id.txt_emoticon_4), (ImageButton) mView.findViewById(R.id.ic_emotion_4)));
        btnList.add(new EffortButton(5, (TextView) mView.findViewById(R.id.txt_emoticon_5), (ImageButton) mView.findViewById(R.id.ic_emotion_5)));

        TextView timeResult = (TextView) mView.findViewById(R.id.txt_time_result);
        timeResult.setText(timer.getText());
        final TextView motivation = (TextView) mView.findViewById(R.id.txt_motivation);
        motivation.setVisibility(View.INVISIBLE);
        /*TextView emotTitle1 = (TextView) mView.findViewById(R.id.txt_emoticon_1);
        TextView emotTitle2 = (TextView) mView.findViewById(R.id.txt_emoticon_2);
        TextView emotTitle3 = (TextView) mView.findViewById(R.id.txt_emoticon_3);
        TextView emotTitle4 = (TextView) mView.findViewById(R.id.txt_emoticon_4);
        TextView emotTitle5 = (TextView) mView.findViewById(R.id.txt_emoticon_5);*/

        /*final ImageButton btnlist [] = {(ImageButton)mView.findViewById(R.id.ic_emotion_1),
                (ImageButton) mView.findViewById(R.id.ic_emotion_2),
                (ImageButton) mView.findViewById(R.id.ic_emotion_3),
                (ImageButton) mView.findViewById(R.id.ic_emotion_4),
                (ImageButton) mView.findViewById(R.id.ic_emotion_5)};*/


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
                for (EffortButton btn : btnList) {
                    DrawableCompat.setTint(btn.getBtn().getDrawable(), ContextCompat.getColor(ExerciseExecutionActivity.this, R.color.icons_black));
                }
                finishAlert.hide();
            }
        });

        btnList.get(0).getBtn().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // setDefaultButton(btnList.get(0), btnList);
                btnList.get(0).select(ExerciseExecutionActivity.this, btnList, motivation);
            }
        });

        btnList.get(1).getBtn().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // setDefaultButton(btnList.get(1), btnList);
                btnList.get(1).select(ExerciseExecutionActivity.this, btnList, motivation);
            }
        });

        btnList.get(2).getBtn().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // setDefaultButton(btnList.get(2), btnList);
                btnList.get(2).select(ExerciseExecutionActivity.this, btnList, motivation);
            }
        });

        btnList.get(3).getBtn().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //setDefaultButton(btnList.get(3), btnList);
                btnList.get(3).select(ExerciseExecutionActivity.this, btnList, motivation);
            }
        });

        btnList.get(4).getBtn().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //setDefaultButton(btnList.get(4), btnList);
                btnList.get(4).select(ExerciseExecutionActivity.this, btnList, motivation);
            }
        });


    }

/*    public void setDefaultButton(EffortButton btn, List<EffortButton> btnList) {
        for (EffortButton eBtn : btnList) {
            if (eBtn.isSelected()) {
                if (eBtn.getLevel() != btn.getLevel()) {
                    eBtn.deselect(ExerciseExecutionActivity.this);
                }
            } else {
                if (eBtn.getLevel() != btn.getLevel())
                    eBtn.getTitle().setVisibility(View.INVISIBLE);
            }
        }
    }*/


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

package br.ufc.quixada.backontrack.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import br.ufc.quixada.backontrack.EffortButton;
import br.ufc.quixada.backontrack.ExerciseReport;
import br.ufc.quixada.backontrack.ProgressBarAnimation;
import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.StorageController;
import br.ufc.quixada.backontrack.chronometer.Chronometer;
import br.ufc.quixada.backontrack.model.Exercise;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by samue on 28/07/2017.
 */

public class ExerciseExecutionActivity extends AppCompatActivity {

    private StorageController storage;
    private Gson jsonParser = new GsonBuilder().create();

    private TextView timer;
    private Button startStop;
    private Button finish;
    private Button playWatch;

    private CircularProgressBar pgBar;
    private CircularProgressDrawable pgBarDrawable;
    private Chronometer chronometer;

    private Exercise exerc;
    private MediaPlayer musicPlayer;
    private List<Event> eventList;

    private ExerciseReport report;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_execution);

        storage = new StorageController();
        loadData();
        exerc.setId(1);
        report = new ExerciseReport(exerc.getId());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exercise_execution);
        toolbar.setTitle(exerc.getTitle());
        setSupportActionBar(toolbar);

        timer = (TextView) findViewById(R.id.timer);
        startStop = (Button) findViewById(R.id.btn_start_stop);
        finish = (Button) findViewById(R.id.btn_finish);
        playWatch = (Button) findViewById(R.id.btn_play);
        pgBar = (CircularProgressBar) findViewById(R.id.pgBar);
        pgBarDrawable = (CircularProgressDrawable) pgBar.getIndeterminateDrawable();
        chronometer = new Chronometer(timer, this);

        finish.setVisibility(View.INVISIBLE);
        setupSteps();

        setupScreen();

    }

    private void loadData() {
        exerc = (Exercise) getIntent().getSerializableExtra(getString(R.string.exercise_execution_extra));
        eventList = storage.getCalendar(getString(R.string.CALENDAR_KEY), this);
        if (eventList == null) {
            eventList = new ArrayList<>();
        }
    }

    private void setupScreen() {
        setupButtons();
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

        //sets the OnClickListener for all steps buttons
        for (int i = 0; i < btnSteps.size(); i++) {
            final int j = i;

            //hide steps buttons and bars according to the numbers of steps.
            if (i > exerc.getStepsAudio().size() - 1) {
                Log.v("step", "" + i);
                if (i == 4) {
                    stepsProgress.get(i - 1).setVisibility(View.GONE);
                }
                btnSteps.get(i).setVisibility(View.GONE);
                stepsProgress.get(i).setVisibility(View.GONE);
            }

            btnSteps.get(i).setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    btnSteps.get(j).setSelected(!btnSteps.get(j).isSelected());

                    stopAudioSteps();
                    musicPlayer = MediaPlayer.create(getApplicationContext(), exerc.getStepsAudio().get(j));
                    musicPlayer.start();


                    //Handle selected state change
                    if (btnSteps.get(j).isSelected()) {
                        for (int k = 0; k <= j; k++) {
                            if (k == 0) {
                                btnSteps.get(k).setSelected(true);
                            } else if (k > 3) {
                                if (stepsProgress.get(k).getProgress() == 0 || !btnSteps.get(k).isSelected()) {
                                    if (k == 4) {
                                        movePgBar(stepsProgress.get(k - 1), 0, 100);
                                    }
                                    btnSteps.get(k).setSelected(true);
                                    movePgBar(stepsProgress.get(k), 0, 100);
                                }
                            } else {
                                if (stepsProgress.get(k - 1).getProgress() == 0 || !btnSteps.get(k).isSelected()) {
                                    btnSteps.get(k).setSelected(true);
                                    movePgBar(stepsProgress.get(k - 1), 0, 100);
                                }
                            }
                        }
                    }
                    //Handle de-select state
                    else {
                        btnSteps.get(j).setSelected(true);


                        for (int k = j + 1; k <= btnSteps.size() - 1; k++) {
                            if (k == 0) {
                                btnSteps.get(k).setSelected(false);
                            } else if (k > 3) {
                                if (stepsProgress.get(k).getProgress() == 100 || btnSteps.get(k).isSelected()) {
                                    if (k == 4) {
                                        movePgBar(stepsProgress.get(k - 1), 100, 0);
                                    }
                                    btnSteps.get(k).setSelected(false);
                                    movePgBar(stepsProgress.get(k), 100, 0);
                                }
                            } else {
                                if (stepsProgress.get(k - 1).getProgress() == 100 || btnSteps.get(k).isSelected()) {
                                    btnSteps.get(k).setSelected(false);
                                    movePgBar(stepsProgress.get(k - 1), 100, 0);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public boolean stopAudioSteps() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.reset();
            musicPlayer.release();
            musicPlayer = null;
            return false;
        }
        return true;
    }

    public void movePgBar(ProgressBar pgBar, int start, int end) {
        ProgressBarAnimation anim = new ProgressBarAnimation(pgBar, start, end);
        anim.setDuration(1000);
        pgBar.startAnimation(anim);
    }

    private void setupButtons() {
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
        stopAudioSteps();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExerciseExecutionActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_video_lesson, null);
        mView.setBackground(null);

        final ImageButton btnPlay = (ImageButton) mView.findViewById(R.id.btn_play_video);
        btnPlay.setImageAlpha(180);

        final ImageView dimBehind = (ImageView) findViewById(R.id.view_cover_dialog);

        final VideoView mVideoView = (VideoView) mView.findViewById(R.id.video_view);
        String uriPath = "android.resource://br.ufc.quixada.backontrack/" + exerc.getVideoPath();

        mBuilder.setView(mView);
        final AlertDialog videoDialog = mBuilder.create();
        dimBehind.setVisibility(View.VISIBLE);
        videoDialog.show();

        WindowManager.LayoutParams a = videoDialog.getWindow().getAttributes();
        a.dimAmount = 0;
        videoDialog.getWindow().setAttributes(a);

        videoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dimBehind.setVisibility(View.GONE);
            }
        });

        Uri uri2 = Uri.parse(uriPath);
        mVideoView.setVideoURI(uri2);
        mVideoView.requestFocus();
        mVideoView.seekTo(100);

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (btnPlay.getImageAlpha() == 180) {
                    btnPlay.setImageAlpha(0);
                    mVideoView.start();
                } else {
                    btnPlay.setImageResource(R.drawable.ic_play);
                    btnPlay.setImageAlpha(180);
                    mVideoView.pause();

                }
            }
        });

        //Called when the video ends.
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                btnPlay.setImageResource(R.drawable.ic_replay);
                btnPlay.setImageAlpha(180);
                mVideoView.seekTo(100);
            }
        });


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
        stopAudioSteps();

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

        final TextView timeResult = (TextView) mView.findViewById(R.id.txt_time_result);
        timeResult.setText(timer.getText());
        final TextView motivation = (TextView) mView.findViewById(R.id.txt_motivation);
        motivation.setVisibility(View.INVISIBLE);

        mBuilder.setView(mView);
        final AlertDialog finishAlert = mBuilder.create();
        finishAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        finishAlert.show();

        //closes the dialog window when touched outside.
        finishAlert.setCanceledOnTouchOutside(true);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(ExerciseExecutionActivity.this, "Confirmado!", Toast.LENGTH_SHORT).show();
                if (report.getEffort() > 0) {

                    //-------Send data back to the previous activity---|
                    Intent intent = new Intent();
                    String dataResult[] = {exerc.getId().toString(), statusBuilder(timeResult.getText().toString())};
                    intent.putExtra("EXERCISE_RESULT", dataResult);
                    setResult(RESULT_OK, intent);
                    //-------------------------------------------------|
                    saveData();

                    finishAlert.dismiss();
                    resetFinishConfirmationDialog(btnList);

                    finish();
                }else{
                    Toast.makeText(ExerciseExecutionActivity.this, "Selecione um esforço!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resetFinishConfirmationDialog(btnList);
                finishAlert.hide();
            }
        });

        for (int i = 0; i < btnList.size(); i++) {
            final int j = i;
            btnList.get(i).getBtn().setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    // setDefaultButton(btnList.get(0), btnList);
                    report.setEffort(j + 1);
                    btnList.get(j).select(ExerciseExecutionActivity.this, btnList, motivation);
                }
            });
        }
    }

    private String statusBuilder(String timer) {
        String[] timeText = timer.split(":");
        int[] timeInt = {Integer.parseInt(timeText[0]), Integer.parseInt(timeText[1]), Integer.parseInt(timeText[2])};
        if (timeInt[0] < 1) {
            if (timeInt[1] < 30) {
                return "UNFINISHED";
            }
        }
        return "DONE";
    }

    public void resetFinishConfirmationDialog(List<EffortButton> btnList) {
        for (EffortButton btn : btnList) {
            DrawableCompat.setTint(btn.getBtn().getDrawable(), ContextCompat.getColor(ExerciseExecutionActivity.this, R.color.icons_black));
        }
    }

    public void saveData() {
        long currentDate = System.currentTimeMillis();

        report.setTime(timer.getText().toString());
        report.setDateMillis(currentDate);
        report.setExerciseTitle(exerc.getTitle());

        Event ev1 = new Event(ContextCompat.getColor(this, R.color.colorPrimary), currentDate, jsonParser.toJson(report));
        Log.v("getTimeInMillis Test", "" + currentDate);
        eventList.add(ev1);

        StorageController storage = new StorageController();
        storage.saveCalendar(getString(R.string.CALENDAR_KEY), eventList, this);
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
        stopAudioSteps();

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
                finishAlert.dismiss();
                finishExerciseConfirmation();
//              Toast.makeText(ExerciseExecutionActivity.this, "Dados salvos", Toast.LENGTH_SHORT).show();

/*//-------------------TESTING SEND DATA BACK TO THE PREVIOUS ACTIVITY----|
                Intent intent = new Intent();
                String dataResult[] = {exerc.getId().toString(), "DONE"};
                intent.putExtra("exercise_conclusion", dataResult);
                setResult(RESULT_OK, intent);
//----------------------------------------------------------------------|
                saveData();

                finishAlert.dismiss();
                ExerciseExecutionActivity.super.onBackPressed();*/
            }
        });

        mDecline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finishAlert.hide();
            }
        });
    }

    private void saveSession() {
        SharedPreferences settings = this.getSharedPreferences("TESTE", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Usuario", "Teste");
        // Commit the edits!
        editor.commit();
    }

}

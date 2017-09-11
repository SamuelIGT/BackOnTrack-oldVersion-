package br.ufc.quixada.backontrack.Fragments;

/**
 * Created by samue on 11/08/2017.
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import br.ufc.quixada.backontrack.ExerciseReport;
import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.StorageController;

public class CalendarFragment extends Fragment {
    private View rootView;
    private CompactCalendarView calendarView;
    private boolean isCreated = false;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private TextView txtMothYear;
    private StorageController storage;
    private List<Event> eventList;
    private Gson jsonParser = new GsonBuilder().create();

    private Animation slideAnim;
    private TextView txtvDayActivityTitle;
    private TextView txtvDayActivityTime;
    private ImageButton btnNextDayActivity;
    private ImageButton btnPreviousDayActivity;
    private LinearLayout dayActivityLayout;

    private int index = 0;
    private ExerciseReport report;

    public CalendarFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        storage = new StorageController();
        calendarView = (CompactCalendarView) rootView.findViewById(R.id.view_calendar);
        txtMothYear = (TextView) rootView.findViewById(R.id.txt_month_year);
        btnNext = (ImageButton) rootView.findViewById(R.id.btn_next_month);
        btnPrevious = (ImageButton) rootView.findViewById(R.id.btn_previous_month);

        setupCalendar();
        setupGraph();

        isCreated = true;

        return rootView;
    }

    private void setupGraph() {
        List<ProgressBar> pBarsWeek = new ArrayList<>();
        pBarsWeek.add((ProgressBar) rootView.findViewById(R.id.calendar_pgBar_1));
        pBarsWeek.add((ProgressBar) rootView.findViewById(R.id.calendar_pgBar_2));
        pBarsWeek.add((ProgressBar) rootView.findViewById(R.id.calendar_pgBar_3));
        pBarsWeek.add((ProgressBar) rootView.findViewById(R.id.calendar_pgBar_4));
        pBarsWeek.add((ProgressBar) rootView.findViewById(R.id.calendar_pgBar_5));
        pBarsWeek.add((ProgressBar) rootView.findViewById(R.id.calendar_pgBar_6));
        pBarsWeek.add((ProgressBar) rootView.findViewById(R.id.calendar_pgBar_7));


        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        dt = c.getTime();
        while (!dt.toString().split(" ")[0].equals("Sun")) {
            //Log.d("TESTE","Esperado:Sun"+ dt.toString().split(" ")[0]);
            c.add(Calendar.DATE, -1);
            dt = c.getTime();
        }
        List<Event> eventsDay;
        for (int i = 0; i < 7; i++) {
            Log.d("Calendar graph", "for " + c.getTime().toString());
            eventsDay = calendarView.getEvents(c.getTime());
            if (eventsDay.size() >= 1) {
                Log.d("Calendar graph", "IF " + eventsDay.get(0).toString());
                int sum = 0;
                int count = 0;
                for (Event event : eventsDay) {
                    ExerciseReport report = jsonParser.fromJson(event.getData().toString(), ExerciseReport.class);
                    sum += report.getEffort();
                    count++;
                }
                if (count > 0) {
                    pBarsWeek.get(i).setProgress((sum / count) * 10);
                }
            } else {
                Log.d("TESTE", "Nenhum evento");
                pBarsWeek.get(i).setProgress(0);
            }
            c.add(Calendar.DATE, 1);
        }
        Log.d("TESTE", "Quarta Progresso" + pBarsWeek.get(3).getProgress());
    }

    private void setupCalendar() {

        final Calendar c = Calendar.getInstance();
        Locale brLocale = new Locale("PT", "BR");
        final SimpleDateFormat df = new SimpleDateFormat("MMM - yyyy", brLocale);
        // df.getDateFormatSymbols().setMonths(uppercaseMonths);
        final String formattedDate = df.format(c.getTime());

        calendarView.setLocale(TimeZone.getTimeZone("GMT-03:00"), brLocale);
        calendarView.setUseThreeLetterAbbreviation(true);
        calendarView.shouldScrollMonth(false);
        calendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        getCalendarEvents();
            /*
        DateFormatSymbols symbols = new DateFormatSymbols(brLocale);

        String[] uppercaseMonths = {
                "Jan", "Fev",
                "Mar", "Abr", "Mai",
                "Jun", "Jul", "Ago",
                "Set", "Out", "Nov",
                "Dez"
        };f
        */
        txtMothYear.setText(formattedDate);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.MONTH, 1);
                txtMothYear.setText(df.format(c.getTime()));
                calendarView.showNextMonth();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.MONTH, -1);
                txtMothYear.setText(df.format(c.getTime()));
                calendarView.showPreviousMonth();
            }
        });

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events;
                events = calendarView.getEvents(dateClicked);
                if (events.size() > 0) {
                    showDayEvents(events);
                } else {
                    dayActivityLayout = (LinearLayout) rootView.findViewById(R.id.layout_day_exercise);
                    showDayEventAnimation(false);
                }


                //Adds an event on the day clicked
                /*long currentDate = dateClicked.getTime();
                Event ev1 = new Event(ContextCompat.getColor(rootView.getContext(), R.color.colorPrimary), currentDate);
                Log.v("getTimeInMillis Test",""+currentDate);
                calendarView.addEvent(ev1, false);*/
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });
    }

    private void showDayEvents(final List<Event> events) {
        dayActivityLayout = (LinearLayout) this.rootView.findViewById(R.id.layout_day_exercise);
        showDayEventAnimation(true);

        txtvDayActivityTitle = (TextView) this.rootView.findViewById(R.id.txt_day_exercise_title);
        txtvDayActivityTime = (TextView) this.rootView.findViewById(R.id.txt_day_exercise_time);
        btnNextDayActivity = (ImageButton) this.rootView.findViewById(R.id.btn_day_next_exercise);
        btnPreviousDayActivity = (ImageButton) this.rootView.findViewById(R.id.btn_day_previous_exercise);

        index = 0;
        this.report = jsonParser.fromJson(eventList.get(index).getData().toString(), ExerciseReport.class);

        txtvDayActivityTitle.setText(report.getExerciseTitle());
        txtvDayActivityTime.setText(report.getTime());

        btnNextDayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if (index >= 0) {
                    if (index < events.size()) {
                        report = jsonParser.fromJson(events.get(index).getData().toString(), ExerciseReport.class);
                        if (report != null) {
                            txtvDayActivityTitle.setText(report.getExerciseTitle());
                            txtvDayActivityTime.setText(String.valueOf(report.getTime()));
                        }
                    } else
                        index = events.size() - 1;
                } else
                    index = 0;
            }
        });

        btnPreviousDayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                if (index >= 0) {
                    if (index < events.size()) {
                        report = jsonParser.fromJson(events.get(index).getData().toString(), ExerciseReport.class);
                        if (report != null) {
                            txtvDayActivityTitle.setText(report.getExerciseTitle());
                            txtvDayActivityTime.setText(String.valueOf(report.getTime()));
                        }
                    } else
                        index = events.size() - 1;
                } else
                    index = 0;
            }
        });
    }

    //true = show animation | false = hide animation
    private void showDayEventAnimation(boolean showOrHide) {
        if (showOrHide) {
            //prepares the animation
            slideAnim = AnimationUtils.loadAnimation(this.getActivity().getApplicationContext(), R.anim.move_right);
            slideAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    dayActivityLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }else{
            slideAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.move_left);
            slideAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                //hides the layout when the animation is done
                @Override
                public void onAnimationEnd(Animation animation) {
                    dayActivityLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        dayActivityLayout.startAnimation(slideAnim); // starts the animation
    }

    private void getCalendarEvents() {
        List<Event> newEventList;
        newEventList = storage.getCalendar(getString(R.string.CALENDAR_KEY), rootView.getContext());
        if (eventList == null) {
            Log.d("EVENT_LIST", "EMPTY EVENT LIST");
            eventList = new ArrayList<>();
        } else if (newEventList != eventList) {
            eventList = newEventList;
            Log.d("EVENT_LIST", "EVENT LIST ELSE");
            calendarView.addEvents(eventList);
        }
    }

    @Override
    public void onStop() {
        Log.v("DEBUG_ON_STOP_CALENDAR", "ON STOP TRIGGERED");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.v("DEBUG_ON_PAUSE_CALENDAR", "ON PAUSE TRIGGERED");
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.v("DEBUG_ON_HIDE_CALENDAR", "ON HIDDEN CHANGED TRIGGERED");
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isCreated) {
            if (isVisibleToUser) {
                Log.v("DEBUG_VISIBLE_CALENDAR", "VISIBLE: " + isVisibleToUser);
                calendarShowAnimation();
                getCalendarEvents();
                setupGraph();
            } else {
                dayActivityLayout = (LinearLayout) rootView.findViewById(R.id.layout_day_exercise);
                dayActivityLayout.setVisibility(View.GONE);
            }
        }
    }

    public void calendarShowAnimation() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                calendarView.showCalendarWithAnimation();
            }
            //Do something after 100ms
        }, 100);
    }
    //Example of how to format a locale
    /*String[] capitalDays = {
            "", "SUN", "MON",
            "TUE", "WED", "THU",
            "FRI", "SAT"
    };
    symbols = new DateFormatSymbols( new Locale("en", "US"));
symbols.setShortWeekdays(capitalDays);*/

}

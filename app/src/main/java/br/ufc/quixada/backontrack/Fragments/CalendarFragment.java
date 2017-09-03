package br.ufc.quixada.backontrack.Fragments;

/**
 * Created by samue on 11/08/2017.
 */

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

        isCreated = true;

        return rootView;
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
        };
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
                List<Event> events = new ArrayList<>();
                events = calendarView.getEvents(dateClicked);
                if (events.size() > 0) {
                    showDayEvents(events);
                }else{
                    dayActivityLayout = (LinearLayout) rootView.findViewById(R.id.layout_day_exercise);
                    dayActivityLayout.setVisibility(View.GONE);
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
        dayActivityLayout.setVisibility(View.VISIBLE);

        txtvDayActivityTitle = (TextView) this.rootView.findViewById(R.id.txt_day_exercise_title);
        txtvDayActivityTime = (TextView) this.rootView.findViewById(R.id.txt_day_exercise_time);
        btnNextDayActivity = (ImageButton) this.rootView.findViewById(R.id.btn_day_next_exercise);
        btnPreviousDayActivity = (ImageButton) this.rootView.findViewById(R.id.btn_day_previous_exercise);


        this.report = jsonParser.fromJson(eventList.get(index).getData().toString(), ExerciseReport.class);

        txtvDayActivityTitle.setText(report.getExerciseTitle());
        txtvDayActivityTime.setText(report.getTime());


        btnNextDayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if (index >= 0) {
                    if (index < events.size()) {
                        report = jsonParser.fromJson(eventList.get(index).getData().toString(), ExerciseReport.class);
                        if (report != null) {
                            txtvDayActivityTitle.setText(report.getExerciseTitle());
                            txtvDayActivityTime.setText(String.valueOf(report.getTime()));
                        }
                    } else
                        index = events.size();
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
                        report = jsonParser.fromJson(eventList.get(index).getData().toString(), ExerciseReport.class);
                        if (report != null) {
                            txtvDayActivityTitle.setText(report.getExerciseTitle());
                            txtvDayActivityTime.setText(String.valueOf(report.getTime()));
                        }
                    } else
                        index = events.size();
                } else
                    index = 0;
            }
        });
    }

    private void getCalendarEvents() {
        eventList = storage.getCalendar(getString(R.string.CALENDAR_KEY), rootView.getContext());
        if (eventList == null) {
            Log.d("EVENT_LIST", "EMPTY EVENT LIST");
            eventList = new ArrayList<>();
        }
        calendarView.addEvents(eventList);
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
        Log.v("DEBUG_VISIBLE_CALENDAR", "VISIBLE HINT TRIGGERED: " + isVisibleToUser);
        if (isCreated) {
            if (isVisibleToUser) {
                calendarShowAnimation();
                getCalendarEvents();
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

    @Override
    public void onStart() {

        Log.v("DEBUG_ONSTART_CALENDAR", "ON START TRIGGERED");
        super.onStart();

    }

    @Override
    public void onResume() {
        Log.v("DEBUG_ONRESUME_CALENDAR", "ON RESUME TRIGGERED");
        super.onResume();
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

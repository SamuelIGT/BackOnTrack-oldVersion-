package br.ufc.quixada.backontrack.Fragments;

/**
 * Created by samue on 11/08/2017.
 */

import android.icu.text.CompactDecimalFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import br.ufc.quixada.backontrack.R;

public class CalendarFragment extends Fragment {
    private View rootView;
    private CompactCalendarView calendarView;
    private boolean isCreated = false;

    public CalendarFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        isCreated = true;


        calendarView = (CompactCalendarView) rootView.findViewById(R.id.view_calendar);
        Locale brLocale = new Locale("PT", "BR");
        calendarView.setLocale(TimeZone.getTimeZone("GMT-03:00"), brLocale );
        calendarView.setUseThreeLetterAbbreviation(true);
        calendarView.shouldScrollMonth(false);




        final TextView txtMothYear = (TextView) rootView.findViewById(R.id.txt_month_year);

        final Calendar c = Calendar.getInstance();
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
        final SimpleDateFormat df = new SimpleDateFormat("MMM - yyyy", brLocale);
       // df.getDateFormatSymbols().setMonths(uppercaseMonths);
        final String formattedDate = df.format(c.getTime());

        txtMothYear.setText(formattedDate);

        ImageButton btnNext = (ImageButton) rootView.findViewById(R.id.btn_next_month);
        ImageButton btnPrevious = (ImageButton) rootView.findViewById(R.id.btn_previous_month);


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
                long currentDate = dateClicked.getTime();
                Event ev1 = new Event(ContextCompat.getColor(rootView.getContext(), R.color.colorPrimary), currentDate);
                Log.v("getTimeInMillis Test",""+currentDate);
                calendarView.addEvent(ev1, false);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });


        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isCreated) {
            if (isVisibleToUser) {
            } else {
                calendarView.showCalendarWithAnimation();

            }
        }
    }

    @Override
    public void onStart() {

        calendarView.showCalendarWithAnimation();

        super.onStart();

    }

    @Override
    public void onStop() {
        calendarView.hideCalendarWithAnimation();
        super.onStop();
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

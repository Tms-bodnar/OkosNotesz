package okosnotesz.hu.okosnotesz;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import okosnotesz.hu.okosnotesz.adapters.GridAdapter;
import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Reports;

public class CalendarActivity extends Fragment {

    private TextView nextMonth;
    private TextView prevMonth;
    private TextView currDate;
    private GridView calGrid;
    private Calendar cal = Calendar.getInstance();

    private final int MAX_CALENDAR_CELLS = 42;
    private GridAdapter gridAdapter;
    private ViewGroup container;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.container = container;
        View v = initUiLayout();
        setupCalAdapter();
        setPrevMonth();
        setNextMonth();

        return v;

    }

    private void setNextMonth() {
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setupCalAdapter();
            }
        });
    }

    private void setPrevMonth() {
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setupCalAdapter();
            }
        });
    }

    private void setupCalAdapter() {

        List<Date> dayValueInCells = new ArrayList<Date>();
        List<Reports> reportsList = ListHelper.getAllReports(getContext());
        Calendar mCal = (Calendar)cal.clone();
        //mCal.setFirstDayOfWeek(Calendar.MONDAY);
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK)+4;
        Log.d("xxxx", firstDayOfTheMonth+"firstof month");
        mCal.set(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        Log.d("xxxx", "firstday: " +mCal.getFirstDayOfWeek()+ ", dayofweek: "+ mCal.get(Calendar.DAY_OF_WEEK)+
                ", dayofw inMonth: " + mCal.get(Calendar.DAY_OF_WEEK_IN_MONTH) + "dayofmonth "+mCal.get(Calendar.DAY_OF_MONTH));
        while(dayValueInCells.size() < MAX_CALENDAR_CELLS){
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        String sDate = formatter.format(cal.getTime());
        currDate.setText(sDate);
        gridAdapter = new GridAdapter(getContext(), dayValueInCells, cal, reportsList);
        calGrid.setAdapter(gridAdapter);
    }

 /*       List<Date> dayCells = new ArrayList<>();
        List<Reports> reportsList = ListHelper.getAllReports(getContext());
        Calendar tempCal = (Calendar) cal.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
     // tempCal.set(Calendar.DAY_OF_WEEK, tempCal.get(Calendar.DAY_OF_WEEK)+1);
        Log.d("xxxx",tempCal.get(Calendar.DAY_OF_MONTH)+"dayofmonth");
        int dayOfMonth = tempCal.get(Calendar.DAY_OF_MONTH);
        Log.d("xxxx", tempCal.get(Calendar.WEEK_OF_YEAR)+ "weekofyear");
        Log.d("xxxx", tempCal.get(Calendar.WEEK_OF_MONTH)+"weekofmonth");
        int dayOfWeekInMonth =  tempCal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        Log.d("xxxx", tempCal.get(Calendar.DAY_OF_WEEK_IN_MONTH)+ "dayofweekinmonth");
        int firstDayOfMonth = tempCal.get(Calendar.DAY_OF_WEEK)-1;
        int dayOfWeek = 7-(dayOfWeekInMonth - firstDayOfMonth);
        Log.d("xxxx", firstDayOfMonth+"firstdayofmonth");
        tempCal.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

            if (dayOfWeek!= 7) {
                Log.d("xxxx", dayOfWeek+": dayofweek activity");
                Calendar prevMonthDays = (GregorianCalendar) cal.clone();
                prevMonthDays.set(Calendar.DAY_OF_MONTH, prevMonthDays.getActualMaximum(Calendar.DAY_OF_MONTH)-dayOfWeek);
                prevMonthDays.add(Calendar.MONTH, -1);
                for (int i = dayOfWeek; i > 0; i--) {
                    Log.d("xxxx", i+" i");
                    dayCells.add(prevMonthDays.getTime());
                    prevMonthDays.add(Calendar.DATE, +1);
                    Log.d("xxxx",prevMonthDays.get(Calendar.MONTH)+": Month Activity");

                }
            }
            while(dayCells.size() <MAX_CALENDAR_CELLS) {
                dayCells.add(tempCal.getTime());
                tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        for (Date d : dayCells) {
        Log.d("xxxx", d.toString());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy, MM");
        Date d = cal.getTime();
        String currentDate = formatter.format(d);
        currDate.setText(currentDate);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        gridAdapter = new GridAdapter(getContext(), dayCells, cal, reportsList, dm);
        calGrid.setAdapter(gridAdapter);
    }*/

    private View initUiLayout() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_view, null);
        nextMonth = (TextView) v.findViewById(R.id.next_month);
        prevMonth = (TextView) v.findViewById(R.id.previous_month);
        currDate = (TextView) v.findViewById(R.id.display_current_date);
        calGrid = (GridView) v.findViewById(R.id.calendar_grid);
        return v;
    }


}


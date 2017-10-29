package okosnotesz.hu.okosnotesz;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
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
    private Calendar cal = Calendar.getInstance(Locale.FRANCE);
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
        return v;

    }

    private void setupCalAdapter() {
        List<Date> dayCells = new ArrayList<>();
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
                Log.d("xxxx", dayOfWeek+"dayofweek");
                for (int i = dayOfWeek; i > 0; i--) {
                    Log.d("xxxx", i+" i");
                    Calendar prevMonthDays = (Calendar) cal.clone();
                    dayCells.add(prevMonthDays.getTime());
                    prevMonthDays.add(Calendar.DATE, -i);

                }
            }
            while(dayCells.size() <MAX_CALENDAR_CELLS) {
                dayCells.add(tempCal.getTime());
                tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        for (Date d : dayCells) {
        //Log.d("xxxx", d.toString());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy, MM");
        Date d = cal.getTime();
        String currentDate = formatter.format(d);
        currDate.setText(currentDate);
        gridAdapter = new GridAdapter(getContext(), dayCells, cal, reportsList);
        calGrid.setAdapter(gridAdapter);

    }

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

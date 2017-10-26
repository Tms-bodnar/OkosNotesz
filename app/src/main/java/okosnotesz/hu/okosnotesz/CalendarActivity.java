package okosnotesz.hu.okosnotesz;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okosnotesz.hu.okosnotesz.adapters.GridAdapter;
import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Reports;

public class CalendarActivity extends LinearLayout {

    private Context context;
    private TextView nextMonth;
    private TextView prevMonth;
    private TextView currDate;
    private GridView calGrid;
    private Calendar cal = Calendar.getInstance();
    private final int MAX_CALENDAR_CELLS = 42;
    private GridAdapter gridAdapter;

    public CalendarActivity(Context context) {
        super(context);
    }

    public CalendarActivity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CalendarActivity(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        initUiLayout();
        setupCalAdapter();
    }

    private void setupCalAdapter() {
        List<Date> dayCells = new ArrayList<>();
        List<Reports> reportsList = ListHelper.getAllReports(getContext());
        Calendar tempCal = (Calendar) cal.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = tempCal.get(Calendar.DAY_OF_WEEK)-1;
        tempCal.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
        while(dayCells.size() <MAX_CALENDAR_CELLS){
            dayCells.add(tempCal.getTime());
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy, MM");
        String currentDate = formatter.format(cal);
        currDate.setText(currentDate);
        gridAdapter = new GridAdapter(context, dayCells, cal, reportsList);
        calGrid.setAdapter(gridAdapter);

    }

    private void initUiLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_view, this);
        nextMonth = (TextView) v.findViewById(R.id.next_month);
        prevMonth = (TextView) v.findViewById(R.id.previous_month);
        currDate = (TextView) v.findViewById(R.id.display_current_date);
        calGrid = (GridView) v.findViewById(R.id.calendar_grid);
        
    }
}

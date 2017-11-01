package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.Reports;

/**
 * Created by user on 2017.10.26..
 */

public class GridAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context mContext;
    List<Date> datesList;
    Calendar currentDate;
    List<Reports> allReports;
    int reqCode;


    public GridAdapter(Context context, List<Date> datesList, Calendar currentDate, List<Reports> allReports) {
        this.mContext = context;
        this.datesList = datesList;
        this.currentDate = currentDate;
        this.allReports = allReports;
        this.reqCode = reqCode;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Date mDate = datesList.get(position);
                Calendar mCal = Calendar.getInstance();
                mCal.setTime(mDate);
                int dayValue = mCal.get(Calendar.DAY_OF_MONTH);
                final int monthValue = mCal.get(Calendar.MONTH) + 1;
                int yearValue = mCal.get(Calendar.YEAR);
                int currentMonth = currentDate.get(Calendar.MONTH) + 1;
                int currentYear = currentDate.get(Calendar.YEAR);
                View monthView = convertView;
                if (monthView == null) {
                    monthView = inflater.inflate(R.layout.custom_calendar_grid_item, parent, false);

                }
                if (monthValue == currentMonth && yearValue == currentYear) {
                    monthView.setBackgroundColor(Color.WHITE);
                    if (mCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || mCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        monthView.setBackgroundColor(Color.parseColor("#E8EAF6"));
                    }
                } else {
                    monthView.setBackgroundColor(Color.parseColor("#5C6BC0"));
                    if (mCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || mCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        monthView.setBackgroundColor(Color.parseColor("#9FA8DA"));
                    }
                }
                TextView cellDate = (TextView) monthView.findViewById(R.id.calendar_date_id);
                cellDate.setText(String.valueOf(dayValue));
                Calendar eventCal = Calendar.getInstance();
                for (int i = 0; i < allReports.size(); i++) {
                    eventCal.setTimeInMillis(allReports.get(position).getDate());
                    cellDate.setText(eventCal.getTime().toString());
                    if (dayValue == eventCal.get(Calendar.DAY_OF_MONTH) && monthValue == eventCal.get(Calendar.MONTH) + 1
                            && yearValue == eventCal.get(Calendar.YEAR)) {
                    }
                }
                return monthView;


    }

    @Override
    public int getCount() {

            return datesList.size();

    }

    @Nullable
    @Override
    public Object getItem(int position) {

            return datesList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}


package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.Reports;

/**
 * Created by user on 2017.10.26..
 */

public class GridAdapter extends ArrayAdapter{
    LayoutInflater inflater;
    Context mContext;
    List<Date> monthlyDates;
    Calendar currentDate;
    List<Reports> allReports;

    public GridAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
    public GridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<Reports> allReports) {
        super(context, R.layout.custom_calendar_grid_item);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allReports = allReports;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        Calendar mCal = Calendar.getInstance();
        mCal.setTime(mDate);
        int dayValue = mCal.get(Calendar.DAY_OF_MONTH);
       // Log.d("xxxx", dayValue+"dayvalue");
        int monthValue = mCal.get(Calendar.MONTH) + 1;
        int yearValue = mCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        View v = convertView;
        if(v == null){
            v =  inflater.inflate(R.layout.custom_calendar_grid_item, parent,false);
        }
        if(monthValue == currentMonth && yearValue == currentYear){
            v.setBackgroundColor(Color.parseColor("#5C6BC0"));
        }else{
            v.setBackgroundColor(Color.WHITE);
        }
        TextView cellDate = (TextView) v.findViewById(R.id.calendar_date_id);
        cellDate.setText(String.valueOf(dayValue));
        Calendar eventCal = Calendar.getInstance();
        for (int i = 0; i < allReports.size(); i++){
            eventCal.setTimeInMillis(allReports.get(position).getDate());
            cellDate.setText(eventCal.getTime().toString());
            if(dayValue == eventCal.get(Calendar.DAY_OF_MONTH) && monthValue == eventCal.get(Calendar.MONTH)+1
                && yearValue == eventCal.get(Calendar.YEAR)){

            }
        }
        return v;
    }
    @Override
    public int getCount() {
        return monthlyDates.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }
    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }
}


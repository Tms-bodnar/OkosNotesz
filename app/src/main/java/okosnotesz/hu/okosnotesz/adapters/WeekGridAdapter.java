package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import okosnotesz.hu.okosnotesz.fragments.CalendarActivity;
import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.Reports;

/**
 * Created by user on 2017.10.31..
 */

public class WeekGridAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Reports> allReports;
    private CalendarActivity.Hours[] hoursArray;
    private LayoutInflater inflater;
    private String hour;
    private String minute;


    public WeekGridAdapter(Context context, CalendarActivity.Hours[] array, List<Reports> allReports){
        this.mContext = context;
        this.hoursArray = array;
        this.allReports = allReports;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return hoursArray.length;
    }

    @Override
    public Object getItem(int position) {
        return hoursArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View weekView = convertView;
        if (weekView == null) {
            weekView = inflater.inflate(R.layout.custom_calendar_week_grid_item, parent, false);
        }
        TextView dailyHour = (TextView) weekView.findViewById(R.id.tv_week_hour);
        if(hoursArray[position].getHour()<10){
            hour= "0"+String.valueOf(hoursArray[position].getHour());
        }else{
            hour = String.valueOf(hoursArray[position].getHour());
        }
        if(hoursArray[position].getMinute()<1){
            minute = "0"+String.valueOf(hoursArray[position].getMinute());
        }else{
            minute = String.valueOf(hoursArray[position].getMinute());
        }
        dailyHour.setText(hour + ":" + minute);
        return weekView;
    }
}

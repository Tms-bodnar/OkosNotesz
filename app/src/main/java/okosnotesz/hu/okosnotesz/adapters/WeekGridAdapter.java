package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.fragments.CalendarActivity;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Reports;
import okosnotesz.hu.okosnotesz.model.Treatments;

/**
 * Created by user on 2017.10.31..
 */

public class WeekGridAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Reports> dailyReports;
    private CalendarActivity.Hours[] hoursArray;
    private LayoutInflater inflater;
    private String hour;
    private String minute;
    private String startHour = "";
    private String endHour;
    private ViewHolder holder;


    public WeekGridAdapter(Context context, CalendarActivity.Hours[] array, List<Reports> dailyReports) {
        this.mContext = context;
        this.hoursArray = array;
        this.dailyReports = dailyReports;
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
            holder = new ViewHolder();
            holder.weekHour = (TextView) weekView.findViewById(R.id.tv_week_hour);
            weekView.setTag(holder);
        }

        if (hoursArray[position].getHour() < 10) {
            hour = "0" + String.valueOf(hoursArray[position].getHour());
        } else {
            hour = String.valueOf(hoursArray[position].getHour());
        }
        if (hoursArray[position].getMinute() < 1) {
            minute = "0" + String.valueOf(hoursArray[position].getMinute());
        } else {
            minute = String.valueOf(hoursArray[position].getMinute());
        }

        holder.weekHour.setText(hour + ":" + minute);
        for (int i = 0; i < dailyReports.size(); i++) {
            Date tempRep = new Date(dailyReports.get(i).getDate());
            Calendar calRep = Calendar.getInstance();
            calRep.setTime(tempRep);
            int stHour = calRep.get(Calendar.HOUR_OF_DAY);
            int startMinute = calRep.get(Calendar.MINUTE);
            String treatments[] = (dailyReports.get(i).getTreatment()).split(",");
            int treatmentPeriod = 0;
            for (int j = 0; j < treatments.length; j++) {
                treatments[j] = treatments[j].trim();
                Treatments t = null;
                if (!treatments[j].isEmpty() || treatments[j].length() > 0) {
                    t = ListHelper.getTreatment(Integer.valueOf(treatments[j]), mContext);
                }
                if (t != null) {
                    treatmentPeriod = treatmentPeriod + t.getTime();
                }
            }
            int ndHour = treatmentPeriod / 60 + stHour;

            int endMinute = (startMinute != 30) ? treatmentPeriod % 60 : treatmentPeriod % 60 + startMinute;


           // Log.d("wxx", calRep.get(Calendar.DAY_OF_MONTH) + ": day");
           // Log.d("wxx", treatmentPeriod + "period");
          //  Log.d("wxx", stHour + "-" + ndHour + " hour" + startMinute + "-" + endMinute + ": minute");
            startHour = String.valueOf(stHour);
            endHour = String.valueOf(ndHour);

        }

            if (startHour.equals(String.valueOf(hoursArray[position].getHour()))) {
                Log.d("wxx", String.valueOf(hoursArray[position].getHour()) + "gethour, " + startHour + ": starthour, pos: " + position);
                holder.weekHour.setBackgroundColor(Color.GRAY);
            }
        return weekView;
    }
    class ViewHolder{
        TextView weekHour;
    }
}

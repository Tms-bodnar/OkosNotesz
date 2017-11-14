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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.Reports;
import okosnotesz.hu.okosnotesz.model.Treatments;

/**
 * Created by user on 2017.10.26..
 */

public class GridAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context mContext;
    List<Date> datesList;
    Calendar currentDate;
    List<Reports> allReports;
    List<Treatments> allTreatments;
    int reqCode;


    public GridAdapter(Context context, List<Date> datesList, Calendar currentDate, List<Reports> allReports, List<Treatments> allTreatments) {
        this.mContext = context;
        this.datesList = datesList;
        this.currentDate = currentDate;
        this.allReports = allReports;
        this.allTreatments = allTreatments;
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
                Map<Integer, TextView> eventMap =  getEventViewMap(monthView);
                for (int i = 0; i < allReports.size(); i++) {
                    eventCal.setTimeInMillis(allReports.get(i).getDate());
                    if (dayValue == eventCal.get(Calendar.DAY_OF_MONTH) && monthValue == eventCal.get(Calendar.MONTH) + 1
                            && yearValue == eventCal.get(Calendar.YEAR)) {
                        int startHour = eventCal.get(Calendar.HOUR_OF_DAY);
                        for(int j = 7; j<22; j++){
                            if(j==startHour){
                                eventMap.get(j).setBackgroundColor(Color.MAGENTA);
                            }
                        }
                    }

                }

        return monthView;
    }

    private Map<Integer,TextView> getEventViewMap(View monthView){
        Map<Integer, TextView> eventMap = new HashMap<>(16);
//        eventMap.put(6, (TextView) monthView.findViewById(R.id.event_id6));
        eventMap.put(7, (TextView) monthView.findViewById(R.id.event_id7));
        eventMap.put(8,(TextView) monthView.findViewById(R.id.event_id8));
        eventMap.put(9,(TextView) monthView.findViewById(R.id.event_id9));
        eventMap.put(10,(TextView) monthView.findViewById(R.id.event_id10));
        eventMap.put(11,(TextView) monthView.findViewById(R.id.event_id11));
        eventMap.put(12,(TextView) monthView.findViewById(R.id.event_id12));
        eventMap.put(13,(TextView) monthView.findViewById(R.id.event_id13));
        eventMap.put(14,(TextView) monthView.findViewById(R.id.event_id14));
        eventMap.put(15,(TextView) monthView.findViewById(R.id.event_id15));
        eventMap.put(16,(TextView) monthView.findViewById(R.id.event_id16));
        eventMap.put(17,(TextView) monthView.findViewById(R.id.event_id17));
        eventMap.put(18,(TextView) monthView.findViewById(R.id.event_id18));
        eventMap.put(19,(TextView) monthView.findViewById(R.id.event_id19));
        eventMap.put(20,(TextView) monthView.findViewById(R.id.event_id20));
        eventMap.put(21,(TextView) monthView.findViewById(R.id.event_id21));
        eventMap.put(22,(TextView) monthView.findViewById(R.id.event_id22));
        return eventMap;
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


package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
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
    private String startMinute = "";
    private String endMinute = "";
    private String startHour = "";
    private String endHour = "";
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
            holder.gridItem = (RelativeLayout) weekView.findViewById(R.id.week_drid_item_layout);
            holder.bottomBorder = weekView.findViewById(R.id.week_border2);
            holder.topBorder = weekView.findViewById(R.id.week_border_top);
        }
        hour = hoursArray[position].getHour();
        minute = hoursArray[position].getMinute();
        holder.weekHour.setText(hour + ":" + minute);
        if(dailyReports!= null) {
            View finalWeekView = weekView;
            for (int i = 0; i < dailyReports.size(); i++) {
                Date tempRep = new Date(dailyReports.get(i).getDate());
                Calendar calRep = Calendar.getInstance();
                calRep.setTime(tempRep);
                int stHour = calRep.get(Calendar.HOUR_OF_DAY);
                int sMinute = calRep.get(Calendar.MINUTE);
                String treatments[] = (dailyReports.get(i).getTreatment()).split(",");
                int treatmentPeriod = 0;
                String[] tArray = new String[treatments.length];
                for (int j = 0; j < treatments.length; j++) {
                    treatments[j] = treatments[j].trim();
                    Treatments t = null;
                    if (!treatments[j].isEmpty() || treatments[j].length() > 0) {
                        t = ListHelper.getTreatment(Integer.valueOf(treatments[j]), mContext);
                        tArray[j] = t.getName();
                        finalWeekView.setTag(R.id.TRE_ARRAY, tArray);
                    }
                    if (t != null) {
                        treatmentPeriod = treatmentPeriod + t.getTime();
                    }
                }
                int ndHour = treatmentPeriod / 60 + stHour;
                int eMinute = (sMinute != 30) ? treatmentPeriod % 60 : treatmentPeriod % 60 + sMinute;
                if (eMinute == 60) {
                    eMinute = 0;
                    ndHour = ndHour + 1;
                }
                if (sMinute == 30 && (treatmentPeriod % 60) > 0) {
                    ndHour = treatmentPeriod / 60 + ndHour;
                }


                //Log.d("wwx", treatmentPeriod + "period");
                Log.d("wwwx", stHour + "-" + startMinute + " start" + ndHour + "-" + endMinute + ": end" + " period: "+treatmentPeriod);
                startHour = (stHour < 10) ? "0" + String.valueOf(stHour) : String.valueOf(stHour);
                endHour = (ndHour < 10) ? "0" + String.valueOf(ndHour) : String.valueOf(ndHour);
                startMinute = (sMinute < 10) ? String.valueOf(sMinute) + "0" : String.valueOf(sMinute);
                endMinute = (eMinute < 10) ? String.valueOf(eMinute) + "0" : String.valueOf(eMinute);

//            Log.d("wxx", startHour +":" + startMinute + ", eventStart") ;
//            Log.d("wxx", endHour +":" + endMinute + ", eventEnd") ;

                if (treatmentPeriod < 60) {
                    if (startHour.equals(String.valueOf(hoursArray[position].getHour()))
                            && startMinute.equals(String.valueOf(hoursArray[position].getMinute()))) {
                        Log.d("wwwx", startHour + ":" + startMinute + ", eventStart 1 booked, period: " + treatmentPeriod);
                        holder.gridItem.setBackgroundColor(Color.parseColor("#e0e0e0"));
                        holder.bottomBorder.setBackgroundColor(Color.DKGRAY);
                        holder.topBorder.setBackgroundColor(Color.DKGRAY);
                        finalWeekView.setTag(R.id.REPORT, dailyReports.get(i));
                    }
                } else if (treatmentPeriod == 60) {
                    if (startHour.equals(String.valueOf(hoursArray[position].getHour()))
                            && startMinute.equals(String.valueOf(hoursArray[position].getMinute()))) {
                        Log.d("wwwx", startHour + ":" + startMinute + ", eventStart 2 booked, period: " + treatmentPeriod);
                        holder.gridItem.setBackgroundColor(Color.parseColor("#e0e0e0"));
                        holder.topBorder.setBackgroundColor(Color.DKGRAY);
                        finalWeekView.setTag(R.id.REPORT, dailyReports.get(i));
                    }
                    if (eMinute == 30) {
                        eMinute = 0;
                    } else {
                        ndHour -= 1;
                        eMinute = 30;
                    }
                    endHour = (ndHour < 10) ? "0" + String.valueOf(ndHour) : String.valueOf(ndHour);
                    endMinute = (eMinute < 10) ? String.valueOf(eMinute) + "0" : String.valueOf(eMinute);
                    if (endHour.equals(String.valueOf(hoursArray[position].getHour()))
                            && endMinute.equals(String.valueOf(hoursArray[position].getMinute()))) {
                        Log.d("wwwx", endHour + ":" + endMinute + ", eventEnd 2 booked");
                        holder.gridItem.setBackgroundColor(Color.parseColor("#e0e0e0"));
                        holder.bottomBorder.setBackgroundColor(Color.DKGRAY);
                        finalWeekView.setTag(R.id.REPORT, dailyReports.get(i));
                    }
                } else if (treatmentPeriod > 60) {
                    if (startHour.equals(String.valueOf(hoursArray[position].getHour()))
                            && startMinute.equals(String.valueOf(hoursArray[position].getMinute()))) {
                        Log.d("wwwx", startHour + ":" + startMinute + ", eventStart 3 booked, period: " + treatmentPeriod);
                        holder.gridItem.setBackgroundColor(Color.parseColor("#e0e0e0"));
                        holder.topBorder.setBackgroundColor(Color.DKGRAY);
                        finalWeekView.setTag(R.id.REPORT, dailyReports.get(i));
                    }
                    int temp = treatmentPeriod / 30;
                    for (int j = 1; j < temp; j++) {
                        if (eMinute == 30) {
                            eMinute = 0;
                        } else {
                            ndHour -= 1;
                            eMinute = 30;
                        }
                        endHour = (ndHour < 10) ? "0" + String.valueOf(ndHour) : String.valueOf(ndHour);
                        endMinute = (eMinute < 10) ? String.valueOf(eMinute) + "0" : String.valueOf(eMinute);
                        if (endHour.equals(String.valueOf(hoursArray[position].getHour()))
                                && endMinute.equals(String.valueOf(hoursArray[position].getMinute()))) {
                            Log.d("wwx", endHour + ":" + endMinute + ", eventEnd 3 booked");
                            holder.gridItem.setBackgroundColor(Color.parseColor("#e0e0e0"));
                            finalWeekView.setTag(R.id.REPORT, dailyReports.get(i));
                            if (j == 1) {
                                holder.bottomBorder.setBackgroundColor(Color.DKGRAY);
                            }
                        }

                    }
                }
            }
            if (finalWeekView.getTag() != null) {
                Log.d("wwx", finalWeekView.getTag().toString() + " tag");
            }
            Log.d("wwx", "animLoad  2 adapter " + position);

            return finalWeekView;
        }else {
            Log.d("wwx", "animLoad 1 adapter" + position);
            return weekView;
        }
    }

    class ViewHolder {
        TextView weekHour;
        RelativeLayout gridItem;
        View bottomBorder;
        View topBorder;
    }

}

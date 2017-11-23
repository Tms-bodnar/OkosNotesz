package okosnotesz.hu.okosnotesz.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okosnotesz.hu.okosnotesz.ChartHelper;
import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.adapters.WeekViewAdapter;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Reports;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeekFragment extends Fragment {

    Date d;
    List<Reports> reportsList;
    List<Integer> treatmentsList;
    Map<Integer, boolean[]> weeklyReports;
    WeekViewAdapter adapter;
    View v;
    public WeekFragment() {
    }

    public static Fragment newInstance(long cal){
        Fragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putLong("day", cal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("week", "start db");
        weeklyReports = new LinkedHashMap<>(7);
        reportsList = ListHelper.getAllReports(getContext());

        Log.d("week", "end db" + reportsList.size());
        Bundle b = getArguments();
        d = (Date) new Date((Long) b.get("day"));
        setDailyReports(d);
        Log.d("week", weeklyReports.size()+ "size");
        v = initUiLayout();
        return v;
    }

    private Map<Integer, boolean[]> setDailyReports(Date d) {
        weeklyReports = new LinkedHashMap<>(7);
        boolean[] mon = new boolean[32];
        boolean[] tue = new boolean[32];
        boolean[] wed = new boolean[32];
        boolean[] thu = new boolean[32];
        boolean[] fri = new boolean[32];
        boolean[] sat = new boolean[32];
        boolean[] sun = new boolean[32];
        for(int i = 0; i<16; i++){
            mon[i] = false;
            tue[i] = false;
            wed[i] = false;
            thu[i] = false;
            fri[i] = false;
            sat[i] = false;
            sun[i] = false;
        }
        Calendar weekCal = Calendar.getInstance();
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(d);
        tempCal.get(Calendar.WEEK_OF_YEAR);
        for(Reports r : reportsList) {
            weekCal.setTimeInMillis(r.getDate());
            if(weekCal.get(Calendar.WEEK_OF_YEAR) == tempCal.get(Calendar.WEEK_OF_YEAR)){
                int weekDay = weekCal.get(Calendar.DAY_OF_WEEK)-1;
                int dayHour= -1;
                int dayMinute = -1;
                if(weekCal.get(Calendar.HOUR_OF_DAY)>7) {
                    dayHour = weekCal.get(Calendar.HOUR_OF_DAY) - 7;
                    dayMinute = weekCal.get(Calendar.MINUTE);
                }
                Log.d("week", weekDay + "--hét napja");
                if(dayHour>-1) {
                    switch (weekDay) {
                        case 1:

                            if(dayMinute> 29){
                                mon[dayHour+1] = true;
                            }else{
                                mon[dayHour] = true;
                            }
                            break;
                        case 2:
                            if(dayMinute> 29){
                                tue[dayHour+1] = true;
                            }else{
                                tue[dayHour] = true;
                            }
                            break;
                        case 3:
                            if(dayMinute> 29){
                                wed[dayHour+1] = true;
                            }else{
                                wed[dayHour] = true;
                            }
                            break;
                        case 4:
                            if(dayMinute> 29){
                                thu[dayHour+1] = true;
                            }else{
                                thu[dayHour] = true;
                            }
                            break;
                        case 5:
                            if(dayMinute> 29){
                                fri[dayHour+1] = true;
                            }else{
                                fri[dayHour] = true;
                            }
                            break;
                        case 6:

                            if(dayMinute> 29){
                                sat[dayHour+1] = true;
                            }else{
                                sat[dayHour] = true;
                            }
                            break;
                        case 7:

                            if(dayMinute> 29){
                                sun[dayHour+1] = true;
                            }else{
                                sun[dayHour] = true;
                            }
                            break;
                    }
                }
            }
        }
        weeklyReports.put(0, mon);
        weeklyReports.put(1, tue);
        weeklyReports.put(2, wed);
        weeklyReports.put(3, thu);
        weeklyReports.put(4, fri);
        weeklyReports.put(5, sat);
        weeklyReports.put(6, sun);
        return weeklyReports;
    }

    private View initUiLayout() {
         LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_week_view, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.week_view_recycle);
        adapter = new WeekViewAdapter(ChartHelper.Days.values(), weeklyReports);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
//        Log.d("week", recyclerView.getAdapter().getItemCount()+"");
        recyclerView.setAdapter(adapter);
        return v;
    }
//        adapter = new WeekGridAdapter(getContext(), Hours.values(), reportsList);
//        ListView lvMon = (ListView) v.findViewById(R.id.mon_list);
//        lvMon.setAdapter(adapter);




}

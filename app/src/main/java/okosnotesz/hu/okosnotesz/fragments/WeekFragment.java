package okosnotesz.hu.okosnotesz.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okosnotesz.hu.okosnotesz.BookingActivity;
import okosnotesz.hu.okosnotesz.ChartHelper;
import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.adapters.WeekViewAdapter;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Reports;

public class WeekFragment extends Fragment {

    Date d;
    List<Reports> reportsList;
    List<Integer> weekDays;
    List<Integer> treatmentsList;
    List<Calendar> weeklyCalendarsDatas;
    Map<Integer, Reports[]> weeklyReports;
    RecyclerView recyclerView;
    private final int REQ_CODE = 9;
    DrawerLayout drawer;
    ActionBarDrawerToggle barDrawerToggle;
    android.support.v7.widget.Toolbar toolbar;
    Button todayButton;

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
        reportsList = ListHelper.getAllReports(getContext());
        Bundle b = getArguments();
        d = new Date((Long) b.get("day"));
        setDailyReports(d);
        weeklyCalendarsDatas = setWeekCalendars(d);
        v = initUiLayout();
        setWeekDays(d, v);
        return v;
    }

    private View initUiLayout() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_week_view, null);
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        barDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawer,toolbar,R.string.welcome,R.string.cancel);
        barDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        todayButton = (Button) toolbar.findViewById(R.id.tollbar_button);
        recyclerView = (RecyclerView) v.findViewById(R.id.week_view_recycle);
        adapter = new WeekViewAdapter(this,  getContext(), ChartHelper.Days.values(), weeklyReports, weeklyCalendarsDatas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return v;
    }
    //recyclerview clicklistener, átadja a dátumot + időt
    //meghívja a booking activityt,
    //visszakapja egy Reports objektumban a  kezelés(ek) neveit, időtartamát, a foglalás dátumát, az Expertet, a Guestet.
    //frissíti a weeklyreportsot, ez alapján a setdailyreportsot.
    //elmenti a Reports adatbázist.

    public void booking(Calendar temp) {
        Toast.makeText(getContext(), temp.getTime().toString() + "", Toast.LENGTH_SHORT).show();
        Reports sendReport = new Reports();
        sendReport.setDate(temp.getTimeInMillis());
        Intent i = new Intent(getContext(), BookingActivity.class);
        i.putExtra("newRep", sendReport);
        startActivityForResult(i, REQ_CODE);
    }

    private void setWeekDays(Date d, View v){
        TextView[] weekDates = new TextView[7];
        weekDates[0] = (TextView) v.findViewById(R.id.mon_date);
        weekDates[1] = (TextView) v.findViewById(R.id.tue_date);
        weekDates[2] = (TextView) v.findViewById(R.id.wed_date);
        weekDates[3] = (TextView) v.findViewById(R.id.thu_date);
        weekDates[4] = (TextView) v.findViewById(R.id.fri_date);
        weekDates[5] = (TextView) v.findViewById(R.id.sat_date);
        weekDates[6] = (TextView) v.findViewById(R.id.sun_date);
        weekDays = new ArrayList<>(7);
        Calendar tempCal = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        tempCal.setTime(d);
        List<Date> datesList = CalendarFragment.getDayValueInCells(tempCal);
        int weekNumber = tempCal.get(Calendar.WEEK_OF_YEAR);
        Calendar weekCal = Calendar.getInstance();
        for (int i = 0; i < datesList.size(); i++) {
            weekCal.setTime(datesList.get(i));
            if (weekNumber == weekCal.get(Calendar.WEEK_OF_YEAR) && !weekDays.contains(weekCal.get(Calendar.DAY_OF_MONTH))) {
                weekDays.add(weekCal.get(Calendar.DAY_OF_MONTH));
            }
        }
        for (int i = 0; i < weekDates.length; i++) {
            weekDates[i].setText(String.valueOf(weekDays.get(i)));
            if(tempCal.get(Calendar.MONTH)==(today.get(Calendar.MONTH))
                    && tempCal.get(Calendar.YEAR)==today.get(Calendar.YEAR)
                    && weekDays.get(i)==today.get(Calendar.DAY_OF_MONTH)){
                    weekDates[i].setBackgroundColor(Color.parseColor("#FF4081"));
            }
        }
    }

    private List<Calendar> setWeekCalendars(Date d){

        List<Calendar> weeklyCalendars = new ArrayList<>(7);
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTimeInMillis(d.getTime());
        List<Date> datesList = CalendarFragment.getDayValueInCells(tempCal);
        int index = 0;
        for (int i = 0; i < datesList.size(); i++) {
            Calendar calInList = Calendar.getInstance();
            calInList.setTimeInMillis(datesList.get(i).getTime());
            if(calInList.get(Calendar.YEAR)==tempCal.get(Calendar.YEAR)
                    && calInList.get(Calendar.WEEK_OF_YEAR)==tempCal.get(Calendar.WEEK_OF_YEAR)){
               weeklyCalendars.add(calInList);
            }
        }
        return weeklyCalendars;

    }

    private Map<Integer, Reports[]> setDailyReports(Date d) {
        weeklyReports = new LinkedHashMap<>(7);
        Reports[] mon = new Reports[31];
        Reports[] tue = new Reports[31];
        Reports[] wed = new Reports[31];
        Reports[] thu = new Reports[31];
        Reports[] fri = new Reports[31];
        Reports[] sat = new Reports[31];
        Reports[] sun = new Reports[31];
        for(int i = 0; i<31; i++){
            mon[i] = null;
            tue[i] = null;
            wed[i] = null;
            thu[i] = null;
            fri[i] = null;
            sat[i] = null;
            sun[i] = null;
        }
        Calendar weekCal = Calendar.getInstance();
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(d);
        int dayHour = -1;
        int dayMinute = -1;
        int duration = -1;
        for(Reports r : reportsList) {
            weekCal.setTimeInMillis(r.getDate());
            if (weekCal.get(Calendar.WEEK_OF_YEAR) == tempCal.get(Calendar.WEEK_OF_YEAR) &&
                    weekCal.get(Calendar.MONTH) == tempCal.get(Calendar.MONTH)&&
                    weekCal.get(Calendar.WEEK_OF_YEAR)== tempCal.get(Calendar.WEEK_OF_YEAR)) {
                int weekDay = weekCal.get(Calendar.DAY_OF_WEEK)-1;
                dayHour = weekCal.get(Calendar.HOUR_OF_DAY) - 7;
                dayMinute = weekCal.get(Calendar.MINUTE);
                duration = r.getDuration();
                Log.d("weekrep", r.toString());
                int cellCount = duration/30;
                int temp = 0;
                if(dayHour> -1){
                    switch (weekDay){
                        case 1:
                            do{
                                mon[dayHour + temp] = r;
                                temp += 1;
                            }while(temp < cellCount);
                            break;
                        case 2:
                            do{
                                tue[dayHour + temp] = r;
                                temp += 1;
                            }while(temp < cellCount);
                            break;
                        case 3:
                            do{
                                wed[dayHour + temp] = r;
                                temp += 1;
                            }while(temp < cellCount);
                            break;
                        case 4:
                            do{
                                thu[dayHour + temp] = r;
                                temp += 1;
                            }while(temp < cellCount);
                            break;
                        case 5:
                            do{
                                fri[dayHour + temp] = r;
                                temp += 1;
                            }while(temp < cellCount);
                            break;
                        case 6:
                            do{
                                sat[dayHour + temp] = r;
                                temp += 1;
                            }while(temp < cellCount);
                            break;
                        case 7:
                            do{
                                sun[dayHour + temp] = r;
                                temp += 1;
                            }while(temp < cellCount);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       adapter.onActivityResult(requestCode, resultCode, data);
        Reports backRep = null;
        if (resultCode == 22) {
            backRep = data.getParcelableExtra("backRep");
            Date temp = new Date(backRep.getDate());
            setDailyReports(temp);
        }
       // formatter = new SimpleDateFormat("yyyy MM dd HH:mm");
    }
}


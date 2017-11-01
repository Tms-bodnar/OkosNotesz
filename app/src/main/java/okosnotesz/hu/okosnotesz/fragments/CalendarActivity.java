package okosnotesz.hu.okosnotesz.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.adapters.GridAdapter;
import okosnotesz.hu.okosnotesz.adapters.WeekGridAdapter;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Reports;

public class CalendarActivity extends Fragment {

    private TextView nextMonth;
    private TextView prevMonth;
    private TextView currDate;
    private GridView calGrid;
    private GridView weekGrid;
    private List<TextView> weeklyDates;
    private List<GridView> weekGridList;
    private ViewFlipper vf;
    private float coord = 0;
    private DisplayMetrics dm;
    private Calendar cal = Calendar.getInstance();
    private final int MAX_CALENDAR_CELLS = 42;
    private GridAdapter gridAdapter;
    private WeekGridAdapter weekGridAdapter;
    private List<Reports> reportsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "oncreate Calendar");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = initUiMonthlyLayout();
        setupCalMonthlyAdapter(0);
        setPrevMonth();
        setNextMonth();
        Log.d("TAG", "oncreateview Calendar");
        return v;

    }

    private void setupCalMonthlyAdapter(int animCode) {
        reportsList = ListHelper.getAllReports(getContext());
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        String dateString = formatter.format(cal.getTime());
        currDate.setText(dateString);
        currDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                coord = dm.heightPixels;
                setupCalMonthlyAdapter(0);

            }
        });
        final List<Date> dayValueInCells = getDayValueInCells();
        gridAdapter = new GridAdapter(getContext(), dayValueInCells, cal, reportsList);
        calGrid.setAdapter(gridAdapter);
        calGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calendar clickedCal = Calendar.getInstance();
                clickedCal.setTimeInMillis(dayValueInCells.get(position).getTime());
                Log.d("xx", clickedCal.get(Calendar.DAY_OF_MONTH)+" click");
                setupCalWeeklyAdapter(clickedCal, 0);

    }
        });
        if(animCode == 0){
            vf.setInAnimation(inToTopAnimation());
        }

        vf.setDisplayedChild(0);
        Log.d("xx", "MonthlyLoad");
    }

    private void setupCalWeeklyAdapter(Calendar clickedCal, int animCode) {
        Calendar tempCal = Calendar.getInstance();
        int weekNumber = clickedCal.get(Calendar.WEEK_OF_YEAR);
        List<Date> weekDates = new ArrayList<>(7);
        List<Date> dayValueInCells = getDayValueInCells();
        Log.d("xx", dayValueInCells.size()+" dayvaluesincells");
        for(int i = 0; i < dayValueInCells.size(); i++){
            tempCal.setTimeInMillis(dayValueInCells.get(i).getTime());
            int tempWeekNumber = tempCal.get(Calendar.WEEK_OF_YEAR);
            if(tempWeekNumber == weekNumber){
                weekDates.add(dayValueInCells.get(i));
            }
        }
        Log.d("xx",weekDates.size()+"weekdatessize");
        for(int i = 0; i < weekDates.size();i++){
            Calendar temp = Calendar.getInstance();
            temp.setTime(weekDates.get(i));
            //Log.d("xx", temp.get(Calendar.DAY_OF_MONTH)+": dom");
            weeklyDates.get(i).setText(String.valueOf(temp.get(Calendar.DAY_OF_MONTH)));
            //Log.d("xx",weeklyDates.get(i).getText().toString());
        }
        weekGridAdapter = new WeekGridAdapter(getContext(), Hours.values(), reportsList);
        for (GridView gv : weekGridList) {
            gv.setAdapter(weekGridAdapter);
            gv.setFocusable(false);
        }
        if(animCode==0) {
            vf.setInAnimation(inToTopAnimation());
        }
            vf.setDisplayedChild(1);
    }

    private List<Date> getDayValueInCells(){
        List<Date> dayValueInCells = new ArrayList<Date>();
        Calendar tempCal = (Calendar)cal.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = tempCal.get(Calendar.DAY_OF_WEEK)+4;
        tempCal.set(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while(dayValueInCells.size() < MAX_CALENDAR_CELLS){
            dayValueInCells.add(tempCal.getTime());
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dayValueInCells;
    }


    private View initUiMonthlyLayout() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_view, null);
        dm = new DisplayMetrics();
        nextMonth = (TextView) v.findViewById(R.id.next_month);
        prevMonth = (TextView) v.findViewById(R.id.previous_month);
        currDate = (TextView) v.findViewById(R.id.display_current_date);
        calGrid = (GridView) v.findViewById(R.id.calendar_grid);
        weekGridList = new ArrayList<>(7);
        weekGridList.add((GridView) v.findViewById(R.id.week_mon_grid));
        weekGridList.add((GridView) v.findViewById(R.id.week_tue_grid));
        weekGridList.add((GridView) v.findViewById(R.id.week_wed_grid));
        weekGridList.add((GridView) v.findViewById(R.id.week_thu_grid));
        weekGridList.add((GridView) v.findViewById(R.id.week_fri_grid));
        weekGridList.add((GridView) v.findViewById(R.id.week_sat_grid));
        weekGridList.add((GridView) v.findViewById(R.id.week_sun_grid));
        weeklyDates = new ArrayList<>(7);
        weeklyDates.add((TextView) v.findViewById(R.id.mon_week_date));
        weeklyDates.add((TextView) v.findViewById(R.id.tue_week_date));
        weeklyDates.add((TextView) v.findViewById(R.id.wed_week_date));
        weeklyDates.add((TextView) v.findViewById(R.id.thu_week_date));
        weeklyDates.add((TextView) v.findViewById(R.id.fri_week_date));
        weeklyDates.add((TextView) v.findViewById(R.id.sat_week_date));
        weeklyDates.add((TextView) v.findViewById(R.id.sun_week_date));
        vf = (ViewFlipper) v.findViewById(R.id.vf);
        return v;
    }

    private void setNextMonth() {
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vf.getDisplayedChild()==0) {
                    cal.add(Calendar.MONTH, 1);
                    vf.setInAnimation(toLeftAnimation());
                    setupCalMonthlyAdapter(1);

                }else{
                    cal.add(Calendar.WEEK_OF_YEAR, 1);
                    vf.setInAnimation(toLeftAnimation());
                    setupCalWeeklyAdapter(cal,1);

                }
            }
        });
    }

    private void setPrevMonth() {
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vf.getDisplayedChild()==0) {
                    cal.add(Calendar.MONTH, -1);
                    vf.setInAnimation(toRightAnimation());
                    setupCalMonthlyAdapter(1);

                }else{
                    cal.add(Calendar.WEEK_OF_YEAR, -1);
                    vf.setInAnimation(toRightAnimation());
                    setupCalWeeklyAdapter(cal,1);

                }
            }
        });
    }
    protected Animation inToTopAnimation() {
        Animation inToTOp = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.1f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inToTOp.setDuration(500);
        inToTOp.setInterpolator(new AccelerateInterpolator());
        return inToTOp;
    }
    protected Animation toLeftAnimation() {
        Animation toLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        toLeft.setDuration(500);
        toLeft.setInterpolator(new AccelerateInterpolator());
        return toLeft;
    }
    protected Animation toRightAnimation() {
        Animation toRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        toRight.setDuration(500);
        toRight.setInterpolator(new AccelerateInterpolator());
        return toRight;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TAG", "onActivityCreated Calendar");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TAG", "onStart Calendar");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onresume Calendar");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TAG", "onPause Calendar");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "onStop Calendar");
    }

    public enum Hours{

        SIX("06:00"), SEVEN("07:00"), EIGTH("08:00"), NINE("09:00"), TEN("10:00"), ELEVEN("11:00"),
        TWELVE("12:00"), THIRTEEN("13:00"), FOURTEEN("14:00"), FIFTEEN("15:00"), SIXTEEN("16:00"),
        SEVENTEEN("17:00"), EIGHTEEN("18:00"), NINETEEN("19:00"), TWENTY("20:00"), TWENTYONE("21:00"),
        TWENTYTWO("22:00");

        private String hour;
        Hours(String s) {
            this.hour=s;
        }

        public String getHour(){
            return hour;
        }
    }

}


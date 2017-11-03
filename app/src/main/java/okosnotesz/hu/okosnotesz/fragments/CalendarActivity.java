package okosnotesz.hu.okosnotesz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okosnotesz.hu.okosnotesz.BookingActivity;
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
    private Calendar clickedCal = Calendar.getInstance();
    private final int MAX_CALENDAR_CELLS = 42;
    private GridAdapter gridAdapter;
    private WeekGridAdapter weekGridAdapter;
    private List<Reports> reportsList;
    private Reports report;
    private final int REQ_CODE = 9;

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
        setupCalMonthlyAdapter(cal, 0);
        setPrevMonth();
        setNextMonth();
        Log.d("TAG", "oncreateview Calendar");
        return v;

    }

    private void setupCalMonthlyAdapter(Calendar day, int animCode) {
        reportsList = ListHelper.getAllReports(getContext());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMMM");
        String dateString = formatter.format(day.getTime());
        currDate.setText(dateString);
        final Calendar finalDay = day;
        currDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                coord = dm.heightPixels;
                setupCalMonthlyAdapter(finalDay,0);

            }
        });

        final List<Date> dayValueInCells = getDayValueInCells(cal);
        gridAdapter = new GridAdapter(getContext(), dayValueInCells, day, reportsList);
        calGrid.setAdapter(gridAdapter);
        calGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedCal.setTimeInMillis(dayValueInCells.get(position).getTime());
                cal=clickedCal;
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

    private void setupCalWeeklyAdapter(final Calendar clickedDate, int animCode) {
        Calendar tempCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy ww");
        String dateString = formatter.format(clickedDate.getTime());
        currDate.setText(dateString +". "+ getResources().getString(R.string.week));
        int weekNumber = clickedDate.get(Calendar.WEEK_OF_YEAR);
        final List<Date> weekDates = new ArrayList<>(7);
        List<Date> dayValueInCells = getDayValueInCells(clickedDate);
        for(int i = 0; i < dayValueInCells.size(); i++){
            tempCal.setTimeInMillis(dayValueInCells.get(i).getTime());
            int tempWeekNumber = tempCal.get(Calendar.WEEK_OF_YEAR);
            if(tempWeekNumber == weekNumber){
                //Date temp = new Date(86400000+tempCal.getTimeInMillis());
                weekDates.add(tempCal.getTime());
            }
        }
        Log.d("xx",weekDates.size()+"weekdatessize");
        for(int i = 0; i < weekDates.size();i++){
            Calendar temp = Calendar.getInstance();
            temp.setTime(weekDates.get(i));
            weeklyDates.get(i).setText(String.valueOf(temp.get(Calendar.DAY_OF_MONTH)));
        }
        weekGridAdapter = new WeekGridAdapter(getContext(), Hours.values(), reportsList);
        for (int i = 0; i < weekGridList.size(); i++) {
            weekGridList.get(i).setAdapter(weekGridAdapter);
            weekGridList.get(i).setFocusable(false);
            final int finalI = i;
            weekGridList.get(i).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Calendar temp = Calendar.getInstance();
                    temp.setTime(weekDates.get(finalI));
                    Hours h = (Hours) weekGridList.get(finalI).getAdapter().getItem(position);
                    int hour = h.getHour();
                    int minute = h.getMinute();
                    temp.set(Calendar.HOUR_OF_DAY, hour);
                    temp.set(Calendar.MINUTE, minute);
                    booking(temp);
                    return false;
                }
            });
        }
        if(animCode==0) {
            vf.setInAnimation(inToTopAnimation());
        }
            vf.setDisplayedChild(1);
    }

    private void booking(Calendar temp) {
        Toast.makeText(getContext(), temp.getTime().toString()+ "",Toast.LENGTH_SHORT).show();
        report = new Reports();
        report.setDate(temp.getTimeInMillis());
        Intent i = new Intent(getContext(), BookingActivity.class);
        i.putExtra("newRep", report);
        startActivityForResult(i, REQ_CODE);
    }


    private List<Date> getDayValueInCells(Calendar day){
        List<Date> dayValueInCells = new ArrayList<Date>();
        Calendar tempCal = (Calendar)day.clone();
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
        weeklyDates.add((TextView) v.findViewById(R.id.mon_date));
        weeklyDates.add((TextView) v.findViewById(R.id.tue_date));
        weeklyDates.add((TextView) v.findViewById(R.id.wed_date));
        weeklyDates.add((TextView) v.findViewById(R.id.thu_date));
        weeklyDates.add((TextView) v.findViewById(R.id.fri_date));
        weeklyDates.add((TextView) v.findViewById(R.id.sat_date));
        weeklyDates.add((TextView) v.findViewById(R.id.sun_date));

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
                    setupCalMonthlyAdapter(cal,1);

                }else{
                    clickedCal.add(Calendar.WEEK_OF_YEAR, 1);
                    vf.setInAnimation(toLeftAnimation());
                    setupCalWeeklyAdapter(clickedCal,1);

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
                    setupCalMonthlyAdapter(cal,1);

                }else{
                    clickedCal.add(Calendar.WEEK_OF_YEAR, -1);
                    vf.setInAnimation(toRightAnimation());
                    setupCalWeeklyAdapter(clickedCal,1);

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

        SIX(6,00), SIXTTY(6,30), SEVEN(7,00), SEVENTTY(7,30), EIGTH(8 ,00), EIGTHTTY(8,30),
        NINE(9,00), NINETTY(9,30), TEN(10,00), TENTTY(10,30), ELEVEN(11,00), ELEVENTTY(11,30),
        TWELVE(12,00), TWELVETTY(12,30), THIRTEEN(13,00), THIRTEENTTY(13,30), FOURTEEN(14,00),
        FOURTEENTTY(14,30), FIFTEEN(15,00), FIFTEENTTY(15,30), SIXTEEN(16,00), SIXTEENTTY(16,30),
        SEVENTEEN(17,00), SEVENTEENTTY(17,30), EIGHTEEN(18,00), EIGHTEENTTY(18,30), NINETEEN(19,00),
        NINETEENTTY(19,30), TWENTY(20,00), TWENTYTTY(20,30), TWENTYONE(21,00), TWENTYONETTY(21,30);


        private int hour;
        private int minute;

        Hours(int hour, int minute) {
            this.hour=hour;
            this.minute=minute;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }
    }

}


package okosnotesz.hu.okosnotesz.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.adapters.GridAdapter;
import okosnotesz.hu.okosnotesz.adapters.PagerAdapter;
import okosnotesz.hu.okosnotesz.adapters.WeekGridAdapter;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Reports;
import okosnotesz.hu.okosnotesz.model.Treatments;

public class CalendarFragment extends Fragment {


    private GridView calGrid;
    private GridView weekGrid;
    DrawerLayout drawer;
    LayoutInflater inflater;
    ActionBarDrawerToggle barDrawerToggle;
    PagerAdapter sectionsPagerAdapter;
    private List<TextView> weeklyDates;
    //private List<ViewFlipper> weekGridFlipperList;
   // private ViewFlipper vf;
    private float coord = 0;
    private DisplayMetrics dm;
    private Calendar cal = Calendar.getInstance();
    private Calendar clickedCal = Calendar.getInstance();
    private static final int MAX_CALENDAR_CELLS = 49;
    private GridAdapter gridAdapter;
    private WeekGridAdapter weekGridAdapter;
    private List<Reports> reportsList;
    private List<Treatments> treatmentsList;
    private Reports sendReport;
    private Reports getReport;
    PagerAdapter adapter;
    android.support.v7.widget.Toolbar toolbar;
    SimpleDateFormat formatter;
    MaterialDialog dialog;
    GestureDetector gd;
    float initialX;
    Context mcontext;
    String dateString;
    ViewPager viewPager;
    Button todayButton;

    public static Fragment newInstance(long cal) {
        Fragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putLong("position", cal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = getContext();

        Log.d("xxx", "oncreate Calendar");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = initUiLayout();
        Bundle b = getArguments();
        if(b != null){
            long calMillis = b.getLong("position");
            cal.setTimeInMillis(calMillis);
            setupCalMonthlyAdapter(cal,0);
            Log.d("eee", cal.getTime()+"fragment bundle");

        }else {
            setupCalMonthlyAdapter(cal, 0);
            Log.d("eee", cal.getTime()+"fragment null bundle");
        }
        return v;

    }


    private void setupCalMonthlyAdapter(Calendar day, int animCode) {
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        todayButton = (Button) toolbar.findViewById(R.id.tollbar_button);
        reportsList = ListHelper.getAllReports(getContext());
        treatmentsList = ListHelper.getAllTreatments(getContext());
        final List<Date> dayValueInCells = getDayValueInCells(day);
        gridAdapter = new GridAdapter(mcontext, dayValueInCells, day, reportsList, treatmentsList);
        calGrid.setAdapter(gridAdapter);
        calGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date clickedDate = dayValueInCells.get(position);
                Log.d("daycells", clickedDate.toString() + ": clicked!");
                long clickedLong = clickedDate.getTime();
                setViewPager(53, clickedLong);

            }
        });
      /*  todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                setViewPager(49, today.getTimeInMillis());
            }
        });*/


    }

    private void setViewPager(int i, long l){

        adapter = new PagerAdapter(getContext(), getFragmentManager(), i, l);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(26);
        viewPager.setOffscreenPageLimit(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toolbar.setTitle(adapter.getPageTitle(viewPager.getCurrentItem()));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /*private void setupCalWeeklyAdapter(final Calendar clickedDate, int animCode) {

        Log.d("wwwx", "startadapters");
        Calendar tempCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy ww");
        String dateString = formatter.format(clickedDate.getTime());
        currDate.setText(dateString + ". " + getResources().getString(R.string.week));
        int weekNumber = clickedDate.get(Calendar.WEEK_OF_YEAR);
        final List<Date> weekDates = new ArrayList<>(7);
        List<Date> dayValueInCells = getDayValueInCells(clickedDate);
        for (int i = 0; i < dayValueInCells.size(); i++) {
            tempCal.setTimeInMillis(dayValueInCells.get(i).getTime());
            int tempWeekNumber = tempCal.get(Calendar.WEEK_OF_YEAR);
            if (tempWeekNumber == weekNumber) {
                weekDates.add(tempCal.getTime());
                Log.d("wwwx", "dayvalues:" + i);
            }
        }
        for (int i = 0; i < weekDates.size(); i++) {
            Calendar temp = Calendar.getInstance();
            temp.setTime(weekDates.get(i));
            weeklyCalendars.get(i).setText(String.valueOf(temp.get(Calendar.DAY_OF_MONTH)));
        }

        for (int i = 0; i < weekGridFlipperList.size(); i++) {
            List<Reports> dailyReports = new ArrayList<>();
            Calendar tempCalWD = Calendar.getInstance();
            tempCalWD.setTime(weekDates.get(i));
            for (int j = 0; j < reportsList.size(); j++) {
                Date temp = new Date(reportsList.get(j).getDate());
                Calendar tempCalRepD = Calendar.getInstance();
                tempCalRepD.setTime(temp);
                if (tempCalWD.get(Calendar.DAY_OF_MONTH) == tempCalRepD.get(Calendar.DAY_OF_MONTH)) {
                    dailyReports.add(reportsList.get(j));
                }
            }
            weekGridAdapter = new WeekGridAdapter(mcontext, Hours.values(), dailyReports);
            GridView gridView = (GridView) weekGridFlipperList.get(i).getChildAt(0).findViewById(R.id.week_mon_grid);
            gridView.setAdapter(weekGridAdapter);
            gridView.setFocusable(false);
            int finalI = i;
            weekGridFlipperList.get(finalI).setInAnimation(inToTopAnimation());
            weekGridFlipperList.get(finalI).setDisplayedChild(0);
            handler.sendEmptyMessage(finalI);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Calendar temp = Calendar.getInstance();
                    temp.setTime(weekDates.get(finalI));
                    Hours h = (Hours) gridView.getAdapter().getItem(position);
                    String hour = h.getHour();
                    String minute = h.getMinute();
                    temp.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));
                    temp.set(Calendar.MINUTE, Integer.valueOf(minute));
                    booking(temp);
                }
            });

            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean result = false;
                    Log.d("wwx", position + "position");
                    Reports temp = (Reports) view.getTag(R.id.REPORT);
                    String[] tArray = (String[]) view.getTag(R.id.TRE_ARRAY);
                    if (temp != null && tArray != null) {
                        StringBuilder trename = new StringBuilder(tArray.length);
                        for (int k = 0; k < tArray.length; k++) {
                            if (tArray[k] != null && !tArray[k].isEmpty() && tArray[k].length() > 0) {
                                Log.d("wwx", tArray[k]);
                                trename.append(tArray[k] + ", ");
                            }
                        }
                        View detailsView = inflater.inflate(R.layout.booking_details, null);
                        Button detailsOK = (Button) detailsView.findViewById(R.id.booking_details_button);
                        TextView detailsDate = (TextView) detailsView.findViewById(R.id.booking_date_details);
                        TextView detailsGuest = (TextView) detailsView.findViewById(R.id.booking_guest_datails);
                        TextView detailsTreatment = (TextView) detailsView.findViewById(R.id.booking_treatment_details);
                        TextView detailsExp = (TextView) detailsView.findViewById(R.id.booking_expert_details);
                        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy MMMM dd, HH:mm");
                        detailsDate.setText(formatter2.format(new Date(temp.getDate())));
                        if (temp.getGuestName() != null) {
                            detailsGuest.setText(temp.getGuestName());
                        } else detailsGuest.setText(R.string.noDatas);
                        if (trename != null) {
                            detailsTreatment.setText(trename.toString());
                        } else detailsTreatment.setText(R.string.noDatas);
                        Experts tempExpert = ListHelper.getExpert(temp.getExpert(), getContext());
                        if (tempExpert != null) {
                            detailsExp.setText(tempExpert.getName());
                        } else detailsExp.setText(R.string.noDatas);
                        final PopupWindow pop = new PopupWindow(detailsView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        pop.showAtLocation(detailsView, Gravity.CENTER, 0, 0);
                        detailsOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pop.dismiss();
                            }
                        });
                        result = true;
                    }
                    return result;
                }
            });
        }
        Log.d("wwwx", "endAdapters");
    }*/


    public static List<Date> getDayValueInCells(Calendar day) {
        List<Date> dayValueInCells = new ArrayList<>();
        Log.d("daycells", "getdayvalues start");
        Calendar tempCal = (Calendar) day.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        Log.d("caldaysok", tempCal.get(Calendar.DAY_OF_MONTH)+" *");
        int firstDayOfTheMonth = tempCal.get(Calendar.DAY_OF_WEEK) + 4;
        tempCal.set(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        Log.d("caldaysok", tempCal.get(Calendar.DAY_OF_MONTH)+" **");
         do{
            dayValueInCells.add(tempCal.getTime());
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }while (dayValueInCells.size() < MAX_CALENDAR_CELLS);
        return dayValueInCells;
    }


    private View initUiLayout() {
        inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_view, null);
        calGrid = (GridView) v.findViewById(R.id.calendar_grid);
        viewPager = (ViewPager) getActivity().findViewById(R.id.container);
        weeklyDates = new ArrayList<>(7);
        weeklyDates.add((TextView) v.findViewById(R.id.mon_date));
        weeklyDates.add((TextView) v.findViewById(R.id.tue_date));
        weeklyDates.add((TextView) v.findViewById(R.id.wed_date));
        weeklyDates.add((TextView) v.findViewById(R.id.thu_date));
        weeklyDates.add((TextView) v.findViewById(R.id.fri_date));
        weeklyDates.add((TextView) v.findViewById(R.id.sat_date));
        weeklyDates.add((TextView) v.findViewById(R.id.sun_date));
        return v;
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

    protected Animation toLeftInAnimation() {
        Animation toLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        toLeft.setDuration(500);
        toLeft.setInterpolator(new AccelerateInterpolator());
        return toLeft;
    }

    protected Animation toLeftOutAnimation() {
        Animation toLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        toLeft.setDuration(500);
        toLeft.setInterpolator(new AccelerateInterpolator());
        return toLeft;
    }

    protected Animation toRightInAnimation() {
        Animation toRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        toRight.setDuration(500);
        toRight.setInterpolator(new AccelerateInterpolator());
        return toRight;
    }

    protected Animation toRightOutAnimation() {
        Animation toRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        toRight.setDuration(500);
        toRight.setInterpolator(new AccelerateInterpolator());
        return toRight;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


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

}


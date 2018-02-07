package hu.okosnotesz.okosnotesz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hu.okosnotesz.okosnotesz.R;
import hu.okosnotesz.okosnotesz.adapters.MonthGridAdapter;
import hu.okosnotesz.okosnotesz.model.ListHelper;
import hu.okosnotesz.okosnotesz.model.Reports;

public class CalendarFragment extends Fragment {

    LayoutInflater inflater;
    private Calendar cal = Calendar.getInstance();
    static int MAX_CALENDAR_CELLS = 1095;
    private List<Reports> reportsList;
    private List<Calendar> dayValueInCells;
    android.support.v7.widget.Toolbar toolbar;
    Context mContext;
    ViewPager viewPager;
    Button todayButton;
    static Fragment mFragment;
    RecyclerView monthRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    FragmentManager fragmentManager;
    private GestureDetectorCompat mDetector;
    View.OnTouchListener listener;

    public static Fragment newInstance(long cal) {
        mFragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putLong("position", cal);
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
        mContext = getContext();
/*        mDetector = new GestureDetectorCompat(mContext, this);
        listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return true;
            }
        };*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = initUiLayout();
        Bundle b = getArguments();
        if (b != null) {
            long calMillis = b.getLong("position");
            cal.setTimeInMillis(calMillis);
            setupDayValues(cal);
        } else {
            setupDayValues(cal);
        }
        return v;

    }

    private void setupRecViewAdapter(Toolbar toolbar){
        if (getActivity() == null || dayValueInCells == null) {
            return;
        }
        if (dayValueInCells != null) {
            monthRecyclerView.setAdapter(new MonthGridAdapter(mContext, fragmentManager, toolbar, monthRecyclerView, dayValueInCells, cal, reportsList,  viewPager));
        } else {
            monthRecyclerView.setAdapter(null);
        }
    }

    private void setupDayValues(Calendar day) {

        Calendar tempCal = (Calendar) day.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = tempCal.get(Calendar.DAY_OF_WEEK) + 4;
        tempCal.set(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        tempCal.setFirstDayOfWeek(Calendar.MONDAY);
        for (int i = 0; i < MAX_CALENDAR_CELLS; i++) {
                Calendar newCal = Calendar.getInstance();
                newCal.setTimeInMillis(tempCal.getTimeInMillis());
                dayValueInCells.add(newCal);
                tempCal.add(Calendar.DAY_OF_MONTH, 1);
                monthRecyclerView.getAdapter().notifyItemInserted(i);
                monthRecyclerView.getAdapter().notifyItemRangeInserted(0, i);
            }

    }

    private View initUiLayout() {
        reportsList = ListHelper.getAllReports(getContext());
        inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_month_layout,null,false);
        monthRecyclerView = (RecyclerView) v.findViewById(R.id.week_view_recycle);
        mLayoutManager = new GridLayoutManager(mContext, 7, GridLayoutManager.VERTICAL,false);
        monthRecyclerView.setLayoutManager(mLayoutManager);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        dayValueInCells = new ArrayList<>();
        setupRecViewAdapter(toolbar);
        TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tab.setVisibility(View.GONE);
        todayButton = (Button) toolbar.findViewById(R.id.tollbar_button);
        viewPager = (ViewPager) getActivity().findViewById(R.id.container);
        monthRecyclerView.canScrollVertically(RecyclerView.VERTICAL);
   //     monthRecyclerView.setOnTouchListener(listener);
  //      animateItems();
        return v;
    }

   /* public void manipulateDayValues(float i){
        int ratio = (int) (i / 100);
        Calendar tempCal;
        switch (ratio){
            case 1:
                    tempCal = dayValueInCells.get(dayValueInCells.size()-1);
                    for (int j = 0; j < 7; j++) {
                        tempCal.add(Calendar.DAY_OF_YEAR, 1);
                        dayValueInCells.add(j, tempCal);
                    }
                    Log.d("scrollEvent", "2.1 +:" + ratio);
                    monthRecyclerView.getAdapter().notifyItemRangeInserted(0,7);
                break;
            case -1:
                tempCal = dayValueInCells.get(0);
                for (int j = 0; j < 7; j++) {
                    tempCal.add(Calendar.DAY_OF_YEAR, -1);
                    dayValueInCells.add(dayValueInCells.size() - 1 - j, tempCal);
                }
                Log.d("scrollEvent", "2.1 -:" + ratio);
                monthRecyclerView.getAdapter().notifyItemRangeInserted(dayValueInCells.size()-8,7);
                break;
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        float i = motionEvent.getY() - motionEvent1.getY();
        manipulateDayValues(i);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }*/
}
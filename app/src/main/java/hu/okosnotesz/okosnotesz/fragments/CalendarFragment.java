package hu.okosnotesz.okosnotesz.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.okosnotesz.okosnotesz.R;
import hu.okosnotesz.okosnotesz.adapters.MonthGridAdapter;
import hu.okosnotesz.okosnotesz.model.ListHelper;
import hu.okosnotesz.okosnotesz.model.Reports;
import hu.okosnotesz.okosnotesz.model.Treatments;

public class CalendarFragment extends Fragment {

    LayoutInflater inflater;
    private Calendar cal = Calendar.getInstance();
    static final int MAX_CALENDAR_CELLS = 6;
    private List<Reports> reportsList;
    private List<Treatments> treatmentsList;
    private Map<Integer,List<Calendar>> dayValueInCells;
    android.support.v7.widget.Toolbar toolbar;
    Context mContext;
    ViewPager viewPager;
    Button todayButton;
    static Fragment mFragment;
    RecyclerView monthRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

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
        mContext = getContext();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewPager = (ViewPager) container;
        View v = initUiLayout();
        Bundle b = getArguments();
        if (b != null) {
            long calMillis = b.getLong("position");
            cal.setTimeInMillis(calMillis);
            setupCalMonthlyAdapter(cal);
        } else {
            setupCalMonthlyAdapter(cal);
        }
        return v;

    }

    private void setupRecViewAdapter(Toolbar toolbar){
        if (getActivity() == null || dayValueInCells == null) {
            return;
        }
        if (dayValueInCells != null) {
            monthRecyclerView.setAdapter(new MonthGridAdapter(mContext, mFragment, toolbar, monthRecyclerView, dayValueInCells, cal, reportsList,  viewPager));
        } else {
            monthRecyclerView.setAdapter(null);
        }
    }

    private void setupCalMonthlyAdapter(Calendar day) {
        reportsList = ListHelper.getAllReports(getContext());
        treatmentsList = ListHelper.getAllTreatments(getContext());
        Map<Integer, List<Calendar>> tempDayValueInCells = new HashMap<>();
        Calendar tempCal = (Calendar) day.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = tempCal.get(Calendar.DAY_OF_WEEK) + 4;
        tempCal.set(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        tempCal.setFirstDayOfWeek(Calendar.MONDAY);
        int weekNum = tempCal.get(Calendar.WEEK_OF_YEAR);
        for (int i = 0; i < MAX_CALENDAR_CELLS; i++) {
            List<Calendar> calendarList = new ArrayList<>(7);
            while (weekNum == tempCal.get(Calendar.WEEK_OF_YEAR)){
                Calendar newCal = Calendar.getInstance();
                newCal.setTimeInMillis(tempCal.getTimeInMillis());
                calendarList.add(newCal);
                tempCal.add(Calendar.DAY_OF_MONTH, 1);
            }
            weekNum = tempCal.get(Calendar.WEEK_OF_YEAR);
            tempDayValueInCells.put(i, calendarList);
        }
        dayValueInCells = tempDayValueInCells;
        setupRecViewAdapter(toolbar);
    }

    private View initUiLayout() {
        inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_month_layout,null,false);
        monthRecyclerView = (RecyclerView) v.findViewById(R.id.week_view_recycle);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false);
        monthRecyclerView.setLayoutManager(mLayoutManager);
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        setupRecViewAdapter(toolbar);
        TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tab.setVisibility(View.GONE);
        todayButton = (Button) toolbar.findViewById(R.id.tollbar_button);
        viewPager = (ViewPager) getActivity().findViewById(R.id.container);
        return v;
    }
}
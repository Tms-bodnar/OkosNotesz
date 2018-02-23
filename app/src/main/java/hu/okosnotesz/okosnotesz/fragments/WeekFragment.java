package hu.okosnotesz.okosnotesz.fragments;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hu.okosnotesz.okosnotesz.BookingActivity;
import hu.okosnotesz.okosnotesz.ChartHelper;
import hu.okosnotesz.okosnotesz.MainActivity;
import hu.okosnotesz.okosnotesz.R;
import hu.okosnotesz.okosnotesz.WeekItemClickListener;
import hu.okosnotesz.okosnotesz.adapters.WeekViewAdapter;
import hu.okosnotesz.okosnotesz.model.DBHelper;
import hu.okosnotesz.okosnotesz.model.ListHelper;
import hu.okosnotesz.okosnotesz.model.Reports;
import hu.okosnotesz.okosnotesz.model.Treatments;

public class WeekFragment extends Fragment implements WeekItemClickListener {

    Date d;
    List<Reports> reportsList;
    List<Integer> weekDays;
    List<Treatments> treatmentsList;
    List<Calendar> weeklyCalendarsDatas;
    Map<Integer, Reports[]> weeklyReports;
    List<Date> datesList;
    RecyclerView recyclerView;
    android.support.v7.widget.Toolbar toolbar;
    Button todayButton;
    WeekViewAdapter adapter;
    ScrollView scrollView;
    View v;
    RecyclerView.LayoutManager layoutManager;
    static Fragment fragment;
    Reports menuReport;
    static int cellCount;
    int clickedDay;
    Context mContext;
    ImageView delete_iv;
    private final int REQ_CODE = 9;
    ViewPager mViewPager;
    int fragmentItemPosition;
    public WeekFragment() {
    }

    public static Fragment newInstance(long cal) {
        fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putLong("day", cal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        mViewPager = (ViewPager) container;
        fragmentItemPosition = mViewPager.getAdapter().getItemPosition(fragment);
        Bundle b = getArguments();
        d = new Date((Long) b.get("day"));
        Log.d("open weekfragment", "week onCreate: "+d);
        v = initUiLayout();
        weeklyCalendarsDatas = setWeekCalendars(d);
        setWeekDays(d, v);
        new SetRecViewItems().execute();
        return v;
    }

    private View initUiLayout() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_week_view, null);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        datesList = getDayValueInCells(cal);
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tab.setVisibility(View.GONE);
        todayButton = (Button) toolbar.findViewById(R.id.tollbar_button);
        delete_iv = (ImageView) v.findViewById(R.id.delete_iv);
        delete_iv.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.week_view_recycle);
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        scrollView = (ScrollView) v.findViewById(R.id.rec_scroll);
        scrollView.setSmoothScrollingEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        setupRecViewAdapter();
        return v;
    }

    private class SetRecViewItems extends AsyncTask<Void, Void, Map<Integer, Reports[]>> {

        @Override
        protected Map<Integer, Reports[]> doInBackground(Void... voids) {
            treatmentsList = ListHelper.getAllTreatments(mContext);
            Map<Integer, Reports[]> integerMap = setDailyReports(d);
            weeklyCalendarsDatas = setWeekCalendars(d);
            return integerMap;
        }

        @Override
        protected void onPostExecute(Map<Integer, Reports[]> integerMap) {
            weeklyReports = integerMap;
            setupRecViewAdapter();
        }
    }

    private void setupRecViewAdapter() {
        if (getActivity() == null || weeklyCalendarsDatas == null || weeklyReports == null) {
            return;
        }
        if (weeklyCalendarsDatas != null && weeklyReports != null) {
            recyclerView.setAdapter(new WeekViewAdapter(this, mContext, ChartHelper.Days.values(), weeklyCalendarsDatas, treatmentsList, weeklyReports));
            adapter = (WeekViewAdapter) recyclerView.getAdapter();
        } else {
            recyclerView.setAdapter(null);
        }
    }

    public static List<Date> getDayValueInCells(Calendar day) {
        List<Date> dayValueInCells = new ArrayList<>(7);
        Calendar tempCal = (Calendar) day.clone();
        int weekNum = tempCal.get(Calendar.WEEK_OF_YEAR);
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = tempCal.get(Calendar.DAY_OF_WEEK) + 4;
        tempCal.set(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        tempCal.setFirstDayOfWeek(Calendar.MONDAY);
        Log.d("open weekfragment", "weeknum: "+ weekNum + ", "+ tempCal.get(Calendar.WEEK_OF_YEAR));
        while (weekNum != tempCal.get(Calendar.WEEK_OF_YEAR)){
                tempCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        while (weekNum == tempCal.get(Calendar.WEEK_OF_YEAR)){
            Log.d("open weekfragment", ":"+ new Date(tempCal.getTimeInMillis()) + ", "+ tempCal.get(Calendar.WEEK_OF_YEAR));
            Calendar newCal = Calendar.getInstance();
            newCal.setTimeInMillis(tempCal.getTimeInMillis());
            dayValueInCells.add(new Date(newCal.getTimeInMillis()));
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dayValueInCells;
    }

    private void setWeekDays(Date d, View v) {
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
        tempCal.setFirstDayOfWeek(Calendar.MONDAY);
        int weekNumber = tempCal.get(Calendar.WEEK_OF_YEAR);
        int yearNumber = tempCal.get(Calendar.YEAR);
        Calendar weekCal = Calendar.getInstance();
        weekCal.setFirstDayOfWeek(Calendar.MONDAY);
        for (int i = 0; i < datesList.size(); i++) {
            weekCal.setTime(datesList.get(i));
            if (weekNumber == tempCal.get(Calendar.WEEK_OF_YEAR) && !weekDays.contains(weekCal.get(Calendar.DAY_OF_MONTH))) {
                weekDays.add(weekCal.get(Calendar.DAY_OF_MONTH));
                Log.d("weeek", ""+weekDays.size());
                Log.d("weeek", "day:"+weekCal.get(Calendar.DAY_OF_MONTH));
            }
        }

        for (int i = 0; i < weekDates.length; i++) {
            weekDates[i].setText(String.valueOf(weekDays.get(i)));
            if (tempCal.get(Calendar.MONTH) == (today.get(Calendar.MONTH))
                    && tempCal.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                    && weekDays.get(i) == today.get(Calendar.DAY_OF_MONTH)) {
                weekDates[i].setBackgroundColor(Color.parseColor("#FF4081"));
            }
        }
    }


    private List<Calendar> setWeekCalendars(Date d) {
        List<Calendar> weeklyCalendars = new ArrayList<>(7);
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTimeInMillis(d.getTime());
        int weekNumber = tempCal.get(Calendar.WEEK_OF_YEAR);
        Calendar weekCal = Calendar.getInstance();
        weekCal.setFirstDayOfWeek(Calendar.MONDAY);
        for (int i = 0; i < datesList.size(); i++) {
            weekCal.setTime(datesList.get(i));
            if (weekNumber == tempCal.get(Calendar.WEEK_OF_YEAR) && !weeklyCalendars.contains(weekCal.get(Calendar.DAY_OF_MONTH))) {
                weeklyCalendars.add(weekCal);
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
        for (int i = 0; i < 31; i++) {
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
        int hour;
        int dayHour;
        int dayMinute;
        reportsList = ListHelper.getAllReports(mContext);
        Log.d("bookingdata", "replist: "+reportsList.size());
        for (Reports r : reportsList) {
            weekCal.setTimeInMillis(r.getDate());
            if (weekCal.get(Calendar.WEEK_OF_YEAR) == tempCal.get(Calendar.WEEK_OF_YEAR) &&
                    weekCal.get(Calendar.MONTH) == tempCal.get(Calendar.MONTH) &&
                    weekCal.get(Calendar.YEAR) == tempCal.get(Calendar.YEAR)) {
                int weekDay = weekCal.get(Calendar.DAY_OF_WEEK);
                hour = weekCal.get(Calendar.HOUR_OF_DAY);
                switch (hour) {
                    case 14:
                        dayHour = hour;
                        break;
                    case 7:
                        dayHour = 0;
                        break;
                    default:
                        dayHour = hour < 14 ? hour - (14 % hour) : hour + (hour % 14);
                        break;
                }
                dayMinute = weekCal.get(Calendar.MINUTE);
                int cellMod = dayMinute < 30 ? 0 : 1;
                int duration = r.getDuration();
                int cellCount = duration / 30;
                dayHour += cellMod;
                if (dayHour > -1) {
                    int temp = 0;
                    switch (weekDay) {
                        case 1:
                            if (dayHour + cellCount < sun.length) {
                                do {
                                    sun[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            } else if (dayHour + cellCount == sun.length) {
                                sun[dayHour] = r;
                            } else if (dayHour + cellCount > sun.length) {
                                cellCount = sun.length - dayHour;
                                do {
                                    sun[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            }
                            break;
                        case 2:
                            if (dayHour + cellCount < mon.length) {
                                do {
                                    mon[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            } else if (dayHour + cellCount == mon.length) {
                                mon[dayHour] = r;
                            } else if (dayHour + cellCount > mon.length) {
                                cellCount = mon.length - dayHour;
                                do {
                                    mon[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            }
                            break;
                        case 3:
                            if (dayHour + cellCount < tue.length) {
                                do {
                                    tue[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            } else if (dayHour + cellCount == tue.length) {
                                tue[dayHour] = r;
                            } else if (dayHour + cellCount > tue.length) {
                                cellCount = tue.length - dayHour;
                                do {
                                    tue[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            }
                            break;
                        case 4:
                            if (dayHour + cellCount < wed.length) {
                                do {
                                    wed[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            } else if (dayHour + cellCount == wed.length) {
                                wed[dayHour] = r;
                            } else if (dayHour + cellCount > wed.length) {
                                cellCount = wed.length - dayHour;
                                do {
                                    wed[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            }
                            break;
                        case 5:
                            if (dayHour + cellCount < thu.length) {
                                do {
                                    thu[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            } else if (dayHour + cellCount == thu.length) {
                                thu[dayHour] = r;
                            } else if (dayHour + cellCount > thu.length) {
                                cellCount = thu.length - dayHour;
                                do {
                                    thu[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            }
                            break;
                        case 6:
                            if (dayHour + cellCount < fri.length) {
                                do {
                                    fri[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            } else if (dayHour + cellCount == fri.length) {
                                fri[dayHour] = r;
                            } else if (dayHour + cellCount > fri.length) {
                                cellCount = fri.length - dayHour;
                                do {
                                    fri[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            }
                            break;
                        case 7:
                            if (dayHour + cellCount < sat.length) {
                                do {
                                    sat[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
                            } else if (dayHour + cellCount == sat.length) {
                                sat[dayHour] = r;
                            } else if (dayHour + cellCount > sat.length) {
                                cellCount = sat.length - dayHour;
                                do {
                                    sat[dayHour + temp] = r;
                                    temp += 1;
                                } while (temp < cellCount);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        menuReport = null;
        if (resultCode == 22) {
            menuReport = data.getParcelableExtra("backRep");
            Log.d("bookingdata", "weekfrag"+menuReport.getGuestName()+", "+menuReport.getExpertname()+", "+menuReport.getTreatment()+", "+menuReport.getDate()+", ");
            Date reportDate = new Date(menuReport.getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, HH:mm");
            int cellNumber = data.getIntExtra("position", 0);
            clickedDay = data.getIntExtra("day", 0);
            int cellCount = menuReport.getDuration() / 30;
            WeekViewAdapter.ViewHolder holder = (WeekViewAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(clickedDay);
            TextView[] textViewArray = holder.getTextViewArray();
            boolean free = true;
            for (int i = 0; i < cellCount; i++) {
                if (cellNumber + i < 31) {
                    if (textViewArray[cellNumber + i].getTag() != null) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.reserved) + ": " + sdf.format(reportDate), Toast.LENGTH_LONG).show();
                        free = false;
                        return;
                    }
                }
            }
            if (free) {
                for (int j = 0; j < cellCount; j++) {
                    if (cellNumber + j < 31) {
                        adapter.itemAdd(menuReport, clickedDay, cellNumber + j);
                    }
                }
                adapter.notifyItemChanged(clickedDay);
            }
            DBHelper helper = DBHelper.getHelper(mContext);
            if(helper.addReport(menuReport))  Log.d("bookingdata", "weekdb Ok"+menuReport.getGuestName()+", "+menuReport.getExpertname()+", "+menuReport.getTreatment()+", "+menuReport.getDate()+", ");
            helper.close();
            scrollView.smoothScrollTo(0, cellNumber * 60);
        }
    }
    @Override
    public void onClick( int position, Reports report, int day) {
        if (report != null) {
            View detailsView = LayoutInflater.from(mContext).inflate(R.layout.booking_details, null);
            Button detailsOK = (Button) detailsView.findViewById(R.id.booking_details_button);
            TextView detailsDate = (TextView) detailsView.findViewById(R.id.booking_date_details);
            TextView detailsGuest = (TextView) detailsView.findViewById(R.id.booking_guest_datails);
            TextView detailsTreatment = (TextView) detailsView.findViewById(R.id.booking_treatment_details);
            TextView detailsExp = (TextView) detailsView.findViewById(R.id.booking_expert_details);
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy MMMM dd, HH:mm");
            detailsDate.setText(formatter2.format(new Date(report.getDate())));
            if (report.getGuestName() != null) {
                detailsGuest.setText(report.getGuestName());
            } else detailsGuest.setText(R.string.noDatas);
            if (report.getTreatment() != null) {
                String[] tempTreName = report.getTreatment().split("ß");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < tempTreName.length; i++) {
                    sb.append(tempTreName[i]);
                }
                detailsTreatment.setText(sb);
            } else detailsTreatment.setText(R.string.noDatas);
            if (report.getExpertname() != null) {
                detailsExp.setText(report.getExpertname());
            } else detailsExp.setText(R.string.noDatas);
            final PopupWindow pop = new PopupWindow(detailsView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pop.showAtLocation(detailsView, Gravity.CENTER, 0, 0);
            detailsOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.dismiss();
                }
            });
        } else {
            Log.d("bookingdata" , "position: " + position + ", day: "+ day + ", date: "+ new Date(datesList.get(day).getTime()));
            Reports sendReport = new Reports();
            WeekViewAdapter.Hours[] hours = WeekViewAdapter.Hours.values();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(datesList.get(day).getTime());
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hours[position].getHour()));
            cal.set(Calendar.MINUTE, Integer.parseInt(hours[position].getMinute()));
            cal.set(Calendar.SECOND, 0);
            sendReport.setDate(cal.getTimeInMillis());
            Intent i = new Intent(mContext, BookingActivity.class);
            i.putExtra("newRep", sendReport);
            i.putExtra("position", position);
            i.putExtra("day", day);
            fragment.startActivityForResult(i, REQ_CODE);
        }
    }

    @Override
    public void onLongClick(int position, Reports temp, int day) {

        cellCount = temp.getDuration() == 0 ? 30 : temp.getDuration() / 30;
        WeekViewAdapter.ViewHolder holder = (WeekViewAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(day);
        TextView draggedTV = holder.getTextViewArray()[position];
        draggedTV.setTag(R.id.TEXT_VIEW_TAG, temp.getGuestName());
        ClipData.Item item = new ClipData.Item((CharSequence) draggedTV.getTag(R.id.TEXT_VIEW_TAG));
        ClipData dragData = new ClipData((CharSequence) draggedTV.getTag(R.id.TEXT_VIEW_TAG), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
        View.DragShadowBuilder dragShadow = new MyDragShadowBuilder(draggedTV);
        draggedTV.startDrag(dragData, dragShadow, null, 0);
        View.OnDragListener mDragListener = new MyOnDragListener(position, temp, day, draggedTV);
        recyclerView.setOnDragListener(mDragListener);
    }

    private boolean deleteReport(Reports deleteReport, int day) {
        DBHelper helper = DBHelper.getHelper(mContext);
        boolean successful = helper.deleteReport(deleteReport);
        helper.close();
        if (successful) {
            adapter.itemRemoved(deleteReport, day);
            adapter.notifyItemChanged(day);
        }
        return successful;
    }


    private static class MyDragShadowBuilder extends View.DragShadowBuilder {
        private static Drawable shadow;

        public MyDragShadowBuilder(View v) {
            super(v);
            shadow = new ColorDrawable(Color.GREEN);
        }

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            int width;
            int height;
            width = getView().getWidth() * 90 / 100;
            height = getView().getHeight() * 90 / 100;
            shadow.setBounds(0, 0, width, height);
            outShadowSize.set(width, height);
            outShadowTouchPoint.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }

    private class MyOnDragListener implements View.OnDragListener {

        int position;
        Reports temp;
        int day;
        int windowHeight;
        TextView tv;
        int xCoord;
        int yCoord;

        public MyOnDragListener(int position, Reports temp, int day, TextView tv) {
            this.position = position;
            this.temp = temp;
            this.day = day;
            this.tv = tv;
            windowHeight = scrollView.getHeight();
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            xCoord = (int) event.getX();
            yCoord = (int) event.getY();
            int[] coordsOfRecView = new int[2];
            int[] coordsOfImageView = new int[2];
            if (v instanceof RecyclerView) {
                v.getLocationOnScreen(coordsOfRecView);
            } else if (v instanceof ImageView) {
                v.getLocationOnScreen(coordsOfImageView);
            }
            int viewTopOfRecView = coordsOfRecView[1];
            int viewTopOfImageView = coordsOfImageView[1];
            final int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        delete_iv.setVisibility(View.VISIBLE);
                        delete_iv.bringToFront();
                        createTopDownAnimation(delete_iv, null, delete_iv.getHeight()).start();
                        v.invalidate();
                        break;
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.invalidate();
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    int value = yCoord - windowHeight / 2;
                    ObjectAnimator.ofInt(scrollView, "scrollY", value).setDuration(200).start();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    v.invalidate();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.getBackground().clearColorFilter();
                    createBottomUpAnimation(delete_iv, null, delete_iv.getHeight()).start();
                    v.invalidate();
                    if (event.getResult()) {
                        if (yCoord < viewTopOfRecView && yCoord > viewTopOfImageView) {
                            if (deleteReport(temp, day)) {
                                Snackbar.make(getView(), R.string.deleteSuccessful, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        } else if (yCoord > viewTopOfRecView) {
                            Snackbar.make(getView(), "Itt kell az áthelyezést megírni", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    } else {
                        Toast.makeText(mContext, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }

        public ObjectAnimator createTopDownAnimation(View view, AnimatorListenerAdapter listener,
                                                     float distance) {
            view.setTranslationY(-distance);
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0);
            animator.setDuration(500);
            animator.removeAllListeners();
            if (listener != null) {
                animator.addListener(listener);
            }
            return animator;
        }

        private ObjectAnimator createBottomUpAnimation(View view,
                                                       AnimatorListenerAdapter listener, float distance) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -distance);
            animator.setDuration(500);
            animator.removeAllListeners();
            if (listener != null) {
                animator.addListener(listener);
            }
            return animator;
        }
    }

}



package okosnotesz.hu.okosnotesz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okosnotesz.hu.okosnotesz.BookingActivity;
import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.adapters.GridAdapter;
import okosnotesz.hu.okosnotesz.adapters.WeekGridAdapter;
import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Experts;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Reports;
import okosnotesz.hu.okosnotesz.model.Treatments;

public class CalendarActivity extends Fragment {

    private TextView nextMonth;
    private TextView prevMonth;
    private TextView currDate;
    private GridView calGrid;
    private GridView weekGrid;
    LayoutInflater inflater;
    private List<TextView> weeklyDates;
    private List<ViewFlipper> weekGridFlipperList;
    private ViewFlipper vf;
    private float coord = 0;
    private DisplayMetrics dm;
    private Calendar cal = Calendar.getInstance();
    private Calendar clickedCal = Calendar.getInstance();
    private final int MAX_CALENDAR_CELLS = 49;
    private GridAdapter gridAdapter;
    private WeekGridAdapter weekGridAdapter;
    private List<Reports> reportsList;
    private List<Treatments> treatmentsList;
    private Reports sendReport;
    private Reports getReport;
    private final int REQ_CODE = 9;
    SimpleDateFormat formatter;
    MaterialDialog dialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("xxx", "oncreate Calendar");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = initUiLayout();
        setupCalMonthlyAdapter(cal, 0);
        setPrevMonth();
        setNextMonth();
        return v;

    }


    private void setupCalMonthlyAdapter(Calendar day, int animCode) {
        if (animCode == 0) {
            vf.setInAnimation(inToTopAnimation());
        }
        vf.setDisplayedChild(0);

        reportsList = ListHelper.getAllReports(getContext());
        treatmentsList = ListHelper.getAllTreatments(getContext());
        formatter = new SimpleDateFormat("yyyy MMMM");
        String dateString = formatter.format(day.getTime());
        currDate.setText(dateString);
        final Calendar finalDay = day;
        currDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupCalMonthlyAdapter(finalDay, 0);
            }
        });

        MaterialDialog.Builder progressDialog = new MaterialDialog.Builder(getContext());
        progressDialog.title(R.string.booking);
        progressDialog.content(getString(R.string.loading));
        progressDialog.progress(true,0);
        dialog = progressDialog.build();

        final List<Date> dayValueInCells = getDayValueInCells(day);
        gridAdapter = new GridAdapter(getContext(), dayValueInCells, day, reportsList, treatmentsList);
        calGrid.setAdapter(gridAdapter);
        calGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.show();

                new Thread() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (this) {
                                    if (animCode == 0) {
                                    }
                                    vf.setDisplayedChild(1);
                                    for (int i = 0; i < weekGridFlipperList.size(); i++) {
                                        weekGridFlipperList.get(i).setDisplayedChild(1);
                                    }
                                    clickedCal.setTimeInMillis(dayValueInCells.get(position).getTime());
                                    cal = clickedCal;
                                    Log.d("xx", clickedCal.get(Calendar.DAY_OF_MONTH) + " click");
                                    Log.d("wwwx", "threadstart");
                                    setupCalWeeklyAdapter(clickedCal, 0);
                                }
                            }
                        });
                    }
                }.start();
            }
        });

    }


    private void setupCalWeeklyAdapter(final Calendar clickedDate, int animCode) {

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
            weeklyDates.get(i).setText(String.valueOf(temp.get(Calendar.DAY_OF_MONTH)));
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
            weekGridAdapter = new WeekGridAdapter(getContext(), Hours.values(), dailyReports);
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
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           // weekGridFlipperList.get(msg.what).setInAnimation(inToTopAnimation());
           // weekGridFlipperList.get(msg.what).setDisplayedChild(0);
           // weekGridFlipperList.get(msg.what).setInAnimation(inToTopAnimation());
           // weekGridFlipperList.get(msg.what).setDisplayedChild(0);
           // vf.setInAnimation(inToTopAnimation());
            //vf.setDisplayedChild(1);
            dialog.dismiss();
        }
    };

    private List<Date> getDayValueInCells(Calendar day) {
        List<Date> dayValueInCells = new ArrayList<Date>();
        Calendar tempCal = (Calendar) day.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = tempCal.get(Calendar.DAY_OF_WEEK) + 4;
        tempCal.set(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while (dayValueInCells.size() < MAX_CALENDAR_CELLS) {
            dayValueInCells.add(tempCal.getTime());
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dayValueInCells;
    }


    private View initUiLayout() {
        inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_calendar_view, null);
        nextMonth = (TextView) v.findViewById(R.id.next_month);
        prevMonth = (TextView) v.findViewById(R.id.previous_month);
        currDate = (TextView) v.findViewById(R.id.display_current_date);
        calGrid = (GridView) v.findViewById(R.id.calendar_grid);
        weekGridFlipperList = new ArrayList<>(7);
        weekGridFlipperList.add((ViewFlipper) v.findViewById(R.id.monvf));
        weekGridFlipperList.add((ViewFlipper) v.findViewById(R.id.tuenvf));
        weekGridFlipperList.add((ViewFlipper) v.findViewById(R.id.wedvf));
        weekGridFlipperList.add((ViewFlipper) v.findViewById(R.id.thuvf));
        weekGridFlipperList.add((ViewFlipper) v.findViewById(R.id.frivf));
        weekGridFlipperList.add((ViewFlipper) v.findViewById(R.id.satvf));
        weekGridFlipperList.add((ViewFlipper) v.findViewById(R.id.sunvf));
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
                if (vf.getDisplayedChild() == 0) {
                    cal.add(Calendar.MONTH, 1);
                    vf.setInAnimation(toLeftAnimation());
                    setupCalMonthlyAdapter(cal, 1);

                } else {
                    dialog.show();

                    new Thread() {
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (this) {

                                        for (int i = 0; i < weekGridFlipperList.size(); i++) {
                                            weekGridFlipperList.get(i).setDisplayedChild(1);
                                        }
                                        clickedCal.add(Calendar.WEEK_OF_YEAR, 1);
                                        vf.setInAnimation(toLeftAnimation());
                                        cal = clickedCal;
                                        Log.d("xx", clickedCal.get(Calendar.DAY_OF_MONTH) + " click");
                                        Log.d("wwwx", "threadstart");
                                        setupCalWeeklyAdapter(clickedCal, 0);
                                    }
                                }
                            });
                        }
                    }.start();

                }
            }
        });
    }

    private void setPrevMonth() {
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vf.getDisplayedChild() == 0) {
                    cal.add(Calendar.MONTH, -1);
                    vf.setInAnimation(toRightAnimation());
                    setupCalMonthlyAdapter(cal, 1);

                } else {
                    dialog.show();

                    new Thread() {
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (this) {

                                        for (int i = 0; i < weekGridFlipperList.size(); i++) {
                                            weekGridFlipperList.get(i).setDisplayedChild(1);
                                        }
                                        clickedCal.add(Calendar.WEEK_OF_YEAR, 1);
                                        vf.setInAnimation(toLeftAnimation());
                                        cal = clickedCal;
                                        Log.d("xx", clickedCal.get(Calendar.DAY_OF_MONTH) + " click");
                                        Log.d("wwwx", "threadstart");
                                        setupCalWeeklyAdapter(clickedCal, 0);
                                    }
                                }
                            });
                        }
                    }.start();

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

    private void booking(Calendar temp) {
        Toast.makeText(getContext(), temp.getTime().toString() + "", Toast.LENGTH_SHORT).show();
        sendReport = new Reports();
        sendReport.setDate(temp.getTimeInMillis());
        Intent i = new Intent(getContext(), BookingActivity.class);
        i.putExtra("newRep", sendReport);
        startActivityForResult(i, REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        formatter = new SimpleDateFormat("yyyy MM dd HH:mm");
        Calendar repCal = Calendar.getInstance();
        Reports backRep = null;
        if (resultCode == 22) {
            backRep = data.getParcelableExtra("backRep");
            Date repDate = new Date(backRep.getDate());
            repCal.setTime(repDate);
            reportsList.add(backRep);
            DBHelper helper = DBHelper.getHelper(getContext());
            helper.addReport(backRep);
            helper.close();
            setupCalWeeklyAdapter(repCal, 0);
        }

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


    public enum Hours {

        SEVEN("07", "00"), SEVENTTY("07", "30"), EIGTH("08", "00"), EIGTHTTY("08", "30"), NINE("09", "00"),
        NINETTY("09", "30"), TEN("10", "00"), TENTTY("10", "30"), ELEVEN("11", "00"), ELEVENTTY("11", "30"),
        TWELVE("12", "00"), TWELVETTY("12", "30"), THIRTEEN("13", "00"), THIRTEENTTY("13", "30"),
        FOURTEEN("14", "00"), FOURTEENTTY("14", "30"), FIFTEEN("15", "00"), FIFTEENTTY("15", "30"),
        SIXTEEN("16", "00"), SIXTEENTTY("16", "30"), SEVENTEEN("17", "00"), SEVENTEENTTY("17", "30"),
        EIGHTEEN("18", "00"), EIGHTEENTTY("18", "30"), NINETEEN("19", "00"), NINETEENTTY("19", "30"),
        TWENTY("20", "00"), TWENTYTTY("20", "30"), TWENTYONE("21", "00"), TWENTYONETTY("21", "30"),
        TWENTYTWO("22", "00");


        private String hour;
        private String minute;

        Hours(String hour, String minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public String getHour() {
            return hour;
        }

        public String getMinute() {
            return minute;
        }
    }

}


package hu.okosnotesz.okosnotesz.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hu.okosnotesz.okosnotesz.BookingActivity;
import hu.okosnotesz.okosnotesz.ChartHelper;
import hu.okosnotesz.okosnotesz.R;
import hu.okosnotesz.okosnotesz.WeekItemClickListener;
import hu.okosnotesz.okosnotesz.fragments.WeekFragment;
import hu.okosnotesz.okosnotesz.model.ListHelper;
import hu.okosnotesz.okosnotesz.model.Reports;
import hu.okosnotesz.okosnotesz.model.Treatments;

/**
 * Created by user on 2017.11.23..
 */

public class WeekViewAdapter extends RecyclerView.Adapter<WeekViewAdapter.ViewHolder> {

    WeekFragment fragment;
    RecyclerView rv;
    int position;
    private final Context context;
    ViewHolder holder;
    ChartHelper.Days[] days;
    Map<Integer, Reports[]> dailymap;
    List<TextView> textViewList;
    List<Treatments> treatmentsList;
    private final int REQ_CODE = 9;
    int delayAnimate = 150;

    public WeekViewAdapter(WeekFragment fragment, Context context, ChartHelper.Days[] days, List<Treatments> treatmentsList, Map<Integer, Reports[]> dailymap) {
        this.fragment = fragment;
        this.context = context;
        this.days = days;
        this.dailymap = dailymap;
        this.treatmentsList = treatmentsList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_week_item, parent, false);
        rv = (RecyclerView) parent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.holder = holder;
        this.position = position;
        textViewList = Arrays.asList(holder.getTextViewArray());
        Map<Reports, Integer> colorMap = getColors(position);
        Hours[] hours = Hours.values();
        for (int i = 0; i < hours.length; i++) {
            if (i % 2 == 0 && position == 0) {
                textViewList.get(i).setText(hours[i].getHour() + ":" + hours[i].getMinute());
            }
        }
        Reports[] dailyReports = dailymap.get(position);
        for (int i = 0; i < dailyReports.length; i++) {
            if (dailyReports[i] != null && dailyReports[i].getTreatment() != null) {
                Treatments[] treList = ListHelper.getTreatmentsOfReport(treatmentsList, dailyReports[i].getTreatment());
                textViewList.get(i).setBackgroundColor(colorMap.get(dailyReports[i]));
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textViewList.get(i).getLayoutParams();
                lp.setMargins(0, 0, 0, 0);
                textViewList.get(i).setLayoutParams(lp);
                textViewList.get(i).setTag(dailyReports[i]);
                Log.d("bookindata", "tag1: " +  i + "--"+textViewList.get(i).getTag());
                String guestName = dailyReports[i].getGuestName() + ", ";
                String treName = dailyReports[i].getTreatment().replace('ß', ',');
                int price = 0;
                for (int k = 0; k < treList.length; k++) {
                    if (treList[k] != null) {
                        price = price + treList[k].getPrice();
                    }
                }
                if (i == 0 ? textViewList.get(i).getTag() == dailyReports[i] : textViewList.get(i - 1).getTag() != dailyReports[i]) {
                    if (dailyReports[i].getDuration() <= 30 | textViewList.size() == i) {
                        textViewList.get(i).setText(guestName + treName + price);
                    } else if (dailyReports[i].getDuration() > 30 && textViewList.size() - 1 != i) {
                        textViewList.get(i).setText(guestName);
                        textViewList.get(i + 1).setText(treName + price);
                    }
                }
            }
            int finalI = i;
            textViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onClick(finalI, dailyReports[finalI], position);
                }
            });
            textViewList.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (v.getTag() != null && dailyReports[finalI] != null) {
                        fragment.onLongClick(finalI, dailyReports[finalI], position);
                        return true;
                    } else {
                        fragment.onClick(finalI, dailyReports[finalI], position);

                    }
                    return true;
                }
            });
        }
        holder.setTextViewArray((TextView[]) textViewList.toArray());
        holder.itemView.setVisibility(View.INVISIBLE);
        setAnimation(holder.itemView);
    }


    private void setAnimation(View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
                if(view!=null){
                    view.startAnimation(animation);
                    view.setVisibility(View.VISIBLE);
                }
            }
        }, delayAnimate);
        delayAnimate+=150;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    public Map<Reports, Integer> getColors(int position) {
        Map<Reports, Integer> colorMap = new HashMap<>();
        Reports[] dailyReports = dailymap.get(position);
        String[] colorArray = context.getResources().getStringArray(R.array.appColors);
        Random r = new Random();
        for (int i = 0; i < dailyReports.length; i++) {
            int j = r.nextInt((30 - 1) + 1) + 1;
            colorMap.put(dailyReports[i], Color.parseColor(colorArray[j]));
        }
        return colorMap;
    }

    public void itemAdd(Map<Integer, Reports[]> newDailymap) {
        dailymap.clear();
        dailymap.putAll(newDailymap);
    }


    public List<TextView> getTextViewList() {
        return textViewList;
    }

    public void itemRemoved(Reports menuReport, int day) {
        for (int i = 0; i < dailymap.get(day).length; i++) {
            if (menuReport != null && menuReport == dailymap.get(day)[i]) {
                dailymap.get(day)[i] = null;
            }
        }


    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView7, textView730, textView8, textView830, textView9, textView930, textView10,
                textView1030, textView11, textView1130, textView12, textView1230, textView13,
                textView1330, textView14, textView1430, textView15, textView1530, textView16,
                textView1630, textView17, textView1730, textView18, textView1830, textView19,
                textView1930, textView20, textView2030, textView21, textView2130, textView22;
        TextView[] textViewArray;

        public ViewHolder(View itemView) {
            super(itemView);
            textView7 = (TextView) itemView.findViewById(R.id.tv_week_hour_7);
            textView730 = (TextView) itemView.findViewById(R.id.weekhalf7);
            textView8 = (TextView) itemView.findViewById(R.id.tv_week_hour_8);
            textView830 = (TextView) itemView.findViewById(R.id.weekhalf8);
            textView9 = (TextView) itemView.findViewById(R.id.tv_week_hour_9);
            textView930 = (TextView) itemView.findViewById(R.id.weekhalf9);
            textView10 = (TextView) itemView.findViewById(R.id.tv_week_hour_10);
            textView1030 = (TextView) itemView.findViewById(R.id.weekhalf10);
            textView11 = (TextView) itemView.findViewById(R.id.tv_week_hour_11);
            textView1130 = (TextView) itemView.findViewById(R.id.weekhalf11);
            textView12 = (TextView) itemView.findViewById(R.id.tv_week_hour_12);
            textView1230 = (TextView) itemView.findViewById(R.id.weekhalf12);
            textView13 = (TextView) itemView.findViewById(R.id.tv_week_hour_13);
            textView1330 = (TextView) itemView.findViewById(R.id.weekhalf13);
            textView14 = (TextView) itemView.findViewById(R.id.tv_week_hour_14);
            textView1430 = (TextView) itemView.findViewById(R.id.weekhalf14);
            textView15 = (TextView) itemView.findViewById(R.id.tv_week_hour_15);
            textView1530 = (TextView) itemView.findViewById(R.id.weekhalf15);
            textView16 = (TextView) itemView.findViewById(R.id.tv_week_hour_16);
            textView1630 = (TextView) itemView.findViewById(R.id.weekhalf16);
            textView17 = (TextView) itemView.findViewById(R.id.tv_week_hour_17);
            textView1730 = (TextView) itemView.findViewById(R.id.weekhalf17);
            textView18 = (TextView) itemView.findViewById(R.id.tv_week_hour_18);
            textView1830 = (TextView) itemView.findViewById(R.id.weekhalf18);
            textView19 = (TextView) itemView.findViewById(R.id.tv_week_hour_19);
            textView1930 = (TextView) itemView.findViewById(R.id.weekhalf19);
            textView20 = (TextView) itemView.findViewById(R.id.tv_week_hour_20);
            textView2030 = (TextView) itemView.findViewById(R.id.weekhalf20);
            textView21 = (TextView) itemView.findViewById(R.id.tv_week_hour_21);
            textView2130 = (TextView) itemView.findViewById(R.id.weekhalf21);
            textView22 = (TextView) itemView.findViewById(R.id.tv_week_hour_22);
            textViewArray  = new TextView[]{textView7, textView730, textView8, textView830, textView9, textView930, textView10,
                    textView1030, textView11, textView1130, textView12, textView1230, textView13,
                    textView1330, textView14, textView1430, textView15, textView1530, textView16,
                    textView1630, textView17, textView1730, textView18, textView1830, textView19,
                    textView1930, textView20, textView2030, textView21, textView2130, textView22};

        }

        public TextView[] getTextViewArray() {
            return textViewArray;
        }

        public void setTextViewArray(TextView[] textViewArray) {
            this.textViewArray = textViewArray;
        }
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

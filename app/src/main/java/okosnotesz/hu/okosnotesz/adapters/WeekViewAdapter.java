package okosnotesz.hu.okosnotesz.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okosnotesz.hu.okosnotesz.ChartHelper;
import okosnotesz.hu.okosnotesz.R;

/**
 * Created by user on 2017.11.23..
 */

public class WeekViewAdapter extends RecyclerView.Adapter<WeekViewAdapter.ViewHolder> {



   ChartHelper.Days[] days;
   Map<Integer, boolean[]> dailymap;
   List<TextView> textViewList;

    public WeekViewAdapter(ChartHelper.Days[] days, Map<Integer, boolean[]> dailymap){
        this.days = days;
        this.dailymap = dailymap;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_week_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        textViewList = new ArrayList<>(Arrays.asList( holder.textView7, holder.textView730,
                holder.textView8, holder.textView830,  holder.textView9, holder.textView930,
                holder.textView10, holder.textView1030, holder.textView11, holder.textView1130,
                holder.textView12, holder.textView1230, holder.textView13, holder.textView1330,
                holder.textView14, holder.textView1430, holder.textView15, holder.textView1530,
                holder.textView16, holder.textView1630, holder.textView17, holder.textView1730,
                holder.textView18, holder.textView1830, holder.textView19, holder.textView1930,
                holder.textView20, holder.textView2030, holder.textView21, holder.textView2130,
                holder.textView22, holder.textView2230));
        Hours[] hours = Hours.values();
        for(int i = 0; i < hours.length; i++){
            if(i%2==0) {
                textViewList.get(i).setText(hours[i].getHour() + ":" + hours[i].getMinute());
            }
        }
        boolean[] bookingTimes = dailymap.get(position);
        for(int i = 0; i < bookingTimes.length; i++){
            if(bookingTimes[i]){
                textViewList.get(i).setBackgroundColor(Color.LTGRAY);
            }
        }


    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView7, textView730, textView8,textView830,  textView9,textView930,  textView10,
                textView1030, textView11,textView1130, textView12, textView1230, textView13,
                textView1330, textView14, textView1430, textView15, textView1530, textView16,
                textView1630, textView17, textView1730, textView18, textView1830, textView19,
                textView1930, textView20, textView2030, textView21, textView2130, textView22, textView2230;




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
            textView2230 = (TextView) itemView.findViewById(R.id.weekhalf22);

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

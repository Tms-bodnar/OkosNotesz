package hu.okosnotesz.okosnotesz.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

import java.util.List;

import hu.okosnotesz.okosnotesz.R;

/**
 * Created by user on 2017.09.14..
 */

public class CustomChartsAdapter extends ArrayAdapter<BarData> {

    ViewHolder holder;

    public CustomChartsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BarData> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BarData data = getItem(position);

        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chart_list_item,null);
            holder.chart = (BarChart) convertView.findViewById(R.id.barChart);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.chart.setDrawGridBackground(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);

        YAxis leftAxis = holder.chart.getAxisLeft();

        leftAxis.setLabelCount(5);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = holder.chart.getAxisRight();

        rightAxis.setLabelCount(5);
        rightAxis.setSpaceTop(15f);

        // set data
        holder.chart.setData(data);



        // do not forget to refresh the chart
        holder.chart.invalidate();
        holder.chart.animateY(700);

        return convertView;
    }
private class ViewHolder{
    BarChart chart;
}
}

package okosnotesz.hu.okosnotesz.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.adapters.CustomChartsAdapter;
import okosnotesz.hu.okosnotesz.model.Sales;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment {


    private List<Sales> salesList ;
    private List<String> chartSalesLabelsWeekly = new ArrayList<>();
    private List<BarEntry> salesBarEntryWeekly=new ArrayList<>();
    private List<Entry> entries;
    private List<BarData> barDataList;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports, container, false);
        ListView lv = (ListView) view.findViewById(R.id.chartList);
        barDataList = new ArrayList<>(2);
        barDataList.add(0, getSalesdatas());
       // barDataList.add(0, getBookingdatas());
        CustomChartsAdapter cca = new CustomChartsAdapter(getContext(),0, barDataList);
        lv.setAdapter(cca);
        Log.d("TAG", "onCreateView Reports");
        return view;
    }

    private BarData getBookingdatas() {
            BarData bd = new BarData();

        return null;
    }

    private BarData getSalesdatas() {
        BarData bd = new BarData();
        /*ChartHelper ch = new ChartHelper();
        salesList = ListHelper.getAllSales(getContext());
        if(!salesList.isEmpty()) {
            ArrayList<BarData> ci;
            int values = 0;
            int i = 0;
            String tempDate = salesList.get(0).getDate();
            String[] splitted = tempDate.split("\\s+", 3);
            splitted[1] = splitted[1].replace('.', ' ').trim();
            int year = Integer.parseInt(splitted[0]);
            int month = Integer.parseInt(splitted[1])-1;
            int date = Integer.parseInt(splitted[2]);
            Calendar mcal = new GregorianCalendar(year, month , date);
            Log.d("xxx", mcal.toString()+" :mcal");
            int week = mcal.get(Calendar.WEEK_OF_YEAR);
            int dayofmonth = mcal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int day = mcal.get(Calendar.DAY_OF_WEEK);
            List<Days> days = ch.getChartWeeklyLabels(getContext());
            List<Months> months = ch.getChartMonthlyLabels(getContext());
            String dayname="";
            for (Days d : days) {
                if(d.value()==day){
                   dayname= getString(d.longName);
                Log.d("xxx", getString(d.longName));
                }
            }
            Log.d("xxx", "" + week + ", daycount of week:" + dayname + ", dateday:  " + dayofmonth );
            Log.d("xxx", splitted[0] + splitted[1] + splitted[2]);
            for (int k = 1; k < dayofmonth+1; k++) {
                BarEntry be = new BarEntry(0f, k);
                chartSalesLabelsWeekly.add("");
                salesBarEntryWeekly.add(be);
            }
            for (int k = 0; k <= dayofmonth; k++) {
                for (int j = 0; j < salesList.size(); j++) {
                    String dateLabel = salesList.get(j).getDate();
                    splitted = dateLabel.split("\\s+", 3);
                    if (k == Integer.parseInt(splitted[2])) {
                        if (tempDate.equals(dateLabel)) {
                            values += salesList.get(j).getValue();
                            salesBarEntryWeekly.set(k, new BarEntry(values, k));

                        } else {
                            BarEntry be = new BarEntry(values, k);
                            salesBarEntryWeekly.remove(k);
                            salesBarEntryWeekly.add(k, be);
                            values = salesList.get(j).getValue();
                            tempDate = dateLabel;
                            Log.d("xxx", splitted[1] + splitted[2]);
                            Log.d("xxx", be.getVal() + ", " + be.getXIndex() + ".. " + k);
                        }
                        if (salesList.get(j).equals(salesList.get(salesList.size() - 1))) {
                            tempDate = "...";
                            Log.d("xxx", tempDate);
                        }
                        if (!tempDate.equals(dateLabel)) {


                        }
                    }
                }
            }
            BarDataSet ds = new BarDataSet(salesBarEntryWeekly, getString(R.string.commercial));
            ds.setColors(ColorTemplate.PASTEL_COLORS);
            ds.setDrawValues(false);


            bd = new BarData(chartSalesLabelsWeekly, ds);
        }*/
        return null;
    }

    private BarData getIncomeDatas() {
        return null;
    }


}

/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports,container,false);
        BarChart chartSales = (BarChart) view.findViewById(R.id.chartSales);
        ChartHelper ch = new ChartHelper();
        salesBarEntryWeekly = new ArrayList<>();
        chartSalesLabelsWeekly = new ArrayList<>();
        ArrayList<String> weekDays = ch.getsChartWeeklyLabelsShort(getContext());
        salesList = getAllSales();
        ArrayList<BarData> ci;
        int values = 0;
        int i = 0;
        String tempDate = salesList.get(0).getDate();
        String[] splitted = tempDate.split("\\s+", 3);
        Calendar mcal = new GregorianCalendar(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1].replace('.',' ').trim())-1, Integer.parseInt(splitted[2]));
        Log.d("xxx", mcal.toString());
        int week = mcal.get(Calendar.WEEK_OF_YEAR);
        int dayofmonth = mcal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int day = mcal.get(Calendar.DAY_OF_WEEK);
        Log.d("xxx",""+week+"day:"+day +"..."+dayofmonth);
       // chartSalesLabelsWeekly.add(splitted[1]+splitted[2]);
        Log.d("xxx",splitted[0]+ splitted[1]+splitted[2]);
        for (int k = 0; k < dayofmonth; k++) {;
            BarEntry be = new BarEntry(0f,k);
            Log.d("xxx", "nap: " +k);
            chartSalesLabelsWeekly.add("");
            salesBarEntryWeekly.add(be);
            Log.d("xxx", ""+salesBarEntryWeekly.size());
        }
        for (int k = 0; k <= dayofmonth; k++) {
            for (int j = 0; j < salesList.size(); j++) {
                String dateLabel = salesList.get(j).getDate();
                splitted = dateLabel.split("\\s+", 3);
                if (k == Integer.parseInt(splitted[2])) {
                    if (tempDate.equals(dateLabel)) {
                        values += salesList.get(j).getValue();
                        salesBarEntryWeekly.set(k, new BarEntry(values,k));
                    }else{
                        BarEntry be = new BarEntry(values, k);
                        salesBarEntryWeekly.remove(k);
                        salesBarEntryWeekly.add(k,be);
                        values = salesList.get(j).getValue();
                        tempDate = dateLabel;
                        Log.d("xxx", splitted[1] + splitted[2]);
                        Log.d("xxx" , be.getVal()+", "+be.getXIndex()+ ".. "+k);
                    }
                    if (salesList.get(j).equals(salesList.get(salesList.size() - 1))) {
                        tempDate = "...";
                        Log.d("xxx", tempDate);
//                        chartSalesLabelsWeekly.remove(chartSalesLabelsWeekly.size() - 1);
                    }
                    if (!tempDate.equals(dateLabel)) {


                    }
                }
            }
        }
        BarDataSet ds = new BarDataSet(salesBarEntryWeekly, getString(R.string.commercial));
        BarData bd = new BarData(chartSalesLabelsWeekly, ds);

        ds.setColors(ColorTemplate.PASTEL_COLORS);

        ds.setDrawValues(false);

        chartSales.setData(bd);
        chartSales.setBackgroundColor(getResources().getColor(R.color.Color14));
        chartSales.setDrawGridBackground(false);
        chartSales.setDescription(getString(R.string.commercial)+"  "+ Integer.parseInt(splitted[1].replace('.',' ').trim())+  getString(R.string.reportsOfMonth));
        chartSales.setVisibleXRange((float)dayofmonth);
        chartSales.invalidate();


        PieChart pc = (PieChart) view.findViewById(R.id.chartBookings);
        entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(3f, 1));
        entries.add(new Entry(6f, 2));
        PieDataSet pds = new PieDataSet(entries, getString(R.string.booking));
        pds.setColors(ColorTemplate.PASTEL_COLORS);
        ArrayList<String> pieLabels = new ArrayList<>();
        pieLabels = ch.getChartMonthlylabelsShort(getContext());
        PieData pd = new PieData(pieLabels, pds);
        pc.setData(pd);
        pc.setBackgroundColor(getResources().getColor(R.color.Color14));
        pc.setHoleColor(getResources().getColor(R.color.Color14));
        return view;
    }


    private List<Sales> getAllSales() {
        List<Sales> sList = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(getActivity());
        Cursor cursor = helper.getAllSales();
        cursor.moveToFirst();
        try {
            while (!cursor.isAfterLast()) {
                Sales s = new Sales();
                s.setProductID(cursor.getInt(cursor.getColumnIndex("saleProduct")));
                s.setGuestName(cursor.getString(cursor.getColumnIndex("saleGuest")));
                s.setDate(cursor.getString(cursor.getColumnIndex("saleDate")));
                s.setQuantity(cursor.getInt(cursor.getColumnIndex("saleQuantity")));
                s.setValue(cursor.getInt(cursor.getColumnIndex("saleValue")));
                sList.add(s);
                cursor.moveToNext();
            }
        }
        finally{
            cursor.close();
            helper.close();
            }
        if(sList.isEmpty()){
            sList.add(new Sales(0, getString(R.string.noDatas), "", getString(R.string.noDatas)));
        }
        return  sList;
    }

    public List<BarEntry> getWeeklyBarEntry(List<Sales> salesList){
        List<BarEntry> barEntries = new ArrayList<>();

        return barEntries;
    }

    public List<BarEntry> getMonthlyBarEntry(List<Sales> salesList){

        return null;
    }*/

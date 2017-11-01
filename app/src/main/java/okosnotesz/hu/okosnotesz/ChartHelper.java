package okosnotesz.hu.okosnotesz;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.MonthDisplayHelper;

import com.github.mikephil.charting.data.BarEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by user on 2017.09.07..
 */

public class ChartHelper {

    static ArrayList<Months> chartMonthlyLabels;
    ArrayList<BarEntry> salesMonthlyBarEntry;
    ArrayList<Days> chartWeeklyLabels;
    ArrayList<BarEntry> salesWeeklyBarEntry;

    static {

    }

    public static ArrayList<Months> getChartMonthlyLabels(Context c) {
        chartMonthlyLabels = new ArrayList<>();
        for (Months month : Months.values()) {
            chartMonthlyLabels.add(month);
        }
        return chartMonthlyLabels;
    }

    public ArrayList<Days> getChartWeeklyLabels(Context c) {
        chartWeeklyLabels = new ArrayList<>();
        for (Days d : Days.values()) {
            chartWeeklyLabels.add(d);
        }
        return chartWeeklyLabels;
    }


    public enum Months {

        JAN(1, R.string.january), FEB(2, R.string.february), MAR(3, R.string.march), APR(4, R.string.april),
        MAY(5, R.string.may), JUN(6, R.string.june), JUL(7, R.string.july), AUG(8, R.string.august),
        SEP(9, R.string.september), OKT(10, R.string.october), NOV(11, R.string.november), DEC(12, R.string.december);

        private int i;
        public int longName;

        Months(int i, int longname) {
            this.i = i;
            this.longName = longname;
        }

        public int value() {
            return i;
        }

        private Months(int resId) {
            this.longName = resId;
        }

        public String getMonth() {
            return (String.valueOf(this));
        }

    }

    public enum Days {
        MON(2, R.string.monday), TUE(3, R.string.tuesday), WED(4, R.string.wednesday),
        THU(5, R.string.thursday), FRI(6, R.string.friday), SAT(7, R.string.saturday), SUN(1, R.string.sunday);

        private int i;
        public int longName;

        Days(int i, int longName) {
            this.i = i;
            this.longName = longName;
        }

        public int value() {
            return i;
        }

        private Days(int resId) {
            this.longName = resId;
        }

        public String getDay() {
            return String.valueOf(this);
        }
    }

    }


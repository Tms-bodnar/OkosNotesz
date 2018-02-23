package hu.okosnotesz.okosnotesz.adapters;

import android.content.Context;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hu.okosnotesz.okosnotesz.MainActivity;
import hu.okosnotesz.okosnotesz.R;
import hu.okosnotesz.okosnotesz.fragments.AdminExpertsFragment;
import hu.okosnotesz.okosnotesz.fragments.AdminGuestsFragment;
import hu.okosnotesz.okosnotesz.fragments.AdminProductsFragment;
import hu.okosnotesz.okosnotesz.fragments.AdminTreatmentsFragment;
import hu.okosnotesz.okosnotesz.fragments.CalendarFragment;
import hu.okosnotesz.okosnotesz.fragments.ReportsFragment;
import hu.okosnotesz.okosnotesz.fragments.SalesFragment;
import hu.okosnotesz.okosnotesz.fragments.WeekFragment;

/**
 * Created by user on 2017.11.03..
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    int numOfTabs;
    long clickedDate;
    int fragmentType;



    public PagerAdapter(Context context, FragmentManager supportFragmentManager, int i, int j) {
        super(supportFragmentManager);
        this.numOfTabs = i;
        this.mContext = context;
        this.fragmentType = j;

    }

    public PagerAdapter(Context context, FragmentManager supportFragmentManager, int i, long date, int j) {
        super(supportFragmentManager);
        this.numOfTabs = i;
        this.mContext = context;
        this.clickedDate = date;
        this.fragmentType = j;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (fragmentType) {
            case 4:
                switch (position) {
                    case 0:
                        fragment = new AdminGuestsFragment();
                        break;
                    case 1:
                        fragment = new AdminExpertsFragment();
                        break;
                    case 2:
                        fragment = new AdminTreatmentsFragment();
                        break;
                    case 3:
                        fragment = new AdminProductsFragment();
                        break;
                }
                break;
            case 3:
                Calendar cal = Calendar.getInstance();
                long calLong = cal.getTimeInMillis();
                if (position == numOfTabs/2) {
                    MainActivity.clickedPosition = position;
                    fragment = CalendarFragment.newInstance(calLong);
                    Log.d("clickmonthpager"," adapterposition: "+ position);
                } else {
                    MainActivity.clickedPosition = position;
                    cal.add(Calendar.MONTH, position - numOfTabs / 2);
                    calLong = cal.getTimeInMillis();
                    fragment = CalendarFragment.newInstance(calLong);
                    Log.d("clickmonthpager"," adapterposition: "+ position);
                }
                break;
            case 2:
                        fragment = new SalesFragment();
                break;
            case 1:
                        fragment = new ReportsFragment();
                break;
            case 5:
                if (position == numOfTabs/2) {
                    fragment = WeekFragment.newInstance(clickedDate);
                } else {
                    Calendar clickedDay = Calendar.getInstance();
                    clickedDay.setTimeInMillis(clickedDate);
                    clickedDay.add(Calendar.DAY_OF_YEAR, (position - numOfTabs / 2) * 7);
                    fragment = WeekFragment.newInstance(clickedDay.getTimeInMillis());
                }
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence pageTitle = "";
        SimpleDateFormat formatter;
        switch (numOfTabs) {
            case 4:
                switch (position) {
                    case 0:
                        pageTitle = mContext.getResources().getString(R.string.guests);
                        break;
                    case 1:
                        pageTitle = mContext.getResources().getString(R.string.expert);
                        break;
                    case 2:
                        pageTitle = mContext.getResources().getString(R.string.treatment);
                        break;
                    case 3:
                        pageTitle = mContext.getResources().getString(R.string.goods);
                        break;
                }
                break;
            case 2:
                switch (position) {
                    case 0:
                        pageTitle = mContext.getResources().getString(R.string.commercial);

                        break;
                    case 1:
                        pageTitle = mContext.getResources().getString(R.string.reports);
                        break;
                }
                break;
            case 49:
                formatter = new SimpleDateFormat("yyyy MMMM");
                Calendar cal = Calendar.getInstance();
                switch (position) {
                    case 24:
                        pageTitle = formatter.format(cal.getTime());
                        break;
                    default:
                        cal.add(Calendar.MONTH, position - numOfTabs / 2);
                        pageTitle = formatter.format(cal.getTime());
                        break;
                }
                break;
            case 53:
                formatter = new SimpleDateFormat("yyyy MMM, ww");
                Calendar clickedDay = Calendar.getInstance();
                clickedDay.setTimeInMillis(clickedDate);
                switch (position) {
                    case 26:
                        pageTitle = formatter.format(clickedDay.getTime()) +"."+ mContext.getString(R.string.week);
                        break;
                    default:
                        clickedDay.add(Calendar.DAY_OF_YEAR, (position- numOfTabs/2)*7);
                        pageTitle = formatter.format(clickedDay.getTime()) +"."+ mContext.getString(R.string.week);
                        break;
                }

        }
        return pageTitle;
    }
}
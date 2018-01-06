package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.fragments.AdminExpertsFragment;
import okosnotesz.hu.okosnotesz.fragments.AdminGuestsFragment;
import okosnotesz.hu.okosnotesz.fragments.AdminProductsFragment;
import okosnotesz.hu.okosnotesz.fragments.AdminTreatmentsFragment;
import okosnotesz.hu.okosnotesz.fragments.CalendarFragment;
import okosnotesz.hu.okosnotesz.fragments.ReportsFragment;
import okosnotesz.hu.okosnotesz.fragments.SalesFragment;
import okosnotesz.hu.okosnotesz.fragments.WeekFragment;

/**
 * Created by user on 2017.11.03..
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    int numOfTabs;
    long clickedDate;

    public PagerAdapter(Context context, FragmentManager supportFragmentManager, int i) {
        super(supportFragmentManager);
        this.numOfTabs = i;
        this.mContext = context;
    }

    public PagerAdapter(Context context, FragmentManager supportFragmentManager, int i, long date) {
        super(supportFragmentManager);
        this.numOfTabs = i;
        this.mContext = context;
        this.clickedDate = date;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CalendarFragment();
        switch (numOfTabs) {
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
            case 49:
                Calendar cal = Calendar.getInstance();
                long calLong = cal.getTimeInMillis();
                Log.d("eee", cal.getTime() + "adapter first");
                if (position == 24) {
                    Log.d("eee", cal.getTime() + "adapter first, Position: " + position + ", numOftabs/2: " + numOfTabs / 2);
                    fragment = CalendarFragment.newInstance(calLong);

                } else {
                    cal.add(Calendar.MONTH, position - numOfTabs / 2);
                    calLong = cal.getTimeInMillis();
                    Log.d("eee", cal.getTime() + "adapter moving, Position: " + position + ", numOftabs/2: " + numOfTabs / 2);
                    fragment = CalendarFragment.newInstance(calLong);
                }
                break;
            case 1:
                switch (position) {
                    case 0:
                        fragment = new SalesFragment();
                        break;
                }
                break;
            case 2:
                        fragment = new ReportsFragment();
                break;
            case 53:
                if (position == 26) {
                    fragment = WeekFragment.newInstance(clickedDate);
                    Log.d("daycells",new Date(clickedDate).toString()+ ", adapterposition: "+ position + "adapter: " +numOfTabs);
                } else {
                    Calendar clickedDay = Calendar.getInstance();
                    clickedDay.setTimeInMillis(clickedDate);
                    clickedDay.add(Calendar.DAY_OF_YEAR, (position - numOfTabs / 2) * 7);
                    Log.d("daycells", clickedDay.get(Calendar.DAY_OF_MONTH)+": dom");
                    fragment = WeekFragment.newInstance(clickedDay.getTimeInMillis());
                }
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
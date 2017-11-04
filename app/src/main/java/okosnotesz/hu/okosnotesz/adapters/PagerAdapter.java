package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.fragments.AdminExpertsFragment;
import okosnotesz.hu.okosnotesz.fragments.AdminGuestsFragment;
import okosnotesz.hu.okosnotesz.fragments.AdminProductsFragment;
import okosnotesz.hu.okosnotesz.fragments.AdminTreatmentsFragment;
import okosnotesz.hu.okosnotesz.fragments.CalendarActivity;
import okosnotesz.hu.okosnotesz.fragments.ReportsFragment;
import okosnotesz.hu.okosnotesz.fragments.SalesFragment;

/**
 * Created by user on 2017.11.03..
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    int numOfTabs;

    public PagerAdapter(Context context, FragmentManager supportFragmentManager, int i) {
        super(supportFragmentManager);
        this.numOfTabs = i;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(numOfTabs) {
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
                switch (position) {
                    case 0:
                        fragment = new SalesFragment();
                        break;
                    case 1:
                        fragment = new CalendarActivity();
                        break;
                    case 2:
                        fragment = new ReportsFragment();
                        break;
                }
                break;
            default:
                fragment = new CalendarActivity();
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
            case 3:
                switch (position) {
                    case 0:
                        pageTitle = mContext.getResources().getString(R.string.commercial);
                        break;
                    case 1:
                        pageTitle = mContext.getResources().getString(R.string.calendar);
                        break;
                    case 2:
                        pageTitle = mContext.getResources().getString(R.string.reports);
                        break;
                }
                break;
        }
        return pageTitle;
    }
}
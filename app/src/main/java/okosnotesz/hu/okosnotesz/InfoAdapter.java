package okosnotesz.hu.okosnotesz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by user on 2017.05.11..
 */

public class InfoAdapter extends FragmentPagerAdapter {

    public InfoAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch (position){
            case 0:
                f = new InfoFragmentGuest();
                break;
            case 1:
                f = new InfoFragmentCalendar();
                break;
            case 2:
                f = new InfoFragmentReport();
                break;
            case 3:
                f = new InfoFragmentBooking();
        }
        return f;
    }

    @Override
    public int getCount() {
        return 4;
    }
}

package hu.okosnotesz.okosnotesz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 2018.01.18..
 */

public interface MonthItemClickListener{
    void onClick(int position, int i);
}

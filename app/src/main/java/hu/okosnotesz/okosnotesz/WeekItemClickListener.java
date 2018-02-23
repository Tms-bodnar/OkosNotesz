package hu.okosnotesz.okosnotesz;

import java.util.Calendar;

import hu.okosnotesz.okosnotesz.model.Reports;

/**
 * Created by user on 2017.12.10..
 */

public interface WeekItemClickListener {
        void onClick(int position, Reports report, int day);
        void  onLongClick(int position, Reports temp, int day);
}


package okosnotesz.hu.okosnotesz;

import java.util.Calendar;

import okosnotesz.hu.okosnotesz.model.Reports;

/**
 * Created by user on 2017.12.10..
 */

public interface WeekItemClickListener {
        void onClick(Calendar cal, int position, Reports report);
        void  onLongClick(Calendar cal);
    }


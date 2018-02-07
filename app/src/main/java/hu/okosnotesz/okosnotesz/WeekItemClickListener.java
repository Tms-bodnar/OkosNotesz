package hu.okosnotesz.okosnotesz;

import java.util.Calendar;

import hu.okosnotesz.okosnotesz.model.Reports;

/**
 * Created by user on 2017.12.10..
 */

public interface WeekItemClickListener {
        void onClick(Calendar cal, int position, Reports report, int day);
        void  onLongClick(Calendar cal, int position, Reports temp, int day);
       // void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);
}


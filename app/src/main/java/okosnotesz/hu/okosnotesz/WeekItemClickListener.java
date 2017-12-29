package okosnotesz.hu.okosnotesz;

import android.view.ContextMenu;
import android.view.View;

import java.util.Calendar;

import okosnotesz.hu.okosnotesz.model.Reports;

/**
 * Created by user on 2017.12.10..
 */

public interface WeekItemClickListener {
        void onClick(Calendar cal, int position, Reports report, int day);
        void  onLongClick(Calendar cal);
        void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);
}


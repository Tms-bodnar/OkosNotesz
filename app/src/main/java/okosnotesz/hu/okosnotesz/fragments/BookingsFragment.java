package okosnotesz.hu.okosnotesz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.CalendarActivity;

/**
 * Created by user on 2017.08.05..
 */

public class BookingsFragment extends Fragment {

    long startMillis;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookings, container, false );
        startMillis = Calendar.getInstance().getTimeInMillis();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabBooking);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ide jön az új időpont foglalása
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(getContext(), CalendarActivity.class);
                startActivity(i);


            }
        });
        return view;
    }
}

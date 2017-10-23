package okosnotesz.hu.okosnotesz.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okosnotesz.hu.okosnotesz.BookingActivity;
import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.Treatments;


/**
 * Created by user on 2017.08.05..
 */

public class BookingsFragment extends Fragment {

    final int PICK_CONTACT = 333;
    final int PICK_TREATMENT = 334;
    long startMillis;
    TextView tvGuestName;
    TextView tvTreatmentsName;
    StringBuffer trenames = new StringBuffer("");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Context context = getActivity();
        View view = inflater.inflate(R.layout.booking, container, false );
        startMillis = Calendar.getInstance().getTimeInMillis();
        tvGuestName = (TextView) view.findViewById(R.id.tvGuestBooking);
        tvTreatmentsName = (TextView) view.findViewById(R.id.tvTreatmentBooking);
        final Button guestPicker = (Button) view.findViewById(R.id.btnGuestBooking);
        Button trePicker = (Button) view.findViewById(R.id.btnTreatmentBooking);
        Button datePick = (Button) view.findViewById(R.id.btnDatePick);
        guestPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isIntentAvailable(context, Intent.ACTION_PICK)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
            }
        });
        trePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookingActivity.class);
                startActivityForResult(intent, PICK_TREATMENT);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String guestName = "";

        if(data != null && requestCode == PICK_CONTACT){
            Uri uri = data.getData();
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri,null,null,null,null);
            if(cursor!=null) {
                cursor.moveToFirst();
                try{
                    guestName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.d("xxxx", cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                    int contId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                }finally {
                    cursor.close();
                }
            }
            tvGuestName.setText(guestName);
        }
        if(data!=null && requestCode== PICK_TREATMENT){
            if( data.getExtras()!=null){
                Log.d("xxx", "van valami");
            }
            ArrayList<Treatments> checkedList = data.getParcelableArrayListExtra("treBooking");
            trenames.delete(0, trenames.length());
            for (Treatments t : checkedList) {
                trenames.append(t.getName()+ ", ");
                Log.d("xxx", t.getName()+"!");
                Log.d("xxx", trenames.toString());
            }
        }
        tvTreatmentsName.setText(trenames.toString());

    }
    public static boolean isIntentAvailable(Context context, String action)
    {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}

/*
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath
                ("time");
                        ContentUris.appendId(builder, startMillis);
                        Intent i = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                        startActivityForResult(i, PICK_DATE);*/

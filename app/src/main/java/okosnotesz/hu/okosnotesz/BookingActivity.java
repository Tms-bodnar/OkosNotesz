package okosnotesz.hu.okosnotesz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.adapters.CustomTreatmentsAdapter;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Treatments;

public class BookingActivity extends AppCompatActivity {

    ListView lv;
    final int REQUEST_CODE = 666;
    final int RESULT_CODE  = 334;
    Treatments t;
    ArrayList<Treatments> checkedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        checkedList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treatment_booking);
        lv = (ListView) findViewById(R.id.treatmentBookingList);

        Context context = getBaseContext();
        final ArrayList<Treatments> treatmentList = ListHelper.getAllTreatments(context);
        CustomTreatmentsAdapter adapter = new CustomTreatmentsAdapter(context, treatmentList, REQUEST_CODE);
        lv.setAdapter(adapter);

        Log.d("xxx", "adapterOK");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox c = (CheckBox) view.findViewById(R.id.cbTre);
                if (!c.isChecked()) {
                    c.setChecked(!c.isChecked());
                    Treatments t = (Treatments) lv.getItemAtPosition(position);
                    if(!checkedList.contains(t)) {
                        checkedList.add(t);
                    }
                    Log.d("xxx", position + " listába: " + t.getName());
                }else if(c.isChecked()){
                    c.setChecked(false);
                    Treatments t = (Treatments) lv.getItemAtPosition(position);
                    if(checkedList.contains(t)) {
                        checkedList.remove(t);
                    }
                    Log.d("xxx", position + " listából " + t.getName());
                }
            }
        }) ;

        Button btnOK = (Button) findViewById(R.id.btnTreBookOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Treatments t : checkedList) {
                    Log.d("xxx", t.getName() +"?");
                }
                Intent i = new Intent();
                i.putParcelableArrayListExtra("treBooking", checkedList);
                setResult(RESULT_CODE, i);
                finish();
            }
        });
        Button btnCancel = (Button) findViewById(R.id.btnTreBookCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedList.clear();
                onBackPressed();
            }
        });

    }
}

package okosnotesz.hu.okosnotesz;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Set;

import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Treatments;

/**
 * Created by user on 2017.07.23..
 */

public class AdminDialog extends AppCompatActivity {

    private Bundle inBundle;
    private Bundle backBundle;


     @Override
    public void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.treatment_add_dialog);
         Intent intent = getIntent();
         inBundle = intent.getExtras();

         Set<String> keys = inBundle.keySet();
         for (String s : keys) {
             Log.d("xxx", s);
             switch (s){
                 case "hu.okosnotesz.Treatments":
                     Treatments t = inBundle.getParcelable(s);
                     treatmentDialogStart(t);
                     break;



             }
         }
     }

    private void treatmentDialogStart(final Treatments t) {
        final Treatments[] treatment = {null};
        setContentView(R.layout.treatment_add_dialog);
        final TextInputEditText treName = (TextInputEditText) this.findViewById(R.id.treatmentNameInput);
        final TextInputEditText treTime = (TextInputEditText) this.findViewById(R.id.treatmentTimeInput);
        final TextInputEditText trePrice = (TextInputEditText) this.findViewById(R.id.treatmentPriceInput);
        final TextInputEditText treCost = (TextInputEditText) this.findViewById(R.id.treatmentCostInput);
        final TextInputEditText treNote = (TextInputEditText) this.findViewById(R.id.treatmentNoteInput);
        if(t!=null){
            treName.setText(t.getName());
            trePrice.setText(String.valueOf(t.getPrice()));
            treTime.setText(String.valueOf(t.getTime()));
            treCost.setText(String.valueOf(t.getCost()));
            treNote.setText(t.getNote());
        }
            Button btnOK= (Button) this.findViewById(R.id.btnAddTreatmentOK);
            btnOK.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (t == null) {
                        String name = String.valueOf(treName.getText());
                        if (name == null || name.isEmpty()) {
                            name = "-";
                        }
                        String time = treTime.getText().toString();
                        if (time == null || time.isEmpty()) {
                            time = "0";
                        }
                        final int timeInt = Integer.valueOf(time);
                        String price = trePrice.getText().toString();
                        if (price == null || price.isEmpty()) {
                            price = "0";
                        }
                        final int priceInt = Integer.valueOf(price);
                        String cost = treCost.getText().toString();
                        if (cost == null || cost.isEmpty()) {
                            cost = "0";
                        }
                        final int costInt = Integer.valueOf(cost);
                        String note = treNote.getText().toString();
                        if (note == null || note.isEmpty()) {
                            note = "-";
                        }
                        treatment[0] = new Treatments(name, timeInt, priceInt, costInt, note);

                        DBHelper helper = DBHelper.getHelper(v.getContext());
                        Log.d("xxx", " " + treatment[0].getName());
                        boolean success = helper.addTreatment(treatment[0]);
                        Log.d("xxx", "" + success);
                        helper.close();
                        if (success) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inf = getLayoutInflater();
                            View alertView = inf.inflate(R.layout.admin_ok_dialog, null);
                            builder.setView(alertView);
                            TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                            tv.setText(treatment[0].getName() + " hozzáadása sikeres!");

                            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            });
                            AlertDialog ad = builder.create();
                            ad.show();
                        }
                        /*backBundle = new Bundle();
                        backBundle.putParcelable("hu.okosnotesz.Treatments", treatment[0]);
                        AdminTreatmentsFragment adminTreatmentsFragment = new AdminTreatmentsFragment();
                        adminTreatmentsFragment.setArguments(backBundle);*/

                    } else {
                        treatment[0]=null;
                        String name = String.valueOf(treName.getText());
                        if (name == null || name.isEmpty()) {
                            name = "-";
                        }
                        String time = treTime.getText().toString();
                        if (time == null || time.isEmpty()) {
                            time = "0";
                        }
                        final int timeInt = Integer.valueOf(time);
                        String price = trePrice.getText().toString();
                        if (price == null || price.isEmpty()) {
                            price = "0";
                        }
                        final int priceInt = Integer.valueOf(price);
                        String cost = treCost.getText().toString();
                        if (cost == null || cost.isEmpty()) {
                            cost = "0";
                        }
                        final int costInt = Integer.valueOf(cost);
                        String note = treNote.getText().toString();
                        if (note == null || note.isEmpty()) {
                            note = "-";
                        }
                        t.setName(name);
                        t.setPrice(priceInt);
                        t.setTime(timeInt);
                        t.setCost(costInt);
                        t.setNote(note);

                        DBHelper helper = DBHelper.getHelper(v.getContext());
                        Log.d("xxx", " " + t.getName());
                        boolean success = helper.updateTreatment(t);
                        Log.d("xxx", "" + success);
                        helper.close();
                        if (success) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inf = getLayoutInflater();
                            View alertView = inf.inflate(R.layout.admin_ok_dialog, null);
                            builder.setView(alertView);
                            builder.setTitle(R.string.editSuccessful);
                            TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                            tv.setText(t.getName());

                            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            });
                            AlertDialog ad = builder.create();
                            ad.show();
                        }
                        finish();
                    }
                }
            });

                Button btnCancel = (Button) this.findViewById(R.id.btnAddTreatmentCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        }

    }


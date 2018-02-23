package hu.okosnotesz.okosnotesz;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.okosnotesz.okosnotesz.adapters.CustomExpertsAdapter;
import hu.okosnotesz.okosnotesz.adapters.CustomTreatmentsAdapter;
import hu.okosnotesz.okosnotesz.model.Experts;
import hu.okosnotesz.okosnotesz.model.ListHelper;
import hu.okosnotesz.okosnotesz.model.Reports;
import hu.okosnotesz.okosnotesz.model.Treatments;

public class BookingActivity extends AppCompatActivity {

    private Reports report;
    private int position;
    private int day;
    private final int CONTACT_REQ_CODE = 55;
    private final int TREATMENT_REQ_CODE = 44;
    Button btnGuest;
    View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_layout);
        contentView = findViewById(R.id.booking_layout_id);
        Intent intent = getIntent();
        report = intent.getParcelableExtra("newRep");
        position = intent.getIntExtra("position", 0);
        day = intent.getIntExtra("day", 0);
        TextView dateTextView = (TextView) this.findViewById(R.id.tvBookingDate);
        final Date date = new Date(report.getDate());
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMMM dd, HH:mm");
        dateTextView.setText(formatter.format(date));
        btnGuest = (Button) this.findViewById(R.id.btnBookingGuest);
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, CONTACT_REQ_CODE);
            }
        });

        final Button btnTreatment = (Button) this.findViewById(R.id.btnBookingTreatment);
        btnTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Treatments> treatmentList = ListHelper.getAllTreatments(v.getContext());
                final List<String> selectedTreatmentNames = new ArrayList<>(treatmentList.size());
                final int[] duration = {0};
                final String treatmentNames[] = new String[treatmentList.size()];
                final StringBuilder sbNames = new StringBuilder();
                final StringBuilder sbIds = new StringBuilder();
                for (int i = 0; i < treatmentList.size(); i++) {
                    treatmentNames[i] = treatmentList.get(i).getName();
                }
                CustomTreatmentsAdapter treatmentsAdapter = new CustomTreatmentsAdapter(v.getContext(), treatmentList, TREATMENT_REQ_CODE);
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setAdapter(treatmentsAdapter, null);
                builder.setMultiChoiceItems(treatmentNames, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectedTreatmentNames.add(treatmentNames[which]);

                        } else if (selectedTreatmentNames.contains(treatmentNames[which])) {
                            selectedTreatmentNames.remove(treatmentNames[which]);

                        }
                    }

                });

                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (String  name: selectedTreatmentNames) {
                            sbNames.append(name+", ");
                        }
                        btnTreatment.setText(sbNames.toString());
                        for (Treatments t : treatmentList) {
                            for (String name : selectedTreatmentNames) {
                                if (t.getName().equals(name)) {
                                    sbIds.append(String.valueOf(t.getName()) + "ß");
                                    duration[0] += t.getTime();
                                }
                            }
                        }
                        report.setTreatment(sbIds.toString());
                        report.setDuration(duration[0]);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedTreatmentNames.clear();
                        sbIds.delete(0, sbIds.length());
                        sbNames.delete(0,sbNames.length());
                        btnTreatment.setText(R.string.tre);
                        report.setTreatment(sbIds.toString());
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        final Button btnExpert = (Button) this.findViewById(R.id.btnBookingExpert);
        btnExpert.setOnClickListener(new View.OnClickListener(){
        int id = 0;
            @Override
            public void onClick(View v) {
                final ArrayList<Experts> experts = ListHelper.getAllExperts(v.getContext());
                CustomExpertsAdapter expertsAdapter = new CustomExpertsAdapter(experts, v.getContext(), 1);
                final String[] expertNames = new String[experts.size()];
                final String[] selectedExpert = {""};
                for(int i = 0; i < expertNames.length; i++){
                    expertNames[i] = experts.get(i).getName();
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setAdapter(expertsAdapter, null);
                builder.setSingleChoiceItems(expertNames, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedExpert[0] = expertNames[which];
                        for(Experts e : experts){
                            if(e.getName().equals(selectedExpert[0])){
                                id = e.getId();
                                report.setExpert(id);
                                report.setExpertName(e.getName());
                                btnExpert.setText(selectedExpert[0]);
                                dialog.dismiss();
                            }
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        id = 0;
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        Button btnOK = (Button) this.findViewById(R.id.btnBookingOk);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bookingdata", "booking"+report.getGuestName()+", "+report.getExpertname()+", "+report.getTreatment()+", "+report.getDate()+", ");
                if(report.getGuestName()==null || report.getDate() == null || report.getExpertname() == null || report.getTreatment()==null){
                        Toast.makeText(getApplicationContext() , R.string.noDatas, Toast.LENGTH_LONG).show();

                }else {
                    Intent backIntent = new Intent();
                    backIntent.putExtra("backRep", report);
                    backIntent.putExtra("position", position);
                    backIntent.putExtra("day", day);
                    setResult(22, backIntent);
                onBackPressed();
                }
            }
        });
        Button btnCancel = (Button) this.findViewById(R.id.btnBookingCance);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String guestName = " ";
        if (intent != null && requestCode == CONTACT_REQ_CODE) {
            Uri uri = intent.getData();
            Cursor cursor = this.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            guestName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int contId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            cursor.close();
            btnGuest.setText(guestName);
            report.setGuestName(guestName);
            report.setConID(contId);
        } else {
            btnGuest.setText(guestName);
        }
    }

}

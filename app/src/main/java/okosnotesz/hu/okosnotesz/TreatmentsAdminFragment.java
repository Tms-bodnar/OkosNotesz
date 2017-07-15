package okosnotesz.hu.okosnotesz;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.TraceCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Treatments;

/**
 * Created by user on 2017.07.15..
 */

public class TreatmentsAdminFragment extends Fragment{

    ArrayList<Treatments> treatmentsList = null;
    ListView lv;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.treatments, container,false);
        FloatingActionButton treFab = (FloatingActionButton) view.findViewById(R.id.fabTre);
        treFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.addTreatment);
//                        final View addTreatmentView = LayoutInflater.from(getContext()).inflate(R.layout.treatment_add_dialog, (ViewGroup)getView(), false);
                        builder.setView(R.layout.treatment_add_dialog);
                        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog d = (Dialog) dialog;
                                final EditText treName = (EditText) d.findViewById(R.id.treatmentNameInput);
                                final EditText treTime = (EditText) d.findViewById(R.id.treatmentTimeInput);
                                final EditText trePrice = (EditText) d.findViewById(R.id.treatmentPriceInput);
                                final EditText treCost = (EditText) d.findViewById(R.id.treatmentCostInput);
                                final EditText treNote = (EditText) d.findViewById(R.id.treatmentNoteInput);

                                String name = String.valueOf(treName.getText());
                                int time = Integer.valueOf(treTime.getText().toString());
                                int price = Integer.valueOf(trePrice.getText().toString());
                                int cost = Integer.valueOf(treCost.getText().toString());
                                String note = treNote.getText().toString();


                                Log.d("xxx", ""+note);



                                Treatments t = new Treatments(name,time,price,cost,note);
                                DBHelper helper = DBHelper.getHelper(getActivity());
                                boolean success = helper.addTreatment(t);
                                helper.close();
                                refreshView(getAllTreatments());
                                dialog.dismiss();
                                if(success){
                                    Snackbar.make(getView(), R.string.addTreatmentOK, Snackbar.LENGTH_LONG).setAction("Action",null).show();
                                }
                            }
                        });
                        builder.show();
                    }
                }
        );

        lv = (ListView) view.findViewById(R.id.treatmentList);
        Context context = getActivity();
        treatmentsList = getAllTreatments();
        CustomTreatmentsAdapter adapter = new CustomTreatmentsAdapter(context, treatmentsList);
        lv.setAdapter(adapter);

        return view;
    }

    public ArrayList<Treatments> getAllTreatments() {
        ArrayList<Treatments> treatmentList = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(getActivity());
        Cursor c = helper.getAllTreatment();
        c.moveToFirst();
        try {
            while (!c.isAfterLast()) {
                Treatments t = new Treatments();
                t.setName(c.getString(c.getColumnIndex("treatmentName")));
                t.setTime(c.getInt(c.getColumnIndex("treatmentTime")));
                t.setPrice(c.getInt(c.getColumnIndex("treatmentPrice")));
                t.setCost(c.getInt(c.getColumnIndex("treatmentCost")));
                t.setNote(c.getString(c.getColumnIndex("treatmentNote")));
                treatmentList.add(t);
                c.moveToNext();
            }
        } finally {
            c.close();
        }
        if (treatmentList.isEmpty()) {
            treatmentList.add(new Treatments("No treaments", 0, 0, 0, "Please, add a treatment"));
        }
        return treatmentList;
    }

    public View refreshView(ArrayList<Treatments> treatmentsList){
        lv = (ListView) getActivity().findViewById(R.id.treatmentList);
        Context context = getActivity();
        CustomTreatmentsAdapter adapter = new CustomTreatmentsAdapter(context, treatmentsList);
        lv.setAdapter(adapter);
        return getView();
    }
}

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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Treatments;

/**
 * Created by user on 2017.07.15..
 */

public class AdminTreatmentsFragment extends Fragment{


    final int REQUEST_CODE = 101;
    static ArrayList<Treatments> treatmentsList = null;
    ListView lv;
    Menu menu;
    Treatments t;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.treatments, container, false);
        FloatingActionButton treFab = (FloatingActionButton) view.findViewById(R.id.fabTre);
        treFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(), AdminDialog.class);
                        i.putExtra("hu.okosnotesz.Treatments",(Treatments)null);
                        startActivityForResult(i,REQUEST_CODE);
                    }
                }
        );
        lv = (ListView) view.findViewById(R.id.treatmentList);
        final Context context = getActivity();
        treatmentsList = getAllTreatments();
        CustomTreatmentsAdapter adapter = new CustomTreatmentsAdapter(context, treatmentsList);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
        Log.d("xxx", "List:" + treatmentsList.size());
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            refreshView(getAllTreatments());
        }
    }

    public ArrayList<Treatments> getAllTreatments() {
        ArrayList<Treatments> treatmentList = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(getActivity());
        Cursor c = helper.getAllTreatment();
        c.moveToFirst();
        try {
            while (!c.isAfterLast()) {
                Treatments t = new Treatments();
                t.setId(c.getInt(c.getColumnIndex("treatmentID")));
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
            helper.close();
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
        registerForContextMenu(lv);
        return getView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info){
        super.onCreateContextMenu(menu, v, info);
        if (v.getId()== R.id.treatmentList){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) info;
            ListView lv = (ListView) v;
            this.t = (Treatments) lv.getItemAtPosition(menuInfo.position);
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.admin_menu, menu);
            menu.setHeaderTitle(this.t.getName());

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.adminMenuEdit:
                return treatmentEdit(this.t);
            case R.id.adminMenuDelete:
                return treatmentDelete(this.t);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean treatmentDelete(final Treatments  t) {
        final boolean[] successful = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inf = this.getLayoutInflater(null);
        View alertView = inf.inflate(R.layout.admin_ok_dialog, null);
        builder.setView(alertView);
        builder.setTitle(R.string.deleteAlert);
        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
        tv.setText(t.getName());
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper helper = DBHelper.getHelper(getContext());
                successful[0] = helper.deleteTreatment(t);
                helper.close();
                if(successful[0]) {
                    refreshView(getAllTreatments());
                    Snackbar.make(getView(), R.string.deleteSuccessful, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(getView(), R.string.cancel, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
        return successful[0];
    }

    private boolean treatmentEdit(Treatments t) {
        Intent i = new Intent(getContext(), AdminDialog.class);
        i.putExtra("hu.okosnotesz.Treatments",t);
        startActivityForResult(i,REQUEST_CODE);
        /*Treatments tre = openDialog(t);
        DBHelper helper = DBHelper.getHelper(getContext());
        boolean successful = helper.updateTreatment(tre);
        helper.close();*/
        refreshView(getAllTreatments());
        return true;
    }

    private Treatments openDialog(final Treatments tre){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.addTreatment);
        builder.setView(R.layout.treatment_add_dialog);

            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Dialog d = (Dialog) dialog;
                    final Treatments t = new Treatments();
                    final EditText treName = (EditText) d.findViewById(R.id.treatmentNameInput);
                    final EditText treTime = (EditText) d.findViewById(R.id.treatmentTimeInput);
                    final EditText trePrice = (EditText) d.findViewById(R.id.treatmentPriceInput);
                    final EditText treCost = (EditText) d.findViewById(R.id.treatmentCostInput);
                    final EditText treNote = (EditText) d.findViewById(R.id.treatmentNoteInput);
                    String name = "-";
                    String price = "0";
                    String cost ="0";
                    String time = "0";
                    String note = "-";

                    if(tre == null) {
                        treName.setText(name);
                        name = String.valueOf(treName.getText());
                        treTime.setText(time);
                        time = treTime.getText().toString();
                        int timeInt = Integer.valueOf(time);
                        trePrice.setText(price);
                        price = trePrice.getText().toString();
                        int priceInt = Integer.valueOf(price);
                        treCost.setText(cost);
                        cost = treCost.getText().toString();
                        int costInt = Integer.valueOf(cost);
                        treNote.setText(note);
                        note = treNote.getText().toString();
                        t.setName(name);
                        t.setPrice(priceInt);
                        t.setTime(timeInt);
                        t.setCost(costInt);
                        t.setNote(note);
                    /*}else{
                        treName.setText(tre.getName() == null || tre.getName().isEmpty() ? "-" : tre.getName());
                        name = String.valueOf(treName.getText());
                        treTime.setText(tre.getTime());
                        time = treTime.getText().toString();
                        int timeInt = Integer.valueOf(time);
                        trePrice.setText(tre.getPrice());
                        price = trePrice.getText().toString();
                        int priceInt = Integer.valueOf(price);
                        treCost.setText(tre.getCost());
                        cost = treCost.getText().toString();
                        int costInt = Integer.valueOf(cost);
                        treNote.setText(tre.getNote() == null || tre.getNote().isEmpty() ? "-" : tre.getNote());
                        note = treNote.getText().toString();
                        t.setName(name);
                        t.setPrice(priceInt);
                        t.setTime(timeInt);
                        t.setCost(costInt);
                        t.setNote(note);*/
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Snackbar.make(getView(), R.string.cancel, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    dialog.dismiss();
                }
            });
            builder.show();

       return t;
    }

    public static ArrayList<Treatments> getTreatmentsList() {
        return treatmentsList;
    }

    public static void setTreatmentsList(ArrayList<Treatments> treatmentsList) {
        AdminTreatmentsFragment.treatmentsList = treatmentsList;
    }
}
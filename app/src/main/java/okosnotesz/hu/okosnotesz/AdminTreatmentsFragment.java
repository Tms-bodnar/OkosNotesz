package okosnotesz.hu.okosnotesz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Treatments;

/**
 * Created by Bodnar Tamas on 2017.07.15.. tms.bodnar@gmail.com | okosnotesz.hu
 */

public class AdminTreatmentsFragment extends Fragment{

    final int REQUEST_CODE = 104;
    static ArrayList<Treatments> treatmentsList = null;
    ListView lv;
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
        Context context = getActivity();
        treatmentsList = getAllTreatments();
        CustomTreatmentsAdapter adapter = new CustomTreatmentsAdapter(context, treatmentsList);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
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
        if(getUserVisibleHint()) {
            switch (item.getItemId()) {
                case R.id.adminMenuEdit:
                    return treatmentEdit(this.t);
                case R.id.adminMenuDelete:
                    return treatmentDelete(this.t);
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return false;
    }

    private boolean treatmentDelete(final Treatments  t) {
        final boolean[] successful = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inf = this.getLayoutInflater(null);
        View alertView = inf.inflate(R.layout.admin_ok_dialog, null);
        builder.setView(alertView);
        builder.setTitle(R.string.deleteAlert);
        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
        if (t != null) {
            tv.setText(t.getName());
        }
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
        return true;
    }
}
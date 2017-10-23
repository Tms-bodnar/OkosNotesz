package okosnotesz.hu.okosnotesz.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import java.util.List;

import okosnotesz.hu.okosnotesz.AdminActivity;
import okosnotesz.hu.okosnotesz.adapters.CustomTreatmentsAdapter;
import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Treatments;

/**
 * Created by Bodnar Tamas on 2017.07.15.. tms.bodnar@gmail.com | okosnotesz.hu
 */

public class AdminTreatmentsFragment extends Fragment{

    final int REQUEST_CODE = 104;
    final int MENU_GROUP_ID = 104;
    final int MENU_OPT_1 = 1;
    final int MENU_OPT_2 = 2;
    final int MENU_OPT_3 = 3;
    static ArrayList<Treatments> treatmentsList = null;
    ListView lv;
    Treatments t;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d("xxx", "onCreateTreatments");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("xxx", "onCreateViewTreatments");
        final View view = inflater.inflate(R.layout.treatments, container, false);
        FloatingActionButton treFab = (FloatingActionButton) view.findViewById(R.id.fabTre);
        treFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(), AdminActivity.class);
                        i.putExtra("hu.okosnotesz.Treatments",(Treatments)null);
                        startActivityForResult(i,REQUEST_CODE);
                    }
                }
        );
        lv = (ListView) view.findViewById(R.id.treatmentList);
        Context context = getActivity();
        treatmentsList = ListHelper.getAllTreatments(context);
        CustomTreatmentsAdapter adapter = new CustomTreatmentsAdapter(context, treatmentsList, 0);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            refreshView(ListHelper.getAllTreatments(getContext()));
        }
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
            menu.add(MENU_GROUP_ID, MENU_OPT_1, 0, R.string.edit );
            menu.add(MENU_GROUP_ID, MENU_OPT_2, 0, R.string.delete );
            menu.add(MENU_GROUP_ID, MENU_OPT_3, 0, R.string.cancel );
//            MenuInflater inflater = getActivity().getMenuInflater();
//            inflater.inflate(R.menu.admin_menu, menu);
            menu.setHeaderTitle(this.t.getName());

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        Log.d("xxx", "onitemselectedTreatments: " +item.getGroupId());
        if(MENU_GROUP_ID == item.getGroupId()) {
            switch (item.getItemId()) {
                case MENU_OPT_1:
                    return treatmentEdit(this.t);
                case MENU_OPT_2:
                    return treatmentDelete(this.t);
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
                    refreshView(ListHelper.getAllTreatments(getContext()));
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
        Log.d("xxx", "editTreatments");
        Intent i = new Intent(getContext(), AdminActivity.class);
        i.putExtra("hu.okosnotesz.Treatments",t);
        startActivityForResult(i,REQUEST_CODE);
        Log.d("xxx", "editTreatmentsactivity" + REQUEST_CODE);
        return true;
    }

    private boolean treatmentCancel(){
        return true;
    }
}
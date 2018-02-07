package hu.okosnotesz.okosnotesz.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hu.okosnotesz.okosnotesz.AdminActivity;
import hu.okosnotesz.okosnotesz.R;
import hu.okosnotesz.okosnotesz.adapters.CustomExpertsAdapter;
import hu.okosnotesz.okosnotesz.model.DBHelper;
import hu.okosnotesz.okosnotesz.model.Experts;
import hu.okosnotesz.okosnotesz.model.ListHelper;

/**
 * Created by user on 2017.07.09..
 */

public class AdminExpertsFragment extends Fragment {

    final int REQUEST_CODE = 102;
    final int MENU_GROUP_ID = 102;
    final int MENU_OPT_1 = 1;
    final int MENU_OPT_2 = 2;
    final int MENU_OPT_3 = 3;
    static ArrayList<Experts> expertsList= null;
    ListView lv;
    Experts e;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstnanceState){
        final View view = inflater.inflate(R.layout.experts, container, false);
        FloatingActionButton fabExp = (FloatingActionButton) view.findViewById(R.id.fabExp);

        fabExp.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent i = new Intent(getContext(), AdminActivity.class);
                                          i.putExtra("hu.hu.okosnotesz.okosnotesz.Experts", (Experts) null);
                                          startActivityForResult(i, REQUEST_CODE);
                                      }
                                  });
        lv = (ListView) view.findViewById(R.id.experts_list);
        Context context = getActivity();
        expertsList = ListHelper.getAllExperts(context);
        CustomExpertsAdapter adapter = new CustomExpertsAdapter(expertsList, context,0);
        registerForContextMenu(lv);
        lv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            refreshView(ListHelper.getAllExperts(getContext()));
        }
    }



    public View refreshView(ArrayList<Experts> expList){
        lv = (ListView) getActivity().findViewById(R.id.experts_list);
        Context context = getActivity();
        CustomExpertsAdapter adapter = new CustomExpertsAdapter(expList, context,0);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
        return getView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.experts_list){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            ListView lv = (ListView) v;
            e = (Experts) lv.getItemAtPosition(info.position);
            menu.add(MENU_GROUP_ID, MENU_OPT_1, 0, R.string.edit );
            menu.add(MENU_GROUP_ID, MENU_OPT_2, 0, R.string.delete );
            menu.add(MENU_GROUP_ID, MENU_OPT_3, 0, R.string.cancel );
//            MenuInflater inflater = getActivity().getMenuInflater();
//            inflater.inflate(R.menu.admin_menu, menu);
            menu.setHeaderTitle(e.getName());


        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(MENU_GROUP_ID == item.getGroupId()) {
            switch (item.getItemId()) {
                case MENU_OPT_1 :
                    return editExpert(e);
                case MENU_OPT_2:
                    return deleteExpert(e);
            }
        }
        return false;
        }

    private boolean deleteExpert(final Experts e) {
        final boolean[] successful = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
        builder.setView(alertView);
        builder.setTitle(R.string.deleteAlert);
        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
        if (e != null){
            tv.setText(e.getName());
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DBHelper helper = DBHelper.getHelper(getContext());
                    successful[0] = helper.deleteExpert(e);
                    helper.close();
                    if(successful[0]){
                        refreshView(ListHelper.getAllExperts(getContext()));
                        Snackbar.make(getView(), R.string.deleteSuccessful, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Snackbar.make(getView(), R.string.cancel, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
        AlertDialog ad = builder.create();
        ad.show();
        return successful[0];
    }

    private boolean editExpert(Experts e) {
        Intent i = new Intent(getContext(), AdminActivity.class);
        i.putExtra("hu.hu.okosnotesz.okosnotesz.Experts", e);
        startActivityForResult(i, REQUEST_CODE);
        return true;
    }
}

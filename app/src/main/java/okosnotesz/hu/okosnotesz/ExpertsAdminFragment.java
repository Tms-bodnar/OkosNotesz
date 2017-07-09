package okosnotesz.hu.okosnotesz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Experts;

/**
 * Created by user on 2017.07.09..
 */

public class ExpertsAdminFragment extends Fragment {

    ArrayList<Experts> expertsList= null;
    ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstnanceState){
        final View view = inflater.inflate(R.layout.experts, container, false);
        FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        Log.d("xxx","FAB");
        fab2.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) {
                                       Log.d("xxx", "clicked");
                                       AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                       builder.setTitle(R.string.addExpert);
                                       final View addExpertViev = LayoutInflater.from(getContext()).inflate(R.layout.expert_add_dialog, (ViewGroup) getView(), false);

                                       builder.setView(R.layout.expert_add_dialog);

                                       builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       Dialog d = (Dialog) dialog;

                                                       final EditText inputName = (EditText) d.findViewById(R.id.expertNameInput);
                                                       Log.d("xxx", "name: "+inputName.getHint());
                                                       final EditText inputNote = (EditText) d.findViewById(R.id.expertNoteInput);
                                                       Log.d("xxx", " note: "+inputNote.getHint());

                                                       String name = inputName.getText().toString();
                                                       String note = inputNote.getText().toString();
                                                       Log.d("xxx", ""+name+", "+note);
                                                       Experts expert = new Experts(name, note);
                                                        DBHelper helper = DBHelper.getHelper(getActivity());
                                                       Log.d("xxx", ""+helper);
                                                        boolean success = helper.addExpert(expert);
                                                        helper.close();
                                                       refreshView(getAllExperts());
                                                       dialog.dismiss();
                                                       if(success) {
                                                           Snackbar.make(getView(), R.string.addExpertOK, Snackbar.LENGTH_LONG)
                                                                   .setAction("Action", null).show();
                                                       }
                                                   }
                                               });

                                        builder.show();
                                   }

                               }
        );
        lv = (ListView) view.findViewById(R.id.experts_list);
        Context context = getActivity();
        expertsList = getAllExperts();
        ExpertsAdapter adapter = new ExpertsAdapter(context, R.layout.expert_list_item, expertsList);
        lv.setAdapter(adapter);
        return view;
    }

    public View refreshView(ArrayList<Experts> expList){
        lv = (ListView) getActivity().findViewById(R.id.experts_list);
        Context context = getActivity();
        ExpertsAdapter adapter = new ExpertsAdapter(context, R.layout.expert_list_item, expList);
        lv.setAdapter(adapter);
        return getView();

    }

    public ArrayList<Experts> getAllExperts(){
        ArrayList<Experts> explist = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(getActivity());
        Log.d("xxx", helper.toString());
        Cursor c = helper.getAllExperts();
        Log.d("xxx", ""+c.getCount());
        Log.d("xxx", ""+c.getColumnName(1));
        c.moveToFirst();
        try {
            do{
                Log.d("xxx", "name:"+c.getString(c.getColumnIndex("expertName")));
                Experts e = new Experts();
                e.setName(c.getString(c.getColumnIndex("expertName")));
                e.setNote(c.getString(c.getColumnIndex("expertNote")));
                explist.add(e);
            }while (c.moveToNext());
        }finally {
            c.close();
        }


        if(explist.isEmpty()){
            explist.add(new Experts("No datas", "Please, add an expert!"));
        }
        return explist;

    }
}

package okosnotesz.hu.okosnotesz;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        View view = inflater.inflate(R.layout.experts, container, false);
        lv = (ListView) view.findViewById(R.id.experts_list);
        Context context = getActivity();
        expertsList = getAllExperts();
        ArrayAdapter<Experts> adapter = new ArrayAdapter<>(context, R.layout.expert_list_item, expertsList);
        lv.setAdapter(adapter);
        return view;
    }

    public ArrayList<Experts> getAllExperts(){
        ArrayList<Experts> explist = new ArrayList<>();
        DBHelper helper = new DBHelper(getActivity().getBaseContext());
        Cursor c = helper.getAllExperts();
        c.moveToFirst();
        while(!c.isAfterLast()){
            explist.add(new Experts(c.getString(c.getColumnIndex("expertName")), c.getString(c.getColumnIndex("expertNote"))));
        }
        c.close();
        if(explist.isEmpty()){
            explist.add(new Experts("No datas", "Please, add an expert!"));
        }
        return explist;

    }
}

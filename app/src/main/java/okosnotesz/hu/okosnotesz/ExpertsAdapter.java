package okosnotesz.hu.okosnotesz;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import okosnotesz.hu.okosnotesz.model.Experts;

/**
 * Created by user on 2017.07.09..
 */

public class ExpertsAdapter extends ArrayAdapter<Experts> {

    public ExpertsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }


    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.expert_list_item, null);
        }
        Experts e = getItem(position);

        if(e != null){
            TextView name = (TextView) v.findViewById(R.id.expertNameItem);
            TextView note = (TextView) v.findViewById(R.id.expertNoteItem);
            if(name != null){
                name.setText(e.getName());
            }
            if(note != null){
                note.setText(e.getNote());
            }
        }
        return v;
    }
}

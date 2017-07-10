package okosnotesz.hu.okosnotesz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.model.Experts;

/**
 * Created by user on 2017.07.10..
 */

public class CustomExpertsAdapter extends BaseAdapter {

    private ArrayList<Experts> expertsList;
    Context mContext;
    ViewHolder holder;

    public CustomExpertsAdapter(ArrayList<Experts> expertsList, Context mContext) {
        this.expertsList = expertsList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return expertsList.size();
    }

    @Override
    public Object getItem(int position) {
        return expertsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return expertsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.expert_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) v.findViewById(R.id.expertNameItem);
            holder.note = (TextView) v.findViewById(R.id.expertNoteItem);
            v.setTag(holder);
        }

        Experts e = expertsList.get(position);
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
    static class ViewHolder{
        TextView name;
        TextView note;
    }
}

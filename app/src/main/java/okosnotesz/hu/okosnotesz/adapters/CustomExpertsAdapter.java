package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.Experts;

/**
 * Created by user on 2017.07.10..
 */

public class CustomExpertsAdapter extends BaseAdapter {

    private ArrayList<Experts> expertsList;
    Context mContext;
    ViewHolder holder;
    int reqCode;

    public CustomExpertsAdapter(ArrayList<Experts> expertsList, Context mContext, int reqCode) {
        this.expertsList = expertsList;
        this.mContext = mContext;
        this.reqCode = reqCode;
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
        if(reqCode==1){
            holder.name.setTextColor(Color.BLACK);
            holder.note.setTextColor(Color.BLACK);
        }
        return v;
    }
    static class ViewHolder{
        TextView name;
        TextView note;
    }
}

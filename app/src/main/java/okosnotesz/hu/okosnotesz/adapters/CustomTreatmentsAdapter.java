package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.Treatments;

/**
 * Created by user on 2017.07.15..
 */

public class CustomTreatmentsAdapter extends BaseAdapter {

    ArrayList<Treatments> treatmentsList;
    Context mcontext;
    ViewHolder holder;

    public CustomTreatmentsAdapter(Context context, ArrayList<Treatments> treList){
        this.mcontext = context;
        this.treatmentsList = treList;
    }

    @Override
    public int getCount() {
        return treatmentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return treatmentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return treatmentsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.treatment_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) v.findViewById(R.id.treatmentNameItem);
            holder.time = (TextView) v.findViewById(R.id.treatmentTimeItem);
            holder.price = (TextView) v.findViewById(R.id.treatmentPriceItem);
            holder.note = (TextView) v.findViewById(R.id.treatmentNoteItem);
            v.setTag(holder);
        }

        Treatments t = treatmentsList.get(position);
        if (t != null){
            TextView name = (TextView) v.findViewById(R.id.treatmentNameItem);
            TextView time = (TextView) v.findViewById(R.id.treatmentTimeItem);
            TextView price = (TextView) v.findViewById(R.id.treatmentPriceItem);
            TextView note = (TextView) v.findViewById(R.id.treatmentNoteItem);
            if(name != null){
                name.setText(t.getName());
            }
            if(time != null){
                time.setText(String.valueOf(t.getTime()));
            }
            if(price != null){
                price.setText(String.valueOf(t.getPrice()));
            }
            if(note != null){
                note.setText(t.getNote());
            }
        }
        return v;
    }

    private class ViewHolder {
        TextView name;
        TextView time;
        TextView price;
        TextView note;
    }
}

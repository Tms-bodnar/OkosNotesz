package hu.okosnotesz.okosnotesz.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hu.okosnotesz.okosnotesz.model.GuestsDatas;

/**
 * Created by user on 2017.07.09..
 */

public class CustomGuestsAdapter extends BaseAdapter {

    public final Context context;
    public final ArrayList<GuestsDatas> guestsDatasList;
    public final int REQUEST_CODE;

    public CustomGuestsAdapter(Context context, ArrayList<GuestsDatas> guestsDatasList, int request_code) {
        this.context = context;
        this.guestsDatasList = guestsDatasList;
        REQUEST_CODE = request_code;
    }

    @Override
    public int getCount() {
        return guestsDatasList.size();
    }

    @Override
    public Object getItem(int position) {
        return guestsDatasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return guestsDatasList.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;
      /*  ViewHolder holder = null;
        if(REQUEST_CODE == 101) {
            if (v == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                v = inflater.inflate(R.layout.guests_list_item, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) v.findViewById(R.id.guestNameItem);
                holder.phone = (TextView) v.findViewById(R.id.guestPhoneItem);
                holder.email = (TextView) v.findViewById(R.id.guestMailItem);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            GuestsDatas gd = (GuestsDatas) getItem(position);

            if (gd != null) {
                if (holder.name != null) {
                    holder.name.setText(gd.getName());
                }
                if (holder.phone != null) {
                    holder.phone.setText(gd.getPhone1());
                }
                if (holder.email != null) {
                    holder.email.setText(gd.getEmail1());
                }
            }
        }
        if(REQUEST_CODE == 103){
            if (v == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                v = inflater.inflate(R.layout.guests_list_item, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) v.findViewById(R.id.guestNameItem);
                holder.phone = (TextView) v.findViewById(R.id.guestPhoneItem);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            GuestsDatas gd = (GuestsDatas) getItem(position);

            if (gd != null) {
                if (holder.name != null) {
                    holder.name.setText(gd.getName());
                }
                if (holder.phone != null) {
                    holder.phone.setText(gd.getPhone1());
                }
            }
        }*/
        return v;
    }

    static class ViewHolder{
        TextView name;
        TextView phone;
        TextView email;
    }
}


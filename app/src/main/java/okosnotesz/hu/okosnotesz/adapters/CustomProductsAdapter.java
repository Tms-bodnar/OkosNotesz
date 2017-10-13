package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.Products;

/**
 * Created by user on 2017.07.18..
 */

public class CustomProductsAdapter extends BaseAdapter {

    ArrayList<Products> productsList;
    Context mContext;
    final int REQUEST_CODE;
    ViewHolder holder;
    public CustomProductsAdapter(ArrayList<Products> productsList, Context mContext, int requestCode ) {
        this.productsList = productsList;
        this.mContext = mContext;
        this.REQUEST_CODE = requestCode;
    }


    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int position) {
        return productsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Products p;
        switch (REQUEST_CODE) {
            case 103:
                if (v == null) {
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.product_list_item, null);
                    holder = new ViewHolder();
                    holder.name = (TextView) v.findViewById(R.id.productNameItem);
                    holder.price = (TextView) v.findViewById(R.id.productPriceItem);
                    holder.cost = (TextView) v.findViewById(R.id.productCostItem);
                    holder.note = (TextView) v.findViewById(R.id.productNoteItem);
                    v.setTag(holder);
                }
                p = (Products) getItem(position);
                if (p != null) {
                    TextView name = (TextView) v.findViewById(R.id.productNameItem);
                    TextView price = (TextView) v.findViewById(R.id.productPriceItem);
                    TextView cost = (TextView) v.findViewById(R.id.productCostItem);
                    TextView note = (TextView) v.findViewById(R.id.productNoteItem);
                    if (name != null) {
                        name.setText(p.getName());
                    }
                    if (price != null) {
                        price.setText(String.valueOf(p.getPrice()));
                    }
                    if (cost != null) {
                        cost.setText(String.valueOf(p.getCost()));
                    }
                    if (note != null) {
                        note.setText(p.getNote());
                    }
                }
                break;
            case 113:
                if (v == null) {
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.product_spinner, null);
                    holder = new ViewHolder();
                    holder.name = (TextView) v.findViewById(R.id.productNameItem);
                    v.setTag(holder);
                }
                p = (Products) getItem(position);
                if (p != null) {
                    TextView name = (TextView) v.findViewById(R.id.productNameItem);
                    TextView price = (TextView) v.findViewById(R.id.productPriceItem);
                    if (name != null) {
                        name.setText(p.getName());
                    }
                }
                break;

        }
        return v;
    }

    class ViewHolder{
        TextView name;
        TextView price;
        TextView cost;
        TextView note;
    }
}

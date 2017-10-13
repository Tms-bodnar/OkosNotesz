package okosnotesz.hu.okosnotesz.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Products;
import okosnotesz.hu.okosnotesz.model.Sales;

/**
 * Created by user on 2017.09.05..
 */

public class CustomSalesAdapter extends BaseAdapter {

    List<Sales> salesList;
    List<Products> productsList;
    Context mContext;
    ViewHolder holder;
    Sales s;

    public CustomSalesAdapter(List<Sales> salesList, List<Products> productsList, Context mContext) {
        this.salesList = salesList;
        this.productsList = productsList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return salesList.size();
    }

    @Override
    public Object getItem(int position) {
        return salesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return salesList.get(position).getId();
    }

    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sales_list_item, null );
            holder = new ViewHolder();
            holder.salesName = (TextView) view.findViewById(R.id.salesItemName);
            holder.salesValue = (TextView) view.findViewById(R.id.salesItemValue);
            holder.salesDate = (TextView) view.findViewById(R.id.salesDate);
            view.setTag(holder);
        }
        s = (Sales) getItem(position);
        Products p = new Products();
        for (Products products : productsList) {
            if (s.getProductID() == products.getId());
            p = products;

        }

        TextView name = (TextView) view.findViewById(R.id.salesItemName);
        TextView value = (TextView) view.findViewById(R.id.salesItemValue);
        TextView date = (TextView) view.findViewById(R.id.salesDate);
        if(s!=null){
            if(value!=null) {
                value.setText(String.valueOf(s.getValue()));
            }
            if(name != null){
                name.setText(String.valueOf(p.getName()));
            }
            if(date != null){
                date.setText(String.valueOf(s.getDate()));
            }

        }
        return view;
    }
    class ViewHolder{
        TextView salesName;
        TextView salesValue;
        TextView salesDate;

    }
}

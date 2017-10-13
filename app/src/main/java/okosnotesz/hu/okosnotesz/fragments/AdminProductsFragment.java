package okosnotesz.hu.okosnotesz.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.AdminActivity;
import okosnotesz.hu.okosnotesz.adapters.CustomProductsAdapter;
import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.ListHelper;
import okosnotesz.hu.okosnotesz.model.Products;

/**
 * Created by user on 2017.07.16..
 */

public class AdminProductsFragment extends Fragment {

    static ArrayList<Products> productsList = null;
    ListView lv;
    Products p;
    final int REQUEST_CODE = 103;
    final int MENU_GROUP_ID = 103;
    final int MENU_OPT_1 = 1;
    final int MENU_OPT_2 = 2;
    final int MENU_OPT_3 = 3;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d("xxx", "onCreateProducts");
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("xxx", "onCreateProducts");
        final View v = inflater.inflate(R.layout.products, container, false);
        FloatingActionButton fabProd = (FloatingActionButton) v.findViewById(R.id.fabProd);
        fabProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(getContext(), AdminActivity.class);
                i.putExtra("hu.okosnotesz.Products", (Products) null);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        lv = (ListView) v.findViewById(R.id.productList);
        Context context = getActivity();
        productsList = ListHelper.getAllProducts(context);
        CustomProductsAdapter adapter = new CustomProductsAdapter(productsList, context, REQUEST_CODE);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            refreshView(ListHelper.getAllProducts(getContext()));
        }
    }

    private View refreshView(ArrayList<Products> allProducts) {
        lv = (ListView) getActivity().findViewById(R.id.productList);
        Context context = getActivity();
        CustomProductsAdapter adapter = new CustomProductsAdapter(allProducts, context, REQUEST_CODE);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
        return getView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info) {
        if (v.getId() == R.id.productList) {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) info;
            ListView lv = (ListView) v;
            this.p = (Products) lv.getItemAtPosition(menuInfo.position);
            menu.add(MENU_GROUP_ID, MENU_OPT_1, 0, R.string.edit );
            menu.add(MENU_GROUP_ID, MENU_OPT_2, 0, R.string.delete );
            menu.add(MENU_GROUP_ID, MENU_OPT_3, 0, R.string.cancel );

//            MenuInflater inflater = getActivity().getMenuInflater();
//            inflater.inflate(R.menu.admin_menu, menu);
            menu.setHeaderTitle(this.p.getName());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("xxx", "itemSelectedProducts: "+ item.getGroupId());
        if (MENU_GROUP_ID == item.getGroupId()) {
            switch (item.getItemId()) {
                case MENU_OPT_1:
                    return productsEdit(this.p);
                case MENU_OPT_2:
                    return productsDelete(this.p);
            }
        }
        return false;
    }

    private boolean productsEdit(Products p) {
        Log.d("xxx", "editProducts");
        Intent i = new Intent(getContext(), AdminActivity.class);
        i.putExtra("hu.okosnotesz.Products", p);
        startActivityForResult(i, REQUEST_CODE);
        Log.d("xxx", "editProductsactivity" + REQUEST_CODE);
        return true;
    }

    private boolean productsDelete(final Products p) {
        final boolean[] successful = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater(null);
        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
        builder.setView(alertView);
        builder.setTitle(R.string.deleteAlert);
        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
        if (p != null) {
            tv.setText(p.getName());
        }
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper helper = DBHelper.getHelper(getContext());
                successful[0] = helper.deleteProduct(p);
                helper.close();
                if (successful[0]) {
                    refreshView(ListHelper.getAllProducts(getContext()));
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
        AlertDialog ad = builder.create();
        ad.show();
        return successful[0];
    }
}

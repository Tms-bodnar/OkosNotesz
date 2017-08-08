package okosnotesz.hu.okosnotesz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.test.suitebuilder.TestMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.R;
import okosnotesz.hu.okosnotesz.adapters.CustomGuestsAdapter;
import okosnotesz.hu.okosnotesz.adapters.CustomProductsAdapter;
import okosnotesz.hu.okosnotesz.model.GuestsDatas;
import okosnotesz.hu.okosnotesz.model.Products;

/**
 * Created by user on 2017.08.05..
 */

public class SalesFragment extends Fragment {

    Spinner productsSpinner;
    Spinner guestSpinner;
    ArrayList<Products> productsList;
    ArrayList<GuestsDatas> guestsDatasList;
    final int REQUEST_CODE = 113;
    int quantity = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Context context = getActivity();
        View view = inflater.inflate(R.layout.sales, container, false);
        AdminProductsFragment apf = new AdminProductsFragment();
        productsList = apf.getAllProducts();
        productsSpinner = (Spinner) view.findViewById(R.id.spnProducts);
        CustomProductsAdapter productsAdapter = new CustomProductsAdapter(productsList, context, REQUEST_CODE);
        productsSpinner.setAdapter(productsAdapter);
        final EditText quantityEdittext = (EditText) view.findViewById(R.id.etSalesQuantity);
        quantityEdittext.setText(String.valueOf(quantity));
        final TextView tvValue = (TextView) view.findViewById(R.id.tvSalesValue);
        final Products[] p = {null};
        productsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                p[0] = (Products) parent.getSelectedItem();
                int value = 0;
                quantity = 1;
                quantityEdittext.setText(String.valueOf(quantity));
                if(p[0] != null) {
                    value = quantity * p[0].getPrice();
                    tvValue.setText(String.valueOf(value));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button quantityPlus = (Button) view.findViewById(R.id.btnQuantityPlus);
        quantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               plusQuantity();
               quantityEdittext.setText(String.valueOf(quantity));
                int value = quantity * p[0].getPrice();
                tvValue.setText(String.valueOf(value));
            }
        });
        Button quantityMinus = (Button) view.findViewById(R.id.btnQuantityMinus);
        quantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusQuantity();
                quantityEdittext.setText(String.valueOf(quantity));
                int value = quantity * p[0].getPrice();
                tvValue.setText(String.valueOf(value));
            }
        });
        guestSpinner= (Spinner) view.findViewById(R.id.spnGuests);

        return view;
    }

    public void plusQuantity(){
        this.quantity += 1;
    }
    public void minusQuantity(){
        if(this.quantity!=1) {
            this.quantity -= 1;
        }else{
            quantity = 1;
        }
    }
}

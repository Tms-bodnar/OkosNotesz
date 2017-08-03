package okosnotesz.hu.okosnotesz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Products;
import okosnotesz.hu.okosnotesz.model.Treatments;

/**
 * Created by user on 2017.07.23..
 */

public class AdminDialog extends AppCompatActivity {

    private Bundle inBundle;
    private Bundle backBundle;


     @Override
    public void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.treatment_add_dialog);
         Intent intent = getIntent();
         inBundle = intent.getExtras();

         Set<String> keys = inBundle.keySet();
         for (String s : keys) {
             Log.d("xxx", s);
             switch (s){
                 case "hu.okosnotesz.Treatments":
                     Treatments t = inBundle.getParcelable(s);
                     treatmentDialogStart(t);
                     break;
                 case "hu.okosnotesz.Products":
                     Products p = inBundle.getParcelable(s);
                     productsDialogStart(p);
                     break;
             }
         }
     }

    private void treatmentDialogStart(final Treatments t) {
        final Treatments[] treatment = {null};
        setContentView(R.layout.treatment_add_dialog);
        final TextInputEditText treName = (TextInputEditText) this.findViewById(R.id.treatmentNameInput);
        final TextInputEditText treTime = (TextInputEditText) this.findViewById(R.id.treatmentTimeInput);
        final TextInputEditText trePrice = (TextInputEditText) this.findViewById(R.id.treatmentPriceInput);
        final TextInputEditText treCost = (TextInputEditText) this.findViewById(R.id.treatmentCostInput);
        final TextInputEditText treNote = (TextInputEditText) this.findViewById(R.id.treatmentNoteInput);
        if(t!=null){
            treName.setText(t.getName());
            trePrice.setText(String.valueOf(t.getPrice()));
            treTime.setText(String.valueOf(t.getTime()));
            treCost.setText(String.valueOf(t.getCost()));
            treNote.setText(t.getNote());
        }
            Button btnOK= (Button) this.findViewById(R.id.btnAddTreatmentOK);
            btnOK.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (t == null) {
                        String name = String.valueOf(treName.getText());
                        if (name == null || name.isEmpty()) {
                            name = "-";
                        }
                        String time = treTime.getText().toString();
                        if (time == null || time.isEmpty()) {
                            time = "0";
                        }
                        final int timeInt = Integer.valueOf(time);
                        String price = trePrice.getText().toString();
                        if (price == null || price.isEmpty()) {
                            price = "0";
                        }
                        final int priceInt = Integer.valueOf(price);
                        String cost = treCost.getText().toString();
                        if (cost == null || cost.isEmpty()) {
                            cost = "0";
                        }
                        final int costInt = Integer.valueOf(cost);
                        String note = treNote.getText().toString();
                        if (note == null || note.isEmpty()) {
                            note = "-";
                        }
                        treatment[0] = new Treatments(name, timeInt, priceInt, costInt, note);

                        DBHelper helper = DBHelper.getHelper(v.getContext());
                        boolean success = helper.addTreatment(treatment[0]);
                        helper.close();
                        if (success) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inf = getLayoutInflater();
                            View alertView = inf.inflate(R.layout.admin_ok_dialog, null);
                            builder.setView(alertView);
                            TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                            tv.setText(treatment[0].getName() + " " + getString(R.string.editSuccessful));

                            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            });
                            AlertDialog ad = builder.create();
                            ad.show();
                        }
                    } else {
                        treatment[0]=null;
                        String name = String.valueOf(treName.getText());
                        if (name == null || name.isEmpty()) {
                            name = "-";
                        }
                        String time = treTime.getText().toString();
                        if (time == null || time.isEmpty()) {
                            time = "0";
                        }
                        final int timeInt = Integer.valueOf(time);
                        String price = trePrice.getText().toString();
                        if (price == null || price.isEmpty()) {
                            price = "0";
                        }
                        final int priceInt = Integer.valueOf(price);
                        String cost = treCost.getText().toString();
                        if (cost == null || cost.isEmpty()) {
                            cost = "0";
                        }
                        final int costInt = Integer.valueOf(cost);
                        String note = treNote.getText().toString();
                        if (note == null || note.isEmpty()) {
                            note = "-";
                        }
                        t.setName(name);
                        t.setPrice(priceInt);
                        t.setTime(timeInt);
                        t.setCost(costInt);
                        t.setNote(note);

                        DBHelper helper = DBHelper.getHelper(v.getContext());
                        boolean success = helper.updateTreatment(t);
                        helper.close();
                        if (success) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inf = getLayoutInflater();
                            View alertView = inf.inflate(R.layout.admin_ok_dialog, null);
                            builder.setView(alertView);
                            builder.setTitle(R.string.editSuccessful);
                            TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                            tv.setText(t.getName());

                            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            });
                            AlertDialog ad = builder.create();
                            ad.show();
                        }
                        finish();
                    }
                }
            });

                Button btnCancel = (Button) this.findViewById(R.id.btnAddTreatmentCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        }

    private void productsDialogStart(final Products p){
        final Products[] products = {null};
        setContentView(R.layout.product_add_dialog);
        final TextInputEditText proName = (TextInputEditText) this.findViewById(R.id.productNameInput);
        final TextInputEditText proPrice = (TextInputEditText)this.findViewById(R.id.productPriceInput);
        final TextInputEditText proCost = (TextInputEditText) this.findViewById(R.id.productCostInput);
        final TextInputEditText proNote = (TextInputEditText) this.findViewById(R.id.productNoteInput);
        if(p != null){
            proName.setText(p.getName());
            proPrice.setText(String.valueOf(p.getPrice()));
            proCost.setText(String.valueOf(p.getCost()));
            proNote.setText(p.getNote());
        }
        Button btnOK = (Button) this.findViewById(R.id. btnAddPoductsOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p==null){
                    String name = String.valueOf(proName.getText());
                    if(name == null || name.isEmpty()){
                        name = "-";
                    }
                    String price = String.valueOf(proPrice.getText());
                    if(price == null || price.isEmpty()){
                        price = "0";
                    }
                    int priceInt = Integer.valueOf(price);
                    String cost = String.valueOf(proCost.getText());
                    if(cost == null || cost.isEmpty()){
                        cost = "0";
                    }
                    int costInt = Integer.valueOf(cost);
                    String note = String.valueOf(proNote.getText());
                    if(note == null || note.isEmpty()){
                        note = "-";
                    }
                    products[0] = new Products(name,priceInt,costInt,note);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean success = helper.addProduct(products[0]);
                    if(success){
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
                        builder.setView(alertView);
                        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                        tv.setText(products[0].getName() + " " + getString(R.string.addProductOK));
                        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        AlertDialog ad = builder.create();
                        ad.show();
                    }
                }else{
                    products[0]=null;
                    String name = String.valueOf(proName.getText());
                    if(name == null || name.isEmpty()){
                        name = "-";
                    }
                    String price = String.valueOf(proPrice.getText());
                    if(price == null || price.isEmpty()){
                        price = "0";
                    }
                    int priceInt = Integer.valueOf(price);
                    String cost = String.valueOf(proCost.getText());
                    if(cost == null || cost.isEmpty()){
                        cost = "0";
                    }
                    int costInt = Integer.valueOf(cost);
                    String note = String.valueOf(proNote.getText());
                    if(note == null || note.isEmpty()){
                        note = "-";
                    }
                    p.setName(name);
                    p.setPrice(priceInt);
                    p.setCost(costInt);
                    p.setNote(note);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean successful = helper.updateProduct(p);
                    if(successful){
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
                        builder.setView(alertView);
                        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                        tv.setText(p.getName() + " " + getString(R.string.editSuccessful));
                        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        AlertDialog ad = builder.create();
                        ad.show();
                    }
                }
            }
        });
        Button btnCancel = (Button) this.findViewById(R.id.btnAdProductsCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        }

    }




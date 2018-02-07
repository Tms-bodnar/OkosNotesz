package hu.okosnotesz.okosnotesz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

import hu.okosnotesz.okosnotesz.model.DBHelper;
import hu.okosnotesz.okosnotesz.model.Experts;
import hu.okosnotesz.okosnotesz.model.Products;
import hu.okosnotesz.okosnotesz.model.Treatments;

/**
 * Created by user on 2017.07.23..
 */

public class AdminActivity extends AppCompatActivity {

    private Bundle inBundle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_add_dialog);
        Intent intent = getIntent();
        inBundle = intent.getExtras();

        Set<String> keys = inBundle.keySet();
        for (String s : keys) {
            switch (s) {
                case "hu.hu.okosnotesz.okosnotesz.Treatments":
                    Treatments t = inBundle.getParcelable(s);
                    treatmentsDialogStart(t);
                    break;
                case "hu.hu.okosnotesz.okosnotesz.Products":
                    Products p = inBundle.getParcelable(s);
                    productsDialogStart(p);
                    break;
                case "hu.hu.okosnotesz.okosnotesz.Experts":
                    Experts e = inBundle.getParcelable(s);
                    expertsDialogStart(e);
                    break;
            }
        }
    }

    private void treatmentsDialogStart(final Treatments t) {
        final Treatments[] treatment = {null};
        setContentView(R.layout.treatment_add_dialog);
        final TextView title = (TextView) this.findViewById(R.id.treatmentAddTitle);
        title.setText(getString(R.string.addTreatment));
        final TextInputEditText treName = (TextInputEditText) this.findViewById(R.id.treatmentNameInput);
        final TextInputEditText treTime = (TextInputEditText) this.findViewById(R.id.treatmentTimeInput);
        final TextInputEditText trePrice = (TextInputEditText) this.findViewById(R.id.treatmentPriceInput);
        final TextInputEditText treCost = (TextInputEditText) this.findViewById(R.id.treatmentCostInput);
        final TextInputEditText treNote = (TextInputEditText) this.findViewById(R.id.treatmentNoteInput);
        if (t != null) {
            title.setText(getString(R.string.editTreatment));
            treName.setText(t.getName());
            trePrice.setText(String.valueOf(t.getPrice()));
            treTime.setText(String.valueOf(t.getTime()));
            treCost.setText(String.valueOf(t.getCost()));
            treNote.setText(t.getNote());
        }
        Button btnOK = (Button) this.findViewById(R.id.btnAddTreatmentOK);
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (t == null) {

                    String name = String.valueOf(treName.getText()).trim();
                    if (name == null || name.isEmpty()) {
                        name = "-";
                    }
                    String time = treTime.getText().toString().trim();
                    if (time == null || time.isEmpty()) {
                        time = "0";
                    }
                    final int timeInt = Integer.valueOf(time);
                    String price = trePrice.getText().toString().trim();
                    if (price == null || price.isEmpty()) {
                        price = "0";
                    }
                    final int priceInt = Integer.valueOf(price);
                    String cost = treCost.getText().toString().trim();
                    if (cost == null || cost.isEmpty()) {
                        cost = "0";
                    }
                    final int costInt = Integer.valueOf(cost);
                    String note = treNote.getText().toString().trim();
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
                        builder.setTitle(R.string.addTreatmentOK);
                        View alertView = inf.inflate(R.layout.admin_ok_dialog, null);
                        builder.setView(alertView);
                        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                        tv.setText(treatment[0].getName());

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
                    treatment[0] = null;
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

                }
            }
        });

        Button btnCancel = (Button) this.findViewById(R.id.btnAddTreatmentCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void productsDialogStart(final Products p) {
        final Products[] products = {null};
        setContentView(R.layout.product_add_dialog);
        final TextView title = (TextView) this.findViewById(R.id.productAddTitle);
        title.setText(getString(R.string.addProduct));
        final TextInputEditText proName = (TextInputEditText) this.findViewById(R.id.productNameInput);
        final TextInputEditText proPrice = (TextInputEditText) this.findViewById(R.id.productPriceInput);
        final TextInputEditText proCost = (TextInputEditText) this.findViewById(R.id.productCostInput);
        final TextInputEditText proNote = (TextInputEditText) this.findViewById(R.id.productNoteInput);
        if (p != null) {
            title.setText(getString(R.string.editproduct));
            proName.setText(p.getName());
            proPrice.setText(String.valueOf(p.getPrice()));
            proCost.setText(String.valueOf(p.getCost()));
            proNote.setText(p.getNote());
        }
        Button btnOK = (Button) this.findViewById(R.id.btnAddProductsOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p == null) {
                    String name = String.valueOf(proName.getText());
                    if (name == null || name.isEmpty()) {
                        name = "-";
                    }
                    String price = String.valueOf(proPrice.getText());
                    if (price == null || price.isEmpty()) {
                        price = "0";
                    }
                    int priceInt = Integer.valueOf(price);
                    String cost = String.valueOf(proCost.getText());
                    if (cost == null || cost.isEmpty()) {
                        cost = "0";
                    }
                    int costInt = Integer.valueOf(cost);
                    String note = String.valueOf(proNote.getText());
                    if (note == null || note.isEmpty()) {
                        note = "-";
                    }
                    products[0] = new Products(name, priceInt, costInt, note);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean success = helper.addProduct(products[0]);
                    helper.close();
                    if (success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
                        builder.setView(alertView);
                        builder.setTitle(R.string.addProductOK);
                        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                        tv.setText(products[0].getName());
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
                    products[0] = null;
                    String name = String.valueOf(proName.getText());
                    if (name == null || name.isEmpty()) {
                        name = "-";
                    }
                    String price = String.valueOf(proPrice.getText());
                    if (price == null || price.isEmpty()) {
                        price = "0";
                    }
                    int priceInt = Integer.valueOf(price);
                    String cost = String.valueOf(proCost.getText());
                    if (cost == null || cost.isEmpty()) {
                        cost = "0";
                    }
                    int costInt = Integer.valueOf(cost);
                    String note = String.valueOf(proNote.getText());
                    if (note == null || note.isEmpty()) {
                        note = "-";
                    }
                    p.setName(name);
                    p.setPrice(priceInt);
                    p.setCost(costInt);
                    p.setNote(note);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean successful = helper.updateProduct(p);
                    helper.close();
                    if (successful) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
                        builder.setView(alertView);
                        builder.setTitle(R.string.editSuccessful);
                        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                        tv.setText(p.getName());
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
        Button btnCancel = (Button) this.findViewById(R.id.btnAddProductsCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void expertsDialogStart(final Experts e) {
        final Experts[] expert = {null};
        setContentView(R.layout.expert_add_dialog);
        TextView title = (TextView) this.findViewById(R.id.expertAddTitle);
        title.setText(R.string.addExpert);
        final TextView expName = (TextView) this.findViewById(R.id.expertNameInput);
        final TextView expNote = (TextView) this.findViewById(R.id.expertNoteInput);
        if (e != null) {
            title.setText(getString(R.string.editExpert));
            expName.setText(e.getName());
            expNote.setText(e.getNote());
        }
        final Button btnOk = (Button) this.findViewById(R.id.btnAddExpertOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (e == null) {
                    String name = String.valueOf(expName.getText());
                    if (name == null || name.isEmpty()) {
                        name = "-";
                    }
                    String note = String.valueOf(expNote.getText());
                    if (note == null || note.isEmpty()) {
                        note = "-";
                    }
                    expert[0] = new Experts(name, note);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean success = helper.addExpert(expert[0]);
                    helper.close();
                    if (success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle(R.string.addExpertOK);
                        LayoutInflater inflater = getLayoutInflater();
                        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
                        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                        tv.setText(expert[0].getName());
                        builder.setView(alertView);
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
                    expert[0] = null;
                    String name = String.valueOf(expName.getText());
                    if (name == null || name.isEmpty()) {
                        name = "-";
                    }
                    String note = String.valueOf(expNote.getText());
                    if (note == null || note.isEmpty()) {
                        note = "-";
                    }
                    e.setName(name);
                    e.setNote(note);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean successful = helper.updateExpert(e);
                    helper.close();
                    if (successful) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        builder.setTitle(R.string.editSuccessful);
                        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
                        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                        tv.setText(e.getName());
                        builder.setView(alertView);
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
        Button btnCancel = (Button) this.findViewById(R.id.btnAddExpertCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
package okosnotesz.hu.okosnotesz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Set;

import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Experts;
import okosnotesz.hu.okosnotesz.model.GuestsDatas;
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
                     treatmentsDialogStart(t);
                     break;
                 case "hu.okosnotesz.Products":
                     Products p = inBundle.getParcelable(s);
                     productsDialogStart(p);
                     break;
                 case "hu.okosnotesz.Experts":
                     Experts e = inBundle.getParcelable(s);
                     expertsDialogStart(e);
                     break;
                 case "hu.okosnotesz.GuestsDatas":
                     GuestsDatas gd = inBundle.getParcelable(s);
                     guestDatasDialogsStart(gd);
                     break;
                 default: finish();
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
        if(t!=null){
            title.setText(getString(R.string.editTreatment));
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
        final TextView title = (TextView) this.findViewById(R.id.productAddTitle);
        title.setText(getString(R.string.addProduct));
        final TextInputEditText proName = (TextInputEditText) this.findViewById(R.id.productNameInput);
        final TextInputEditText proPrice = (TextInputEditText)this.findViewById(R.id.productPriceInput);
        final TextInputEditText proCost = (TextInputEditText) this.findViewById(R.id.productCostInput);
        final TextInputEditText proNote = (TextInputEditText) this.findViewById(R.id.productNoteInput);
        if(p != null){
            title.setText(getString(R.string.editproduct));
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
                    helper.close();
                    if(success){
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
                    helper.close();
                    boolean successful = helper.updateProduct(p);
                    if(successful){
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
        Button btnCancel = (Button) this.findViewById(R.id.btnAdProductsCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        if(e != null){
            title.setText(getString(R.string.editExpert));
            expName.setText(e.getName());
            expNote.setText(e.getNote());
        }
        final Button btnOk = (Button) this.findViewById(R.id.btnAddExpertOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (e == null){
                    String name = String.valueOf(expName.getText());
                    if ( name == null || name.isEmpty()){
                        name = "-";
                    }
                    String note = String.valueOf(expNote.getText());
                    if(note == null || note.isEmpty()){
                        note = "-";
                    }
                    expert[0] = new Experts(name, note);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean success = helper.addExpert(expert[0]);
                    helper.close();
                    if(success){
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
                }else{
                    expert[0] = null;
                    String name = String.valueOf(expName.getText());
                    if(name == null || name.isEmpty()){
                        name = "-";
                    }
                    String note = String.valueOf(expNote.getText());
                    if(note == null || note.isEmpty()){
                        note = "-";
                    }
                    e.setName(name);
                    e.setNote(note);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean successful = helper.updateExpert(e);
                    helper.close();
                    if(successful){
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
                finish();
            }
        });
    }

    private void guestDatasDialogsStart(final GuestsDatas gd) {
        if(gd == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            builder.setTitle(R.string.addGuest);
            View selectView = inflater.inflate(R.layout.guest_import_select, null);
            TextView tv = (TextView) selectView.findViewById(R.id.tvGuestImport);
            tv.setText(R.string.guestImportSelect);
            builder.setView(selectView);
            builder.setPositiveButton(R.string.importGuest, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton(R.string.addNewGuest, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    guestDatasDialogsAdmin(gd);
                }
            });
            builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog ad = builder.create();
            ad.show();
        }else{
            guestDatasDialogsAdmin(gd);
        }

    }


    private void guestDatasDialogsAdmin(final GuestsDatas gd) {
        final GuestsDatas[] guestDatas = {new GuestsDatas()};
        setContentView(R.layout.guest_add_dialog);
        TextView title = (TextView) this.findViewById(R.id.guestAddTitle);
        title.setText(R.string.addGuest);
        final TextView guestName = (TextView) this.findViewById(R.id.guestNameInput);
        final TextView guestPhone1 = (TextView) this.findViewById(R.id.guestPhone1Input);
        final TextView guestPhone2 = (TextView) this.findViewById(R.id.guestPhone2Input);
        final TextView guestEmail1 = (TextView) this.findViewById(R.id.guestEmailInput);
        final TextView guestEmail2 = (TextView) this.findViewById(R.id.guestEmail2Input);
        final TextView guestContact1 = (TextView) this.findViewById(R.id.guestContact1Input);
        final TextView guestContact2 = (TextView) this.findViewById(R.id.guestContact2Input);
        if(gd != null){
            title.setText(R.string.editGuest);
            guestName.setText(gd.getName());
            guestPhone1.setText(gd.getPhone1());
            guestPhone2.setText(gd.getPhone2());
            guestEmail1.setText(gd.getEmail1());
            guestEmail2.setText(gd.getEmail2());
            guestContact1.setText(gd.getContact1());
            guestContact2.setText(gd.getContact2());
        }
        Button btnOK = (Button) this.findViewById(R.id.btnAddGuestOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gd == null){
                    String name = String.valueOf(guestName.getText());
                    if (name == null || name.isEmpty()){
                        name = "-";
                    }
                    String phone1 = String.valueOf(guestPhone1.getText());
                    if(phone1 == null || phone1.isEmpty()){
                        phone1 = "-";
                    }
                    String phone2 = String.valueOf(guestPhone2.getText());
                    if(phone2 == null || phone2.isEmpty()){
                        phone2 = "-";
                    }
                    String email1 = String.valueOf(guestEmail1.getText());
                    if(email1 == null || email1.isEmpty()){
                        email1 = "-";
                    }
                    String email2 = String.valueOf(guestEmail2.getText());
                    if(email2 == null || email2.isEmpty()){
                        email2 = "-";
                    }
                    String contact1 = String.valueOf(guestContact1.getText());
                    if(contact1 == null || contact1.isEmpty()){
                        contact1 = "-";
                    }
                    String contact2 = String.valueOf(guestContact2.getText());
                    if(contact2 == null || contact2.isEmpty()){
                        contact2 = "-";
                    }
                    guestDatas[0] = new GuestsDatas(name,phone1,phone2,email1,email2,contact1,contact2);
                    guestDatas[0].setName(name);
                    guestDatas[0].setPhone1(phone1);
                    guestDatas[0].setPhone2(phone2);
                    guestDatas[0].setEmail1(email1);
                    guestDatas[0].setEmail2(email2);
                    guestDatas[0].setContact1(contact1);
                    guestDatas[0].setContact2(contact2);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean successful = helper.addGuest(guestDatas[0]);
                    helper.close();
                    if(successful){
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle(R.string.addGuestOK);
                        LayoutInflater inflater = getLayoutInflater();
                        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
                        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                        tv.setText(guestDatas[0].getName());
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
                }else{
                    guestDatas[0] = null;
                    String name = String.valueOf(guestName.getText());
                    if (name == null || name.isEmpty()){
                        name = "-";
                    }
                    String phone1 = String.valueOf(guestPhone1.getText());
                    if(phone1 == null || phone1.isEmpty()){
                        phone1 = "-";
                    }
                    String phone2 = String.valueOf(guestPhone2.getText());
                    if(phone2 == null || phone2.isEmpty()){
                        phone2 = "-";
                    }
                    String email1 = String.valueOf(guestEmail1.getText());
                    if(email1 == null || email1.isEmpty()){
                        email1 = "-";
                    }
                    String email2 = String.valueOf(guestEmail2.getText());
                    if(email2 == null || email2.isEmpty()){
                        email2 = "-";
                    }
                    String contact1 = String.valueOf(guestContact1.getText());
                    if(contact1 == null || contact1.isEmpty()){
                        contact1 = "-";
                    }
                    String contact2 = String.valueOf(guestContact2.getText());
                    if(contact2 == null || contact2.isEmpty()){
                        contact2 = "-";
                    }
                    gd.setName(name);
                    gd.setPhone1(phone1);
                    gd.setPhone2(phone2);
                    gd.setEmail1(email1);
                    gd.setEmail2(email2);
                    gd.setContact1(contact1);
                    gd.setContact2(contact2);
                    DBHelper helper = DBHelper.getHelper(v.getContext());
                    boolean successful = helper.updateGuest(gd);
                    if(successful){
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        builder.setTitle(R.string.editSuccessful);
                        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
                        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
                        tv.setText(gd.getName());
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
        Button btnCancel = (Button) this.findViewById(R.id.btnAddGuestCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}




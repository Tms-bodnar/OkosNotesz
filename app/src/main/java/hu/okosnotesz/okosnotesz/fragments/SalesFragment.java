package hu.okosnotesz.okosnotesz.fragments;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hu.okosnotesz.okosnotesz.R;
import hu.okosnotesz.okosnotesz.adapters.CustomProductsAdapter;
import hu.okosnotesz.okosnotesz.model.DBHelper;
import hu.okosnotesz.okosnotesz.model.GuestsDatas;
import hu.okosnotesz.okosnotesz.model.ListHelper;
import hu.okosnotesz.okosnotesz.model.Products;
import hu.okosnotesz.okosnotesz.model.Sales;

/**
 * Created by user on 2017.08.05..
 */

public class SalesFragment extends Fragment {


    ArrayList<Products> productsList;
    final int REQUEST_CODE = 113;
    final int PICK_CONTACT = 001;
    int quantity = 1;
    Products prod;
    String guestName;
    Button btnGuest;

    public SalesFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Context context = getActivity();
        final View[] view = {inflater.inflate(R.layout.sales, container, false)};
        guestName = getString(R.string.customer);

        productsList = ListHelper.getAllProducts(context);
        final Products[] p = new Products[1];
        final CustomProductsAdapter productsAdapter = new CustomProductsAdapter(productsList, context, REQUEST_CODE);
        final Button btnProd = (Button) view[0].findViewById(R.id.btnSalesProduct);
        final Button btnQuantity = (Button) view[0].findViewById(R.id.salesQuantity);
        final Button btnValue = (Button) view[0].findViewById(R.id.salesValue);
        btnProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity=1;
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.product))
                        .setSingleChoiceItems(productsAdapter, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                p[0]= productsList.get(which);
                                prod = p[0];
                                btnProd.setText(p[0].getName());
                                btnValue.setText(p[0].getPrice() + "");
                                btnQuantity.setText(String.valueOf(quantity) + " " + getString(R.string.pieces));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        btnQuantity.setText(String.valueOf(quantity) + " " + getString(R.string.pieces));
        Button quantityPlus = (Button) view[0].findViewById(R.id.btnQuantityPlus);
        quantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               plusQuantity();
               btnQuantity.setText(String.valueOf(quantity) + " " + getString(R.string.pieces));
                if(p[0]!= null) {
                    int value = quantity * p[0].getPrice();
                    btnValue.setText(String.valueOf(value));
                }
                else{
                    btnValue.setText(getString(R.string.chooseProducet));
                }

            }
        });
        Button quantityMinus = (Button) view[0].findViewById(R.id.btnQuantityMinus);
        quantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusQuantity();
                btnQuantity.setText(String.valueOf(quantity)+ " " + getString(R.string.pieces));
                if (p[0]!= null) {
                    int value = quantity * p[0].getPrice();
                    btnValue.setText(String.valueOf(value));
                }
                else{
                    btnValue.setText(getString(R.string.chooseProducet));
                }

            }
        });
        btnGuest = (Button) view[0].findViewById(R.id.btnGuests);
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isIntentAvailable(context, Intent.ACTION_PICK)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
                else{
                    btnGuest.setText(guestName);
                }
            }
        });

        Button btnOK = (Button) view[0].findViewById(R.id.btnSaleOk);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prod!=null) {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
                    String date = sdf.format(cal.getTime());
                    Sales sale = new Sales(prod.getId(), guestName, "", date, quantity, Integer.parseInt(String.valueOf(btnValue.getText())));
                    DBHelper helper = DBHelper.getHelper(getContext());
                    boolean successful = helper.addSale(sale);
                    if (successful) {
                        Toast.makeText(getContext(), getString(R.string.saleSuccessful), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getContext(), getString(R.string.unsuccessfulOp), Toast.LENGTH_LONG).show();
                    prod = null;
                    guestName = getString(R.string.customer);
                    quantity = 1;
                    btnQuantity.setText(R.string.pieces);
                    btnValue.setText(getString(R.string.chooseProducet));
                    btnProd.setText(getString(R.string.product));
                    btnGuest.setText(getString(R.string.customer));
                }else
                    Toast.makeText(getContext(), getString(R.string.unsuccessfulOp) + "  " + getString(R.string.chooseProducet), Toast.LENGTH_LONG).show();
            }
        });

        Button btnCancel = (Button) view[0].findViewById(R.id.btnSaleCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prod = null;
                guestName = getString(R.string.customer);
                quantity = 1;
                btnQuantity.setText(R.string.pieces);
                btnValue.setText(getString(R.string.chooseProducet));
                btnProd.setText(getString(R.string.product));
                btnGuest.setText(getString(R.string.customer));
            }
        });
        return view[0];
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(intent !=null && requestCode == PICK_CONTACT){
            Uri uri = intent.getData();
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null,null,null);
            cursor.moveToFirst();
            String[] cnames = cursor.getColumnNames();
            guestName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int contId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            cursor.close();

            btnGuest.setText(guestName);
        }else {
            btnGuest.setText(guestName);
        }
    }

    public static boolean isIntentAvailable(Context context, String action)
    {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}


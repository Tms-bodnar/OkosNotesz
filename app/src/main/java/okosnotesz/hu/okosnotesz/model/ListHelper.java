package okosnotesz.hu.okosnotesz.model;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import okosnotesz.hu.okosnotesz.R;

/**
 * Created by user on 2017.09.16..
 */

public class ListHelper {

    public static ArrayList<Products> getAllProducts(Context context) {
        ArrayList<Products> prodList = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(context);
        Cursor c = helper.getAllProducts();
        c.moveToFirst();
        try {
            while (!c.isAfterLast()) {
                Products p = new Products();
                p.setId(c.getInt(c.getColumnIndex("productID")));
                p.setName(c.getString(c.getColumnIndex("productName")));
                p.setPrice(c.getInt(c.getColumnIndex("productPrice")));
                p.setCost(c.getInt(c.getColumnIndex("productCost")));
                p.setNote(c.getString(c.getColumnIndex("productNote")));
                prodList.add(p);
                c.moveToNext();
            }
        } finally {
            c.close();
            helper.close();
        }
        if (prodList.isEmpty()) {
            prodList.add(new Products(context.getString(R.string.noDatas),0 ,0 , context.getString(R.string.addNewData)));
        }
        return prodList;
    }

    public static ArrayList<Experts> getAllExperts(Context context){
        ArrayList<Experts> explist = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(context);
        Cursor c = helper.getAllExperts();
        c.moveToFirst();
        try {
            while(!c.isAfterLast()){
                Experts e = new Experts();
                e.setId(c.getInt(c.getColumnIndex("expertID")));
                e.setName(c.getString(c.getColumnIndex("expertName")));
                e.setNote(c.getString(c.getColumnIndex("expertNote")));
                explist.add(e);
                c.moveToNext();
            }
        }finally {
            c.close();
            helper.close();
        }
        if(explist.isEmpty()){
            explist.add(new Experts(context.getString(R.string.noDatas), context.getString(R.string.addNewData)));
        }
        return explist;
    }

    public static ArrayList<Treatments> getAllTreatments(Context context) {
        ArrayList<Treatments> treatmentList = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(context);
        Cursor c = helper.getAllTreatment();
        c.moveToFirst();
        try {
            while (!c.isAfterLast()) {
                Treatments t = new Treatments();
                t.setId(c.getInt(c.getColumnIndex("treatmentID")));
                t.setName(c.getString(c.getColumnIndex("treatmentName")));
                t.setTime(c.getInt(c.getColumnIndex("treatmentTime")));
                t.setPrice(c.getInt(c.getColumnIndex("treatmentPrice")));
                t.setCost(c.getInt(c.getColumnIndex("treatmentCost")));
                t.setNote(c.getString(c.getColumnIndex("treatmentNote")));
                treatmentList.add(t);
                c.moveToNext();
            }
        } finally {
            c.close();
            helper.close();
        }
        if (treatmentList.isEmpty()) {
            treatmentList.add(new Treatments(context.getString(R.string.noDatas),0,0,0, context.getString(R.string.addNewData)));
        }
        return treatmentList;
    }

    public static ArrayList<Sales> getAllSales(Context context) {
        ArrayList<Sales> sList = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(context);
        Cursor cursor = helper.getAllSales();
        cursor.moveToFirst();
        try {
            while (!cursor.isAfterLast()) {
                Sales s = new Sales();
                s.setProductID(cursor.getInt(cursor.getColumnIndex("saleProduct")));
                s.setGuestName(cursor.getString(cursor.getColumnIndex("saleGuest")));
                s.setDate(cursor.getString(cursor.getColumnIndex("saleDate")));
                s.setQuantity(cursor.getInt(cursor.getColumnIndex("saleQuantity")));
                s.setValue(cursor.getInt(cursor.getColumnIndex("saleValue")));
                sList.add(s);
                cursor.moveToNext();
            }
        }
        finally{
            cursor.close();
            helper.close();
        }
        return  sList;
    }
}

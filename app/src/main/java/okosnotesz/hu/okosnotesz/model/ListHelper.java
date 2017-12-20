package okosnotesz.hu.okosnotesz.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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

    public static Experts getExpert(int id, Context context){
        Experts e = new Experts();
        DBHelper helper = DBHelper.getHelper(context);
        Cursor c = helper.getExpert(id);
        c.moveToFirst();
        try {
            while(!c.isAfterLast()){
                e.setId(c.getInt(c.getColumnIndex("expertID")));
                e.setName(c.getString(c.getColumnIndex("expertName")));
                e.setNote(c.getString(c.getColumnIndex("expertNote")));
                c.moveToNext();
            }
        }finally {
            c.close();
            helper.close();
        }
        return e;
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

    public static Treatments getTreatment(int id, Context context){
        Treatments t = new Treatments();
        DBHelper helper = DBHelper.getHelper(context);
        Cursor c = helper.getTreatment(id);
        c.moveToFirst();
        try{
            while(!c.isAfterLast()){
                t.setId(c.getInt(c.getColumnIndex("treatmentID")));
                t.setName(c.getString(c.getColumnIndex("treatmentName")));
                t.setTime(c.getInt(c.getColumnIndex("treatmentTime")));
                t.setPrice(c.getInt(c.getColumnIndex("treatmentPrice")));
                t.setCost(c.getInt(c.getColumnIndex("treatmentCost")));
                t.setNote(c.getString(c.getColumnIndex("treatmentNote")));
                c.moveToNext();
            }
        }finally {
            c.close();
            helper.close();
        }
        return t;
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

    public static ArrayList<Reports> getAllReports(Context context){
        Log.d("xxx", "Monthlyadapter repList1.1");
        ArrayList<Reports> rList = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(context);
        Log.d("xxx", "Monthlyadapter repList1.2");
        Cursor cursor = helper.getAllReports();
        Log.d("xxx", "Monthlyadapter repList1.3");

        cursor.moveToFirst();
        Log.d("xxx", "Monthlyadapter repList cursor"+cursor.getColumnCount());
        try{
            while(!cursor.isAfterLast()){
                Log.d("xxx", "Monthlyadapter repList cursor"+cursor.getCount());
                Reports r = new Reports();
                r.setId(cursor.getInt(cursor.getColumnIndex("reportID")));
                Log.d("xxx", "Monthlyadapter repList cursorID"+r.getId());
                r.setTreatment(cursor.getString(cursor.getColumnIndex("reportTreatment")));
                Log.d("xxx", "Monthlyadapter repList cursorTRe"+r.getTreatment());
                r.setExpert(cursor.getInt(cursor.getColumnIndex("reportExpert")));
                Log.d("xxx", "Monthlyadapter repList cursorEXp"+r.getExpert());
                r.setGuestName(cursor.getString(cursor.getColumnIndex("reportGuest")));
                Log.d("xxx", "Monthlyadapter repList cursorGUE"+r.getGuestName());
                r.setDate(cursor.getLong(cursor.getColumnIndex("reportDate")));
                r.setDuration(cursor.getInt((cursor.getColumnIndex("reportDuration"))));
                Log.d("xxx", "Monthlyadapter repList cursorDAt"+r.getDate());
                r.setNote(cursor.getString(cursor.getColumnIndex("reportNote")));
                Log.d("xxx", "Monthlyadapter repList cursorNote"+r.getNote());
                rList.add(r);
                cursor.moveToNext();
            }

        }finally {
            cursor.close();
            helper.close();
        }
        Log.d("xxx", "Monthlyadapter repList1.5");
        return rList;
    }

    public static Treatments[] getTreatmentsOfReport(List<Treatments> allTreatment, String treatmentNames){
        Treatments[] treatments = new Treatments[allTreatment.size()];
        String[] splitted = treatmentNames.split(" ");
        for (int j = 0; j < allTreatment.size(); j++) {
            for (int i = 0; i < splitted.length; i++) {
                if (allTreatment.get(j).getName().equals(splitted[i])) {
                    treatments[i] = allTreatment.get(j);
                }
            }
        }
        return treatments;
    }
}

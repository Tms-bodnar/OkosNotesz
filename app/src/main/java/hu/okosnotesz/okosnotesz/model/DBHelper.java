package hu.okosnotesz.okosnotesz.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by user on 2017.05.19..
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper helper;

    private static final String DATABASE_NAME = "hu.okosnotesz.okosnotesz.db";
    private static final int DATABASE_VESRION = 1;

    private static final String GUESTS_DATAS = "guestsDatas";
    private static final String GUE_COL_ID = "guestId";
    private static final String GUE_COL_CONTID = "contactID";
    private static final String GUE_COL_NAME = "guestName";
    private static final String GUE_COL_PHONE1 = "phone1";
    private static final String GUE_COL_PHONE2 = "phone2";
    private static final String GUE_COL_EMAIL1 = "email1";
    private static final String GUE_COL_EMAIL2 = "email2";
    private static final String GUE_COL_CONTACT1 = "contact1";
    private static final String GUE_COL_CONTACT2 = "contact2";

    private static final String TREATMENT_TABLE = "treatments";
    private static final String TRE_COL_ID = "treatmentID";
    private static final String TRE_COL_NAME = "treatmentName";
    private static final String TRE_COL_TIME = "treatmentTime";
    private static final String TRE_COL_PRICE = "treatmentPrice";
    private static final String TRE_COL_COST = "treatmentCost";
    private static final String TRE_COL_NOTE = "treatmentNote";

    private static final String EXPERT_TABLE = "experts";
    private static final String EXP_COL_ID = "expertID";
    private static final String EXP_COL_NAME = "expertName";
    private static final String EXP_COL_NOTE = "expertNote";

    private static final String PRODUCTS_TABLE = "products";
    private static final String PRO_COL_ID = "productID";
    private static final String PRO_COL_NAME = "productName";
    private static final String PRO_COL_PRICE = "productPrice";
    private static final String PRO_COL_COST = "productCost";
    private static final String PRO_COL_NOTE = "productNote";

    private static final String SALES_TABLE = "sales";
    private static final String SALE_COL_ID = "saleID";
    private static final String SALE_COL_PROD = "saleProduct";
    private static final String SALE_COL_GUEST = "saleGuest";
    private static final String SALE_COL_DATE = "saleDate";
    private static final String SALE_COL_NOTE = "saleNote";
    private static final String SALE_COL_QUANT = "saleQuantity";
    private static final String SALE_COL_VALUE = "saleValue";


    private static final String REPORTS_TABLE = "reports";
    private static final String REP_COL_ID = "reportID";
    private static final String REP_COL_TRE = "reportTreatment";
    private static final String REP_COL_EXP = "reportExpert";
    private static final String REP_COL_EXPNAME = "reportExpertName";
    private static final String REP_COL_GUEST = "reportGuest";
    private static final String REP_COL_DATE = "reportDate";
    private static final String REP_COL_DURA = "reportDuration";
    private static final String REP_COL_CONID = "reportConID";
    private static final String REP_COL_NOTE = "reportNote";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VESRION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VESRION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static synchronized DBHelper getHelper(Context context) {
        if (helper == null)
            helper = new DBHelper(context);
        return helper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + GUESTS_DATAS + " ("
                + GUE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GUE_COL_CONTID + " INTEGER, "
                + GUE_COL_NAME + " TEXT, "
                + GUE_COL_PHONE1 + " TEXT, "
                + GUE_COL_PHONE2 + " TEXT, "
                + GUE_COL_EMAIL1 + " TEXT, "
                + GUE_COL_EMAIL2 + " TEXT, "
                + GUE_COL_CONTACT1 + " TEXT, "
                + GUE_COL_CONTACT2 + " TEXT);");

        db.execSQL("CREATE TABLE " + TREATMENT_TABLE + " ("
                + TRE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TRE_COL_NAME + " TEXT, "
                + TRE_COL_TIME + " INTEGER, "
                + TRE_COL_PRICE + " INTEGER, "
                + TRE_COL_COST + " INTEGER, "
                + TRE_COL_NOTE + " TEXT);");

        db.execSQL("CREATE TABLE " + EXPERT_TABLE + " ("
                + EXP_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EXP_COL_NAME + " TEXT, "
                + EXP_COL_NOTE + " TEXT);");

        db.execSQL("CREATE TABLE " + PRODUCTS_TABLE + " ("
                + PRO_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRO_COL_NAME + " TEXT, "
                + PRO_COL_PRICE + " INTEGER, "
                + PRO_COL_COST + " INTEGER, "
                + PRO_COL_NOTE + " TEXT);");

        db.execSQL("CREATE TABLE " + REPORTS_TABLE + " ("
                + REP_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + REP_COL_EXP + " INTEGER NOT NULL, "
                + REP_COL_EXPNAME + " TEXT ,"
                + REP_COL_TRE + " TEXT, "
                + REP_COL_GUEST + " TEXT ,"
                + REP_COL_CONID + " INTEGER, "
                + REP_COL_DATE + " INTEGER, "
                + REP_COL_DURA + " INTEGER, "
                + REP_COL_NOTE + " TEXT, "
                + "FOREIGN KEY (" + REP_COL_EXP + ") REFERENCES " + EXPERT_TABLE + "(" + EXP_COL_ID + ")); ");

        db.execSQL("CREATE TABLE " + SALES_TABLE + " ("
                + SALE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SALE_COL_PROD + " INTEGER NOT NULL, "
                + SALE_COL_GUEST + " TEXT, "
                + SALE_COL_DATE + " INTEGER, "
                + SALE_COL_NOTE + " TEXT, "
                + SALE_COL_VALUE + " INTEGER, "
                + SALE_COL_QUANT + " INTEGER, "
                + " FOREIGN KEY (" + SALE_COL_PROD + ") REFERENCES " + PRODUCTS_TABLE + "(" + PRO_COL_ID + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS EXPERTS");
            db.execSQL("DROP TABLE IF EXISTS PRODUCTS");
            db.execSQL("DROP TABLE IF EXISTS SALES");
            db.execSQL("DROP TABLE IF EXISTS TREATMENTS");
            onCreate(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public Cursor getAllGuests() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM GUESTSDATAS", null);
        return c;
    }

    public Cursor getGuest(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM GUESTSDATAS WHERE name ='" + name + "'", null);
        return c;
    }

    public Cursor getGuest(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM GUESTSDATAS WHERE guestId ='" + id + "'", null);
        return c;
    }

    public boolean addGuest(GuestsDatas g) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("contactID", g.getConId());
        cv.put("guestName", g.getName());
        cv.put("phone1", g.getPhone1());
        cv.put("phone2", g.getPhone2());
        cv.put("email1", g.getEmail1());
        cv.put("email2", g.getEmail2());
        cv.put("contact1", g.getContact1());
        cv.put("contact2", g.getContact2());
        db.insert("guestsDatas", null, cv);
        db.close();
        return true;
    }

    public boolean updateGuest(GuestsDatas g) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("contactID", g.getConId());
        cv.put("guestName", g.getName());
        cv.put("phone1", g.getPhone1());
        cv.put("phone2", g.getPhone2());
        cv.put("email1", g.getEmail1());
        cv.put("email2", g.getEmail2());
        cv.put("contact1", g.getContact1());
        cv.put("contact2", g.getContact2());
        db.update("guestsDatas", cv, GUE_COL_ID + "=?", new String[]{String.valueOf(g.getId())});
        db.close();
        return true;
    }

    public boolean deleteGuest(GuestsDatas g) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("guestsDatas", GUE_COL_ID + "=?", new String[]{String.valueOf(g.getId())});
        return true;
    }

    public Cursor getAllTreatment() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM TREATMENTS", null);
        return c;
    }

    public Cursor getTreatment(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM TREATMENTS WHERE treatmentName ='" + name + "'", null);
        return c;
    }

    public Cursor getTreatment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM TREATMENTS WHERE treatmentID ='" + id + "'", null);
        return c;
    }

    public boolean addTreatment(Treatments t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("treatmentName", t.getName());
        cv.put("treatmentTime", t.getTime());
        cv.put("treatmentPrice", t.getPrice());
        cv.put("treatmentCost", t.getCost());
        cv.put("treatmentNote", t.getNote());
        db.insert("treatments", null, cv);
        db.close();
        return true;
    }

    public boolean updateTreatment(Treatments t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("treatmentName", t.getName());
        cv.put("treatmentTime", t.getTime());
        cv.put("treatmentPrice", t.getPrice());
        cv.put("treatmentCost", t.getCost());
        cv.put("treatmentNote", t.getNote());
        db.update("treatments", cv, TRE_COL_ID + "=?", new String[]{String.valueOf(t.getId())});
        db.close();
        return true;
    }

    public boolean deleteTreatment(Treatments t) {
        SQLiteDatabase db = this.getWritableDatabase();
        int success = db.delete("treatments", TRE_COL_ID + "=?", new String[]{String.valueOf(t.getId())});
        if (success != 0) {
            return true;
        }
        return false;
    }

    public Cursor getAllExperts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM EXPERTS", null);
        return c;
    }

    public Cursor getExpert(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM EXPERTS WHERE expertName='" + name + "'", null);
        return c;
    }

    public Cursor getExpert(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM EXPERTS WHERE expertID='" + id + "'", null);
        return c;
    }

    public boolean addExpert(Experts e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("expertName", e.getName());
        cv.put("expertNote", e.getNote());
        db.insert("experts", null, cv);
        db.close();
        return true;
    }

    public boolean updateExpert(Experts e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("expertName", e.getName());
        cv.put("expertNote", e.getNote());
        db.update("experts", cv, EXP_COL_ID + "=?", new String[]{String.valueOf(e.getId())});
        db.close();
        return true;
    }

    public boolean deleteExpert(Experts e) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("experts", EXP_COL_ID + "=?", new String[]{String.valueOf(e.getId())});
        db.close();
        return true;
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM PRODUCTS", null);
        return c;
    }

    public Cursor getProduct(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM PRODUCTS WHERE productName ='" + name + "'", null);
        return c;
    }

    public Cursor getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM PRODUCTS WHERE productID ='" + id + "'", null);
        return c;
    }

    public boolean addProduct(Products p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("productName", p.getName());
        cv.put("productPrice", p.getPrice());
        cv.put("productCost", p.getCost());
        cv.put("productNote", p.getNote());
        db.insert("products", null, cv);
        db.close();
        return true;
    }

    public boolean updateProduct(Products p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("productName", p.getName());
        cv.put("productPrice", p.getPrice());
        cv.put("productCost", p.getCost());
        cv.put("productNote", p.getNote());
        db.update("products", cv, PRO_COL_ID + "=?", new String[]{String.valueOf(p.getId())});
        db.close();
        return true;
    }

    public boolean deleteProduct(Products p) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("products", PRO_COL_ID + "=?", new String[]{String.valueOf(p.getId())});
        db.close();
        return true;
    }

    public Cursor getAllSales() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM SALES", null);
        return c;
    }

    public Cursor getSales(Products p) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM SALES WHERE saleProduct ='" + p.getId() + "'", null);
        return c;
    }

    public Cursor getSale(long l) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM SALES WHERE saleProduct ='" + l + "'", null);
        return c;
    }

    public boolean addSale(Sales s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("saleProduct", s.getProductID());
        cv.put("saleGuest", s.getGuestName());
        cv.put("saleDate", s.getDate());
        cv.put("saleNote", s.getNote());
        cv.put("saleQuantity", s.getQuantity());
        cv.put("saleValue", s.getValue());
        db.insert("sales", null, cv);
        db.close();
        return true;
    }

    public boolean updatSale(Sales s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("saleProduct", s.getProductID());
        cv.put("saleGuest", s.getGuestName());
        cv.put("saleDate", s.getDate());
        cv.put("saleNote", s.getNote());
        cv.put("saleQuantity", s.getQuantity());
        cv.put("saleValue", s.getValue());
        db.update("sales", cv, SALE_COL_ID + "=?", new String[]{String.valueOf(s.getId())});
        db.close();
        return true;
    }

    public boolean deleteSale(Sales s) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("sales", SALE_COL_ID + "=?", new String[]{String.valueOf(s.getId())});
        db.close();
        return true;
    }

    public Cursor getAllReports(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM REPORTS", null);
        return c;
    }

    public Cursor getReportTratment(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM REPORTS WHERE reportTreatment ='" + id + "'", null);
        return c;
    }

    public Cursor getReportExpert(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM REPORTS WHERE reportExpert ='" + id + "'", null);
        return c;
    }


    public Cursor getReport(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM REPORTS WHERE reportID ='" + id + "'", null);
        return c;
    }

    public boolean addReport(Reports r) {
        Log.d("bookingdata", "db"+r.getGuestName()+", "+r.getExpertname()+", "+r.getTreatment()+", "+r.getDate()+", ");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("reportTreatment", r.getTreatment());
        cv.put("reportExpert", r.getExpert());
        cv.put("reportExpertName", r.getExpertname());
        cv.put("reportGuest", r.getGuestName());
        cv.put("reportConID", r.getConID());
        cv.put("reportDate", r.getDate());
        cv.put("reportDuration", r.getDuration());
        cv.put("reportNote", r.getNote());
        db.close();
        return true;
    }

    public boolean updateReport(Reports r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("reportTreatment", r.getTreatment());
        cv.put("reportExpert", r.getExpert());
        cv.put("reportExpertName", r.getExpertname());
        cv.put("reportGuest", r.getGuestName());
        cv.put("reportConID", r.getConID());
        cv.put("reportDate", r.getDate());
        cv.put("reportDuration", r.getDuration());
        cv.put("reportNote", r.getNote());
        db.update("reports", cv, REP_COL_ID + "=?", new String[]{String.valueOf(r.getId())});
        db.close();
        return true;
    }

    public boolean deleteReport(Reports r) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("reports", REP_COL_ID + "=?", new String[]{String.valueOf(r.getId())});
        db.close();
        return true;
    }
}

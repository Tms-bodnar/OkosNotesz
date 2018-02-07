package hu.okosnotesz.okosnotesz;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import hu.okosnotesz.okosnotesz.model.GuestsDatas;

/**
 * Created by user on 2017.08.10..
 */

public class ContactsImporter extends Activity{

    public ArrayList<GuestsDatas> contactsImport(ContentResolver cr){
        ArrayList<GuestsDatas> gdList = new ArrayList<>();
        Cursor idCursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);
        idCursor.moveToFirst();
        try {
            while (!idCursor.isAfterLast()) {
                String contactId = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                GuestsDatas gd = new GuestsDatas();
                gd.setName(name);
                if (Integer.parseInt(idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{contactId},
                            null);
                    phoneCursor.moveToFirst();
                    int phoneCursorCount = phoneCursor.getCount();
                    String[] phoneNumbers = new String[phoneCursorCount];
                    int i= 0;
                    try {
                        while (!phoneCursor.isAfterLast()) {
                            phoneNumbers[i] = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            i++;
                            phoneCursor.moveToNext();
                        }
                    switch (i){
                        case 1:
                            gd.setPhone1(phoneNumbers[0]);
                            break;
                        default:
                            gd.setPhone1(phoneNumbers[0]);
                            gd.setPhone2(phoneNumbers[1]);
                            break;
                        }
                    } finally {
                        phoneCursor.close();
                    }
                    Cursor emailCursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID +"=?",
                            new String[]{contactId},
                            null);
                    emailCursor.moveToFirst();
                    int emailCursorCount = emailCursor.getCount();
                    String[] emails = new String[emailCursorCount];
                    int j = 0;
                    try{
                        while (!emailCursor.isAfterLast()) {
                            emails[j] = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            j++;
                            emailCursor.moveToNext();
                        }
                        if(emails.length>0) {
                            switch (j) {
                                case 1:
                                    gd.setEmail1(emails[0]);
                                    break;
                                default:
                                    gd.setEmail1(emails[0]);
                                    gd.setEmail2(emails[1]);
                                    break;
                            }
                        }
                    }
                    finally{
                        emailCursor.close();
                    }
                    gdList.add(gd);
                }
                    idCursor.moveToNext();

            }
        }finally {
            idCursor.close();
        }
        Collections.sort(gdList, new GuestsDatas.SortByName());
        return gdList;
    }
}

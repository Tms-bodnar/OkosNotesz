package okosnotesz.hu.okosnotesz;


import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okosnotesz.hu.okosnotesz.model.GuestsDatas;

public class AdminGuestsFragment extends Fragment {

    ArrayList<String> guestsList = null;
    ListView lv;
    private static final String[] PROJECTION = {
            Contacts._ID,
            Contacts.DISPLAY_NAME_PRIMARY,
    };
    private static final String[] FROM = {Contacts.DISPLAY_NAME_PRIMARY};
    private static final int[] TO = {android.R.id.text1};
    private static final String DISPLAY_NAME = Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
            ContactsContract.Contacts.DISPLAY_NAME;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
     public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guests, container, false);
        lv = (ListView) view.findViewById(R.id.guests_list);
        Context context = getActivity();

       ArrayList<GuestsDatas>gdList = getAllContacts();
        CustomGuestsAdapter adapter = new CustomGuestsAdapter(context, R.layout.guests_list_item, gdList);
        lv.setAdapter(adapter);

        return view;
    }

    public ArrayList<GuestsDatas> getAllContacts() {
        GuestsDatas gd = null;
        ArrayList<GuestsDatas> gdList = new ArrayList<>();
        Cursor mCursor = getActivity().getApplicationContext().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI, null, null, null, ContactsContract.Data.CONTACT_ID);
        long lastContactId = -1L;
        mCursor.moveToFirst();

        try {
            while (!mCursor.isAfterLast()) {
                int colIdxContactId = mCursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
                long contactId = mCursor.getLong(colIdxContactId);


                        int colIdxMimetype = mCursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
                    if(lastContactId!=contactId){
                        if (mCursor.getString(colIdxMimetype).equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                            String contactName = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                            gd = new GuestsDatas();
                            gd.setName(contactName);
                        }
                        if (mCursor.getString(colIdxMimetype).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                            String phone = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            if(gd != null && phone != null || !phone.isEmpty()) {
                                gd.setPhone1(phone);
                            }
                        }
                        if (mCursor.getString(colIdxMimetype).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                            String email = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                            gd.setEmail1(email);
                        }
                        if (gd != null && gd.getName() != null && gd.getPhone1() != null && gd.getEmail1() != null) {

                            gdList.add(gd);
                            lastContactId=contactId;
                            gd = null;
                        }
                    }
                mCursor.moveToNext();
                }

        }finally {
            mCursor.close();
        }

        Collections.sort(gdList, new GuestsDatas.SortByName());
        return gdList;
    }

   /* public ArrayList<String> getAllNames()
    {

        String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
        String[] selectionArgs = { "0", "1" };

        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
        cursor.moveToFirst();
        int len = cursor.getCount();

        ArrayList<String> numbers = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));

                String[] cProjection = { DISPLAY_NAME, ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID };

                Cursor groupCursor = getActivity().getApplicationContext().getContentResolver().query(
                        ContactsContract.Data.CONTENT_URI,
                        cProjection,
                        ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                                + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
                                + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
                        new String[] { String.valueOf(id) }, null);
                if (groupCursor != null && groupCursor.moveToFirst())
                {
                    do
                    {

                        int nameCoumnIndex = groupCursor.getColumnIndex(DISPLAY_NAME);

                        String name = groupCursor.getString(nameCoumnIndex);

                        long contactId = groupCursor.getLong(groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));

                        Cursor numberCursor = getActivity().getApplicationContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                                new String[] { DISPLAY_NAME}, ContactsContract.Contacts._ID + "=" + contactId, null, null);

                        if (numberCursor.moveToFirst())
                        {
                            int numberColumnIndex = numberCursor.getColumnIndex(DISPLAY_NAME);
                            do
                            {
                                String phoneNumber = numberCursor.getString(numberColumnIndex);
                                numbers.add(phoneNumber);
                            } while (numberCursor.moveToNext());
                            numberCursor.close();
                        }
                    } while (groupCursor.moveToNext());
                    groupCursor.close();
                }
            cursor.moveToNext();
        }

        cursor.close();

        return numbers;
    }*/
}
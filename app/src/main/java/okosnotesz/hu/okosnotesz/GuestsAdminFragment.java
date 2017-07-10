package okosnotesz.hu.okosnotesz;


import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import okosnotesz.hu.okosnotesz.model.GuestsDatas;

public class GuestsAdminFragment extends Fragment {

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
        guestsList = getAllNames();
        ArrayList<GuestsDatas> glist = new ArrayList<>(guestsList.size());
        for (String s : guestsList) {
            GuestsDatas gd = new GuestsDatas(s);
            glist.add(gd);
        }
        CustomGuestsAdapter adapter = new CustomGuestsAdapter(context, R.layout.guests_list_item, glist);
        lv.setAdapter(adapter);
        return view;
    }


    public ArrayList<String> getAllNames()
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
    }
}
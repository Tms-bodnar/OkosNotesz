package okosnotesz.hu.okosnotesz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Guests extends AppCompatActivity {

    private ListView guestList;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    @SuppressLint("InlinedApi")
    //public static final String[] GROUP_PROJECTION = { ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };
    private static final String GROUP_SELECTION = ContactsContract.Groups.DELETED +"=? AND " + ContactsContract.Groups.GROUP_VISIBLE + "=?";

    private String groupNameInput = "Kedvencek";
    private String[] groupSelectionArgs = {"0", "1"};
    private static final String[] GROUP_SECOND_PROJECTION =
            {
                            Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID,
            };
    @SuppressLint("InlinedApi")
    private static final String GROUP_SECOND_SELECTION = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" +
                                " AND " +  ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='" +
                                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "='";
            /*(
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME);*/


    String nameSearch = "";
    String[] nameSearchArgs = {nameSearch};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guests);
        guestList = (ListView) findViewById(R.id.guest_list_view);
        Log.d("0001 ", "onCreate");
        showContacts();
    }

    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            Log.d("0002 ", "show1");

        }else{
                List<String> contacts = getAllNumbersFromGroupId();
                Log.d("0003 ", "show2");
            Log.d("contacts list ", ""+contacts.size());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,contacts);

            Log.d("0004 ", "show3");
            guestList.setAdapter(adapter);

            Log.d("0005 ", "show4 " + guestList.getAdapter().getCount());
            }
    }

    private List<String> getContacts() {


        List<String> contacts = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursorGroup = cr.query(ContactsContract.Groups.CONTENT_URI, null, GROUP_SELECTION, groupSelectionArgs,null);
      //  Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        Log.d( "cursor  ",""+cursorGroup.getColumnCount());
       // Log.d("group id ", ""+cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
        cursorGroup.moveToFirst();
        int cursorCount = cursorGroup.getCount();
        for (int i = 0; i < cursorCount; i++) {

            String groupId = cursorGroup.getString(cursorGroup.getColumnIndex(ContactsContract.Groups._ID));
            String groupTitle = cursorGroup.getString(cursorGroup.getColumnIndex(ContactsContract.Groups.TITLE));

            if(groupTitle.equals(groupNameInput)) {
                Cursor cursorGroupSecond = cr.query(
                        ContactsContract.Data.CONTENT_URI,
                        GROUP_SECOND_PROJECTION, GROUP_SECOND_SELECTION,
                        new String[]{String.valueOf(groupId)},
                        null);
                if (cursorGroupSecond != null && cursorGroupSecond.moveToFirst()) {
                    do {
                        int groupColumnIndex = cursorGroupSecond.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
                        String groupName = cursorGroupSecond.getString(groupColumnIndex);
                        long contactId = cursorGroupSecond.getLong(cursorGroupSecond.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));

                        Cursor cursorName = cr.query(ContactsContract.Data.CONTENT_URI,
                        new String[] { ContactsContract.Data.DISPLAY_NAME },
                                ContactsContract.Data.DISPLAY_NAME + "=" + contactId,
                                null, null
                                );
                        if(cursorName.moveToFirst()) {

                            int nameColumnIndex = cursorName.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
                            do {
                                String name = cursorName.getString(nameColumnIndex);
                                contacts.add(name);
                            } while (cursorName.moveToNext());
                            cursorName.close();
                        }
                        break;
                    }while (cursorGroupSecond.moveToNext());
                        cursorGroupSecond.close();
                }
                break;
            }
            cursorGroup.moveToNext();
        }
        cursorGroup.close();
        Log.d("0007", ""+contacts.size());
        return contacts;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            int[] grantResults){
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("00021 ", "showaccess");
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public ArrayList<String> getAllNumbersFromGroupId()
    {
        String navn = "Család" ;
        String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
        String[] selectionArgs = { "0", "1" };
        Cursor cursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
        cursor.moveToFirst();
        int len = cursor.getCount();

        ArrayList<String> numbers = new ArrayList<String>();
        for (int i = 0; i < len; i++)
        {
            String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));

            if (title.equals(navn))
            {
                String[] cProjection = { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID };

                Cursor groupCursor = getContentResolver().query(
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

                        int nameCoumnIndex = groupCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                        String name = groupCursor.getString(nameCoumnIndex);

                        long contactId = groupCursor.getLong(groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));

                        Cursor numberCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                                new String[] { ContactsContract.Contacts.DISPLAY_NAME}, ContactsContract.Contacts._ID + "=" + contactId, null, null);

                        if (numberCursor.moveToFirst())
                        {
                            int numberColumnIndex = numberCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
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
                break;
            }

            cursor.moveToNext();
        }
        cursor.close();

        return numbers;
    }
}
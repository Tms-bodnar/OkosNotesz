package okosnotesz.hu.okosnotesz.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import okosnotesz.hu.okosnotesz.R;

public class AdminGuestsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guests,container, false);
        Button btn = (Button) view.findViewById(R.id.btnGuests);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivity(i);
            }
        });
        return view;
    }
}

/*    @Override
     public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guests, container, false);
        FloatingActionButton fabGuests = (FloatingActionButton) view.findViewById(R.id.fabGuest);
        fabGuests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AdminDialog.class);
                i.putExtra("hu.okosnotesz.GuestsDatas" , (GuestsDatas) null);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        lv = (ListView) view.findViewById(R.id.guests_list);
        Context context = getActivity();
        ArrayList<GuestsDatas>gdList = getAllContacts();
        CustomGuestsAdapter adapter = new CustomGuestsAdapter(context, gdList, REQUEST_CODE);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode  == REQUEST_CODE){
            refreshView(getAllContacts());
        }
    }

    private View refreshView(ArrayList<GuestsDatas> allContacts) {
        lv = (ListView) getActivity().findViewById(R.id.guests_list);
        Context context = getActivity();
        CustomGuestsAdapter adapter = new CustomGuestsAdapter(context, allContacts, REQUEST_CODE);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
        return getView();
    }

    public ArrayList<GuestsDatas> getAllContacts() {
        ArrayList<GuestsDatas> guestsList = new ArrayList<>();
        DBHelper helper = DBHelper.getHelper(getActivity());
        Cursor cursor = helper.getAllGuests();
        cursor.moveToFirst();
        try {
            while (!cursor.isAfterLast()) {
                GuestsDatas gd = new GuestsDatas();
                gd.setId(cursor.getInt(cursor.getColumnIndex("guestId")));
                gd.setName(cursor.getString(cursor.getColumnIndex("guestName")));
                gd.setPhone1(cursor.getString(cursor.getColumnIndex("phone1")));
                gd.setPhone2(cursor.getString(cursor.getColumnIndex("phone2")));
                gd.setEmail1(cursor.getString(cursor.getColumnIndex("email1")));
                gd.setEmail2(cursor.getString(cursor.getColumnIndex("email2")));
                gd.setContact1(cursor.getString(cursor.getColumnIndex("contact1")));
                gd.setContact2(cursor.getString(cursor.getColumnIndex("contact2")));
                guestsList.add(gd);
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
            helper.close();
        }
        if(guestsList.isEmpty()){
            guestsList.add(new GuestsDatas(String.valueOf(R.string.noDatas), String.valueOf(R.string.addNewData)));
        }
        return guestsList;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info) {
        if(v.getId() == R.id.guests_list ){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) info;
            ListView lv = (ListView) v;
            this.gd = (GuestsDatas) lv.getItemAtPosition(menuInfo.position);
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.admin_menu, menu);
            menu.setHeaderTitle(this.gd.getName());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(getUserVisibleHint()){
            switch(item.getItemId()){
                case R.id.adminMenuEdit:
                    return guestGataEdit(this.gd);
                case R.id.adminMenuDelete:
                    return guestDataDelete(this.gd);
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return false;
    }

    private boolean guestGataEdit(GuestsDatas gd) {
        Intent i = new Intent(getContext(), AdminDialog.class);
        i.putExtra("hu.okosnotesz.GuestsDatas", gd);
        startActivityForResult(i, REQUEST_CODE);
        return true;
    }

    private boolean guestDataDelete(final GuestsDatas gd) {
        final boolean[] successful = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater(null);
        View alertView = inflater.inflate(R.layout.admin_ok_dialog, null);
        builder.setView(alertView);
        builder.setTitle(R.string.deleteAlert);
        TextView tv = (TextView) alertView.findViewById(R.id.adminOkTextView);
        if(gd != null){
            tv.setText(gd.getName());
        }
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper helper = DBHelper.getHelper(getContext());
                successful[0] = helper.deleteGuest(gd);
                helper.close();
                if(successful[0]){
                    refreshView(getAllContacts());
                    Snackbar.make(getView(), R.string.deleteSuccessful, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(getView(), R.string.cancel, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
        return successful[0];
    }


}*/

        /*GuestsDatas gd = null;
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
                            Log.d("xxx", gd.getName());
                        }
                        *//*if (mCursor.getString(colIdxMimetype).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
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
*//*
                            gdList.add(gd);
                            lastContactId=contactId;
                            gd = null;
//                        }
                    }
                mCursor.moveToNext();
                }

        }finally {
            mCursor.close();
        }

//        Collections.sort(gdList, new GuestsDatas.SortByName());
        return gdList;
    }

   *//* public ArrayList<String> getAllNames()
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
    }*//*
}*/
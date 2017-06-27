package okosnotesz.hu.okosnotesz;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class GroupCreateFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private CursorAdapter mAdapter;
    private static final String[] PROJECTION = {
            Contacts._ID, // _ID is always required
            Contacts.DISPLAY_NAME_PRIMARY // that's what we want to display
    };
    private static final String[] FROM = {Contacts.DISPLAY_NAME_PRIMARY};
    private static final int[] TO = {android.R.id.text1};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity();
        int layout = android.R.layout.simple_list_item_1;
        Cursor cursor = null;
        int flags = 0;
        mAdapter = new SimpleCursorAdapter(context, layout, cursor, FROM, TO, flags);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri = Contacts.CONTENT_URI;

        return new CursorLoader(getActivity(),
                contentUri,
                PROJECTION,
                null,
                null,
                Contacts.DISPLAY_NAME_PRIMARY);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Once cursor is loaded, give it to adapter
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // on reset take any old cursor away
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("111", "ok");
        Toast t = new Toast(getContext());
        t.setText(""+ mAdapter.getItem(position).toString());
        t.show();
    }
}
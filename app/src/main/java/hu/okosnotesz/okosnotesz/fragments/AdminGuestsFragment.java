package hu.okosnotesz.okosnotesz.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hu.okosnotesz.okosnotesz.R;

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
        btn.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    ContactsContract.Contacts.CONTENT_URI);
            startActivity(i);
        });
        return view;
    }
}
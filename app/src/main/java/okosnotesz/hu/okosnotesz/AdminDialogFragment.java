package okosnotesz.hu.okosnotesz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import okosnotesz.hu.okosnotesz.model.Treatments;


/**
 * Created by user on 2017.07.29..
 */

public class AdminDialogFragment extends DialogFragment implements TextView.OnEditorActionListener{

    private Treatments treatment = null;

    public AdminDialogFragment() {
    }

    public interface AdminTreatmentDialogListener{
        void onFinishedAdmin(Treatments t);
    }

    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.treatment_add_dialog, container, false);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}

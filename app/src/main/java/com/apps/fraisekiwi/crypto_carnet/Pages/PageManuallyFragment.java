package com.apps.fraisekiwi.crypto_carnet.Pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import com.apps.fraisekiwi.crypto_carnet.R;

/**
 * Created by Marc-André Piché on 4/13/18.
 */

/*https://developer.android.com/guide/topics/ui/dialogs.html
* The activity that creates an instance of this dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */

public class PageManuallyFragment extends DialogFragment {

    public interface OnAcceptListener {
        void onAccept(String address_out);
    }

    // Use this instance of the interface to deliver action events
    OnAcceptListener acceptListener;


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            acceptListener = (OnAcceptListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EnterManuallyListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle(R.string.manual_pop_title);
        builder.setView(inflater.inflate(R.layout.enter_manually_popup, null))
                .setPositiveButton(R.string.create_btn_msg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        //from the field manual input
                        Dialog dialogObj =Dialog.class.cast(dialog);
                        EditText manual_input = dialogObj.findViewById(R.id.add_manual_input);
                        String address_input = manual_input.getText().toString();
                        acceptListener.onAccept(address_input);
                    }
                })
                .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dismiss the window on cancel
                        dismiss();
                    }
                });
        return builder.create();
    }
}


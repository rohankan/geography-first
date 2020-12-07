package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
public class WelcomeDialogFragment extends DialogFragment {
    public static final String EXTRA_CHECKED = "com.ropkastudios.android.thegeographyapp.checked";

    public static WelcomeDialogFragment newInstance() {
        return new WelcomeDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_welcome, null);

        final CheckBox checkBox = (CheckBox) v.findViewById(R.id.dialog_welcome_check);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getString(R.string.welcome_exclamation))
                .setPositiveButton(R.string.gotit,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK, checkBox.isChecked());
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, boolean checked) {

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CHECKED, checked);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

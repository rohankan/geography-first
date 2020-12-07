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
import android.widget.TextView;
public class CopyPasteDialogFragment extends DialogFragment {
    private static final String ARG_TITLE = "cptitle";
    private static final String ARG_CONTENTS = "cpcontents";

    public static CopyPasteDialogFragment newInstance(String title, String contents) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENTS, contents);

        CopyPasteDialogFragment fragment = new CopyPasteDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_copy_paste, null);

        TextView tv = (TextView) v.findViewById(R.id.copy_paste_tv);
        tv.setText(getArguments().getString(ARG_CONTENTS));


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getArguments().getString(ARG_TITLE))
                .setPositiveButton(R.string.done,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode) {

        if (getTargetFragment() == null) {
            return;
        }

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, new Intent());
    }
}

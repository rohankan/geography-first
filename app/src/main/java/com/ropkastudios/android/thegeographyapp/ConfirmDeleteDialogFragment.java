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

import java.io.Serializable;

public class ConfirmDeleteDialogFragment extends DialogFragment {
    public static final String EXTRA_ADDITIONAL = "com.ropkastudios.android.thegeographyapp.extra_additional";

    private static final String ARG_DIALOG_TITLE_BASIC = "dialog_title_basic";
    private static final String ARG_PARAGRAPH_BASIC = "paragraph_basic";
    private static final String ARG_NEGATIVE_BUTTON_TEXT_BASIC = "negative_button_basic";
    private static final String ARG_POSITIVE_BUTTON_TEXT_BASIC = "positive_button_basic";
    private static final String ARG_ADDITIONAL = "uuid_basic";

    public static ConfirmDeleteDialogFragment newInstance( String dialogTitle, String paragraph,
                                                           String positiveButtonText, String negativeButtonText,
                                                           Serializable extra) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DIALOG_TITLE_BASIC, dialogTitle);
        args.putSerializable(ARG_PARAGRAPH_BASIC, paragraph);
        args.putSerializable(ARG_NEGATIVE_BUTTON_TEXT_BASIC, negativeButtonText);
        args.putSerializable(ARG_POSITIVE_BUTTON_TEXT_BASIC, positiveButtonText);
        args.putSerializable(ARG_ADDITIONAL, extra);

        ConfirmDeleteDialogFragment fragment = new ConfirmDeleteDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String dialogTitle = (String) getArguments().getSerializable(ARG_DIALOG_TITLE_BASIC);
        String paragraphBasic = (String) getArguments().getSerializable(ARG_PARAGRAPH_BASIC);
        String negativeButtonText = (String) getArguments().getSerializable(ARG_NEGATIVE_BUTTON_TEXT_BASIC);
        String positiveButtonText = (String) getArguments().getSerializable(ARG_POSITIVE_BUTTON_TEXT_BASIC);
        final Serializable preserveUUID = getArguments().getSerializable(ARG_ADDITIONAL);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_dialog_basic_text_and_text_action, null);

        TextView paragraphTextView = (TextView) v.findViewById(R.id.fragment_dialog_basic_text_paragraph);
        paragraphTextView.setText(paragraphBasic);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(dialogTitle)
                .setPositiveButton(positiveButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResultWithExtra(Activity.RESULT_OK, preserveUUID);
                            }
                        })
                .setNegativeButton(negativeButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_CANCELED);
                            }

                        })
                .create();
    }

    private void sendResult(int resultCode) {

        if (getTargetFragment() == null) {
            return;
        }

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
    }

    private void sendResultWithExtra(int resultCode, Serializable additional) {

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADDITIONAL, additional);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

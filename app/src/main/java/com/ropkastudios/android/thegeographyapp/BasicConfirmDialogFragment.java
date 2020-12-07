package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class BasicConfirmDialogFragment extends DialogFragment {
    private static final String ARG_DIALOG_TITLE_BASIC = "dialog_title_basic";
    private static final String ARG_PARAGRAPH_BASIC = "paragraph_basic";
    private static final String ARG_NEGATIVE_BUTTON_TEXT_BASIC = "negative_button_basic";
    private static final String ARG_POSITIVE_BUTTON_TEXT_BASIC = "positive_button_basic";
    private static final String ARG_NEGATIVE_BUTTON_VISIBLE = "negative_button_visible";

    public static BasicConfirmDialogFragment newInstance(String dialogTitle, String paragraph,
                                                         String positiveButtonText, String negativeButtonText,
                                                         boolean negativeButtonVisible) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DIALOG_TITLE_BASIC, dialogTitle);
        args.putSerializable(ARG_PARAGRAPH_BASIC, paragraph);
        args.putSerializable(ARG_NEGATIVE_BUTTON_TEXT_BASIC, negativeButtonText);
        args.putSerializable(ARG_POSITIVE_BUTTON_TEXT_BASIC, positiveButtonText);
        args.putBoolean(ARG_NEGATIVE_BUTTON_VISIBLE, negativeButtonVisible);

        BasicConfirmDialogFragment fragment = new BasicConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String dialogTitle = (String) getArguments().getSerializable(ARG_DIALOG_TITLE_BASIC);
        String paragraphBasic = (String) getArguments().getSerializable(ARG_PARAGRAPH_BASIC);
        String negativeButtonText = (String) getArguments().getSerializable(ARG_NEGATIVE_BUTTON_TEXT_BASIC);
        String positiveButtonText = (String) getArguments().getSerializable(ARG_POSITIVE_BUTTON_TEXT_BASIC);
        boolean negativeButtonVisible = getArguments().getBoolean(ARG_NEGATIVE_BUTTON_VISIBLE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_dialog_basic_text_and_text_action, null);

        TextView paragraphTextView = (TextView) v.findViewById(R.id.fragment_dialog_basic_text_paragraph);
        paragraphTextView.setText(paragraphBasic);

        if (negativeButtonVisible) {
            return new AlertDialog.Builder(getActivity())
                    .setView(v)
                    .setTitle(dialogTitle)
                    .setPositiveButton(positiveButtonText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendResult(Activity.RESULT_OK);
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
        } else {
            return new AlertDialog.Builder(getActivity())
                    .setView(v)
                    .setTitle(dialogTitle)
                    .setPositiveButton(positiveButtonText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendResult(Activity.RESULT_OK);
                                }
                            })
                    .create();
        }
    }

    private void sendResult(int resultCode) {

        if (getTargetFragment() == null) {
            return;
        }

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
    }
}

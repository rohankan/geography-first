package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BasicConfirmCheckerDialogFragment extends DialogFragment {
    private static final String ARG_DIALOG_TITLE_BASIC = "dialog_title_basic";
    private static final String ARG_PARAGRAPH_BASIC = "paragraph_basic";
    private static final String ARG_NEGATIVE_BUTTON_TEXT_BASIC = "negative_button_basic";
    private static final String ARG_POSITIVE_BUTTON_TEXT_BASIC = "positive_button_basic";
    private static final String ARG_PASSABLE = "arg_passable_bool";
    private static final String ARG_ERROR_MESSAGE = "arg_error";
    public static BasicConfirmCheckerDialogFragment newInstance(String dialogTitle, String paragraph,
                                                         String positiveButtonText, String negativeButtonText,
                                                                String errorMessage,
                                                         boolean passable) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DIALOG_TITLE_BASIC, dialogTitle);
        args.putSerializable(ARG_PARAGRAPH_BASIC, paragraph);
        args.putSerializable(ARG_ERROR_MESSAGE, errorMessage);
        args.putSerializable(ARG_NEGATIVE_BUTTON_TEXT_BASIC, negativeButtonText);
        args.putSerializable(ARG_POSITIVE_BUTTON_TEXT_BASIC, positiveButtonText);
        args.putBoolean(ARG_PASSABLE, passable);

        BasicConfirmCheckerDialogFragment fragment = new BasicConfirmCheckerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String dialogTitle = (String) getArguments().getSerializable(ARG_DIALOG_TITLE_BASIC);
        String paragraphBasic = (String) getArguments().getSerializable(ARG_PARAGRAPH_BASIC);
        String errorMessage = (String) getArguments().getSerializable(ARG_ERROR_MESSAGE);
        String negativeButtonText = (String) getArguments().getSerializable(ARG_NEGATIVE_BUTTON_TEXT_BASIC);
        String positiveButtonText = (String) getArguments().getSerializable(ARG_POSITIVE_BUTTON_TEXT_BASIC);
        final boolean passable = getArguments().getBoolean(ARG_PASSABLE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.basic_confirm_checker_dialog_fragment_layout, null);

        TextView paragraphTextView = (TextView)
                v.findViewById(R.id.fragment_confirm_checker_dialog_basic_paragraph);
        paragraphTextView.setText(paragraphBasic);

        final TextView errorPopupTextView = (TextView)
                v.findViewById(R.id.fragment_confirm_checker_dialog_error_paragraph);
        errorPopupTextView.setText(errorMessage);

        final AlertDialog dialogFrag = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(dialogTitle)
                .setPositiveButton(positiveButtonText, null)
                .setNegativeButton(negativeButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_CANCELED);
                            }

                        })
                .create();

        dialogFrag.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = dialogFrag.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (passable) {
                            sendResult(Activity.RESULT_OK);
                            dialogFrag.dismiss();
                        } else {
                            errorPopupTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        return dialogFrag;
    }

    private void sendResult(int resultCode) {

        if (getTargetFragment() == null) {
            return;
        }

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
    }
}

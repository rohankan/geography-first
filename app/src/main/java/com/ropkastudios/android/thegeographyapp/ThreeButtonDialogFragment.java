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
public class ThreeButtonDialogFragment extends DialogFragment {
    public static final String EXTRA_POSITION = "com.ropkastudios.android.thegeographyapp.position";
    private static final String ARG_DIALOG_TITLE = "dialog_title_text";
    private static final String ARG_PARAGRAPH_TEXT = "paragraph_text";
    private static final String ARG_NEGATIVE_BUTTON_TEXT = "negative_button_text";
    private static final String ARG_POSITIVE_BUTTON_TEXT = "positive_button_text";
    private static final String ARG_NEUTRAL_BUTTON_TEXT = "neutral_button_text";
    private static final String ARG_POSITION = "arg_position";

    public static ThreeButtonDialogFragment newInstance(String dialogTitle, String paragraph,
                                                        String neutralButtonText, String negativeButtonText,
                                                        String positiveButtonText, int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DIALOG_TITLE, dialogTitle);
        args.putSerializable(ARG_PARAGRAPH_TEXT, paragraph);
        args.putSerializable(ARG_NEUTRAL_BUTTON_TEXT, neutralButtonText);
        args.putSerializable(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText);
        args.putSerializable(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText);
        args.putInt(ARG_POSITION, position);

        ThreeButtonDialogFragment fragment = new ThreeButtonDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String dialogTitle = (String) getArguments().getSerializable(ARG_DIALOG_TITLE);
        String paragraphBasic = (String) getArguments().getSerializable(ARG_PARAGRAPH_TEXT);
        String negativeButtonText = (String) getArguments().getSerializable(ARG_NEGATIVE_BUTTON_TEXT);
        String positiveButtonText = (String) getArguments().getSerializable(ARG_POSITIVE_BUTTON_TEXT);
        String neutralButtonText = (String) getArguments().getSerializable(ARG_NEUTRAL_BUTTON_TEXT);
        final int position = getArguments().getInt(ARG_POSITION);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_dialog_basic_text_and_text_action, null);

        TextView paragraphTextView = (TextView)
                v.findViewById(R.id.fragment_dialog_basic_text_paragraph);
        paragraphTextView.setText(paragraphBasic);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(dialogTitle)
                .setPositiveButton(positiveButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK, position);
                            }
                        })
                .setNegativeButton(negativeButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_FIRST_USER, position);
                            }

                        })
                .setNeutralButton(neutralButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getTargetFragment() != null) {
                                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                                            Activity.RESULT_CANCELED, null);
                                }
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, int position) {

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, position);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

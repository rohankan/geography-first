package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class BasicTextInputDialogFragment extends DialogFragment {
    public static final String EXTRA_TEXT = "com.ropkastudios.android.thegeographyapp.extra_text";
    public static final String EXTRA_POSITION = "com.ropkastudios.android.thegeographyapp.extra_position";

    private static final String ARG_DIALOG_TITLE_BASIC = "dialog_basic_txt_input_title";
    private static final String ARG_DIALOG_TEXT_VIEW = "dialog_basic_txt_input_txt_view";
    private static final String ARG_EDIT_HINT = "dialog_basic_txt_input_edit_hint";
    private static final String ARG_EDIT_TEXT = "dialog_basic_txt_input_edit_text";
    private static final String ARG_NEGATIVE_BUTTON_TEXT_BASIC = "negative_button_basic_txt_input_dialog";
    private static final String ARG_POSITIVE_BUTTON_TEXT_BASIC = "positive_button_basic_txt_input_dialog";
    private static final String ARG_INDEX = "positive_button_basic_index";
    private static final String ARG_POSITION = "positive_button_basic_position";
    public static final String HIDE_TITLE = "dialog_basic_txt_hidetitle";

    public static BasicTextInputDialogFragment newInstance(String dialogTitle, String textViewTitle, String editHint,
                                                         String positiveButtonText, String negativeButtonText, String editText,
                                                           int Index, int Position) {
        Bundle args = new Bundle();
        args.putString(ARG_DIALOG_TITLE_BASIC, dialogTitle);
        args.putString(ARG_DIALOG_TEXT_VIEW, textViewTitle);
        args.putString(ARG_EDIT_HINT, editHint);
        args.putString(ARG_NEGATIVE_BUTTON_TEXT_BASIC, negativeButtonText);
        args.putString(ARG_POSITIVE_BUTTON_TEXT_BASIC, positiveButtonText);
        args.putString(ARG_EDIT_TEXT, editText);
        args.putInt(ARG_POSITION, Position);
        args.putInt(ARG_INDEX, Index);

        BasicTextInputDialogFragment fragment = new BasicTextInputDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String dialogTitle = getArguments().getString(ARG_DIALOG_TITLE_BASIC, "");
        String textViewText = getArguments().getString(ARG_DIALOG_TEXT_VIEW, "");
        String editHint = getArguments().getString(ARG_EDIT_HINT, "");
        String negativeButtonText = getArguments().getString(ARG_NEGATIVE_BUTTON_TEXT_BASIC, "");
        String positiveButtonText = getArguments().getString(ARG_POSITIVE_BUTTON_TEXT_BASIC, "");
        String text = getArguments().getString(ARG_EDIT_TEXT, "");
        final int position = getArguments().getInt(ARG_POSITION, -1);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_basic_text_input, null);

        TextView textView = (TextView) v.findViewById(R.id.dialog_basic_text_input_text_view);
        if (textViewText.equals(HIDE_TITLE)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(textViewText);
        }

        final EditText editText = (EditText) v.findViewById(R.id.dialog_basic_text_input_edit_text);
        editText.setHint(editHint);
        editText.setText(text);
        editText.setSelection(getArguments().getInt(ARG_INDEX));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(dialogTitle)
                .setPositiveButton(positiveButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getTargetFragment() == null) {
                                    return;
                                }


                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                                Intent intent = new Intent();
                                intent.putExtra(EXTRA_TEXT, editText.getText().toString());
                                intent.putExtra(EXTRA_POSITION, position);

                                getTargetFragment().onActivityResult(getTargetRequestCode(),
                                        Activity.RESULT_OK, intent);
                            }
                        })
                .setNegativeButton(negativeButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getTargetFragment() == null) {
                                    return;
                                }

                                getTargetFragment().onActivityResult(getTargetRequestCode(),
                                        Activity.RESULT_CANCELED, null);
                            }
                        })
                .create();
    }
}
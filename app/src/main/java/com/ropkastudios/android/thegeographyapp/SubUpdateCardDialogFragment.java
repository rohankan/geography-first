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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;
public class SubUpdateCardDialogFragment extends DialogFragment {
    public static final String EXTRA_SUB_TITLE = "com.ropkastudios.android.thegeographyapp.sub_title";
    public static final String EXTRA_SUB_UUID = "com.ropkastudios.android.thegeographyapp.sub_uuid";
    public static final String EXTRA_SUB_CONTENTS = "com.ropkastudios.android.thegeographyapp.sub_contents";
    public static final String EXTRA_SUB_NOTES = "com.ropkastudios.android.thegeographyapp.notes";

    private static final String ARG_SUB_CARD_TITLE = "sub_card_title";
    private static final String ARG_SUB_TITLE = "sub_passTitle";
    private static final String ARG_SUB_UUID = "sub_card_uuid";
    private static final String ARG_SUB_CATEGORY = "sub_category";
    private static final String ARG_SUB_CONTENTS = "sub_contents";
    private static final String ARG_SUB_NOTES = "sub_notes";

    public static SubUpdateCardDialogFragment newInstance(String titleTextView, String titleForEditText, UUID preserveId,
                                                          String category, String contents, String notes) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SUB_CARD_TITLE, titleTextView);
        args.putSerializable(ARG_SUB_TITLE, titleForEditText);
        args.putSerializable(ARG_SUB_UUID, preserveId);
        args.putSerializable(ARG_SUB_CATEGORY, category);
        args.putSerializable(ARG_SUB_CONTENTS, contents);
        args.putSerializable(ARG_SUB_NOTES, notes);

        SubUpdateCardDialogFragment fragment = new SubUpdateCardDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String cardTitle = (String) getArguments().getSerializable(ARG_SUB_CARD_TITLE);
        String titleEditTextHint = (String) getArguments().getSerializable(ARG_SUB_TITLE);
        final UUID preserveUUID = (UUID) getArguments().getSerializable(ARG_SUB_UUID);
        final String category = (String) getArguments().getSerializable(ARG_SUB_CATEGORY);
        final String contents = (String) getArguments().getSerializable(ARG_SUB_CONTENTS);
        final String notes = (String) getArguments().getSerializable(ARG_SUB_NOTES);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_sub_card_creator, null);

        TextView titleTextView = (TextView) v.findViewById(R.id.dialog_sub_card_creator_title_text_view);
        titleTextView.setText(cardTitle);

        final EditText titleEditText = (EditText) v.findViewById(R.id.dialog_sub_card_creator_title_edit_text);
        titleEditText.setText(titleEditTextHint);
        titleEditText.setHint(getString(R.string.title_prompt));

        final TextView popupTextView = (TextView) v.findViewById(R.id.dialog_sub_card_creator_popup_text_view);

        final AlertDialog dialogAlert = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getString(R.string.update_placeholder_colon, titleEditTextHint))
                .setPositiveButton(R.string.update, null)
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelRequest(Activity.RESULT_CANCELED);
                            }

                        })
                .create();

        dialogAlert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = dialogAlert.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String titleEntryText = titleEditText.getText().toString();
                        SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
                        if (cardLab.isTitleUnique(titleEntryText.toLowerCase(), category)) {
                            sendResult(Activity.RESULT_OK, titleEntryText, preserveUUID, contents, notes);
                            dialogAlert.dismiss();
                        } else {
                            popupTextView.setText(getString(R.string.pleaseenterdifftitle, titleEntryText));
                            popupTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        return dialogAlert;
    }

    private void sendResult(int resultCode, String titleEntry, UUID preservedUUID, String content, String note) {

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SUB_TITLE, titleEntry);
        intent.putExtra(EXTRA_SUB_UUID, preservedUUID);
        intent.putExtra(EXTRA_SUB_CONTENTS, content);
        intent.putExtra(EXTRA_SUB_NOTES, note);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void cancelRequest(int resultCode) {

        if (getTargetFragment() == null) {
            return;
        }

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
    }
}

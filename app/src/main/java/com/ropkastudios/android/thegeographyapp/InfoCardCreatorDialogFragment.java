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
public class InfoCardCreatorDialogFragment extends DialogFragment {
    public static final String EXTRA_TITLE = "com.ropkastudios.android.thegeographyapp.title";
    public static final String EXTRA_PARAGRAPH = "com.ropkastudios.android.thegeographyapp.paragraph";

    private static final String ARG_CARD_TITLE = "card_title";
    private static final String ARG_PARAGRAPH = "paragraph";
    private static final String ARG_TITLE = "passTitle";
    private static final String ARG_PARA = "passParagraph";

    public static InfoCardCreatorDialogFragment newInstance(String title, String paragraph, String titleForEditText,
                                                            String paraForEditText) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD_TITLE, title);
        args.putSerializable(ARG_PARAGRAPH, paragraph);
        args.putSerializable(ARG_TITLE, titleForEditText);
        args.putSerializable(ARG_PARA, paraForEditText);

        InfoCardCreatorDialogFragment fragment = new InfoCardCreatorDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String cardTitle = (String) getArguments().getSerializable(ARG_CARD_TITLE);
        String paragraph = (String) getArguments().getSerializable(ARG_PARAGRAPH);
        String titleEditTextHint = (String) getArguments().getSerializable(ARG_TITLE);
        String paraEditTextHint = (String) getArguments().getSerializable(ARG_PARA);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_card_creator, null);

        TextView titleTextView = (TextView) v.findViewById(R.id.dialog_card_creator_title_text_view);
        titleTextView.setText(cardTitle);

        TextView paragraphTextView = (TextView) v.findViewById(R.id.dialog_card_creator_paragraph_text_view);
        paragraphTextView.setText(paragraph);

        final EditText titleEditText = (EditText) v.findViewById(R.id.dialog_card_creator_title_edit_text);
        titleEditText.setHint(titleEditTextHint);

        final EditText paragraphEditText = (EditText) v.findViewById(R.id.dialog_card_creator_paragraph_edit_text);
        paragraphEditText.setHint(paraEditTextHint);

        final TextView popupTextView = (TextView) v.findViewById(R.id.dialog_card_creator_popup_text_view);

        final AlertDialog dialogAlert = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.card_creator_dialog_title)
                .setPositiveButton(R.string.create, null)
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
                        CardInfoLab cardLab = CardInfoLab.get(getActivity());
                        if (titleEntryText.isEmpty()) {
                            popupTextView.setText(getString(R.string.pleaseentertitle));
                            popupTextView.setVisibility(View.VISIBLE);
                        } else if (cardLab.isTitleUnique(titleEntryText)) {
                            String paragraphEntryText = paragraphEditText.getText().toString();
                            sendResult(Activity.RESULT_OK, titleEntryText, paragraphEntryText);
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

    private void sendResult(int resultCode, String titleEntry, String paragraphEntry) {

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, titleEntry);
        intent.putExtra(EXTRA_PARAGRAPH, paragraphEntry);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void cancelRequest(int resultCode) {

        if (getTargetFragment() == null) {
            return;
        }

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
    }
}

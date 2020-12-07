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
public class SubInfoCardCreatorDialogFragment extends DialogFragment {
    public static final String EXTRA_SUB_TITLE = "com.ropkastudios.android.thegeographyapp.sub_title";

    private static final String ARG_SUB_CARD_TITLE = "card_title";
    private static final String ARG_SUB_TITLE = "passTitle";
    private static final String ARG_SUB_CATEGORY = "passCategory";

    public static SubInfoCardCreatorDialogFragment newInstance(String title, String titleForEditText, String category) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SUB_CARD_TITLE, title);
        args.putSerializable(ARG_SUB_TITLE, titleForEditText);
        args.putSerializable(ARG_SUB_CATEGORY, category);

        SubInfoCardCreatorDialogFragment fragment = new SubInfoCardCreatorDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String cardTitle = (String) getArguments().getSerializable(ARG_SUB_CARD_TITLE);
        String titleEditTextHint = (String) getArguments().getSerializable(ARG_SUB_TITLE);
        final String category = (String) getArguments().getSerializable(ARG_SUB_CATEGORY);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_sub_card_creator, null);

        TextView titleTextView = (TextView) v.findViewById(R.id.dialog_sub_card_creator_title_text_view);
        titleTextView.setText(cardTitle);

        final EditText titleEditText = (EditText) v.findViewById(R.id.dialog_sub_card_creator_title_edit_text);
        titleEditText.setHint(titleEditTextHint);

        final TextView popupTextView = (TextView) v.findViewById(R.id.dialog_sub_card_creator_popup_text_view);

        final AlertDialog dialogAlert = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.sub_card_creator_dialog_title)
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
                        SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
                        if (cardLab.isTitleUnique(titleEntryText.toLowerCase(), category)) {
                            sendResult(Activity.RESULT_OK, titleEntryText);
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

    private void sendResult(int resultCode, String titleEntry) {

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SUB_TITLE, titleEntry);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void cancelRequest(int resultCode) {

        if (getTargetFragment() == null) {
            return;
        }

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
    }
}

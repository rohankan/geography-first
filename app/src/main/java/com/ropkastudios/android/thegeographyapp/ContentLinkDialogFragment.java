package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContentLinkDialogFragment extends DialogFragment {
    public static final String EXTRA_TITLE = "com.ropkastudios.android.thegeographyapp.content_title";
    public static final String EXTRA_CATEGORY = "com.ropkastudios.android.thegeographyapp.content_category";
    public static final String EXTRA_SUBCATEGORY = "com.ropkastudios.android.thegeographyapp.content_subcategory";

    private static final String ARG_CONTENT_TITLE = "arg_content_title";
    private static final String ARG_CONTENT_TEXT = "arg_content_text";
    private static final String ARG_CONTENT_EDIT_TEXT = "arg_content_edit_text";
    private static final String ARG_CONTENT_RECYCLER_TEXT = "arg_content_recycler_text";
    private static final String ARG_CONTENT_REC_CONT_TEXT = "arg_content_rec_cont_text";
    private static final String ARG_CONTENT_WARNING_TEXT = "arg_content_warning_text";
    private static final String ARG_CONTENT_NEGATIVE_TEXT = "arg_content_neg_text";
    private static final String ARG_CONTENT_POSITIVE_TEXT = "arg_content_posit_text";
    private static final String ARG_CONTENT_CATEGORY_LIST = "arg_content_category_list";
    private static final String ARG_CONTENT_SUB_CAT_LIST = "arg_content_sub_cat_list";

    TextView contentTextView;
    EditText contentTextEditText;
    TextView recyclerTextView;
    RecyclerView recyclerView;
    TextView recyclerContentTextView;
    ImageButton backButton;
    TextView popupTextView;

    CardAdapter mAdapter;
    CardAdapter mSubAdapter;
    private String currentCategory = "";
    private String currentSubcategory = "";
    private ArrayList<String> categoryList;
    private ArrayList<ArrayList<String>> subcatList;

    public static ContentLinkDialogFragment newInstance(String dialogTitle, String contentText,
                                                           String editViewText, String recyclerText,
                                                        String recContText, String warningText,
                                                        String negativeText, String positiveText,
                                                        ArrayList<String> categoryList,
                                                        ArrayList<ArrayList<String>> subcategoryList) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTENT_TITLE, dialogTitle);
        args.putSerializable(ARG_CONTENT_TEXT, contentText);
        args.putSerializable(ARG_CONTENT_EDIT_TEXT, editViewText);
        args.putSerializable(ARG_CONTENT_RECYCLER_TEXT, recyclerText);
        args.putSerializable(ARG_CONTENT_REC_CONT_TEXT, recContText);
        args.putSerializable(ARG_CONTENT_WARNING_TEXT, warningText);
        args.putSerializable(ARG_CONTENT_NEGATIVE_TEXT, negativeText);
        args.putSerializable(ARG_CONTENT_POSITIVE_TEXT, positiveText);
        args.putStringArrayList(ARG_CONTENT_CATEGORY_LIST, categoryList);
        args.putSerializable(ARG_CONTENT_SUB_CAT_LIST, subcategoryList);

        ContentLinkDialogFragment fragment = new ContentLinkDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String dialogTitle = (String) getArguments().getSerializable(ARG_CONTENT_TITLE);
        String contentText = (String) getArguments().getSerializable(ARG_CONTENT_TEXT);
        String contentEditText = (String) getArguments().getSerializable(ARG_CONTENT_EDIT_TEXT);
        final String recyclerText = (String) getArguments().getSerializable(ARG_CONTENT_RECYCLER_TEXT);
        String recyclerContentText = (String) getArguments().getSerializable(ARG_CONTENT_REC_CONT_TEXT);
        String warningText = (String) getArguments().getSerializable(ARG_CONTENT_WARNING_TEXT);
        String negativeButtonText = (String) getArguments().getSerializable(ARG_CONTENT_NEGATIVE_TEXT);
        String positiveButtonText = (String) getArguments().getSerializable(ARG_CONTENT_POSITIVE_TEXT);
        categoryList = getArguments().getStringArrayList(ARG_CONTENT_CATEGORY_LIST);
        subcatList = (ArrayList<ArrayList<String>>)
                getArguments().getSerializable(ARG_CONTENT_SUB_CAT_LIST);
        mAdapter = new CardAdapter(categoryList, true);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_content_link_creator, null);

        contentTextView = (TextView) v.findViewById(R.id.dialog_content_link_creator_title_text);
        contentTextView.setText(contentText);

        contentTextEditText = (EditText) v.findViewById(R.id.dialog_content_link_creator_edit_text);
        contentTextEditText.setHint(contentEditText);

        recyclerTextView = (TextView) v.findViewById(R.id.dialog_content_link_creator_recycler_text);
        recyclerTextView.setText(recyclerText);

        recyclerView = (RecyclerView) v.findViewById(R.id.dialog_content_link_creator_recycler_view);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        recyclerView.getLayoutParams().height = (int) (displaymetrics.heightPixels * 0.3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        recyclerContentTextView = (TextView) v.findViewById(R.id.dialog_content_link_creator_category_viewer);
        recyclerContentTextView.setText(recyclerContentText);

        popupTextView = (TextView) v.findViewById(R.id.dialog_content_link_creator_popup_text_view);
        popupTextView.setText(warningText);

        backButton = (ImageButton) v.findViewById(R.id.dialog_content_link_creator_back_button);
        backButton.setEnabled(false);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backButton.setEnabled(false);
                recyclerView.setAdapter(mAdapter);
            }
        });

        final AlertDialog dialogAlert = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(dialogTitle)
                .setPositiveButton(positiveButtonText, null)
                .setNegativeButton(negativeButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_CANCELED, null, null, null);
                            }
                        })
                .create();

        dialogAlert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = dialogAlert.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String string = contentTextEditText.getEditableText().toString();
                        if (currentSubcategory.equals("") || string.equals("")) {
                            popupTextView.setVisibility(View.VISIBLE);
                        } else {
                            sendResult(Activity.RESULT_OK, string, currentCategory, currentSubcategory);
                            dialogAlert.dismiss();
                        }
                    }
                });
            }
        });

        return dialogAlert;
    }

    private void sendResult(int resultCode, String textFront, String category, String subcategory) {

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, textFront);
        intent.putExtra(EXTRA_CATEGORY, category);
        intent.putExtra(EXTRA_SUBCATEGORY, subcategory);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private class CardHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        private CardView mCardView;

        public CardHolder(View itemView) {
            super(itemView);

            mCardView = (CardView) itemView.findViewById(R.id.card_view_layout);
            mTitleTextView = (TextView) itemView.findViewById(R.id.card_view_layout_text);
        }

        public void bindCardInfo(final String titleText, final boolean isCategory) {
            mTitleTextView.setText(titleText);
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCategory) {
                        currentCategory = titleText;
                        backButton.setEnabled(true);
                        changeRecyclerView(currentCategory);
                    } else {
                        currentSubcategory = titleText;
                        recyclerContentTextView.setText(getString(R.string.two_dash_placeholder, currentCategory, currentSubcategory));
                        contentTextEditText.setText(currentSubcategory);
                    }
                }
            });
        }
    }

    private void changeRecyclerView(String category) {
        List<String> stringList = subcatList.get(categoryList.indexOf(category));
        if (mSubAdapter == null) {
            mSubAdapter = new CardAdapter(stringList, false);
        } else {
            mSubAdapter.setStringList(stringList);
            mSubAdapter.notifyDataSetChanged();
        }
        recyclerView.setAdapter(mSubAdapter);
    }

    private class CardAdapter extends RecyclerView.Adapter<CardHolder> {

        private List<String> mCardInfos;
        private boolean mIsCategory;

        public CardAdapter(List<String> cardInfoList, boolean isCategory) {
            mCardInfos = cardInfoList;
            mIsCategory = isCategory;
        }

        @Override
        public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.layout_card_view, parent, false);
            return new CardHolder(view);
        }

        @Override
        public void onBindViewHolder(CardHolder holder, int position) {
            String cardInfo = mCardInfos.get(position);
            holder.bindCardInfo(cardInfo, mIsCategory);
        }

        @Override
        public int getItemCount() {
            return mCardInfos.size();
        }

        public void setStringList(List<String> stringList) {
            mCardInfos = stringList;
        }
    }
}

package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by Rohan Kanchana on 6/30/2016.
 */
public class InformationFragment extends BackPressedFragment {
    private static final String TAG = "InformationFragment";

    private RecyclerView mCardRecyclerView;
    private CardAdapter mAdapter;
    private CardAdapter mDeleteAdapter;
    private CardAdapter mUpdateAdapter;
    private CardAdapter mQueryAdapter;
    private TextView mPopupTextView;

    private boolean isUndoDeleteCardMode = false;
    private boolean isUpdateCardMode = false;

    private static final String DIALOG_CREATE_CARD = "DialogCreateCard";
    private static final String DIALOG_CONFIRM_DELETE_CARD = "DialogConfirmDeleteCard";
    private static final String DIALOG_UPDATE_CARD = "DialogUpdateCard";
    private static final int REQUEST_CREATE_CARD = 0;
    private static final int REQUEST_CONFIRM_DELETE_CARD = 1;
    private static final int REQUEST_UPDATE_CARD = 2;

    public static InformationFragment newInstance() {
        return new InformationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.information));

        CardInfoLab cardLab = CardInfoLab.get(getActivity());
        List<CardInfoHolder> cardInfoHolderList = cardLab.getCardInfos();
        if (cardInfoHolderList.size() == 0) {
            Resources res = getResources();
            CardInfoHolder cardInfoOne = new CardInfoHolder(res.getString(R.string.countries), getString(R.string.countries_desc),
                    res.getString(R.string.readmore), false);
            CardInfoHolder cardInfoTwo = new CardInfoHolder(res.getString(R.string.oceans), getString(R.string.oceans_desc),
                    res.getString(R.string.readmore), false);
            CardInfoHolder cardInfoThree = new CardInfoHolder(res.getString(R.string.continents), getString(R.string.continents_desc),
                    res.getString(R.string.readmore), false);
            CardInfoHolder cardInfoFour = new CardInfoHolder(res.getString(R.string.major_world_rivers), getString(R.string.mwr_desc),
                    res.getString(R.string.readmore), false);
            CardInfoHolder cardInfoSix = new CardInfoHolder(res.getString(R.string.major_world_cities), getString(R.string.mwc_desc),
                    res.getString(R.string.readmore), false);
            CardInfoHolder cardInfoSeven = new CardInfoHolder(res.getString(R.string.major_world_islands), getString(R.string.mwi_desc),
                    getString(R.string.readmore), false);
            CardInfoHolder cardInfoEight = new CardInfoHolder(res.getString(R.string.major_world_seas), getString(R.string.mws_desc),
                    res.getString(R.string.readmore), false);
            cardLab.addCardInfo(cardInfoOne);
            cardLab.addCardInfo(cardInfoTwo);
            cardLab.addCardInfo(cardInfoThree);
            cardLab.addCardInfo(cardInfoFour);
            cardLab.addCardInfo(cardInfoSix);
            cardLab.addCardInfo(cardInfoSeven);
            cardLab.addCardInfo(cardInfoEight);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        mCardRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_information_recycler_view);
        mCardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPopupTextView = (TextView) view.findViewById(R.id.fragment_information_delete_text_view);

        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_information_fragment, menu);

        getActivity().setTitle(getString(R.string.information));
        MenuItem plusItem = menu.findItem(R.id.fragment_information_action_add);
        MenuItem deleteItem = menu.findItem(R.id.fragment_information_action_delete);
        MenuItem updateItem = menu.findItem(R.id.fragment_information_action_change);
        MenuItem searchItem = menu.findItem(R.id.fragment_information_action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CardInfoLab cardLab = CardInfoLab.get(getActivity());
                List<CardInfoHolder> cardInfoHolderList = cardLab.getCardInfos();
                if (mQueryAdapter == null) {
                    mQueryAdapter = new CardAdapter(cardInfoHolderList);
                    mPopupTextView.setText(getResources().getString(R.string.no_search_results));
                    mCardRecyclerView.setAdapter(mQueryAdapter);
                } else {
                    mQueryAdapter.setCardInfoList(cardInfoHolderList);
                }

                for (int i = cardInfoHolderList.size() - 1; i >= 0; --i) {
                    String holderText = cardInfoHolderList.get(i).getTitle().toLowerCase();
                    holderText = holderText.replace(" ", "");
                    String newInstanceText = newText.toLowerCase();
                    newInstanceText = newInstanceText.replace(" ", "");
                    if (!holderText.contains(newInstanceText)) {
                        cardInfoHolderList.remove(i);
                    }
                }
                mQueryAdapter.notifyDataSetChanged();
                if (mQueryAdapter.getCardInfoList().size() == 0) {
                    mPopupTextView.setVisibility(View.VISIBLE);
                } else {
                    mPopupTextView.setVisibility(View.GONE);
                }
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mPopupTextView.setVisibility(View.GONE);
                mCardRecyclerView.setAdapter(mAdapter);
                return false;
            }
        });

        if (isUndoDeleteCardMode) {
            deleteItem.setVisible(false);
            updateItem.setVisible(false);
            searchItem.setVisible(false);
            plusItem.setIcon(R.drawable.ic_action_undo_delete);
            plusItem.setTitle(R.string.exit_delete_mode);
            plusItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else if (isUpdateCardMode) {
            deleteItem.setVisible(false);
            updateItem.setVisible(false);
            searchItem.setVisible(false);
            plusItem.setIcon(R.drawable.ic_action_undo_delete);
            plusItem.setTitle(R.string.exit_update_mode);
            plusItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            deleteItem.setVisible(true);
            updateItem.setVisible(true);
            searchItem.setVisible(true);
            plusItem.setIcon(R.drawable.ic_action_add);
            plusItem.setTitle(R.string.add_a_category);
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                plusItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            } else {
                plusItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isUndoDeleteCardMode || isUpdateCardMode) {
            resetToNormalAdapter();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_information_action_add:
                if (isUndoDeleteCardMode || isUpdateCardMode) {
                    resetToNormalAdapter();
                } else {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    InfoCardCreatorDialogFragment dialog = InfoCardCreatorDialogFragment.newInstance(
                            getString(R.string.title_colon), getString(R.string.desc_colon),
                            getString(R.string.title_prompt), getString(R.string.desc_prompt));
                    dialog.setTargetFragment(InformationFragment.this, REQUEST_CREATE_CARD);
                    dialog.show(manager, DIALOG_CREATE_CARD);
                }
                return true;
            case R.id.fragment_information_action_delete:
                isUndoDeleteCardMode = true;

                if (mDeleteAdapter == null) {
                    mDeleteAdapter = new CardAdapter(getOriginalsDeletedCardInfoList());
                } else {
                    mDeleteAdapter.setCardInfoList(getOriginalsDeletedCardInfoList());
                }

                mPopupTextView.setVisibility(View.VISIBLE);
                if (mDeleteAdapter.getCardInfoList().size() == 0) {
                    mPopupTextView.setText(getResources().getText(R.string.no_delete_blue_categories));
                } else {
                    mPopupTextView.setText(getResources().getText(R.string.delete_any_blue_category));
                }

                mCardRecyclerView.swapAdapter(mDeleteAdapter, true);
                getActivity().invalidateOptionsMenu();

                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.delete_mode), Gravity.CENTER, 0, 0, 1000);
                return true;
            case R.id.fragment_information_action_change:
                isUpdateCardMode = true;

                if (mUpdateAdapter == null) {
                    mUpdateAdapter = new CardAdapter(getOriginalsDeletedCardInfoList());
                } else {
                    mUpdateAdapter.setCardInfoList(getOriginalsDeletedCardInfoList());
                }

                mPopupTextView.setVisibility(View.VISIBLE);
                if (mUpdateAdapter.getCardInfoList().size() == 0) {
                    mPopupTextView.setText(getResources().getText(R.string.no_update_pressing_category));
                } else {
                    mPopupTextView.setText(getResources().getText(R.string.update_pressing_category));
                }

                mCardRecyclerView.swapAdapter(mUpdateAdapter, true);
                getActivity().invalidateOptionsMenu();

                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.change_mode), Gravity.CENTER, 0, 0, 1000);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CREATE_CARD) {
            String cardTitle = (String) data.getSerializableExtra(InfoCardCreatorDialogFragment.EXTRA_TITLE);
            String cardParagraph = (String) data.getSerializableExtra(InfoCardCreatorDialogFragment.EXTRA_PARAGRAPH);
            CardInfoHolder addCard = new CardInfoHolder(cardTitle, cardParagraph, getResources().getString(R.string.readmore), true);
            CardInfoLab cardLab = CardInfoLab.get(getActivity());
            cardLab.addCardInfo(addCard);
            updateUI();
            MainMenuActivity.increaseAdCount(getActivity());
        } else if (requestCode == REQUEST_CONFIRM_DELETE_CARD) {
            UUID cardDeleteUUID = (UUID) data.getSerializableExtra(ConfirmDeleteDialogFragment.EXTRA_ADDITIONAL);
            CardInfoLab cardInfoLab = CardInfoLab.get(getActivity());
            if (cardDeleteUUID != null) {
                SubcategoryCardInfoLab subCardInfoLab = SubcategoryCardInfoLab.get(getActivity());
                List<SubcategoryCardInfoHolder> instance =
                        subCardInfoLab.getCategoryCardInfos(cardInfoLab.getSpecificCardInfo(cardDeleteUUID)
                                .getTitle().toLowerCase());
                String stringArray[] = new String[instance.size()];
                for (int i = 0; i < instance.size(); ++i) {
                    stringArray[i] = instance.get(i).getId().toString();
                }
                subCardInfoLab.deleteCardInfos(stringArray);
                cardInfoLab.deleteCardInfo(cardDeleteUUID);
                mAdapter.setCardInfoList(cardInfoLab.getCardInfos());
                mDeleteAdapter.setCardInfoList(getOriginalsDeletedCardInfoList());
                mDeleteAdapter.notifyDataSetChanged();
                if (mDeleteAdapter.getCardInfoList().size() == 0) {
                    mPopupTextView.setText(getResources().getText(R.string.no_delete_blue_categories));
                }
                MainMenuActivity.increaseAdCount(getActivity());
            }
        } else if (requestCode == REQUEST_UPDATE_CARD) {
            UUID cardUpdateUUID = (UUID) data.getSerializableExtra(UpdateCardDialogFragment.EXTRA_UUID);
            String oldTitle = (String) data.getSerializableExtra(UpdateCardDialogFragment.EXTRA_OLD_TITLE);
            String cardTitle = (String) data.getSerializableExtra(UpdateCardDialogFragment.EXTRA_TITLE);
            String cardParagraph = (String) data.getSerializableExtra(UpdateCardDialogFragment.EXTRA_PARAGRAPH);
            CardInfoLab cardInfoLab = CardInfoLab.get(getActivity());
            if (cardUpdateUUID != null) {
                CardInfoHolder holder = new CardInfoHolder(cardUpdateUUID, cardTitle, cardParagraph,
                                                            String.valueOf(getResources().getText(R.string.readmore)),
                                                            true);
                cardInfoLab.updateCardInfo(holder);
                SubcategoryCardInfoLab sLab = SubcategoryCardInfoLab.get(getActivity());
                List<SubcategoryCardInfoHolder> sHolder = sLab.getCategoryCardInfos(oldTitle);
                for (SubcategoryCardInfoHolder shoulder : sHolder) {
                    shoulder.setCategory(cardTitle.toLowerCase());
                    sLab.updateCardInfo(shoulder);
                }
                mAdapter.setCardInfoList(cardInfoLab.getCardInfos());
                mUpdateAdapter.setCardInfoList(getOriginalsDeletedCardInfoList());
                mUpdateAdapter.notifyDataSetChanged();
                MainMenuActivity.increaseAdCount(getActivity());
            }
        }
    }

    private void updateUI() {
        CardInfoLab cardLab = CardInfoLab.get(getActivity());
        List<CardInfoHolder> cardInfoHolderList = cardLab.getCardInfos();

        if (mAdapter == null) {
            mAdapter = new CardAdapter(cardInfoHolderList);
            mCardRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCardInfoList(cardInfoHolderList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CardHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        private Button mAgreeButton;
        private TextView mParagaphTextView;
        private LinearLayout mHolderLinearLayout;

        public CardHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.card_holder_title_text);
            mParagaphTextView = (TextView) itemView.findViewById(R.id.card_holder_content_text);
            mAgreeButton = (Button) itemView.findViewById(R.id.card_holder_read_more_button);
            mHolderLinearLayout = (LinearLayout) itemView.findViewById(R.id.card_holder_linear_layout);
        }

        public void bindCardInfo(final CardInfoHolder cardInfo) {
            mTitleTextView.setText(cardInfo.getTitle());
            mParagaphTextView.setText(cardInfo.getParagraph());
            mAgreeButton.setText(cardInfo.getButtonText());
            if (isUndoDeleteCardMode && cardInfo.getDeleteable()) {
                mAgreeButton.setEnabled(false);
                mHolderLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        triggerDelete(cardInfo.getTitle(), cardInfo.getId());
                    }
                });
            } else if (isUpdateCardMode) {
                mAgreeButton.setEnabled(false);
                mHolderLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        UpdateCardDialogFragment dialog = UpdateCardDialogFragment.newInstance(
                                getString(R.string.new_title_colon), getString(R.string.new_desc_colon), cardInfo.getTitle(),
                                cardInfo.getParagraph(), cardInfo.getId(), cardInfo.getTitle());
                        dialog.setTargetFragment(InformationFragment.this, REQUEST_UPDATE_CARD);
                        dialog.show(manager, DIALOG_UPDATE_CARD);
                    }
                });
            } else  {
                mHolderLinearLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
                mHolderLinearLayout.setOnClickListener(null);
                mAgreeButton.setEnabled(true);
                mAgreeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() instanceof MainMenuActivity) {
                            MainMenuActivity activity = (MainMenuActivity) getActivity();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            InformationSubcategoryFragment fragment = InformationSubcategoryFragment.newInstance();
                            activity.replaceFragmentInActivityWithString(MainMenuActivity.FRAGMENT_CONTAINER,
                                    fragment, fm, cardInfo.getTitle(), new String[]{cardInfo.getTitle()},
                                    new String[]{"InformationCategoryFragment"});
                        }
                    }
                });
            }
        }
    }

    private void triggerDelete(String cardTitleString, UUID cardUUID) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        ConfirmDeleteDialogFragment dialog = ConfirmDeleteDialogFragment.newInstance(
                getString(R.string.aresure_delete_category_question, cardTitleString),
                getString(R.string.deleting_category_warning, cardTitleString),
                getString(R.string.delete), getString(R.string.dont_delete), cardUUID);

        dialog.setTargetFragment(InformationFragment.this, REQUEST_CONFIRM_DELETE_CARD);
        dialog.show(manager, DIALOG_CONFIRM_DELETE_CARD);
    }

    private class CardAdapter extends RecyclerView.Adapter<CardHolder> {

        private List<CardInfoHolder> mCardInfos;

        public CardAdapter(List<CardInfoHolder> cardInfoList) {
            mCardInfos = cardInfoList;
        }

        @Override
        public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.card_holder_navigation, parent, false);
            return new CardHolder(view);
        }

        @Override
        public void onBindViewHolder(CardHolder holder, int position) {
            CardInfoHolder cardInfo = mCardInfos.get(position);
            holder.bindCardInfo(cardInfo);
        }

        @Override
        public int getItemCount() {
            return mCardInfos.size();
        }

        public void setCardInfoList(List<CardInfoHolder> newCardInformation) {
            mCardInfos = newCardInformation;
        }

        public List<CardInfoHolder> getCardInfoList() {
            return mCardInfos;
        }
    }

    private void resetToNormalAdapter() {
        mPopupTextView.setVisibility(View.GONE);
        Log.e(TAG, String.valueOf(mAdapter.getCardInfoList().size()));
        mCardRecyclerView.swapAdapter(mAdapter, true);
        isUndoDeleteCardMode = false;
        isUpdateCardMode = false;
        getActivity().invalidateOptionsMenu();
    }

    private List<CardInfoHolder> getOriginalsDeletedCardInfoList() {
        CardInfoLab cardLab = CardInfoLab.get(getActivity());
        List<CardInfoHolder> instance = cardLab.getCardInfos();
        boolean deletables[] = new boolean[instance.size()];
        int counter = 0;
        for (CardInfoHolder i : instance) {
            deletables[counter] = !i.getDeleteable();
            counter += 1;
        }

        for (int i = (instance.size() - 1); i >= 0; --i) {
            if (deletables[i]) {
                instance.remove(i);
            }
        }

        return instance;
    }
}

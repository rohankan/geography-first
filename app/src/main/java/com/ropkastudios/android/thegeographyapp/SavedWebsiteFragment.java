package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
public class SavedWebsiteFragment extends BackPressedFragment {
    RecyclerView mRecyclerView;
    TextView mTextView;
    SavWebAdapter mAdapter;
    MainAdapter mMainAdapter;
    boolean initStage;
    boolean delOption = false;
    boolean delMode = false;
    boolean noSavedWebs = false;
    float starterY = 0;
    String currentCategory;

    public static final int REQUEST_CONFIRM_DELETE_CARD = 23;
    public static final String DIALOG_CONFIRM_DELETE_CARD = "cdc";

    public static SavedWebsiteFragment newInstance() {
        return new SavedWebsiteFragment();
    }

    @Override
    public boolean onBackPressed() {
        if (delMode) {
            delMode = false;
            SavedWebsiteLab sLab = SavedWebsiteLab.get(getActivity());
            List<SavedWebsiteHolder> list = sLab.getSavedWebs();
            if (!currentCategory.isEmpty()) list = SavedWebsiteLab.sortByCategory(list, currentCategory);
            getActivity().invalidateOptionsMenu();
            mAdapter.setSavedWebList(list);
            mAdapter.notifyDataSetChanged();
            GeoAnimator.makeDurationToast(getActivity(), getString(R.string.newsmode), Gravity.CENTER, 0, 0, 1000);
            return true;
        } else if (!initStage) {
            setMenuList();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_websites, container, false);
        setHasOptionsMenu(true);

        getActivity().setTitle(getString(R.string.saved_articles));

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.fragment_sw_ll);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ll.setBackgroundResource(R.drawable.bglobe);
        } else {
            ll.setBackgroundResource(R.drawable.bglobeland);
        }


        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_sw_recycler);
        initStage = true;

        mTextView = (TextView) view.findViewById(R.id.fragment_sw_tv);
        mTextView.setVisibility(View.GONE);

        mMainAdapter = new MainAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mMainAdapter);
        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        LinearLayout ll;

        public DownloadImageTask(ImageView bmImage, LinearLayout LL) {
            this.bmImage = bmImage;
            this.ll = LL;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                bmImage.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else {
                bmImage.setImageBitmap(result);
            }
        }
    }

    private class SavWebHolder extends RecyclerView.ViewHolder {

        private ImageView mThumbnail;
        private LinearLayout mLL;
        private TextView mTitleTextView;
        private TextView mDescTextView;
        private CardView mCardView;
        private TextView mDateTextView;

        public SavWebHolder(View itemView) {
            super(itemView);

            mThumbnail = (ImageView) itemView.findViewById(R.id.rssImageView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.titleText);
            mDescTextView = (TextView) itemView.findViewById(R.id.descriptionText);
            mCardView = (CardView) itemView.findViewById(R.id.rssCard);
            mDateTextView = (TextView) itemView.findViewById(R.id.rssDateText);
            mLL = (LinearLayout) itemView.findViewById(R.id.rssLinearCancel);
        }

        public void bindSavWeb(final SavedWebsiteHolder holder) {
            if (holder.title == null || holder.title.isEmpty() || holder.title.equals(" ")) {
                try {
                    mTitleTextView.setText(holder.description.substring(0, holder.description.indexOf("<")));
                } catch (StringIndexOutOfBoundsException e) {
                    mTitleTextView.setText(holder.description);
                }
            } else {
                mTitleTextView.setText(holder.title);
            }
            try {
                mDescTextView.setText(holder.description.substring(0, holder.description.indexOf("<")));
            } catch (StringIndexOutOfBoundsException e) {
                mDescTextView.setText(holder.description);
            }
            mDateTextView.setText(holder.getDate());

            mDateTextView.setVisibility(View.VISIBLE);

            if (delMode) {
                mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        triggerDelete(holder.getTitle(), holder.getUuid());
                    }
                });
            } else {
                mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainMenuActivity.increaseAdCount(getActivity());

                        Intent launchBrowser = WebPageActivity.newIntent(getActivity(), Uri.parse(holder.url),
                                holder.title, holder.img_url, holder.date, holder.category, true, holder.description);
                        startActivity(launchBrowser);
                    }
                });
            }

            if (holder.category.equals(SavedWebsiteLab.NAT_GEO_NEWS)) {
                mThumbnail.setVisibility(View.GONE);
                mLL.setVisibility(View.GONE);
            } else {
                new DownloadImageTask(mThumbnail, mLL).execute(holder.img_url);
            }

        }
    }

    private class LargeMainHolder extends RecyclerView.ViewHolder {

        private ImageView mThumbnail;
        private TextView mTitleTextView;
        private CardView mCardView;
        private TextView mDateTextView;
        private LinearLayout mLL;

        public LargeMainHolder(View itemView) {
            super(itemView);

            mThumbnail = (ImageView) itemView.findViewById(R.id.rssImageView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.titleText);
            mCardView = (CardView) itemView.findViewById(R.id.rssCard);
            mDateTextView = (TextView) itemView.findViewById(R.id.rssDateText);
            mLL = (LinearLayout) itemView.findViewById(R.id.rssLinearCancel);
        }

        public void bindMainHolder(int pos) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int dHeight = ((displayMetrics.heightPixels / 6) - 50);
            String category = SavedWebsiteLab.ALL;
            String titleText = getString(R.string.saved_articles);

            if (pos == 0) {
                titleText = getString(R.string.national_geographic_news);
                category = SavedWebsiteLab.NAT_GEO_NEWS;
                Bitmap ngBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.natgeo);
                mThumbnail.setImageBitmap
                        (Bitmap.createScaledBitmap(ngBitmap, (ngBitmap.getWidth() * dHeight) / ngBitmap.getHeight() + 1, dHeight + 1, false));
                ngBitmap.recycle();
                starterY = mCardView.getY();
            } else if (pos == 1) {
                titleText = getString(R.string.cnn);
                category = SavedWebsiteLab.CNN_NEWS;
                Bitmap ngBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.cnnlogo);
                mThumbnail.setImageBitmap
                        (Bitmap.createScaledBitmap(ngBitmap, (ngBitmap.getWidth() * dHeight) / ngBitmap.getHeight() + 1, dHeight + 1, false));
                ngBitmap.recycle();
            } else if (pos == 2) {
                titleText = getString(R.string.bbc);
                category = SavedWebsiteLab.BBC_NEWS;
                Bitmap ngBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.bbclogo);
                mThumbnail.setImageBitmap
                        (Bitmap.createScaledBitmap(ngBitmap, (ngBitmap.getWidth() * dHeight) / ngBitmap.getHeight() + 1, dHeight + 1, false));
                ngBitmap.recycle();
            } else if (pos == 3) {
                titleText = getString(R.string.sciencedailynews);
                category = SavedWebsiteLab.SCIENCE_DAILY_NEWS;
                Bitmap ngBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.sciencedailylogo);
                mThumbnail.setImageBitmap
                        (Bitmap.createScaledBitmap(ngBitmap, (ngBitmap.getWidth() * dHeight) / ngBitmap.getHeight() + 1, dHeight + 1, false));
                ngBitmap.recycle();
            } else if (pos == 4) {
                titleText = getString(R.string.allnews);
                mThumbnail.setVisibility(View.GONE);
                mLL.setVisibility(View.GONE);
                category = SavedWebsiteLab.ALL;
            }
            titleText = ("\n" + titleText);
            mTitleTextView.setText(titleText);

            SavedWebsiteLab lab = SavedWebsiteLab.get(getActivity());
            initStage = false;
            getActivity().invalidateOptionsMenu();
            List<SavedWebsiteHolder> holder = lab.getSavedWebs();

            if (!category.equals(SavedWebsiteLab.ALL)) {
                holder = SavedWebsiteLab.sortByCategory(holder, category);
            }

            mDateTextView.setText(getString(R.string.ph, String.valueOf(holder.size()) + "\n"));

            final String CATEGORY = category;

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setListToCategory(CATEGORY);
                }
            });

            delMode = false;
            getActivity().invalidateOptionsMenu();
        }
    }

    private void triggerDelete(String cardTitleString, UUID cardUUID) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        ConfirmDeleteDialogFragment dialog = ConfirmDeleteDialogFragment.newInstance(
                getString(R.string.delete), getString(R.string.aresure_delete_category_question, cardTitleString),
                getString(R.string.delete), getString(R.string.dont_delete), cardUUID);

        dialog.setTargetFragment(SavedWebsiteFragment.this, REQUEST_CONFIRM_DELETE_CARD);
        dialog.show(manager, DIALOG_CONFIRM_DELETE_CARD);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_saved_website, menu);

        if (initStage) menu.findItem(R.id.return_saved_web).setVisible(false);

        if (delMode || initStage || noSavedWebs) {
            menu.findItem(R.id.delete_saved_web).setVisible(false);
        } else {
            menu.findItem(R.id.delete_saved_web).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.return_saved_web:
                if (delMode) {
                    delMode = false;
                    getActivity().invalidateOptionsMenu();
                    mAdapter.notifyDataSetChanged();
                    GeoAnimator.makeDurationToast(getActivity(), getString(R.string.newsmode), Gravity.CENTER, 0, 0, 1000);
                } else {
                    setMenuList();
                }
                return true;
            case R.id.delete_saved_web:
                delMode = true;
                getActivity().invalidateOptionsMenu();
                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.delete_mode), Gravity.CENTER, 0, 0, 1000);
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SavWebAdapter extends RecyclerView.Adapter<SavWebHolder> {

        private List<SavedWebsiteHolder> mSavedWebs;

        public SavWebAdapter(List<SavedWebsiteHolder> savedWebList) {
            mSavedWebs = savedWebList;
        }

        @Override
        public SavWebHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_rss_feed, parent, false);
            return new SavWebHolder(view);
        }

        @Override
        public void onBindViewHolder(SavWebHolder holder, int position) {
            SavedWebsiteHolder savedWeb = mSavedWebs.get(position);
            holder.bindSavWeb(savedWeb);
        }

        @Override
        public int getItemCount() {
            return mSavedWebs.size();
        }

        public void setSavedWebList(List<SavedWebsiteHolder> newSavedWebs) {
            mSavedWebs = newSavedWebs;
        }
    }

    private class MainAdapter extends RecyclerView.Adapter<LargeMainHolder> {

        @Override
        public LargeMainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_big_rss_feed, parent, false);
            return new LargeMainHolder(view);
        }

        @Override
        public void onBindViewHolder(LargeMainHolder holder, int position) {
            initStage = true;
            holder.bindMainHolder(position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    private void setListToCategory(String category) {
        SavedWebsiteLab lab = SavedWebsiteLab.get(getActivity());
        initStage = false;
        getActivity().invalidateOptionsMenu();
        List<SavedWebsiteHolder> holder = lab.getSavedWebs();

        if (!category.equals(SavedWebsiteLab.ALL)) {
            holder = SavedWebsiteLab.sortByCategory(holder, category);
        }

        if (category.equals(SavedWebsiteLab.NAT_GEO_NEWS)) {
            getActivity().setTitle(getString(R.string.national_geographic_news));
        } else if (category.equals(SavedWebsiteLab.BBC_NEWS)) {
            getActivity().setTitle(getString(R.string.bbc));
        } else if (category.equals(SavedWebsiteLab.CNN_NEWS)) {
            getActivity().setTitle(getString(R.string.cnn));
        } else if (category.equals(SavedWebsiteLab.SCIENCE_DAILY_NEWS)) {
            getActivity().setTitle(getString(R.string.sciencedailynews));
        } else if (category.equals(SavedWebsiteLab.ALL)) {
            getActivity().setTitle(getString(R.string.allnews));
        }

        delOption = true;

        currentCategory = category;

        if (holder.size() == 0) {
            mTextView.setVisibility(View.VISIBLE);
            noSavedWebs = true;
        } else {
            noSavedWebs = false;
        }

        getActivity().invalidateOptionsMenu();

        if (mAdapter == null) {
            mAdapter = new SavWebAdapter(holder);
        } else {
            mAdapter.setSavedWebList(holder);
            mAdapter.notifyDataSetChanged();
        }

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CONFIRM_DELETE_CARD) {
            UUID cardDeleteUUID = (UUID) data.getSerializableExtra(ConfirmDeleteDialogFragment.EXTRA_ADDITIONAL);
            SavedWebsiteLab cardInfoLab = SavedWebsiteLab.get(getActivity());
            if (cardDeleteUUID != null) {
                cardInfoLab.deleteSavedWeb(cardDeleteUUID);
                List<SavedWebsiteHolder> holder = cardInfoLab.getSavedWebs();
                if (!currentCategory.isEmpty() && !currentCategory.equals(SavedWebsiteLab.ALL)) {
                    holder = SavedWebsiteLab.sortByCategory(holder, currentCategory);
                }

                mAdapter.setSavedWebList(holder);
                mAdapter.notifyDataSetChanged();

                MainMenuActivity.increaseAdCount(getActivity());
            }
        }
    }

    private void setMenuList() {
        initStage = true;
        currentCategory = "";
        mRecyclerView.setAdapter(mMainAdapter);
        mTextView.setVisibility(View.GONE);
        delOption = false;
        getActivity().invalidateOptionsMenu();
        getActivity().setTitle(getString(R.string.saved_articles));
    }
}

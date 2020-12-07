package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NatGeoFragment extends BackPressedFragment {
    public static int REQUEST_WELCOME = 45;
    public static String DIALOG_WELCOME = "dialog_welcome";

    public static NatGeoFragment newInstance() {
        return new NatGeoFragment();
    }

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeLayout;
    String currentCategory;
    boolean isOriginal;

    private List<RssFeedModel> mFeedModelList;
    String currentURL = "http://rss.cnn.com/rss/cnn_world.rss";

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nat_geo_news, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
        boolean skipBox = settings.getBoolean(MainMenuActivity.SKIP_OR_NOT, false);

        if (!skipBox && settings.getBoolean(MainMenuActivity.FIRST_TIME_APP_OPENED, true)) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            WelcomeDialogFragment dialog = WelcomeDialogFragment.newInstance();
            dialog.setTargetFragment(NatGeoFragment.this, REQUEST_WELCOME);
            dialog.show(manager, DIALOG_WELCOME);
        }


        mRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.newsRefreshLayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask((MainMenuActivity) getActivity()).execute(currentURL);
            }
        });

        currentCategory = SavedWebsiteLab.CNN_NEWS;

        new FetchFeedTask((MainMenuActivity) getActivity()).execute(currentURL);
        return view;
    }

    private class FetchFeedTask extends AsyncTask<String, Void, Boolean> {
        MainMenuActivity activity;

        public FetchFeedTask(MainMenuActivity Activity) {
            activity = Activity;
        }

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            String urlLink = urls[0];
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException | XmlPullParserException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, getString(R.string.therewasanerror), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
                // Fill RecyclerView
                mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList));
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.errortryagainlater), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public class RssFeedListAdapter
            extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

        private List<RssFeedModel> mRssFeedModels;

        public class FeedModelViewHolder extends RecyclerView.ViewHolder {
            private View rssFeedView;

            public FeedModelViewHolder(View v) {
                super(v);
                rssFeedView = v;
            }
        }

        public RssFeedListAdapter(List<RssFeedModel> rssFeedModels) {
            mRssFeedModels = rssFeedModels;
        }

        @Override
        public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rss_feed, parent, false);
            return new FeedModelViewHolder(v);
        }

        @Override
        public void onBindViewHolder(FeedModelViewHolder holder, int position) {
            final RssFeedModel rssFeedModel = mRssFeedModels.get(position);

            CardView cardView = (CardView) holder.rssFeedView.findViewById(R.id.rssCard);

            final TextView titleTV = (TextView)holder.rssFeedView.findViewById(R.id.titleText);
            if (rssFeedModel.title == null || rssFeedModel.title.isEmpty() || rssFeedModel.title.equals(" ")) {
                try {
                    titleTV.setText(rssFeedModel.description.substring(0, rssFeedModel.description.indexOf("<")));
                } catch (StringIndexOutOfBoundsException e) {
                    titleTV.setText(rssFeedModel.description);
                }
            } else {
                titleTV.setText(rssFeedModel.title);
            }
            TextView descTV = (TextView) holder.rssFeedView.findViewById(R.id.descriptionText);
            try {
                descTV.setText(rssFeedModel.description.substring(0, rssFeedModel.description.indexOf("<")));
            } catch (StringIndexOutOfBoundsException e) {
                descTV.setText(rssFeedModel.description);
            }
            TextView dateTV = (TextView) holder.rssFeedView.findViewById(R.id.rssDateText);
            dateTV.setText(rssFeedModel.pubDate);

            ImageView imageView = (ImageView) holder.rssFeedView.findViewById(R.id.rssImageView);
            if (rssFeedModel.imageURL != null) {
                new DownloadImageTask(imageView).execute(rssFeedModel.imageURL);
                imageView.setVisibility(View.VISIBLE);
                holder.rssFeedView.findViewById(R.id.rssLinearCancel).setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
                holder.rssFeedView.findViewById(R.id.rssLinearCancel).setVisibility(View.GONE);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainMenuActivity.increaseAdCount(getActivity());
                    String title;
                    if (GeoAnimator.ultimateIsNull(rssFeedModel.title) || rssFeedModel.title.matches(".*[a-z].*")) {
                        title = rssFeedModel.title;
                    } else {
                        title = rssFeedModel.description.substring(0, rssFeedModel.description.indexOf("<"));
                    }
                    Intent launchBrowser = WebPageActivity.newIntent(getActivity(), Uri.parse(rssFeedModel.link),
                            title, rssFeedModel.imageURL, rssFeedModel.pubDate, currentCategory, true, rssFeedModel.description);
                    startActivity(launchBrowser);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRssFeedModels.size();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        isOriginal = getArguments() == null || getArguments().getBoolean("Creation", true);

        getActivity().setTitle(getString(R.string.cnnworld));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_current_news, menu);

        MenuItem natgeo = menu.findItem(R.id.menu_rss_natgeo);
        MenuItem bbc = menu.findItem(R.id.menu_rss_bbc);
        MenuItem cnn = menu.findItem(R.id.menu_rss_cnn);
        MenuItem sciencedaily = menu.findItem(R.id.menu_rss_sciencedaily);

        int dHeight = 40;

        SpannableStringBuilder ngBuilder = new SpannableStringBuilder("*  " + getString(R.string.nationalgeographicnews));
        Bitmap ngBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.natgeo);
                ngBuilder.setSpan(new CenteredImageSpan(getActivity(),
                        Bitmap.createScaledBitmap(ngBitmap, (ngBitmap.getWidth() * dHeight) / ngBitmap.getHeight() + 1, dHeight + 1, false)),
                        0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ngBitmap.recycle();
        natgeo.setTitle(ngBuilder);

        SpannableStringBuilder bbcBuilder = new SpannableStringBuilder("*  " + getString(R.string.bbc));
        Bitmap bbcBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.bbclogo);
        bbcBuilder.setSpan(new CenteredImageSpan(getActivity(),
                        Bitmap.createScaledBitmap(bbcBitmap, (bbcBitmap.getWidth() * dHeight) / bbcBitmap.getHeight() + 1, dHeight + 1, false)),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bbcBitmap.recycle();
        bbc.setTitle(bbcBuilder);

        SpannableStringBuilder cnnBuilder = new SpannableStringBuilder("*  " + getString(R.string.cnn));
        Bitmap cnnBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.cnnlogo);
        cnnBuilder.setSpan(new CenteredImageSpan(getActivity(),
                        Bitmap.createScaledBitmap(cnnBitmap, (cnnBitmap.getWidth() * dHeight) / cnnBitmap.getHeight() + 1, dHeight + 1, false)),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cnnBitmap.recycle();
        cnn.setTitle(cnnBuilder);

        SpannableStringBuilder sdBuilder = new SpannableStringBuilder("*  " + getString(R.string.sciencedailynews));
        Bitmap sdBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.sciencedailylogo);
        sdBuilder.setSpan(new CenteredImageSpan(getActivity(),
                        Bitmap.createScaledBitmap(sdBitmap, (sdBitmap.getWidth() * dHeight) / sdBitmap.getHeight() + 1, dHeight + 1, false)),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sdBitmap.recycle();
        sciencedaily.setTitle(sdBuilder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_rss_reload:
                new FetchFeedTask((MainMenuActivity) getActivity()).execute(currentURL);
                return true;
            case R.id.menu_rss_natgeo:
                getActivity().setTitle(getString(R.string.nationalgeographic));
                currentURL = "http://feeds.nationalgeographic.com/ng/News/News_Main";
                new FetchFeedTask((MainMenuActivity) getActivity()).execute(currentURL);
                currentCategory = SavedWebsiteLab.NAT_GEO_NEWS;
                return true;
            case R.id.menu_rss_cnn:
                getActivity().setTitle(getString(R.string.cnnworld));
                currentURL = "http://rss.cnn.com/rss/cnn_world.rss";
                new FetchFeedTask((MainMenuActivity) getActivity()).execute(currentURL);
                currentCategory = SavedWebsiteLab.CNN_NEWS;
                return true;
            case R.id.menu_rss_bbc:
                getActivity().setTitle(getString(R.string.bbcworld));
                currentURL = "http://feeds.bbci.co.uk/news/world/rss.xml";
                new FetchFeedTask((MainMenuActivity) getActivity()).execute(currentURL);
                currentCategory = SavedWebsiteLab.BBC_NEWS;
                return true;
            case R.id.menu_rss_sciencedaily:
                getActivity().setTitle(getString(R.string.sciencedaily));
                currentURL = "https://www.sciencedaily.com/rss/earth_climate/geography.xml";
                new FetchFeedTask((MainMenuActivity) getActivity()).execute(currentURL);
                currentCategory = SavedWebsiteLab.SCIENCE_DAILY_NEWS;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_WELCOME) {
            SharedPreferences settings = getActivity().getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean(MainMenuActivity.SKIP_OR_NOT, data.getBooleanExtra(WelcomeDialogFragment.EXTRA_CHECKED, false));
            editor.putBoolean(MainMenuActivity.FIRST_TIME_APP_OPENED, false);
            editor.apply();
        }
    }

    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String description = null;
        String imageURL = null;
        String pubDate = null;
        boolean doneDeal = false;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                        if (!doneDeal) {
                            RssFeedModel item = new RssFeedModel(title, link, description, imageURL, pubDate);
                            items.add(item);
                        }
                        doneDeal = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (isItem) {
                    if (name.equalsIgnoreCase("title")) {
                        title = result;
                    } else if (name.equalsIgnoreCase("link")) {
                        link = result;
                    } else if (name.equalsIgnoreCase("description")) {
                        description = result;
                    } else if ((name.equalsIgnoreCase("media:content") || name.equalsIgnoreCase("media:thumbnail")) && imageURL == null) {
                        imageURL = xmlPullParser.getAttributeValue(null, "url");
                    } else if (name.equalsIgnoreCase("pubDate")) {
                        pubDate = result.substring(0, result.indexOf(":")-3);
                    }
                }

                if (title != null && link != null && description != null && pubDate != null && imageURL != null) {
                    if(isItem) {
                        if ((link.contains("cnn.com") && !(link.endsWith(".html") || link.endsWith(".cnn")))) {
                            doneDeal = true;
                        } else {
                            RssFeedModel item = new RssFeedModel(title, link, description, imageURL, pubDate);
                            items.add(item);
                            doneDeal = true;
                        }
                    }

                    title = null;
                    link = null;
                    description = null;
                    imageURL = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public class RssFeedModel {

        public String title;
        public String link;
        public String description;
        public String imageURL;
        public String pubDate;

        public RssFeedModel(String title, String link, String description, String imageURL, String pubDate) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.imageURL = imageURL;
            this.pubDate = pubDate;
        }
    }
}

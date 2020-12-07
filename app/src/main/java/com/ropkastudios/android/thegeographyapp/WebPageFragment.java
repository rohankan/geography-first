package com.ropkastudios.android.thegeographyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
public class WebPageFragment extends Fragment {

    private static final String ARG_URI = "photo_page_uri";
    private static final String ARG_TITLE = "photo_page_title";
    private static final String ARG_DESCRIPTION = "photo_page_description";
    private static final String ARG_IMG_URL = "photo_page_url";
    private static final String ARG_DATE = "photo_page_date";
    private static final String ARG_CATEGORY = "photo_page_category";
    private static final String ARG_OPTIONS = "arg_options";

    private Uri mUri;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    private String mTitle;
    private String mDescription;
    private String mImgUrl;
    private String mDate;
    private String mCategory;
    private boolean isOptions;

    public static WebPageFragment newInstance(Uri uri, String title, String imgUrl, String date, String category, boolean options,
                                              String description) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_IMG_URL, imgUrl);
        args.putString(ARG_DATE, date);
        args.putString(ARG_CATEGORY, category);
        args.putBoolean(ARG_OPTIONS, options);
        args.putString(ARG_DESCRIPTION, description);

        WebPageFragment fragment = new WebPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mUri = getArguments().getParcelable(ARG_URI);
        mTitle = getArguments().getString(ARG_TITLE);
        mImgUrl = getArguments().getString(ARG_IMG_URL);
        mDate = getArguments().getString(ARG_DATE);
        mCategory = getArguments().getString(ARG_CATEGORY);
        isOptions = getArguments().getBoolean(ARG_OPTIONS);
        mDescription = getArguments().getString(ARG_DESCRIPTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.web_page_fragment, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_webpage);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProgressBar = (ProgressBar) v.findViewById(R.id.fragment_photo_page_progress_bar);
        mProgressBar.setMax(100); // WebChromeClient reports in range 0-100

        mWebView = (WebView) v.findViewById(R.id.fragment_photo_page_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }

            public void onReceivedTitle(WebView webView, String title) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                if (title != null && activity != null) activity.setTitle(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    return false;
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                    return true;
                }
            }
        });
        mWebView.loadUrl(mUri.toString());

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_save, menu);

        if (!isOptions) {
            menu.findItem(R.id.item_save).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.item_save:
                SavedWebsiteLab lab = SavedWebsiteLab.get(getActivity());
                lab.addSavedWeb(new SavedWebsiteHolder(mTitle, mUri.toString(), mImgUrl,
                        mDate, mCategory, SavedWebsiteHolder.getDateValue(mDate), mDescription));
                Toast.makeText(getActivity(), getString(R.string.saved_exclamation), Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean goBackIfPossible() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return false;
        }
        return true;
    }
}
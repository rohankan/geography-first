package com.ropkastudios.android.thegeographyapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
public class WebPageActivity extends SingleFragmentActivity {
    static final String TITLE = "TItle";
    static final String IMAGE_URL = "Img_URl";
    static final String DATE = "DAte";
    static final String CATEGORY = "Category";
    static final String OPTIONS = "Options";
    static final String DESCRIPTION = "Description";

    public static Intent newIntent(Context context, Uri photoPageUri, String title,
                                   String imgUrl, String date, String category, boolean options, String description) {
        Intent i = new Intent(context, WebPageActivity.class);
        i.setData(photoPageUri);
        i.putExtra(TITLE, title);
        i.putExtra(IMAGE_URL, imgUrl);
        i.putExtra(DATE, date);
        i.putExtra(CATEGORY, category);
        i.putExtra(OPTIONS, options);
        i.putExtra(DESCRIPTION, description);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        Intent i = getIntent();
        return WebPageFragment.newInstance(i.getData(), i.getStringExtra(TITLE), i.getStringExtra(IMAGE_URL),
                i.getStringExtra(DATE), i.getStringExtra(CATEGORY), i.getBooleanExtra(OPTIONS, false), i.getStringExtra(DESCRIPTION));
    }

    @Override
    public void onBackPressed() {
        WebPageFragment currentFrag = getCurrentFragment();
        boolean shouldClose = currentFrag.goBackIfPossible();
        if (shouldClose) {
            this.finish();
        }
    }

    public WebPageFragment getCurrentFragment() {
        return (WebPageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }
}

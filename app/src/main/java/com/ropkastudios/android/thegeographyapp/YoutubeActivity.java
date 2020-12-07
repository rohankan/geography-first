package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
public class YoutubeActivity extends SingleFragmentActivity implements YouTubePlayer.OnInitializedListener {
    public static final String EXTRA_TOASTY = "extra_toasty";

    YouTubePlayer player;
    boolean isFullscreen;

    public static Intent newIntent(Context context) {
        return new Intent(context, YoutubeActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        YouTubePlayerSupportFragment fragment = new YouTubePlayerSupportFragment();
        fragment.initialize(getString(R.string.api_key), this);
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_youtube);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setTitle(getString(R.string.htu));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_youtube_sections, menu);

        int dHeight = 40;

        MenuItem infoItem = menu.findItem(R.id.menu_youtube_information);
        MenuItem curevItem = menu.findItem(R.id.menu_youtube_currentevents);
        MenuItem saItem = menu.findItem(R.id.menu_youtube_savedarticles);
        MenuItem otherItem = menu.findItem(R.id.menu_youtube_other);

        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(173, 173, 173));

        String infoString = getString(R.string.information);
        SpannableStringBuilder infoBuilder = new SpannableStringBuilder("*  " + infoString + " 2:05");
        Bitmap infoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_dark_search);
        infoBuilder.setSpan(new CenteredImageSpan(this,
                        Bitmap.createScaledBitmap(infoBitmap, (infoBitmap.getWidth() * dHeight) / infoBitmap.getHeight() + 1, dHeight + 1, false)),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        infoBitmap.recycle();
        infoBuilder.setSpan(fcs, 3 + infoString.length(), 8 + infoString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        infoItem.setTitle(infoBuilder);

        String curevString = getString(R.string.curev);
        SpannableStringBuilder curevBuilder = new SpannableStringBuilder("*  " + curevString + " 0:14");
        Bitmap curevBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_read);
        curevBuilder.setSpan(new CenteredImageSpan(this,
                        Bitmap.createScaledBitmap(curevBitmap, (curevBitmap.getWidth() * dHeight) / curevBitmap.getHeight() + 1, dHeight + 1, false)),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        curevBitmap.recycle();
        curevBuilder.setSpan(fcs, 3 + curevString.length(), 8 + curevString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        curevItem.setTitle(curevBuilder);

        String saString = getString(R.string.saved_articles);
        SpannableStringBuilder saBuilder = new SpannableStringBuilder("*  " + saString + " 1:07");
        Bitmap saBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_dark_pin);
        saBuilder.setSpan(new CenteredImageSpan(this,
                        Bitmap.createScaledBitmap(saBitmap, (saBitmap.getWidth() * dHeight) / saBitmap.getHeight() + 1, dHeight + 1, false)),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        saBitmap.recycle();
        saBuilder.setSpan(fcs, 3 + saString.length(), 8 + saString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        saItem.setTitle(saBuilder);

        String otherString = getString(R.string.other);
        SpannableStringBuilder otherBuilder = new SpannableStringBuilder("*  " + otherString + " 7:01");
        Bitmap otherBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_tutorial);
        otherBuilder.setSpan(new CenteredImageSpan(this,
                        Bitmap.createScaledBitmap(otherBitmap, (otherBitmap.getWidth() * dHeight) / otherBitmap.getHeight() + 1, dHeight + 1, false)),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        otherBitmap.recycle();
        otherBuilder.setSpan(fcs, 3 + otherString.length(), 8 + otherString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        otherItem.setTitle(otherBuilder);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            player.setFullscreen(false);
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TOASTY, false);
            setResult(Activity.RESULT_OK, data);
            super.onBackPressed();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b){
            youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                @Override
                public void onFullscreen(boolean _isFullScreen) {
                    isFullscreen = _isFullScreen;
                    if (!_isFullScreen) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    }
                }
            });
            youTubePlayer.cueVideo(getString(R.string.htu_video_id));
            player = youTubePlayer;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_youtube_currentevents:
                player.seekToMillis(14000);
                return true;
            case R.id.menu_youtube_savedarticles:
                player.seekToMillis(67000);
                return true;
            case R.id.menu_youtube_information:
                player.seekToMillis(125000);
                return true;
            case R.id.menu_youtube_other:
                player.seekToMillis(421000);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Intent data = new Intent();
        data.putExtra(EXTRA_TOASTY, true);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}

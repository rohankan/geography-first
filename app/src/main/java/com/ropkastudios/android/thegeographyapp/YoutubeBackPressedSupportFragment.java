package com.ropkastudios.android.thegeographyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;
public class YoutubeBackPressedSupportFragment extends YouTubePlayerSupportFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_youtube, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_youtube);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle(getString(R.string.htu));

        return v;
    }
}

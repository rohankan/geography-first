package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.Space;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends BackPressedFragment implements YouTubePlayer.OnInitializedListener {
    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    ScrollView scrollView;
    private boolean isSecondStage;
    private boolean isThirdStage;

    private static final int REQUEST_RESULT = 1;
    private static final String STAGE = "helpfragment.stage";
    private static final String CATEGORY = "helpfragment.category";
    private static final String SECOND_STAGE = "helpfragment.second_stage";
    private static final String THIRD_STAGE = "helpfragment.third_stage";
    private static final String YOUTUBE_VIDEO_ID = "YAd2GuC4l-4";

    LinearLayout mOLinLayout;

    ArrayList<HelperHolder> infoList = new ArrayList<>();
    ArrayList<HelperHolder> ceList = new ArrayList<>();
    ArrayList<HelperHolder> saList = new ArrayList<>();
    ArrayList<HelperHolder> dmList = new ArrayList<>();
    ArrayList<HelperHolder> otherList = new ArrayList<>();
    ArrayList<HelperHolder> currentList = new ArrayList<>();

    HelperHolder currentHolder;

    ArrayList<CardView> buttonList = new ArrayList<>();
    ArrayList<ImageView> imageList = new ArrayList<>();
    ArrayList<TextView> tvList = new ArrayList<>();
    ArrayList<TextView> btvList = new ArrayList<>();
    ArrayList<Space> spaceList = new ArrayList<>();

    CardView videoCardView;
    TextView videoTopTextView;
    TextView videoBottomTextView;

    YouTubePlayerSupportFragment youtubeFragment;
    YouTubePlayer youtubePlayer;
    boolean isFullScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        infoList.add(new HelperHolder(R.string.categories, R.string.help_categories, 125000));
        infoList.add(new HelperHolder(R.string.subcategories, R.string.help_subcategories, 165000));
        infoList.add(new HelperHolder(R.string.notes, R.string.help_notes, 368000));
        infoList.add(new HelperHolder(R.string.readmorecontents, R.string.help_contents, 182000));
        infoList.add(new HelperHolder(R.string.editing, R.string.help_editing, 200000));

        ceList.add(new HelperHolder(R.string.main_page, R.string.help_ce_main_page, 14000));
        ceList.add(new HelperHolder(R.string.articles, R.string.help_ce_articles, 53000));

        saList.add(new HelperHolder(R.string.main_page, R.string.help_sa_main_page, 67000));
        saList.add(new HelperHolder(R.string.adding_articles, R.string.help_sa_adding_articles, 60000));

        dmList.add(new HelperHolder(R.string.htu, R.string.help_dm_htu, 421000));
        dmList.add(new HelperHolder(R.string.functions, R.string.help_dm_functions, 421000));

        otherList.add(new HelperHolder(R.string.general, R.string.help_other_general, 421000));

        isSecondStage = false;
        isThirdStage = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        getActivity().setTitle(getString(R.string.help));

        scrollView = (ScrollView) view.findViewById(R.id.tscroll_view);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            scrollView.setBackgroundResource(R.drawable.bglobe);
        } else {
            scrollView.setBackgroundResource(R.drawable.bglobeland);
        }

        buttonList.add((CardView) view.findViewById(R.id.tutorial_one));
        buttonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(infoList);
                currentList = infoList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.information)));
            }
        });
        buttonList.add((CardView) view.findViewById(R.id.tutorial_two));
        buttonList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(ceList);
                currentList = ceList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.curev)));
            }
        });
             buttonList.add((CardView) view.findViewById(R.id.tutorial_three));
        buttonList.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(saList);
                currentList = saList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.saved_articles)));
            }
        });
        buttonList.add((CardView) view.findViewById(R.id.tutorial_four));
        buttonList.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(dmList);
                currentList = dmList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.dm)));
            }
        });
        buttonList.add((CardView) view.findViewById(R.id.tutorial_five));
        buttonList.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(otherList);
                currentList = otherList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.other)));
            }
        });

        imageList.add((ImageView) view.findViewById(R.id.timage_1));
        imageList.add((ImageView) view.findViewById(R.id.timage_2));
        imageList.add((ImageView) view.findViewById(R.id.timage_3));
        imageList.add((ImageView) view.findViewById(R.id.timage_4));
        imageList.add((ImageView) view.findViewById(R.id.timage_5));

        tvList.add((TextView) view.findViewById(R.id.ttv_1));
        tvList.add((TextView) view.findViewById(R.id.ttv_2));
        tvList.add((TextView) view.findViewById(R.id.ttv_3));
        tvList.add((TextView) view.findViewById(R.id.ttv_4));
        tvList.add((TextView) view.findViewById(R.id.ttv_5));

        btvList.add((TextView) view.findViewById(R.id.tbtv_1));
        btvList.add((TextView) view.findViewById(R.id.tbtv_2));
        btvList.add((TextView) view.findViewById(R.id.tbtv_3));
        btvList.add((TextView) view.findViewById(R.id.tbtv_4));
        btvList.add((TextView) view.findViewById(R.id.tbtv_5));
        for (TextView tv : btvList) {
            tv.setVisibility(View.GONE);
        }

        spaceList.add((Space) view.findViewById(R.id.tspace_1));
        spaceList.add((Space) view.findViewById(R.id.tspace_2));
        spaceList.add((Space) view.findViewById(R.id.tspace_3));
        spaceList.add((Space) view.findViewById(R.id.tspace_4));
        spaceList.add((Space) view.findViewById(R.id.tspace_5));
        for (Space space : spaceList) {
            space.setVisibility(View.GONE);
        }

        mOLinLayout = (LinearLayout) view.findViewById(R.id.tutorial_oll);
        videoCardView = (CardView) view.findViewById(R.id.videocardview);
        videoTopTextView = (TextView) view.findViewById(R.id.videotoptv);
        videoBottomTextView = (TextView) view.findViewById(R.id.videobottomtv);

        youtubeFragment = new YouTubePlayerSupportFragment();
        youtubeFragment.initialize(getString(R.string.api_key), this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.videoframelayout, youtubeFragment);
        fragmentTransaction.commit();

        videoCardView.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            String category = savedInstanceState.getString(CATEGORY);
            currentList = getConfirmList(infoList, category);
            if (currentList == null) {
                currentList = getConfirmList(ceList, category);
                if (currentList == null) {
                    currentList = getConfirmList(saList, category);
                    if (currentList == null) {
                        currentList = getConfirmList(dmList, category);
                        if (currentList == null) {
                            currentList = getConfirmList(otherList, category);
                        }
                    }
                }
            }
            if (currentList != null) {
                if (savedInstanceState.getString(STAGE, "hi").equals(SECOND_STAGE)) {
                    changeButtonsFromList(currentList);
                } else if (savedInstanceState.getString(STAGE, "hola").equals(THIRD_STAGE)) {
                    changeButtonsFromList(currentList);
                    if (currentHolder != null) {
                        mOLinLayout.setVisibility(View.GONE);
                        videoTopTextView.setText(currentHolder.title);
                        videoBottomTextView.setText(currentHolder.contents);
                        videoCardView.setVisibility(View.VISIBLE);
                        isThirdStage = true;
                        getActivity().invalidateOptionsMenu();
                    }
                }
            }
        }

        return view;
    }

    public ArrayList<HelperHolder> getConfirmList(ArrayList<HelperHolder> arrayList, String title) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (getString(arrayList.get(i).title).equals(title)) {
                currentHolder = arrayList.get(i);
                return arrayList;
            }
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (isThirdStage) {
            savedInstanceState.putString(STAGE, THIRD_STAGE);
            savedInstanceState.putString(CATEGORY, getString(currentHolder.title));
        } else if (isSecondStage) {
            savedInstanceState.putString(STAGE, SECOND_STAGE);
            savedInstanceState.putString(CATEGORY, getString(currentList.get(0).title));
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b){
            youtubePlayer = youTubePlayer;
            if (currentHolder != null) {
                youtubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {

                    @Override
                    public void onFullscreen(boolean _isFullScreen) {
                        isFullScreen = _isFullScreen;
                        if (!isFullScreen) {
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        } else {
                            youtubePlayer.seekToMillis(currentHolder.utubeTime);
                        }
                    }
                });
                youtubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
            } else if (isThirdStage) {
                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.errorvideo), Gravity.CENTER,
                        0, 0, 1000);
            }
        }
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), REQUEST_RESULT).show();
        } else {
            GeoAnimator.makeDurationToast(getActivity(), getString(R.string.errorvideo), Gravity.CENTER, 0, 0, 1500);
        }
    }

    public void changeButtonsFromList(final List<HelperHolder> list) {
        videoCardView.setVisibility(View.GONE);
        for (CardView cv : buttonList) {
            cv.setVisibility(View.GONE);
        }
        for (int i = 0; i < list.size(); ++i) {
            buttonList.get(i).setVisibility(View.VISIBLE);
            spaceList.get(i).setVisibility(View.VISIBLE);
            final int finalI = i;
            buttonList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int ii = 0; ii < list.size(); ++ii) {
                        buttonList.get(ii).setVisibility(View.GONE);
                        mOLinLayout.setVisibility(View.GONE);
                        videoTopTextView.setText(list.get(finalI).title);
                        videoBottomTextView.setText(list.get(finalI).contents);
                        if (youtubePlayer != null) {
                            youtubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
                            youtubePlayer.seekToMillis(list.get(finalI).utubeTime);
                        } else {
                            GeoAnimator.makeDurationToast(getActivity(), getString(R.string.errorvideo), Gravity.CENTER,
                                    0, 0, 1000);
                        }
                        currentHolder = list.get(finalI);
                        videoCardView.setVisibility(View.VISIBLE);
                        expand(videoCardView);
                        isThirdStage = true;
                        getActivity().invalidateOptionsMenu();
                    }
                }
            });
            imageList.get(i).setVisibility(View.GONE);
            tvList.get(i).setText(list.get(i).title);
        }
        isSecondStage = true;
        mOLinLayout.setVisibility(View.VISIBLE);
        getActivity().invalidateOptionsMenu();
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 2);
        v.startAnimation(a);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_RESULT) {
                youtubeFragment.initialize(getString(R.string.api_key), this);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isFullScreen) {
            youtubePlayer.setFullscreen(false);
        } else if (isThirdStage) {
            setButtonOriginal();
            changeButtonsFromList(currentList);
            isThirdStage = false;
            getActivity().invalidateOptionsMenu();
        } else if (isSecondStage) {
            setButtonOriginal();
            isSecondStage = false;
            getActivity().invalidateOptionsMenu();
        } else {
            return false;
        }
        return true;
    }


    public void setButtonOriginal() {
        tvList.get(0).setText(R.string.information);
        tvList.get(1).setText(R.string.curev);
        tvList.get(2).setText(R.string.saved_articles);
        tvList.get(3).setText(R.string.dm);
        tvList.get(4).setText(R.string.other);

        for (int i = 0; i < 5; ++i) {
            imageList.get(i).setVisibility(View.VISIBLE);
            buttonList.get(i).setVisibility(View.VISIBLE);
            buttonList.get(i).setAlpha(1f);
            btvList.get(i).setVisibility(View.GONE);
            spaceList.get(i).setVisibility(View.GONE);
        }

        getActivity().setTitle(getString(R.string.help));

        buttonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(infoList);
                currentList = infoList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.information)));
            }
        });
        buttonList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(ceList);
                currentList = ceList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.curev)));
            }
        });
        buttonList.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(saList);
                currentList = saList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.saved_articles)));
            }
        });
        buttonList.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(dmList);
                currentList = dmList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.dm)));
            }
        });
        buttonList.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonsFromList(otherList);
                currentList = otherList;
                getActivity().setTitle(getString(R.string.two_dash_placeholder, getString(R.string.help), getString(R.string.other)));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_help, menu);

        MenuItem backItem = menu.findItem(R.id.menu_action_back);
        MenuItem seekItem = menu.findItem(R.id.menu_action_videoseek);
        if (isThirdStage) {
            backItem.setVisible(true);
            backItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            seekItem.setVisible(true);
        } else if (isSecondStage) {
            backItem.setVisible(true);
            seekItem.setVisible(false);
        } else {
            backItem.setVisible(false);
            seekItem.setVisible(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "ropkaenterprises@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_ellipsis)));
                return true;
            case R.id.menu_action_back:
                if (isThirdStage) {
                    setButtonOriginal();
                    changeButtonsFromList(currentList);
                    isThirdStage = false;
                    getActivity().invalidateOptionsMenu();
                } else if (isSecondStage) {
                    setButtonOriginal();
                    isSecondStage = false;
                    getActivity().invalidateOptionsMenu();
                }
                return true;
            case R.id.menu_action_videoseek:
                if (isThirdStage) {
                    youtubePlayer.seekToMillis(currentHolder.utubeTime);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class HelperHolder {
        public int title;
        public int contents;
        public int utubeTime;

        public HelperHolder(int Title, int Contents, int UtubeTime) {
            title = Title;
            contents = Contents;
            utubeTime = UtubeTime;
        }
    }
}

package com.ropkastudios.android.thegeographyapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class SubcategoryContentFragment extends BackPressedFragment implements FragmentLifecycleInterface {
    private static final int REQUEST_ERROR = 0;
    private static final String ARG_CONTENT_ID = "content_id";
    private static final String ARG_NOTES = "arg_notes";
    public static final String CONTENT_TITLE_INDICATOR = "tle_ntent_lಣnk_123456789_condft";
    public static final String CONTENT_CATEGORY_INDICATOR = "catego_lಣink_1236789_coendft";
    public static final String CONTENT_SUBCAT_INDICATOR = "subor_co_liಣk_12tendft";
    public static final String GMAPS_LATITUDE = "map238234jsdg_ಣdlfಣitude";
    public static final String GMAPS_LONGITUDE = "maps3243jsdsdfಣdllongude";
    public static final String GMAPS_ZOOM = "m3234238_do=zoomಣಣ34l";
    public static final String GMAPS_TILT = "mjiubaijiushe_do=zoomಣಣ34l";
    public static final String GMAPS_BEARING = "bearingasdfjkbajiushiyibai=zoomಣಣ34l";
    public static final String GMAPS_TITLE = "ajsdf83-98dsf=zoomಣಣ3anagelaikidu";
    private static final int REQUEST_CANCEL_EDITS = 30;
    private static final int REQUEST_DONE_EDITS = 50;
    private static final int REQUEST_GALLERY_IMAGE = 5;
    private static final int REQUEST_REPLACE_GALLERY_IMAGE = 8;
    private static final int REQUEST_NONE = 20;
    private static final int REQUEST_CHANGE_IMAGE = 10;
    private static final int REQUEST_CONTENT_LINK = 15;
    private static final int REQUEST_GMAPS = 23;
    private static final int REQUEST_REPLACE_CONTENT_LINK = 9;
    private static final int REQUEST_CHANGE_CONTENT_LINK = 33;
    private static final int REQUEST_TITLE_MAPS_LINK = 49;
    private static final int REQUEST_CHANGE_MAPS_LINK = 51;
    private static final int REQUEST_REPLACE_MAPS_LINK = 53;
    private static final int REQUEST_REPLACE_TITLE_MAPS_LINK = 69;
    private static final int REQUEST_TAKE_PHOTO = 101;
    private static final int REQUEST_TEXT_INPUT = 100;
    private static final String DIALOG_CANCEL_EDITS = "ContentCancelEditsDialog";
    private static final String DIALOG_DONE_EDITS = "ContentDoneEditsDialog";
    private static final String DIALOG_NONE = "ContentNoneDialog";
    private static final String DIALOG_CHANGE_IMAGE = "ChangeImageDialog";
    private static final String DIALOG_CHANGE_CONTENT = "ChangeContentDialog";
    private static final String DIALOG_CONTENT_LINK = "DialogContentLink";
    private static final String DIALOG_REPLACE_CONTENT = "ReplaceContentDialog";
    private static final String DIALOG_TITLE_MAPS_LINK = "DialogTitleMapsLink";
    private static final String DIALOG_CHANGE_MAPS_LINK = "DialogChangeMapsLink";
    private static final String DIALOG_REPLACE_TITLE_MAPS_LINK = "DialogRepTitleMapLink";
    public static final String CURRENT_INDEX = "current_index";
    public static final String CURRENT_TITLE_POSITION = "current_title_position";
    public static final String CURRENT_FILE_PATH = "current_file_path";
    public static final String IS_NOTES = "is_notes";
    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;
    private SubcategoryCardInfoHolder cardInfo;
    private RecyclerView contentRecyclerView;
    private LinearLayout linkHolder;
    private CardView buttonHolder;
    private CardView editOptionsHolder;
    private TextView popupTextView;
    public boolean viewWasCreated = false;
    private TitleParaAdapter mAdapter;
    private TitleParaEditAdapter mEditAdapter;
    private Callbacks mCallbacks;
    private ArrayList<String> titleList;
    private ArrayList<String> paraList;
    private ArrayList<String> contentList;
    private ArrayList<String> mapsList;
    private ArrayList<String> holdTitleList;
    private ArrayList<String> holdParaList;
    private ArrayList<String> holdContentList;
    private ArrayList<String> holdMapList;
    private String currentCategory;
    private ImageButton switchButton;
    private int currentIndex;
    private boolean updateMode = false;
    private boolean imageMode = false;
    private boolean gmapsMode = false;
    private boolean cameraMode = false;
    private boolean isTextSwitch = true;
    private Boolean isNotes;
    private boolean contentMode = false;
    private double holdLat;
    private double holdLong;
    private float holdZoom;
    private float holdTilt;
    private float holdBear;
    String mCurrentPhotoPath;
    private String holdMapsString;
    private int currentIndexOf;
    private int currentTitlePosition;
    private boolean wasEdit;
    //private String currentCameraPath;
    private ArrayList<CloseImageEvent> eventList = new ArrayList<>();

    public interface Callbacks {
        void movePager(int index, boolean smoothScroll);
        ViewPager getViewPager();
        void closeKeyboard();
        void setPagerList(String category, String subcategory);
    }

    public static SubcategoryContentFragment newInstance(UUID uuid, boolean notes) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTENT_ID, uuid);
        args.putBoolean(ARG_NOTES, notes);
        SubcategoryContentFragment fragment = new SubcategoryContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void activateCIListeners(int pos, ImageView imageView, CardView cardView, boolean dOrNah) {
        try {
            for (int i = 0; i < pos; i++) {
                eventList.get(i).activateCloseImage();
            }
            for (int i = pos + 1; i < eventList.size(); i++) {
                eventList.get(i).activateCloseImage();
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }

        imageView.setVisibility(View.VISIBLE);
        imageView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        imageView.requestLayout();
        expand(cardView, dOrNah);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mCallbacks = (Callbacks) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID cardUUID = (UUID) getArguments().getSerializable(ARG_CONTENT_ID);
        SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
        cardInfo = cardLab.getSpecificCardInfo(cardUUID);
        setTitleAndParaLists();
        currentCategory = cardInfo.getCategory().toLowerCase();

        List<SubcategoryCardInfoHolder> currentCardList = cardLab.getCategoryCardInfos(currentCategory);

        for (int i = 0; i < currentCardList.size(); ++i) {
            if (currentCardList.get(i).getTitle().equals(cardInfo.getTitle())) {
                currentIndex = i;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_subcategory_contents, container, false);

        popupTextView = (TextView) view.findViewById(R.id.info_subcat_content_card_holder_popup);
        contentRecyclerView = (RecyclerView) view.findViewById(R.id.info_subcat_content_card_holder_recycler_view);
        Button previousButton = (Button) view.findViewById(R.id.info_subcat_contents_card_holder_previous_button);
        Button nextButton = (Button) view.findViewById(R.id.info_subcat_contents_card_holder_next_button);
        buttonHolder = (CardView) view.findViewById(R.id.info_subcat_contents_bottom_card_holder);
        //final LinearLayout miniLinkHolder = (LinearLayout) view.findViewById(R.id.mini_link_holder);

        editOptionsHolder = (CardView)
                view.findViewById(R.id.info_subcat_content_card_holder_add_delete_holder_fixed);
        editOptionsHolder.setVisibility(View.GONE);
        assert editOptionsHolder != null;
        linkHolder = (LinearLayout) view.findViewById(R.id.info_subcat_content_card_holder_link_holder);
        final Button imageContentButton = (Button) view.findViewById(R.id.info_subcat_contents_card_holder_image_button_fixed);
        imageContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNumOfImages() <= 10) {
                    imageMode = true;
                    updateMode = false;
                    getActivity().invalidateOptionsMenu();
                    //miniLinkHolder.setVisibility(View.GONE);
                    setMiniLinkHolderVisibility(View.GONE);
                    popupTextView.setText(getString(R.string.cursor_image));
                    popupTextView.setVisibility(View.VISIBLE);
                } else {
                    FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                    BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                            getString(R.string.over_10_images),
                            getString(R.string.tobeimage),
                            getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                    dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                    dialogCancel.show(managerCancel, DIALOG_NONE);
                }
            }
        });
        imageContentButton.setVisibility(View.GONE);


        final Button mapsContentButton = (Button) view.findViewById(R.id.info_subcat_contents_card_holder_g_maps_button_fixed);
        mapsContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNumOfMapLinks() > 10) {
                    FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                    BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                            getString(R.string.over_10_maplinks),
                            getString(R.string.tobegmapslink),
                            getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                    dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                    dialogCancel.show(managerCancel, DIALOG_NONE);
                } else {
                    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                    int errorCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
                    if (errorCode != ConnectionResult.SUCCESS) {
                        Dialog errorDialog = apiAvailability
                                .getErrorDialog(getActivity(), errorCode, REQUEST_ERROR);
                        errorDialog.show();
                    } else {
                        gmapsMode = true;
                        updateMode = false;
                        getActivity().invalidateOptionsMenu();
                        //miniLinkHolder.setVisibility(View.GONE);
                        setMiniLinkHolderVisibility(View.GONE);
                        popupTextView.setText(getString(R.string.cursor_gmapslink));
                        popupTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mapsContentButton.setVisibility(View.GONE);

        final Button contentContentButton = (Button) view.findViewById(R.id.info_subcat_contents_card_holder_content_button_fixed);
        contentContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNumOfContentLinks() >= 12) {
                    FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                    BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                            getString(R.string.over_12_clinks),
                            getString(R.string.tobeclink),
                            getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                    dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                    dialogCancel.show(managerCancel, DIALOG_NONE);
                } else {
                    contentMode = true;
                    updateMode = false;
                    getActivity().invalidateOptionsMenu();
                    //miniLinkHolder.setVisibility(View.GONE);
                    setMiniLinkHolderVisibility(View.GONE);
                    popupTextView.setText(getString(R.string.cursor_clink));
                    popupTextView.setVisibility(View.VISIBLE);
                }
            }
        });
        contentContentButton.setVisibility(View.GONE);

        switchButton = (ImageButton) view.findViewById(R.id.info_subcat_contents_card_holder_switcheroo_button_fixed);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTextSwitch = !isTextSwitch;
                if (isTextSwitch) {
                    imageContentButton.setVisibility(View.GONE);
                    mapsContentButton.setVisibility(View.GONE);
                    contentContentButton.setVisibility(View.GONE);
                    popupTextView.setVisibility(View.VISIBLE);
                    popupTextView.setText(R.string.text_placeholder);
                } else {
                    imageContentButton.setVisibility(View.VISIBLE);
                    mapsContentButton.setVisibility(View.VISIBLE);
                    contentContentButton.setVisibility(View.VISIBLE);
                    popupTextView.setVisibility(View.GONE);
                }
            }
        });
        switchButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isTextSwitch) {
                    GeoAnimator.makeDurationToast(getActivity(), getString(R.string.text_switch_no), Gravity.CENTER, 0, 0,
                            1500);
                } else {
                    GeoAnimator.makeDurationToast(getActivity(), getString(R.string.text_switch_yes), Gravity.CENTER, 0, 0,
                            1500);
                }
                return false;
            }
        });
        popupTextView.setVisibility(View.VISIBLE);
        popupTextView.setText(R.string.text_placeholder);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentRecyclerView.setFocusable(false);


        SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
        final List<SubcategoryCardInfoHolder> cardList = cardLab.getCategoryCardInfos(currentCategory);
        int previousValue = 0;
        if (currentIndex == 0) {
            previousValue = cardList.size() - 1;
            previousButton.setText("<<" + cardList.get(previousValue).getTitle());
        } else {
            previousValue = currentIndex - 1;
            previousButton.setText("<<" + cardList.get(previousValue).getTitle());
        }
        final int finalPreviousValue = previousValue;
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.movePager(finalPreviousValue, true);
            }
        });

        int nextValue = 0;
        if (currentIndex == (cardList.size() - 1)) {
            nextValue = 0;
            nextButton.setText(cardList.get(0).getTitle() + ">>");
        } else {
            nextValue = currentIndex + 1;
            nextButton.setText(cardList.get(nextValue).getTitle() + ">>");
        }
        final int finalNextValue = nextValue;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.movePager(finalNextValue, true);
            }
        });

        viewWasCreated = true;

        if (savedInstanceState != null && savedInstanceState.getBoolean(ContentPagerActivity.EDIT_MODE)) {
            setHolderTitleAndParaLists();
            titleList = savedInstanceState.getStringArrayList(ContentPagerActivity.TITLE_LIST);
            paraList = savedInstanceState.getStringArrayList(ContentPagerActivity.PARA_LIST);
            mapsList = savedInstanceState.getStringArrayList(ContentPagerActivity.GMAPS_LIST);
            contentList = savedInstanceState.getStringArrayList(ContentPagerActivity.CONT_LIST);
            wasEdit = true;
            updateMode = true;
            getActivity().invalidateOptionsMenu();
        }

        if (wasEdit) {
            updateMode = true;
            getActivity().invalidateOptionsMenu();
            setTextsToEditViews();
        } else {
            updateUI();
            getActivity().invalidateOptionsMenu();
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        SCREEN_WIDTH = metrics.widthPixels;
        SCREEN_HEIGHT = metrics.heightPixels;

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_subcategory_contents, menu);

        MenuItem linkItem = menu.findItem(R.id.fragment_subcategory_content_link);
        MenuItem changeItem = menu.findItem(R.id.fragment_subcategory_content_action_change);
        MenuItem doneItem = menu.findItem(R.id.fragment_subcategory_content_action_done);
        MenuItem cancelItem = menu.findItem(R.id.fragment_subcategory_content_action_cancel);
        MenuItem backItem = menu.findItem(R.id.fragment_subcategory_content_action_back);
        MenuItem viewItem = menu.findItem(R.id.fragment_subcategory_content_show_other);
        MenuItem cameraItem = menu.findItem(R.id.fragment_subcategory_content_action_camera);
        MenuItem modeItem = menu.findItem(R.id.fragment_subcategory_content_action_mode);

        if (cardInfo.getDeleteable()) {
            menu.findItem(R.id.fragment_subcategory_content_link).setVisible(false);
        }

        changeItem.setTitle(getResources().getString(R.string.edit_mode));

        if (imageMode || contentMode || gmapsMode || cameraMode) {
            doneItem.setVisible(false);
            modeItem.setIcon(imageMode ? R.drawable.ic_action_gallery :
                    (contentMode ? R.drawable.ic_action_clink : (gmapsMode ? R.drawable.ic_action_maps : R.drawable.ic_action_screenshot)));
            modeItem.setVisible(true);
            cancelItem.setVisible(true);
            changeItem.setVisible(false);
            linkItem.setVisible(false);
            backItem.setVisible(false);
            cameraItem.setVisible(false);
        } else if (updateMode) {
            doneItem.setVisible(true);
            doneItem.setTitle(getResources().getString(R.string.save));
            modeItem.setVisible(false);
            cancelItem.setVisible(true);
            linkItem.setVisible(false);
            changeItem.setVisible(false);
            backItem.setVisible(false);
            cameraItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        } else {
            doneItem.setVisible(false);
            cancelItem.setVisible(false);
            linkItem.setVisible(true);
            changeItem.setVisible(true);
            modeItem.setVisible(false);
            backItem.setVisible(true);
            cameraItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        if (getIsNotes() != null) {
            if (getIsNotes()) {
                viewItem.setTitle(getString(R.string.viewcontents));
            } else {
                viewItem.setTitle(getString(R.string.viewnotes));
            }
        }
    }

    public int getNumOfImages() {
        int counter = 0;

        if (updateMode) {
            for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                ImageView image = (ImageView)
                        layout.findViewById(R.id.contents_Holder_m_s_txt_views_contents_edit_image_view);
                if (image.getVisibility() == View.VISIBLE) {
                    counter++;
                }
            }
        } else {
            for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                ImageView image = (ImageView)
                        layout.findViewById(R.id.contents_holder_m_s_txt_views_contents_text_image_view);
                if (image.getVisibility() == View.VISIBLE) {
                    counter++;
                }
            }
        }
        return counter;
    }
    public int getNumOfMapLinks() {
        int counter = 0;
        if (updateMode) {
            for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                CardView cardView = (CardView)
                        layout.findViewById(R.id.contents_holder_m_s_txt_view_edit_map_view);
                if (cardView.getVisibility() == View.VISIBLE) {
                    counter++;
                }
            }
        } else {
            for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                CardView cardView = (CardView)
                        layout.findViewById(R.id.contents_holder_m_s_txt_view__map_view_text);
                if (cardView.getVisibility() == View.VISIBLE) {
                    counter++;
                }
            }
        }
        return counter;
    }
    public int getNumOfContentLinks() {
        int counter = 0;
        if (updateMode) {
            for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                CardView cardView = (CardView)
                        layout.findViewById(R.id.contents_holder_m_s_txt_views_edit_card_view);
                if (cardView.getVisibility() == View.VISIBLE) {
                    counter++;
                }
            }
        } else {
            for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                CardView cardView = (CardView)
                        layout.findViewById(R.id.contents_holder_m_s_txt_views_contents_card_view);
                if (cardView.getVisibility() == View.VISIBLE) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public Boolean getIsNotes() {
        return getActivity().getSharedPreferences(MainMenuActivity.PREFS_NAME, 0).getBoolean(IS_NOTES, Boolean.valueOf(null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_subcategory_content_action_change:
                updateMode = true;
                getActivity().invalidateOptionsMenu();
                setHolderTitleAndParaLists();
                setTitleAndParaLists();
                setTextsToEditViews();
                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.edit_mode), Gravity.CENTER, 0, 0, 1000);
                ContentPagerActivity.increaseAdCount(getActivity());
                isTextSwitch = true;
                switchButton.callOnClick();
                return true;
            case R.id.fragment_subcategory_content_show_other:
                if (getIsNotes() != null) {
                    String s = getIsNotes() ? cardInfo.getContents().substring(0, 10) : cardInfo.getNotes().substring(0, 10);
                    Log.e("asdf", getIsNotes().toString() +s );
                    boolean notes = getIsNotes();
                    String paragraph = TextInterpreter.stripToText(notes ? cardInfo.getContents() : cardInfo.getNotes());
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    CopyPasteDialogFragment dialog = CopyPasteDialogFragment.newInstance(notes ? getString(R.string.contents) : getString(R.string.notes), paragraph);
                    dialog.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                    dialog.show(manager, DIALOG_NONE);
                }
                return true;
            case R.id.fragment_subcategory_content_action_cancel:
                if (updateMode) {
                    FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                    BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                            getString(R.string.cancel_editing_changes, cardInfo.getTitle()),
                            getString(R.string.currentchangesgone),
                            getResources().getString(R.string.cancel), getResources().getString(R.string.dont_cancel), true);
                    dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_CANCEL_EDITS);
                    dialogCancel.show(managerCancel, DIALOG_CANCEL_EDITS);
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mCallbacks.getViewPager().getWindowToken(), 0);
                    return true;
                } else if (imageMode || contentMode || gmapsMode || cameraMode) {
                    linkHolder.setVisibility(View.VISIBLE);
                    updateMode = true;
                    imageMode = false;
                    contentMode = false;
                    cameraMode = false;
                    gmapsMode = false;
                    getActivity().invalidateOptionsMenu();
                    isTextSwitch = true;
                    switchButton.setVisibility(View.VISIBLE);
                    switchButton.callOnClick();
                    return true;
                }
            case R.id.fragment_subcategory_content_action_done:
                if (updateMode) {
                    closeEditTexts();
                    long count = 0;
                    for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                        LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                        EditText editText =
                                (EditText)layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                        String text = editText.getText().toString();
                        count += text.length();
                    }
                    if (count >= (Integer.MAX_VALUE / 8)) {
                        FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                        BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                                getString(R.string.max_text),
                                getString(R.string.max_text_desc),
                                getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                        dialogCancel.show(managerCancel, DIALOG_NONE);
                    } else {
                        FragmentManager managerDone = getActivity().getSupportFragmentManager();

                        BasicConfirmCheckerDialogFragment dialogDone =
                                BasicConfirmCheckerDialogFragment.newInstance(
                                        getString(R.string.finish_editing_changes, cardInfo.getTitle()),
                                        getString(R.string.notdonedone), getResources().getString(R.string.done), getResources().getString(R.string.not_done), null, true);
                        dialogDone.setTargetFragment(SubcategoryContentFragment.this, REQUEST_DONE_EDITS);
                        dialogDone.show(managerDone, DIALOG_DONE_EDITS);
                        mCallbacks.closeKeyboard();
                    }
                    return true;
                }
                return true;
            case R.id.fragment_subcategory_content_action_mode:
                if (imageMode) {
                    mCallbacks.closeKeyboard();
                    boolean hasFocus = false;
                    for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                        LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                        EditText editText =
                                (EditText)layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);

                        if (editText.hasFocus()) {
                            currentIndexOf = editText.getSelectionStart();
                            currentTitlePosition = i;
                            hasFocus = true;
                            break;
                        }
                    }

                    if (!hasFocus) {
                        FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                        BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                                getString(R.string.thereisnocursor),
                                getString(R.string.cursorimage_desc),
                                getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                        dialogCancel.show(managerCancel, DIALOG_NONE);
                    } else {
                        Intent intent;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                        } else {
                            intent = new Intent(Intent.ACTION_GET_CONTENT);
                        }
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.pickimage_colon)),
                                REQUEST_GALLERY_IMAGE);
                    }
                } else if (contentMode) {
                    mCallbacks.closeKeyboard();
                    boolean hasFocus = false;
                    for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                        LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                        EditText editText =
                                (EditText)layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);

                        if (editText.hasFocus()) {
                            currentIndexOf = editText.getSelectionStart();
                            currentTitlePosition = i;
                            hasFocus = true;
                            break;
                        }
                    }

                    if (!hasFocus) {
                        FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                        BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                                getString(R.string.thereisnocursor),
                                getString(R.string.cursorlink_desc),
                                getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                        dialogCancel.show(managerCancel, DIALOG_NONE);
                    } else {
                        CardInfoLab lab = CardInfoLab.get(getActivity());
                        List<CardInfoHolder> holderList = lab.getCardInfos();
                        ArrayList<String> categoryList = new ArrayList<>();
                        for (int i = 0; i < holderList.size(); ++i) {
                            categoryList.add(holderList.get(i).getTitle());
                        }

                        SubcategoryCardInfoLab subLab = SubcategoryCardInfoLab.get(getActivity());
                        ArrayList<ArrayList<String>> subcatList = new ArrayList<>();
                        for (int i = 0; i < categoryList.size(); ++i) {
                            List<SubcategoryCardInfoHolder> list =
                                    subLab.getCategoryCardInfos(categoryList.get(i).toLowerCase());
                            ArrayList<String> stringlist = new ArrayList<>();
                            for (int ii = 0; ii < list.size(); ++ii) {
                                stringlist.add(list.get(ii).getTitle());
                            }
                            subcatList.add(stringlist);
                        }
                        FragmentManager managerContent = getActivity().getSupportFragmentManager();
                        ContentLinkDialogFragment dialogCancel = ContentLinkDialogFragment.newInstance(
                                getString(R.string.createcontentlink_colon),
                                getString(R.string.whatshouldlinkbecalled), getString(R.string.ellipsis),
                                getString(R.string.picksubcatlinkgoesto_colon), "", getString(R.string.link_warning),
                                getString(R.string.cancel), getString(R.string.ok), categoryList, subcatList
                        );
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_CONTENT_LINK);
                        dialogCancel.show(managerContent, DIALOG_CONTENT_LINK);
                    }
                } else if (gmapsMode) {
                    mCallbacks.closeKeyboard();
                    boolean hasFocus = false;
                    for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                        LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                        EditText editText =
                                (EditText)layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);

                        if (editText.hasFocus()) {
                            currentIndexOf = editText.getSelectionStart();
                            currentTitlePosition = i;
                            hasFocus = true;
                            break;
                        }
                    }

                    if (!hasFocus) {
                        FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                        BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                                getString(R.string.thereisnocursor),
                                getString(R.string.cursorlink_desc),
                                getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                        dialogCancel.show(managerCancel, DIALOG_NONE);
                    } else {
                        Intent intent = new Intent(this.getActivity(), MapDataResultActivity.class);
                        intent.putExtra(MapDataResultActivity.ARG_TITLE, getString(R.string.setyourmap_colon));
                        intent.putExtra(MapDataResultActivity.ARG_LATITUDE, 0D);
                        intent.putExtra(MapDataResultActivity.ARG_LONGITUDE, 0D);
                        intent.putExtra(MapDataResultActivity.ARG_ZOOM, 0F);
                        intent.putExtra(MapDataResultActivity.ARG_TILT, 0F);
                        intent.putExtra(MapDataResultActivity.ARG_BEARING, 0F);
                        startActivityForResult(intent, REQUEST_GMAPS);
                    }
                } else if (cameraMode) {
                    mCallbacks.closeKeyboard();
                    boolean hasFocus = false;
                    for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                        LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                        EditText editText =
                                (EditText) layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);

                        if (editText.hasFocus()) {
                            currentIndexOf = editText.getSelectionStart();
                            currentTitlePosition = i;
                            hasFocus = true;
                            break;
                        }
                    }

                    if (!hasFocus) {
                        FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                        BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                                getString(R.string.thereisnocursor),
                                getString(R.string.camera_warning),
                                getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                        dialogCancel.show(managerCancel, DIALOG_NONE);
                    } else {
                        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            File photoFile;
                            try {
                                photoFile = createImageFile();

                                if (photoFile != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                                }
                            } catch (IOException ex) {
                                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.errortryagainlater),  Gravity.CENTER,
                                        0, 0, 1000);
                            }
                        }*/
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.errortryagainlater), Gravity.CENTER,
                                        0, 0, 1000);
                                setMiniLinkHolderVisibility(View.VISIBLE);
                            }
                            if (photoFile != null) {
                                if (getIsNotes() != null) {
                                    SharedPreferences sp = getActivity().getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
                                    sp.edit().putInt(CURRENT_INDEX, currentIndexOf)
                                            .putInt(CURRENT_TITLE_POSITION, currentTitlePosition)
                                            .putString(CURRENT_FILE_PATH, mCurrentPhotoPath)
                                            .putBoolean(IS_NOTES, getIsNotes()).apply();
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                                } else {
                                    GeoAnimator.makeDurationToast(getActivity(), getString(R.string.errortryagainlater), Gravity.CENTER,
                                            0, 0, 1000);
                                }
                            }
                        }
                    }
                }
                return true;
            case R.id.fragment_subcategory_content_action_back:
                startActivity(MainMenuActivity.newIntent(getActivity(), MainMenuActivity.INFORMATION_SUBCATEGORY, cardInfo.getCategory()));
                ((FinishActivity) getActivity()).finishActivity();
                return true;
            case R.id.fragment_subcategory_content_link:
                String url = "https://en.wikipedia.org/wiki/" + getActivity().getTitle().toString().replace(" ", "_");
                Intent launchBrowser = WebPageActivity.newIntent(getActivity(), Uri.parse(url),
                        null, null, null, null, false, null);
                startActivity(launchBrowser);
                return true;
            case R.id.fragment_subcategory_content_action_camera:
                if (!updateMode) {
                    updateMode = true;
                    setHolderTitleAndParaLists();
                    setTitleAndParaLists();
                    setTextsToEditViews();
                    GeoAnimator.makeDurationToast(getActivity(), getString(R.string.edit_mode), Gravity.CENTER, 0, 0, 1000);
                    ContentPagerActivity.increaseAdCount(getActivity());
                    popupTextView.setVisibility(View.GONE);
                    linkHolder.setVisibility(View.VISIBLE);
                    getActivity().invalidateOptionsMenu();
                } if (getNumOfImages() <= 10) {
                    cameraMode = true;
                    updateMode = false;
                    getActivity().invalidateOptionsMenu();
                    popupTextView.setText(getString(R.string.cursor_image));
                    popupTextView.setVisibility(View.VISIBLE);
                    setMiniLinkHolderVisibility(View.GONE);
                } else {
                    FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                    BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                            getString(R.string.over_10_images),
                            getString(R.string.tobeimage),
                            getString(R.string.ok),
                            getResources().getString(R.string.dont_cancel), false);
                    dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                    dialogCancel.show(managerCancel, DIALOG_NONE);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMiniLinkHolderVisibility(int visibility) {
        if (getView() != null) {
            getView().findViewById(R.id.info_subcat_contents_card_holder_content_button_fixed).setVisibility(visibility);
            getView().findViewById(R.id.info_subcat_contents_card_holder_image_button_fixed).setVisibility(visibility);
            getView().findViewById(R.id.info_subcat_contents_card_holder_g_maps_button_fixed).setVisibility(visibility);
            switchButton.setVisibility(visibility);
        }
    }

    private File createImageFile() throws IOException {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MapDataResultActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);
            return null;
        } else {
            /*final Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);*/

            try {
                File storageDir = Environment.getExternalStorageDirectory();
                File image = File.createTempFile(
                        "geographyfirstphoto",  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                mCurrentPhotoPath = image.getAbsolutePath();
                return image;
            } catch (Throwable e) {
                e.printStackTrace();
                FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                        getString(R.string.errorcreatingimage),
                        getString(R.string.sorryaboutthat),
                        getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                dialogCancel.show(managerCancel, DIALOG_NONE);
                return null;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MapDataResultActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCallbacks.closeKeyboard();
                    boolean hasFocus = false;
                    for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                        LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                        EditText editText =
                                (EditText)layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);

                        if (editText.hasFocus()) {
                            currentIndexOf = editText.getSelectionStart();
                            currentTitlePosition = i;
                            hasFocus = true;
                            break;
                        }
                    }

                    if (!hasFocus) {
                        FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                        BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                                getString(R.string.thereisnocursor),
                                getString(R.string.camera_warning),
                                getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_NONE);
                        dialogCancel.show(managerCancel, DIALOG_NONE);
                    } else {
                        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            File photoFile;
                            try {
                                photoFile = createImageFile();

                                if (photoFile != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                                }
                            } catch (IOException ex) {
                                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.errortryagainlater),  Gravity.CENTER,
                                        0, 0, 1000);
                            }
                        }*/
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.errortryagainlater), Gravity.CENTER,
                                        0, 0, 1000);
                            }
                            if (photoFile != null) {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                            }
                        }
                    }
                }
                //miniLinkHolder.setVisibility(View.GONE);
                setMiniLinkHolderVisibility(View.VISIBLE);
                return;
            } default: {
                FragmentManager managerCancel = getActivity().getSupportFragmentManager();
                BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                        getString(R.string.permissions), getString(R.string.permissions_camera_warning),
                        getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                dialogCancel.show(managerCancel, DIALOG_NONE);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (updateMode) {
            FragmentManager managerCancel = getActivity().getSupportFragmentManager();
            BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                    getString(R.string.aresure_delete_category_question, cardInfo.getTitle()),
                    getString(R.string.currentchangesgone),
                    getResources().getString(R.string.cancel), getResources().getString(R.string.dont_cancel), true);
            dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_CANCEL_EDITS);
            dialogCancel.show(managerCancel, DIALOG_CANCEL_EDITS);
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mCallbacks.getViewPager().getWindowToken(), 0);
            return true;
        } else if (imageMode || contentMode || gmapsMode || cameraMode) {
            popupTextView.setVisibility(View.GONE);
            linkHolder.setVisibility(View.VISIBLE);
            updateMode = true;
            imageMode = false;
            contentMode = false;
            gmapsMode = false;
            cameraMode = false;
            getActivity().invalidateOptionsMenu();
            return true;
        } else {
            return false;
        }
    }

    public String getUptilIndex(String string, int index) {
        String output = "";
        for (int i = 0; i < index; ++i) {
            output += string.charAt(i);
        }
        return output;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (updateMode || imageMode || gmapsMode || contentMode) {
            savedInstanceState.putStringArrayList(ContentPagerActivity.TITLE_LIST, titleList);
            savedInstanceState.putStringArrayList(ContentPagerActivity.PARA_LIST, paraList);
            savedInstanceState.putStringArrayList(ContentPagerActivity.GMAPS_LIST, mapsList);
            savedInstanceState.putStringArrayList(ContentPagerActivity.CONT_LIST, contentList);
            savedInstanceState.putBoolean(ContentPagerActivity.EDIT_MODE, true);
        }
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CANCEL_EDITS) {
                closeEditTexts();
                if (getIsNotes()) {
                    cardInfo.setNotes(TextInterpreter.convertContentCardContent(
                            new StringArrayHolder(holdTitleList, holdParaList, holdContentList, holdMapList)
                    ));
                } else {
                    cardInfo.setContents(TextInterpreter.convertContentCardContent(
                            new StringArrayHolder(holdTitleList, holdParaList, holdContentList, holdMapList)
                    ));
                }
                resetToNormalAdapter();
                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.readmode), Gravity.CENTER, 0, 0, 1000);
                ContentPagerActivity.increaseAdCount(getActivity());
            } else if (requestCode == REQUEST_TEXT_INPUT) {
                int position = data.getIntExtra(BasicTextInputActivity.EXTRA_POSITION, -1);
                String text = data.getStringExtra(BasicTextInputActivity.EXTRA_TEXT);

                if (updateMode) {
                    if (position == -1) {
                        GeoAnimator.makeDurationToast(getActivity(), getString(R.string.errorchangingtext), Gravity.CENTER,
                                0, 0, 1000);
                    } else {
                        LinearLayout ll = (LinearLayout) contentRecyclerView.getChildAt(position);
                        EditText editText = (EditText) ll.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                        editText.setText(text);
                        titleList.set(position, text);
                    }

                    isTextSwitch = false;
                    switchButton.callOnClick();
                }
            } else if (requestCode == REQUEST_DONE_EDITS) {
                if (getIsNotes() != null) {
                    String newContents =
                            TextInterpreter
                                    .convertContentCardContent(new StringArrayHolder(titleList, paraList, contentList, mapsList));
                    SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
                    if (getIsNotes()) {
                        cardInfo.setNotes(newContents);
                    } else {
                        cardInfo.setContents(newContents);
                    }
                    cardLab.updateCardInfo(cardInfo);
                    setTitleAndParaLists();
                    resetToNormalAdapter();
                    GeoAnimator.makeDurationToast(getActivity(), getString(R.string.readmode), Gravity.CENTER, 0, 0, 1000);
                    ContentPagerActivity.increaseAdCount(getActivity());
                } else {
                    setTitleAndParaLists();
                    resetToNormalAdapter();
                    GeoAnimator.makeDurationToast(getActivity(), getString(R.string.therewasanerror), Gravity.CENTER, 0, 0, 1000);
                }
            } else if (requestCode == REQUEST_GALLERY_IMAGE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    ContentResolver resolver = getActivity().getContentResolver();
                    resolver.takePersistableUriPermission(data.getData(), takeFlags);
                }
                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(currentTitlePosition);
                EditText editText = (EditText)
                        layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                String text = editText.getText().toString();
                String endText = text.substring(currentIndexOf);
                String startText = getUptilIndex(text, currentIndexOf);
                if (startText.equals("")) {
                    startText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                }
                if (endText.equals("")) {
                    endText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                }

                titleList.add(currentTitlePosition, startText);
                paraList.add(currentTitlePosition, data.getDataString());
                contentList.add(currentTitlePosition, "");
                mapsList.add(currentTitlePosition, "");

                try {
                    titleList.set(currentTitlePosition + 1, endText);
                } catch (IndexOutOfBoundsException ioe) {
                    titleList.add(endText);
                }

                for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                    LinearLayout linlayout = (LinearLayout) contentRecyclerView.getChildAt(i);
                    EditText linEditText = (EditText)
                            linlayout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                    linEditText.setOnFocusChangeListener(null);
                }
                reinstantiateEditAdapter();
                ContentPagerActivity.increaseAdCount(getActivity());
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_CHANGE_IMAGE) {
                int position = data.getIntExtra(ThreeButtonDialogFragment.EXTRA_POSITION, -1);
                if (position != -1) {
                    paraList.remove(position);
                    String startText = titleList.get(position);
                    String endText = titleList.get(position + 1);
                    titleList.remove(position);
                    if (startText.equals(TextInterpreter.TXT_NOT_GONE_INDICATOR)) {
                        startText = "";
                    } if (endText.equals(TextInterpreter.TXT_NOT_GONE_INDICATOR)) {
                        endText = "";
                    }
                    titleList.set(position, startText + endText);
                    for (int i = (contentList.size() - 1); i < 0; --i) {
                        if (GeoAnimator.ultimateIsNull(contentList.get(i))) {
                            contentList.remove(i);
                            break;
                        }
                    }
                    for (int i = (mapsList.size() - 1); i < 0; --i) {
                        if (GeoAnimator.ultimateIsNull(mapsList.get(i))) {
                            mapsList.remove(i);
                            break;
                        }
                    }
                    reinstantiateEditAdapter();
                    ContentPagerActivity.increaseAdCount(getActivity());
                }
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_REPLACE_GALLERY_IMAGE) {
                if (currentTitlePosition != -1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        ContentResolver resolver = getActivity().getContentResolver();
                        resolver.takePersistableUriPermission(data.getData(), takeFlags);
                    }
                    paraList.set(currentTitlePosition, data.getDataString());
                    reinstantiateEditAdapter();
                    ContentPagerActivity.increaseAdCount(getActivity());
                }
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_CONTENT_LINK) {
                String title = data.getStringExtra(ContentLinkDialogFragment.EXTRA_TITLE);
                String category = data.getStringExtra(ContentLinkDialogFragment.EXTRA_CATEGORY);
                String subcategory = data.getStringExtra(ContentLinkDialogFragment.EXTRA_SUBCATEGORY);
                ContentLink contentLink = new ContentLink(title, category, subcategory);
                paraList.add(currentTitlePosition, "");
                mapsList.add(currentTitlePosition, "");
                contentList.add(currentTitlePosition, contentLink.toString());

                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(currentTitlePosition);
                EditText editText = (EditText)
                        layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                String text = editText.getText().toString();
                String endText = text.substring(currentIndexOf);
                String startText = getUptilIndex(text, currentIndexOf);

                if (startText.equals("")) {
                    startText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                }
                if (endText.equals("")) {
                    endText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                }

                titleList.add(currentTitlePosition, startText);
                try {
                    titleList.set(currentTitlePosition + 1, endText);
                } catch (IndexOutOfBoundsException ioe) {
                    titleList.add(endText);
                }

                for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                    LinearLayout linlayout = (LinearLayout) contentRecyclerView.getChildAt(i);
                    EditText linEditText = (EditText)
                            linlayout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                    linEditText.setOnFocusChangeListener(null);
                }
                reinstantiateEditAdapter();
                ContentPagerActivity.increaseAdCount(getActivity());
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_GMAPS) {
                holdLat = data.getDoubleExtra(MapDataResultActivity.EXTRA_LATITUDE, 0);
                holdLong = data.getDoubleExtra(MapDataResultActivity.EXTRA_LONGITUDE, 0);
                holdZoom = data.getFloatExtra(MapDataResultActivity.EXTRA_ZOOM, 0);
                holdTilt = data.getFloatExtra(MapDataResultActivity.EXTRA_TILT, 0);
                holdBear = data.getFloatExtra(MapDataResultActivity.EXTRA_BEARING, 0);
                currentTitlePosition = data.getIntExtra(MapDataResultActivity.EXTRA_CURRENT_TITLE, currentTitlePosition);
                currentIndexOf = data.getIntExtra(MapDataResultActivity.EXTRA_INDEX_OF, currentIndexOf);

                FragmentManager managerContent = getActivity().getSupportFragmentManager();
                BasicTextInputDialogFragment dialogCancel = BasicTextInputDialogFragment.newInstance(
                        getString(R.string.create_the_link_colon),
                        getString(R.string.what_should_link_title), getString(R.string.linktitlehere),
                        getString(R.string.ok), getString(R.string.cancel), "", 0, -1
                );
                dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_TITLE_MAPS_LINK);
                dialogCancel.show(managerContent, DIALOG_TITLE_MAPS_LINK);
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_CHANGE_CONTENT_LINK) {
                int position = data.getIntExtra(ThreeButtonDialogFragment.EXTRA_POSITION, -1);
                if (position != -1) {
                    contentList.remove(position);
                    String startText = titleList.get(position);
                    String endText = titleList.get(position + 1);
                    titleList.remove(position);
                    if (startText.equals(TextInterpreter.TXT_NOT_GONE_INDICATOR)) {
                        startText = "";
                    } if (endText.equals(TextInterpreter.TXT_NOT_GONE_INDICATOR)) {
                        endText = "";
                    }
                    titleList.set(position, startText + endText);
                    for (int i = 0; i < paraList.size(); ++i) {
                        if (GeoAnimator.ultimateIsNull(paraList.get(i))) {
                            paraList.remove(i);
                            break;
                        } else if (paraList.get(i).equals(paraList.get(position))) {
                            break;
                        }
                    }
                    for (int i = 0; i < mapsList.size(); ++i) {
                        if (GeoAnimator.ultimateIsNull(mapsList.get(i))) {
                            mapsList.remove(i);
                            break;
                        } else if (mapsList.get(i).equals(mapsList.get(position))) {
                            break;
                        }
                    }
                    reinstantiateEditAdapter();
                    ContentPagerActivity.increaseAdCount(getActivity());
                }
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_REPLACE_CONTENT_LINK) {
                if (currentTitlePosition != -1) {
                    String title = data.getStringExtra(ContentLinkDialogFragment.EXTRA_TITLE);
                    String category = data.getStringExtra(ContentLinkDialogFragment.EXTRA_CATEGORY);
                    String subcategory = data.getStringExtra(ContentLinkDialogFragment.EXTRA_SUBCATEGORY);
                    ContentLink contentLink = new ContentLink(title, category, subcategory);
                    contentList.set(currentTitlePosition, contentLink.toString());
                    reinstantiateEditAdapter();
                    ContentPagerActivity.increaseAdCount(getActivity());
                }
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_TITLE_MAPS_LINK) {
                String title = data.getStringExtra(BasicTextInputDialogFragment.EXTRA_TEXT);
                GMapsLink mapsLink = new GMapsLink(title, holdLat, holdLong, holdZoom, holdTilt, holdBear);
                mapsList.add(currentTitlePosition, mapsLink.toString());
                paraList.add(currentTitlePosition, "");
                contentList.add(currentTitlePosition, "");

                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(currentTitlePosition);
                EditText editText = (EditText)
                        layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                String text = editText.getText().toString();
                String endText = text.substring(currentIndexOf);
                String startText = getUptilIndex(text, currentIndexOf);

                if (startText.equals("")) {
                    startText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                }
                if (endText.equals("")) {
                    endText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                }

                titleList.add(currentTitlePosition, startText);
                try {
                    titleList.set(currentTitlePosition + 1, endText);
                } catch (IndexOutOfBoundsException ioe) {
                    titleList.add(endText);
                }

                for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                    LinearLayout linlayout = (LinearLayout) contentRecyclerView.getChildAt(i);
                    EditText linEditText = (EditText)
                            linlayout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                    linEditText.setOnFocusChangeListener(null);
                }

                reinstantiateEditAdapter();
                ContentPagerActivity.increaseAdCount(getActivity());
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_CHANGE_MAPS_LINK) {
                int position = data.getIntExtra(ThreeButtonDialogFragment.EXTRA_POSITION, -1);
                if (position != -1) {
                    mapsList.remove(position);
                    String startText = titleList.get(position);
                    String endText = titleList.get(position + 1);
                    titleList.remove(position);
                    if (startText.equals(TextInterpreter.TXT_NOT_GONE_INDICATOR)) {
                        startText = "";
                    } if (endText.equals(TextInterpreter.TXT_NOT_GONE_INDICATOR)) {
                        endText = "";
                    }
                    titleList.set(position, startText + endText);
                    for (int i = 0; i < paraList.size(); ++i) {
                        if (GeoAnimator.ultimateIsNull(paraList.get(i))) {
                            paraList.remove(i);
                            break;
                        } else if (paraList.get(i).equals(paraList.get(position))) {
                            break;
                        }
                    }
                    for (int i = 0; i < contentList.size(); ++i) {
                        if (GeoAnimator.ultimateIsNull(contentList.get(i))) {
                            contentList.remove(i);
                            break;
                        } else if (contentList.get(i).equals(contentList.get(position))) {
                            break;
                        }
                    }
                    reinstantiateEditAdapter();
                    ContentPagerActivity.increaseAdCount(getActivity());
                }
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_REPLACE_MAPS_LINK) {
                holdLat = data.getDoubleExtra(MapDataResultActivity.EXTRA_LATITUDE, 0);
                holdLong = data.getDoubleExtra(MapDataResultActivity.EXTRA_LONGITUDE, 0);
                holdZoom = data.getFloatExtra(MapDataResultActivity.EXTRA_ZOOM, 0);
                holdTilt = data.getFloatExtra(MapDataResultActivity.EXTRA_TILT, 0);
                holdBear = data.getFloatExtra(MapDataResultActivity.EXTRA_BEARING, 0);

                FragmentManager managerContent = getActivity().getSupportFragmentManager();
                BasicTextInputDialogFragment dialogCancel = BasicTextInputDialogFragment.newInstance(
                        getString(R.string.replace_link), getString(R.string.what_should_link_title),
                        getString(R.string.linktitlehere), getString(R.string.ok), getString(R.string.cancel), "", 0, -1
                );
                dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_REPLACE_TITLE_MAPS_LINK);
                dialogCancel.show(managerContent, DIALOG_REPLACE_TITLE_MAPS_LINK);
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_REPLACE_TITLE_MAPS_LINK) {
                if (currentTitlePosition != -1) {
                    String title = data.getStringExtra(BasicTextInputDialogFragment.EXTRA_TEXT);
                    GMapsLink mapsLink = new GMapsLink(title, holdLat, holdLong, holdZoom, holdTilt, holdBear);
                    mapsList.set(currentTitlePosition, mapsLink.toString());
                    reinstantiateEditAdapter();
                    ContentPagerActivity.increaseAdCount(getActivity());
                }
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);
                SharedPreferences sp = getActivity().getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
                currentIndexOf = sp.getInt(CURRENT_INDEX, -1);
                currentTitlePosition = sp.getInt(CURRENT_TITLE_POSITION, -1);
                mCurrentPhotoPath = sp.getString(CURRENT_FILE_PATH, null);

                if (currentIndexOf == -1 || currentTitlePosition == -1 || GeoAnimator.ultimateIsNull(mCurrentPhotoPath)) {
                    GeoAnimator.makeDurationToast(getActivity(), getString(R.string.therewasanerror), Gravity.CENTER, 0, 0, 1000);

                    isTextSwitch = false;
                    switchButton.callOnClick();
                } else {
                    LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(currentTitlePosition);
                    EditText editText = (EditText)
                            layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                    String text = editText.getText().toString();
                    String endText = text.substring(currentIndexOf);
                    String startText = getUptilIndex(text, currentIndexOf);

                    if (startText.equals("")) {
                        startText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                    }
                    if (endText.equals("")) {
                        endText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                    }

                    titleList.add(currentTitlePosition, startText);
                    paraList.add(currentTitlePosition, mCurrentPhotoPath + TextInterpreter.CAMERA_INDICATOR);
                    contentList.add(currentTitlePosition, "");
                    mapsList.add(currentTitlePosition, "");

                    try {
                        titleList.set(currentTitlePosition + 1, endText);
                    } catch (IndexOutOfBoundsException ioe) {
                        titleList.add(endText);
                    }

                    for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                        LinearLayout linlayout = (LinearLayout) contentRecyclerView.getChildAt(i);
                        EditText linEditText = (EditText)
                                linlayout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                        linEditText.setOnFocusChangeListener(null);
                    }
                    reinstantiateEditAdapter();
                    ContentPagerActivity.increaseAdCount(getActivity());

                    isTextSwitch = false;
                    switchButton.callOnClick();
                }
            }
        } else if (resultCode == Activity.RESULT_FIRST_USER) {
            if (requestCode == REQUEST_CHANGE_IMAGE) {
                int position = data.getIntExtra(ThreeButtonDialogFragment.EXTRA_POSITION, -1);
                if (position != -1) {
                    Intent intent;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    } else {
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                    }
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.pickimage_colon)),
                            REQUEST_REPLACE_GALLERY_IMAGE);
                    currentTitlePosition = position;
                }
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_GMAPS) {
                String filePath = data.getStringExtra(MapDataResultActivity.EXTRA_FILE_PATH);
                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(currentTitlePosition);
                EditText editText = (EditText)
                        layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                String text = editText.getText().toString();
                String endText = text.substring(currentIndexOf);
                String startText = getUptilIndex(text, currentIndexOf);

                if (startText.equals("")) {
                    startText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                }
                if (endText.equals("")) {
                    endText = TextInterpreter.TXT_NOT_GONE_INDICATOR;
                }

                titleList.add(currentTitlePosition, startText);
                paraList.add(currentTitlePosition, filePath);
                contentList.add(currentTitlePosition, "");
                mapsList.add(currentTitlePosition, "");

                try {
                    titleList.set(currentTitlePosition + 1, endText);
                } catch (IndexOutOfBoundsException ioe) {
                    titleList.add(endText);
                }

                for (int i = 0; i < contentRecyclerView.getChildCount(); ++i) {
                    LinearLayout linlayout = (LinearLayout) contentRecyclerView.getChildAt(i);
                    EditText linEditText = (EditText)
                            linlayout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                    linEditText.setOnFocusChangeListener(null);
                }
                reinstantiateEditAdapter();
                ContentPagerActivity.increaseAdCount(getActivity());
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_CHANGE_CONTENT_LINK) {
                CardInfoLab lab = CardInfoLab.get(getActivity());
                List<CardInfoHolder> holderList = lab.getCardInfos();
                ArrayList<String> categoryList = new ArrayList<>();
                for (int i = 0; i < holderList.size(); ++i) {
                    categoryList.add(holderList.get(i).getTitle());
                }

                SubcategoryCardInfoLab subLab = SubcategoryCardInfoLab.get(getActivity());
                ArrayList<ArrayList<String>> subcatList = new ArrayList<>();
                for (int i = 0; i < categoryList.size(); ++i) {
                    List<SubcategoryCardInfoHolder> list =
                            subLab.getCategoryCardInfos(categoryList.get(i).toLowerCase());
                    ArrayList<String> stringlist = new ArrayList<>();
                    for (int ii = 0; ii < list.size(); ++ii) {
                        stringlist.add(list.get(ii).getTitle());
                    }
                    subcatList.add(stringlist);
                }

                FragmentManager managerContent = getActivity().getSupportFragmentManager();
                ContentLinkDialogFragment dialogCancel = ContentLinkDialogFragment.newInstance(
                        getString(R.string.changecontentlink_colon),
                        getString(R.string.whatshouldlinkbecalled), getString(R.string.linktitlehere),
                        getString(R.string.picksubcatlinkgoesto_colon), getString(R.string.thecategorygoeshere),
                        getString(R.string.link_warning),
                        getString(R.string.cancel), getString(R.string.ok), categoryList, subcatList
                );
                dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_REPLACE_CONTENT_LINK);
                dialogCancel.show(managerContent, DIALOG_REPLACE_CONTENT);
                isTextSwitch = false;
                switchButton.callOnClick();
            } else if (requestCode == REQUEST_CHANGE_MAPS_LINK) {
                Intent intent = new Intent(this.getActivity(), MapDataResultActivity.class);
                intent.putExtra(MapDataResultActivity.ARG_TITLE, getString(R.string.setyourmap_colon));
                GMapsLink mapsLink = convertToGMapsLink(holdMapsString);
                intent.putExtra(MapDataResultActivity.ARG_LATITUDE, mapsLink.latitude * 1D);
                intent.putExtra(MapDataResultActivity.ARG_LONGITUDE, mapsLink.longitude * 1D);
                intent.putExtra(MapDataResultActivity.ARG_ZOOM, mapsLink.zoom *1F);
                intent.putExtra(MapDataResultActivity.ARG_TILT, mapsLink.tilt * 1F);
                intent.putExtra(MapDataResultActivity.ARG_BEARING, mapsLink.bearing * 1F);
                startActivityForResult(intent, REQUEST_REPLACE_MAPS_LINK);
                isTextSwitch = false;
                switchButton.callOnClick();
            }
        }
    }

    public void reinstantiateEditAdapter() {
        contentRecyclerView.setAdapter(null);
        contentRecyclerView.setLayoutManager(null);
        mEditAdapter.setLists(titleList, paraList, contentList, mapsList);
        contentRecyclerView.setAdapter(mEditAdapter);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEditAdapter.notifyDataSetChanged();
        linkHolder.setVisibility(View.VISIBLE);
        imageMode = false;
        contentMode = false;
        gmapsMode = false;
        cameraMode = false;
        updateMode = true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }



    public boolean isInUpdateMode() {
        return updateMode;
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new TitleParaAdapter(titleList, paraList, contentList, mapsList);
            contentRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTitleAndParagraphLists(titleList, paraList, contentList, mapsList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class TitleParaHolder extends RecyclerView.ViewHolder {
        int mPosition;

        private TextView mTextView;
        private ImageView mImageView;
        private CardView mCardView;
        private Button mCardTextView;
        private CardView mMapsCardView;
        private TextView mMapsButton;
        private CardView mImageCardView;
        private ImageButton mImageButton;
        private CloseImageEvent mEvent;

        public TitleParaHolder(View itemView) {
            super(itemView);

            mTextView = (TextView)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_views_title_text_view);
            mImageView = (ImageView)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_views_contents_text_image_view);
            mCardView = (CardView)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_views_contents_card_view);
            mCardTextView = (Button)
                    itemView.findViewById(R.id.contents_holder_ms_txt_views_contents_content_text_view);
            mMapsCardView = (CardView)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_view__map_view_text);
            mMapsButton = (TextView)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_view__map_view_btn);
            mImageCardView = (CardView)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_view_ecardview);
            mImageButton = (ImageButton)
                    itemView.findViewById(R.id.layout_contents_holder_open_button);
        }

        public void resetImageButton(final String imagePath) {
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEvent.setIsOpened(true);
                    boolean dOrNah;
                    Log.e("asdf", imagePath);
                    if (imagePath.endsWith(TextInterpreter.CAMERA_INDICATOR)) {
                        File imageFile = new File(imagePath.replace(TextInterpreter.CAMERA_INDICATOR, ""));
                        if (imageFile.exists()) {
                            mImageView.setImageBitmap(GeoAnimator.rotateBitmapWithCondition(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), 270));
                        } else {
                            mImageView.setImageResource(R.drawable.ic_action_tutorial);
                        }
                        dOrNah = false;
                    } else {
                        Uri imageUri = Uri.parse(imagePath);
                        try {
                            mImageView.setImageBitmap(GeoAnimator.rotateBitmapWithCondition(BitmapOperations.scaleDownBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri),
                                    Math.round(SCREEN_WIDTH / 2.5F), Math.round(SCREEN_HEIGHT / 7.5F)), 90));
                        } catch (Error | IOException | SecurityException e) {
                            e.printStackTrace();
                            mImageView.setImageResource(R.drawable.ic_action_tutorial);
                        }
                        dOrNah = true;
                    }

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        int iHeight = mImageView.getDrawable().getIntrinsicHeight();
                        mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, iHeight));
                    }
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            Uri uri;
                            if (imagePath.endsWith(TextInterpreter.CAMERA_INDICATOR)) {
                                uri = Uri.fromFile(new File(imagePath.replace(TextInterpreter.CAMERA_INDICATOR, "")));
                            } else {
                                uri = Uri.parse(imagePath);
                            }
                            intent.setDataAndType(uri, "image/*");
                            startActivity(intent);
                        }
                    });

                    mImageButton.setImageResource(R.drawable.ic_action_name2);

                    mImageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEvent.setIsOpened(false);
                            mImageButton.setImageResource(R.drawable.ic_action_name);
                            collapse(mImageView);
                            resetImageButton(imagePath);
                        }
                    });

                    mEvent.setOnCloseImageListener(new CloseImageListener() {
                        @Override
                        public void onCloseImage() {
                            Log.e(String.valueOf(mEvent.getOrder()), "yes garll!!");
                            mEvent.setIsOpened(false);
                            mImageButton.setImageResource(R.drawable.ic_action_name);
                            collapse(mImageView);
                            resetImageButton(imagePath);
                        }
                    });

                    activateCIListeners(mEvent.getOrder(), mImageView, mImageCardView, dOrNah);
                }
            });
        }

        public void bindCardInfo(String title, final String imgPath,
                                 String contentString, String mapsString,
                                 int position, boolean bitmapIsNull) {
            if (mEvent == null) {
                mEvent = new CloseImageEvent(eventList.size());
                eventList.add(mEvent);
            }
            mPosition = position;
            if (title.equals(TextInterpreter.TXT_NOT_GONE_INDICATOR) ||
                    title.equals("")) {
                mTextView.setVisibility(View.GONE);
            } else {
                mTextView.setText(title);
            }

            if (bitmapIsNull || GeoAnimator.ultimateIsNull(imgPath)) {
                mImageCardView.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
            } else {
                mImageView.setVisibility(View.VISIBLE);
                resetImageButton(imgPath);
            }

            if (GeoAnimator.ultimateIsNull(contentString)) {
                mCardView.setVisibility(View.GONE);
                mCardTextView.setVisibility(View.GONE);
            } else {
                final ContentLink contentLink = convertToContentLink(contentString);
                mCardTextView.setText(contentLink.title);
                mCardTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallbacks.setPagerList(contentLink.category, contentLink.subcategory);
                    }
                });
            }

            if (GeoAnimator.ultimateIsNull(mapsString)) {
                mMapsCardView.setVisibility(View.GONE);
                mMapsButton.setVisibility(View.GONE);
            } else {
                final GMapsLink mapsLink = convertToGMapsLink(mapsString);
                mMapsButton.setText(mapsLink.title);
                mMapsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MapViewer.class);
                        intent.putExtra(MapViewer.ARG_LATITUDE, mapsLink.latitude * 1D);
                        intent.putExtra(MapViewer.ARG_LONGITUDE, mapsLink.longitude * 1D);
                        intent.putExtra(MapViewer.ARG_ZOOM, mapsLink.zoom * 1F);
                        intent.putExtra(MapViewer.ARG_TILT, mapsLink.tilt * 1F);
                        intent.putExtra(MapViewer.ARG_BEARING, mapsLink.bearing * 1F);
                        intent.putExtra(MapViewer.ARG_TITLE, mapsLink.title);
                        startActivity(intent);
                    }
                });
            }
        }

    }



    public void collapse(final ImageView v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.getLayoutParams().height = 0;
                    v.setImageResource(R.drawable.download);
                    v.setOnClickListener(null);
                    v.setVisibility(View.GONE);
                    v.requestLayout();
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density)*2);
        v.startAnimation(a);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                v.clearAnimation();
                Log.e("asdf", "its everyday bro");
                v.getLayoutParams().height = 0;
                v.setImageResource(R.drawable.download);
                v.setOnClickListener(null);
                v.setVisibility(View.GONE);
                v.requestLayout();
            }
        }, a.getDuration());
    }

    public static void expand(final View v, boolean dblOrNah) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 30;
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
        int duration = (int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density);
        a.setDuration(dblOrNah ? duration*2 : duration);
        v.startAnimation(a);
    }

    private class FooterHolder extends RecyclerView.ViewHolder {

        private Button previousButton;
        private Button nextButton;

        public FooterHolder(View itemView) {
            super(itemView);

            previousButton = (Button) itemView.findViewById(R.id.info_subcat_contents_card_holder_previous_button);
            nextButton = (Button) itemView.findViewById(R.id.info_subcat_contents_card_holder_next_button);

            final List<SubcategoryCardInfoHolder> cardList = SubcategoryCardInfoLab.get(getActivity()).getCategoryCardInfos(currentCategory);
            int previousValue = 0;
            if (currentIndex == 0) {
                previousValue = cardList.size() - 1;
                previousButton.setText("<<" + cardList.get(previousValue).getTitle());
            } else {
                previousValue = currentIndex - 1;
                previousButton.setText("<<" + cardList.get(previousValue).getTitle());
            }
            final int finalPreviousValue = previousValue;
            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.movePager(finalPreviousValue, true);
                }
            });

            int nextValue = 0;
            if (currentIndex == (cardList.size() - 1)) {
                nextValue = 0;
                nextButton.setText(cardList.get(0).getTitle() + ">>");
            } else {
                nextValue = currentIndex + 1;
                nextButton.setText(cardList.get(nextValue).getTitle() + ">>");
            }
            final int finalNextValue = nextValue;
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.movePager(finalNextValue, true);
                }
            });
        }
    }



    private class TitleParaEditHolder extends RecyclerView.ViewHolder {
        private EditText mTitleEditText;
        private ImageView mImageView;
        private CardView mContentCardView;
        private Button mContentButton;
        private CardView mMapsCardView;
        private TextView mMapsButton;
        private CardView mImageCardView;
        private ImageButton mImageButton;

        public TitleParaEditHolder(View itemView) {
            super(itemView);

            mTitleEditText = (EditText)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
            mImageView = (ImageView)
                    itemView.findViewById(R.id.contents_Holder_m_s_txt_views_contents_edit_image_view);
            mContentCardView = (CardView)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_views_edit_card_view);
            mContentButton = (Button)
                    itemView.findViewById(R.id.contents_holder_ms_txt_views_edit_contents_content_text_view);
            mMapsCardView = (CardView)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_view_edit_map_view);
            mMapsButton = (TextView)
                    itemView.findViewById(R.id.contents_holder_m_s_txt_view_edit_map_view_btn);
            mImageCardView = (CardView)
                    itemView.findViewById(R.id.contents_Holder_m_s_txt_views_contents_edit_cimage_view);
            mImageButton = (ImageButton)
                    itemView.findViewById(R.id.contents_Holder_m_s_txt_views_contents_edit_image_button);
        }

        public void bindCardInfo(String title, final String imagePath, String contentString,
                                 final String mapsString,
                                 final int position, boolean bitmapIsNull) {

            if (bitmapIsNull || GeoAnimator.ultimateIsNull(imagePath)) {
                mImageCardView.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
            } else {
                mImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Resources res = getResources();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        ImageDisplayDialogFragment dialogCancel = ImageDisplayDialogFragment.newInstance(
                                getString(R.string.image), getString(R.string.removereplace_image),
                                res.getString(R.string.cancel), res.getString(R.string.replace), res.getString(R.string.remove),
                                position, imagePath);
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_CHANGE_IMAGE);
                        dialogCancel.show(manager, DIALOG_CHANGE_IMAGE);
                    }
                });
            }

            mTitleEditText.setText(title.equals(TextInterpreter.TXT_NOT_GONE_INDICATOR) ? "" : title);
            mTitleEditText.setEnabled(true);
            mTitleEditText.setSingleLine(false);
            mTitleEditText.setHint(getString(R.string.ellipsis));
            mTitleEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            mTitleEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null && imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            });
            mTitleEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (updateMode && !imageMode && !gmapsMode && !cameraMode && !contentMode) {
                        Intent cIntent = BasicTextInputActivity.newIntent(
                                getActivity(), getString(R.string.change_text_colon),
                                getString(R.string.ellipsis),
                                mTitleEditText.getText().toString(), mTitleEditText.getSelectionStart(), position
                        );

                        startActivityForResult(cIntent, REQUEST_TEXT_INPUT);
                        closeEditTexts();
                    }
                    return true;
                }
            });
            mTitleEditText.setTextIsSelectable(true);

            if (GeoAnimator.ultimateIsNull(contentString)) {
                mContentCardView.setVisibility(View.GONE);
                mContentButton.setVisibility(View.GONE);
            } else {
                ContentLink contentLink = convertToContentLink(contentString);
                mContentButton.setText(contentLink.title);
                mContentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Resources res = getResources();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        ThreeButtonDialogFragment dialogCancel = ThreeButtonDialogFragment.newInstance(
                                getString(R.string.contentlink), getString(R.string.removereplace_link),
                                res.getString(R.string.cancel), res.getString(R.string.replace), res.getString(R.string.remove),
                                position);
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_CHANGE_CONTENT_LINK);
                        dialogCancel.show(manager, DIALOG_CHANGE_CONTENT);
                    }
                });
            }


            if (GeoAnimator.ultimateIsNull(mapsString)) {
                mMapsCardView.setVisibility(View.GONE);
                mMapsButton.setVisibility(View.GONE);
            } else {
                final GMapsLink mapsLink = convertToGMapsLink(mapsString);
                mMapsButton.setText(mapsLink.title);
                mMapsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Resources res = getResources();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        ThreeButtonDialogFragment dialogCancel = ThreeButtonDialogFragment.newInstance(
                                getString(R.string.gmapslink),
                                getString(R.string.removereplace_link),
                                res.getString(R.string.cancel), res.getString(R.string.replace), res.getString(R.string.remove),
                                position);
                        holdMapsString = mapsString;
                        dialogCancel.setTargetFragment(SubcategoryContentFragment.this, REQUEST_CHANGE_MAPS_LINK);
                        dialogCancel.show(manager, DIALOG_CHANGE_MAPS_LINK);
                    }
                });
            }
        }
    }

    private class TitleParaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<String> mTitleInfos;
        private List<String> mImagePaths;
        private List<String> mContentLinks;
        private List<String> mMapLinks;

        private static final int FOOTER_VIEW = 67;

        public TitleParaAdapter(List<String> titleList, List<String> imagePaths, List<String> contentLinks,
                                List<String> mapLinks) {
            mTitleInfos = titleList;
            mTitleInfos.add("");
            mImagePaths = imagePaths;
            mContentLinks = contentLinks;
            mMapLinks = mapLinks;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (viewType == FOOTER_VIEW) {
                View view = layoutInflater.inflate(R.layout.leftrightbuttons, parent, false);
                return new FooterHolder(view);
            } else {
                View view = layoutInflater.inflate(R.layout.contents_holder_middle_small_txt_views, parent, false);
                return new TitleParaHolder(view);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mTitleInfos.size()-1) {
                return FOOTER_VIEW;
            }

            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            try {
                if (holder instanceof TitleParaHolder) {
                    TitleParaHolder vh = (TitleParaHolder) holder;

                    String title = mTitleInfos.get(position);
                    String imagePath;
                    String content = mContentLinks.get(position);
                    String maps = mMapLinks.get(position);
                    if (mImagePaths.size() != 0) {
                        imagePath =  mImagePaths.get(position);
                        if (imagePath == null) {
                            vh.bindCardInfo(title, null, content, maps, position, true);
                        } else {
                            vh.bindCardInfo(title, imagePath, content, maps, position, false);
                        }
                    } else {
                        vh.bindCardInfo(title, null, content, maps, position, true);
                    }
                } else if (holder instanceof FooterHolder) {
                    FooterHolder vh = (FooterHolder) holder;
                }
            } catch (Exception e) {
                e.printStackTrace();
                GeoAnimator.makeDurationToast(getActivity(), "There was an error.", Gravity.CENTER, 0, 0, 2000);
            }

        }

        @Override
        public int getItemCount() {
            return mTitleInfos.size();
        }

        public void setTitleAndParagraphLists(List<String> newTitleList, List<String> newImagePaths,
                                              List<String> newContentLinks, List<String> newMapLinks) {
            mTitleInfos.clear();
            mImagePaths.clear();
            mContentLinks.clear();
            mMapLinks.clear();
            mTitleInfos = newTitleList;
            mTitleInfos.add("");
            mImagePaths = newImagePaths;
            mContentLinks = newContentLinks;
            mMapLinks = newMapLinks;
        }
    }

    private class TitleParaEditAdapter extends RecyclerView.Adapter<TitleParaEditHolder> {

        private ArrayList<String> mTitleInfos;
        private ArrayList<String> mImagePaths;
        private ArrayList<String> mContentLinks;
        private ArrayList<String> mMapLinks;

        public TitleParaEditAdapter(ArrayList<String> titleList, ArrayList<String> imagePaths, ArrayList<String> contentLinks,
                                    ArrayList<String> mapLinks) {
            mTitleInfos = titleList;
            mImagePaths = imagePaths;
            mContentLinks = contentLinks;
            mMapLinks = mapLinks;
        }

        @Override
        public TitleParaEditHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.contents_holder_middle_small_edt_txt_views, parent, false);
            return new TitleParaEditHolder(view);
        }

        @Override
        public void onBindViewHolder(TitleParaEditHolder holder, int position) {
            String title = mTitleInfos.get(position);
            String content = mContentLinks.get(position);
            String maps = mMapLinks.get(position);
            if (mImagePaths.size() != 0) {
                String imagePath =  mImagePaths.get(position);
                if (GeoAnimator.ultimateIsNull(imagePath)) {
                    holder.bindCardInfo(title, null, content, maps, position, true);
                } else {
                    holder.bindCardInfo(title, imagePath, content, maps, position, false);
                }
            } else {
                holder.bindCardInfo(title, null, content, maps, position, true);
            }
        }

        @Override
        public int getItemCount() {
            return mTitleInfos.size();
        }

        public void setLists(ArrayList<String> newTitleList, ArrayList<String> newImagePaths, ArrayList<String> newContentLinks,
                             ArrayList<String> newMapLinks) {
            mTitleInfos = newTitleList;
            mImagePaths = newImagePaths;
            mContentLinks = newContentLinks;
            mMapLinks = newMapLinks;
        }
    }

    public void setTextsToEditViews() {
        buttonHolder.setVisibility(View.GONE);
        switchButton.setVisibility(View.VISIBLE);
        editOptionsHolder.setVisibility(View.VISIBLE);
        if (mEditAdapter == null) {
            mEditAdapter = new TitleParaEditAdapter(titleList, paraList, contentList, mapsList);
        } else {
            mEditAdapter.setLists(titleList, paraList, contentList, mapsList);
        }
        contentRecyclerView.setAdapter(mEditAdapter);
    }

    public void resetToNormalAdapter() {
        contentRecyclerView.setAdapter(mAdapter);
        linkHolder.setVisibility(View.VISIBLE);
        setTitleAndParaLists();
        buttonHolder.setVisibility(View.VISIBLE);
        editOptionsHolder.setVisibility(View.GONE);
        if (mAdapter == null) {
            mAdapter = new TitleParaAdapter(titleList, paraList, contentList, mapsList);
            contentRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTitleAndParagraphLists(titleList, paraList, contentList, mapsList);
            mAdapter.notifyDataSetChanged();
        }
        updateMode = false;
        imageMode = false;
        gmapsMode = false;
        contentMode = false;
        cameraMode = false;
        getActivity().invalidateOptionsMenu();
    }

    public void setTitleAndParaLists() {
        StringArrayHolder stringPair = TextInterpreter.interpretContentCardContents(getNotesOrContents());
        titleList = stringPair.getFirstArray();
        paraList = stringPair.getSecondArray();
        contentList = stringPair.getThirdArray();
        mapsList = stringPair.getFourthArray();
    }

    public void setHolderTitleAndParaLists() {
        holdTitleList = titleList;
        holdParaList = paraList;
        holdContentList = contentList;
        holdMapList = mapsList;
    }

    public String getNotesOrContents() {
        if (getIsNotes() != null) {
            if (getIsNotes()) {
                return cardInfo.getNotes();
            } else {
                return cardInfo.getContents();
            }
        } else {
            return null;
        }
    }

    @Override
    public void closeEditTexts() {
        if (this.isInUpdateMode()) {
            for (int i = contentRecyclerView.getChildCount() - 1; i >= 0; --i) {
                LinearLayout layout = (LinearLayout) contentRecyclerView.getChildAt(i);
                EditText titleEditText = (EditText) layout.findViewById(R.id.contents_holder_m_s_txt_views_title_edit_text);
                titleEditText.clearFocus();
            }
        }
    }

    public class ContentLink {
        public String title;
        public String category;
        public String subcategory;

        public ContentLink(String Title, String Category, String Subcategory) {
            this.title = Title;
            this.category = Category;
            this.subcategory = Subcategory;
        }

        public String toString() {
            String returnString = CONTENT_TITLE_INDICATOR;
            returnString += title;
            returnString += CONTENT_CATEGORY_INDICATOR;
            returnString += category;
            returnString += CONTENT_SUBCAT_INDICATOR;
            returnString += subcategory;
            return returnString;
        }
    }

    public class GMapsLink {
        public String title;
        public double latitude;
        public double longitude;
        public float zoom;
        public float tilt;
        public float bearing;

        public GMapsLink(String Title, double Latitude, double Longitude, float Zoom, float Tilt, float Bearing) {
            this.title = Title;
            this.latitude = Latitude;
            this.longitude = Longitude;
            this.zoom = Zoom;
            this.tilt = Tilt;
            this.bearing = Bearing;
        }

        public String toString() {
            String returnString = GMAPS_LATITUDE;
            returnString += String.valueOf(latitude);
            returnString += GMAPS_LONGITUDE;
            returnString += String.valueOf(longitude);
            returnString += GMAPS_ZOOM;
            returnString += String.valueOf(zoom);
            returnString += GMAPS_TILT;
            returnString += String.valueOf(tilt);
            returnString += GMAPS_BEARING;
            returnString += String.valueOf(bearing);
            returnString += GMAPS_TITLE;
            returnString += title;
            return returnString;
        }
    }

    public ContentLink convertToContentLink(String string) {
        String changeStr = string.replace(CONTENT_TITLE_INDICATOR, "");
        String title = changeStr.substring(0, changeStr.indexOf(CONTENT_CATEGORY_INDICATOR));
        changeStr = changeStr.replace((title + CONTENT_CATEGORY_INDICATOR), "");
        String category = changeStr.substring(0, changeStr.indexOf(CONTENT_SUBCAT_INDICATOR));
        changeStr = changeStr.replace((category + CONTENT_SUBCAT_INDICATOR), "");
        String subcategory = changeStr;
        return new ContentLink(title, category, subcategory);
    }

    public GMapsLink convertToGMapsLink(String string) {
        String changeStr = string.replace(GMAPS_LATITUDE, "");
        String latStr = changeStr.substring(0, changeStr.indexOf(GMAPS_LONGITUDE));
        double latitude = Double.valueOf(latStr);
        changeStr = changeStr.replace((latStr + GMAPS_LONGITUDE), "");
        String longStr = changeStr.substring(0, changeStr.indexOf(GMAPS_ZOOM));
        double longitude = Double.valueOf(longStr);
        changeStr = changeStr.replace((longStr + GMAPS_ZOOM), "");
        String zoomStr = changeStr.substring(0, changeStr.indexOf(GMAPS_TILT));
        float zoom = Float.valueOf(zoomStr);
        changeStr = changeStr.replace((zoomStr + GMAPS_TILT), "");
        String tiltStr = changeStr.substring(0, changeStr.indexOf(GMAPS_BEARING));
        float tilt = Float.valueOf(tiltStr);
        changeStr = changeStr.replace((tiltStr + GMAPS_BEARING), "");
        String bearStr = changeStr.substring(0, changeStr.indexOf(GMAPS_TITLE));
        float bearing = Float.valueOf(bearStr);
        changeStr = changeStr.replace((bearing + GMAPS_TITLE), "");
        String title = changeStr;
        return new GMapsLink(title, latitude, longitude, zoom, tilt, bearing);
    }
}



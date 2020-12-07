package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContentPagerActivity extends AppCompatActivity
        implements SubcategoryContentFragment.Callbacks, NavigationView.OnNavigationItemSelectedListener, AdInterface, FinishActivity {
    private static final String EXTRA_CONTENT_ID = "com.ropkastudios.android.thegeographyapp.content_id";
    private static final String EXTRA_NOTES = "com.ropkastudios.android.thegeographyapp.content_notes";

    public static final String TITLE_LIST = "title_list";
    public static final String PARA_LIST = "para_list";
    public static final String GMAPS_LIST = "gmaps_list";
    public static final String CONT_LIST = "cont_list";
    public static final String EDIT_MODE = "edit_mode";

    public static final int REQUEST_YOUTUBE = 93;
    public static final String DIALOG_NONE = "dialog_none";

    private ViewPager mViewPager;
    private List<SubcategoryCardInfoHolder> mSubCardInfos;
    private String pagerCategory;
    private ArrayList<Bundle> savedInstanceStateList;
    boolean isNotes;

    private int adCount;
    private short pagerCount;

    private SubcategoryContentFragment currentFragment;
    private SubcategoryContentFragment newFragment;

    private InterstitialAd interstitialAd;

    IInAppBillingService mService;
    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    private boolean justStarted;

    public static Intent newIntent(Context packageContext, UUID cardId, boolean notes) {
        Intent intent = new Intent(packageContext, ContentPagerActivity.class);
        intent.putExtra(EXTRA_CONTENT_ID, cardId);
        intent.putExtra(EXTRA_NOTES, notes);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_content_pager);

        MobileAds.initialize(this, getString(R.string.app_admob_id));
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.admob_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        SharedPreferences sp = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
        if (sp == null) {
            adCount = 1;
        } else {
            adCount = sp.getInt(MainMenuActivity.AD_COUNT, 0);
            incrementAdCount();
        }

        if (sp != null && !sp.getBoolean(MainMenuActivity.PAID_FOR_AD, false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(MainMenuActivity.PAID_FOR_AD, false);
            editor.apply();

            Intent serviceIntent =
                    new Intent("com.android.vending.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("com.android.vending");
            bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        }

        isNotes = getIntent().getBooleanExtra(EXTRA_NOTES, true);
        getSharedPreferences(MainMenuActivity.PREFS_NAME, 0).edit().putBoolean(SubcategoryContentFragment.IS_NOTES, isNotes).apply();

        RandomValuesLab randValLab = RandomValuesLab.get(this);
        List<RandomValuesHolder> randomValuesList = randValLab.getRandomValues();
        UUID randomUUID;
        if (randomValuesList.size() != 0) {
            randomUUID = randomValuesList.get(0).getUUID();
        } else if (randomValuesList.size() == 0) {
            randomUUID = UUID.randomUUID();
            randValLab.addRandomValue(new RandomValuesHolder(randomUUID, 0, true, null));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pager);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_pager);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_pager);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            ImageView imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_image_view);
            int value = navigationView.getHeaderView(0).findViewById(R.id.headerview).getLayoutParams().height;
            imageView.getLayoutParams().height = (5*value)/12;
            imageView.getLayoutParams().width = (5*value)/12;
            imageView.requestLayout();

            Button howToUseButton = (Button) navigationView.getHeaderView(0).findViewById(R.id.nav_header_howtouse);
            howToUseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(YoutubeActivity.newIntent(ContentPagerActivity.this), REQUEST_YOUTUBE);

                }
            });
        }

        UUID subcategoryId = (UUID) getIntent().getSerializableExtra(EXTRA_CONTENT_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_content_pager_view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                incrementPagerCount();
                setTitle(mSubCardInfos.get(position).getTitle());
                AccFragPagerAdapter adapter = (AccFragPagerAdapter) mViewPager.getAdapter();

                if (justStarted) {
                    currentFragment = (SubcategoryContentFragment)
                            adapter.instantiateItem(mViewPager, position);
                    justStarted = false;
                }

                newFragment = (SubcategoryContentFragment)
                        adapter.instantiateItem(mViewPager, position);

                if (newFragment != null) {

                    FragmentLifecycleInterface fragInterface = newFragment;
                    fragInterface.closeEditTexts();
                    fragInterface = null;
                }

                if (currentFragment != null) {
                    FragmentLifecycleInterface currentInterface = currentFragment;
                    currentInterface.closeEditTexts();
                    currentInterface = null;
                }

                final InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);

                currentFragment = newFragment;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SubcategoryCardInfoLab lab = SubcategoryCardInfoLab.get(this);
        pagerCategory = lab.getSpecificCardInfo(subcategoryId).getCategory();
        mSubCardInfos = lab.getCategoryCardInfos(pagerCategory);
        FragmentManager fragmentManager = getSupportFragmentManager();
        savedInstanceStateList = new ArrayList<>();
        for (int i = 0; i < mSubCardInfos.size(); ++i) {
            savedInstanceStateList.add(null);
        }
        mViewPager.setAdapter(new AccFragPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                SubcategoryCardInfoHolder cardInfo = mSubCardInfos.get(position);
                return SubcategoryContentFragment.newInstance(cardInfo.getId(), isNotes);
            }

            @Override
            public int getCount() {
                return mSubCardInfos.size();
            }
        });

        for (short i = 0; i < mSubCardInfos.size(); ++i) {
            if (mSubCardInfos.get(i).getId().equals(subcategoryId)) {
                mViewPager.setCurrentItem(i, true);
                setTitle(mSubCardInfos.get(i).getTitle());
                break;
            }
        }
    }

    public class AccFragPagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public AccFragPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return registeredFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return SubcategoryContentFragment.newInstance(null, true);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public SubcategoryContentFragment getRegisteredFragment(int position) {
            return (SubcategoryContentFragment) registeredFragments.get(position);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainMenuActivity.REQUEST_BUY_NO_ADS) {
            if (resultCode == RESULT_OK) {
                SharedPreferences sp = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
                sp.edit().putBoolean(MainMenuActivity.PAID_FOR_AD, true).apply();
                GeoAnimator.makeDurationToast(this, getString(R.string.purchasesuccessful), Gravity.CENTER, 0, 0, 1000);
                FragmentManager manager = getSupportFragmentManager();
                BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                        getString(R.string.thanks_for_purchasing), getString(R.string.thanks_for_purchasing_desc),
                        getString(R.string.cool), "",false);
                dialogCancel.show(manager, DIALOG_NONE);
            } else {
                GeoAnimator.makeDurationToast(this, getString(R.string.purchaseunsuccessful), Gravity.CENTER, 0, 0, 1000);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void incrementPagerCount() {
        pagerCount += 1;
        if (pagerCount >= 3) {
            incrementAdCount();
            pagerCount = 0;
        }
    }

    @Override
    public void incrementAdCount() {
        adCount += 1;
        if (adCount >= 5) {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
                adCount = 0;
            }
        }
    }

    public static void increaseAdCount(Activity mContext) {
        if (!mContext.getSharedPreferences(MainMenuActivity.PREFS_NAME, 0).getBoolean(MainMenuActivity.PAID_FOR_AD, true)) {
            AdInterface adInterface = (AdInterface) mContext;
            adInterface.incrementAdCount();
        }
    }

    @Override
    public void setPagerList(String category, String subcategory) {
        SubcategoryCardInfoLab lab = SubcategoryCardInfoLab.get(this);
        pagerCategory = category.toLowerCase();
        mSubCardInfos = lab.getCategoryCardInfos(pagerCategory);
        savedInstanceStateList = new ArrayList<>();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new AccFragPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                SubcategoryCardInfoHolder cardInfo = mSubCardInfos.get(position);
                return SubcategoryContentFragment.newInstance(cardInfo.getId(), isNotes);
            }

            @Override
            public int getCount() {
                return mSubCardInfos.size();
            }
        });

        for (short i = 0; i < mSubCardInfos.size(); ++i) {
            if (mSubCardInfos.get(i).getTitle().equals(subcategory)) {
                mViewPager.setCurrentItem(i, true);
                setTitle(mSubCardInfos.get(i).getTitle());
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_pager);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!((AccFragPagerAdapter) mViewPager.getAdapter()).getRegisteredFragment(mViewPager.getCurrentItem()).onBackPressed()) {
            Intent intent = MainMenuActivity.newIntent(this, MainMenuActivity.INFORMATION_SUBCATEGORY, pagerCategory);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_information) {
            Intent intent = MainMenuActivity.newIntent(this, MainMenuActivity.INFORMATION, null);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_spec_info) {
            Intent intent = MainMenuActivity.newIntent(this, MainMenuActivity.SPECIFIC_INFO, null);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_nat_geo_news) {
            Intent intent = MainMenuActivity.newIntent(this, MainMenuActivity.NAT_GEO_NEWS, null);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_share) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "ropkaenterprises@gmail.com", null));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else if (id == R.id.nav_send) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ropkastudios.android.thegeographyapp")));
        } else if (id == R.id.nav_help) {
            Intent intent = MainMenuActivity.newIntent(this, MainMenuActivity.HELP, null);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_purchase) {
            if (!getSharedPreferences(MainMenuActivity.PREFS_NAME, 0).getBoolean(MainMenuActivity.PAID_FOR_AD, true)) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mService == null) {throw new RemoteException();}
                            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                                    "geographyfirstnoadvertisements", "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");

                            if (buyIntentBundle.getInt("RESPONSE_CODE", 1) == 0) {
                                PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

                                if (pendingIntent == null) throw new RemoteException();

                                startIntentSenderForResult(pendingIntent.getIntentSender(),
                                        MainMenuActivity.REQUEST_BUY_NO_ADS, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                                        Integer.valueOf(0));
                            } else {
                                throw new RemoteException();
                            }
                        } catch (RemoteException | IntentSender.SendIntentException e) {
                            errorText();
                        }
                    }
                });
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_pager);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void errorText() {
        GeoAnimator.makeDurationToast(this, getString(R.string.errorstartingpurchase), Gravity.CENTER, 0, 0, 1000);
    }

    @Override
    public void movePager(int index, boolean smoothScroll) {
        incrementPagerCount();
        mViewPager.setCurrentItem(index, smoothScroll);
    }

    @Override
    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public void closeKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
    }

    @Override
    public void finishActivity() {
        SharedPreferences sp = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(MainMenuActivity.AD_COUNT, adCount);
        editor.apply();

        finish();
    }

    @Override
    public void onDestroy() {
        if (mService != null) {
            unbindService(mServiceConn);
        }
        super.onDestroy();
    }
}

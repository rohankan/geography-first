package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InformationSubcategoryFragment.Callbacks, AdInterface {
    public static String EXTRA_FRAGMENT_NAME = "com.ropkastudios.android.thegeographyapp.fragment_name";
    public static String EXTRA_CATEGORY = "com.ropkastudios.android.thegeographyapp.subcat_category";
    public static final String PREFS_NAME = "MyPrefsFile1";
    public static final String AD_COUNT = "MyPrefsFile1AdCount";
    public static final String SKIP_OR_NOT = "skipson";
    public static final String PAID_FOR_AD = "paidforad";
    public static final String FIRST_TIME_APP_OPENED = "1stappopened";

    public static String INFORMATION = "com.ropkastudios.android.thegeographyapp.information";
    public static String INFORMATION_SUBCATEGORY = "com.ropkastudious.android.thegeographyapp.sub_category";
    public static String SPECIFIC_INFO = "com.ropkastudios.android.thegeographyapp.specific_info";
    public static String QUIZ_FLASHCARDS = "com.ropkastudios.android.thegeographyapp.quizzes_flashcards";
    public static String NOTES = "com.ropkastudios.android.thegeographyapp.access_notes";
    public static String NAT_GEO_NEWS = "com.ropkastudios.android.thegeographyapp.national_geographic_news";
    public static String HELP = "com.ropkastudios.android.thegeographyapp.help";

    public static final int REQUEST_YOUTUBE = 91;
    public static final int REQUEST_BUY_NO_ADS = 55;
    public static final String DIALOG_NONE = "dialog_none_ads";

    InterstitialAd interstitialAd;
    int adCount;

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

    public static final int FRAGMENT_CONTAINER = R.id.fragment_contain;

    public static Intent newIntent(Context packageContext, String fragmentName, String category) {
        Intent intent = new Intent(packageContext, MainMenuActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName);
        intent.putExtra(EXTRA_CATEGORY, category);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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

        SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
        if (sp == null) {
            adCount = 0;
        } else {
            adCount = sp.getInt(AD_COUNT, 0);
            if (adCount != 5) {
                increaseAdCount(this);
            }
        }

        if (sp != null && !sp.getBoolean(PAID_FOR_AD, false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(PAID_FOR_AD, false);
            editor.apply();

            Intent serviceIntent =
                    new Intent("com.android.vending.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("com.android.vending");
            bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(FRAGMENT_CONTAINER);

        String fragmentString = getIntent().getStringExtra(EXTRA_FRAGMENT_NAME);
        String category = getIntent().getStringExtra(EXTRA_CATEGORY);

        if (sp != null) {
            if (fragmentString == null) {
                sp.edit().putBoolean(FIRST_TIME_APP_OPENED, true).apply();
            } else {
                sp.edit().putBoolean(FIRST_TIME_APP_OPENED, false).apply();
            }
        }

        if (fragment == null) {
            if (fragmentString != null && fragmentString.equals(QUIZ_FLASHCARDS)) {
            } else {
                if (fragmentString == null || fragmentString.equals(NAT_GEO_NEWS)) {
                    fragment = NatGeoFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putBoolean("Creation", fragmentString == null);
                    fragment.setArguments(args);
                    setTitle(getString(R.string.curev));
                } else if (fragmentString.equals(INFORMATION_SUBCATEGORY)) {
                    fragment = InformationSubcategoryFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putString("InformationCategoryFragment", category);
                    fragment.setArguments(args);
                } else if (fragmentString.equals(SPECIFIC_INFO)) {
                    fragment = SavedWebsiteFragment.newInstance();
                    setTitle(getString(R.string.saved_articles));
                } else if (fragmentString.equals(NOTES)) {
                    fragment = HelpFragment.newInstance();
                    setTitle(getString(R.string.help));
                } else if (fragmentString.equals(INFORMATION)) {
                    fragment = InformationFragment.newInstance();
                    setTitle(getString(R.string.information));
                } else if (fragmentString.equals(HELP)) {
                    fragment = HelpFragment.newInstance();
                    setTitle(getString(R.string.help));
                }

                fm.beginTransaction()
                        .add(FRAGMENT_CONTAINER, fragment)
                        .commit();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            if (sp != null && sp.getBoolean(PAID_FOR_AD, false)) {
                navigationView.getMenu().findItem(R.id.nav_purchase).setVisible(false);
            }
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
                    startActivityForResult(YoutubeActivity.newIntent(MainMenuActivity.this), REQUEST_YOUTUBE);
                }
            });
        }
    }

    @Override
    public void incrementAdCount() {
        adCount += 1;
        if (adCount >= 4) {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
                adCount = 0;
            }
        }
    }

    public static void increaseAdCount(Activity mContext) {
        if (!mContext.getSharedPreferences(PREFS_NAME, 0).getBoolean(PAID_FOR_AD, true)) {
            AdInterface adInterface = (AdInterface) mContext;
            adInterface.incrementAdCount();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!((BackPressedFragment) getSupportFragmentManager().findFragmentById(FRAGMENT_CONTAINER)).onBackPressed()){
            super.onBackPressed();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Resources resources = getResources();
        boolean checker = false;
        FragmentManager fm = getSupportFragmentManager();

        if (id == R.id.nav_information) {

            try {
                InformationFragment checkerFragment = (InformationFragment) fm.findFragmentById(FRAGMENT_CONTAINER);
                checker = true;
            } catch (ClassCastException cce) {
                checker = false;
            }
            if (!checker) {
                InformationFragment fragment = InformationFragment.newInstance();
                replaceFragmentInActivity(FRAGMENT_CONTAINER, fragment, fm, resources.getString(R.string.information));
            }
        } else if (id == R.id.nav_spec_info) {

            try {
                SavedWebsiteFragment checkerFragment = (SavedWebsiteFragment) fm.findFragmentById(FRAGMENT_CONTAINER);
                checker = true;
            } catch (ClassCastException cce) {
                checker = false;
            }
            if (!checker) {
                SavedWebsiteFragment fragment = SavedWebsiteFragment.newInstance();
                replaceFragmentInActivity(FRAGMENT_CONTAINER, fragment, fm, resources.getString(R.string.specific_info));
            }
        } else if (id == R.id.nav_nat_geo_news) {

            try {
                NatGeoFragment checkerFragment = (NatGeoFragment) fm.findFragmentById(FRAGMENT_CONTAINER);
                checker = true;
            } catch (ClassCastException cce) {
                checker = false;
            }
            if (!checker) {
                NatGeoFragment fragment = NatGeoFragment.newInstance();
                replaceFragmentInActivity(FRAGMENT_CONTAINER, fragment, fm, resources.getString(R.string.national_geographic_news));
            }

        } else if (id == R.id.nav_share) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "ropkaenterprises@gmail.com", null));
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_ellipsis)));
        } else if (id == R.id.nav_send) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ropkastudios.android.thegeographyapp")));
        } else if (id == R.id.nav_help) {
            try {
                HelpFragment checkerFragment = (HelpFragment) fm.findFragmentById(FRAGMENT_CONTAINER);
                checker = true;
            } catch (ClassCastException cce) {
                checker = false;
            }
            if (!checker) {
                HelpFragment fragment = HelpFragment.newInstance();
                replaceFragmentInActivity(FRAGMENT_CONTAINER, fragment, fm, resources.getString(R.string.help));
            }
        } else if (id == R.id.nav_purchase) {

            if (!getSharedPreferences(PREFS_NAME, 0).getBoolean(PAID_FOR_AD, true)) {
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
                                        REQUEST_BUY_NO_ADS, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                                        Integer.valueOf(0));
                            } else {
                                throw new RemoteException();
                            }
                        } catch (RemoteException | IntentSender.SendIntentException e) {
                            Looper.prepare();
                            errorText();
                        }
                    }
                });
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void errorText() {
        GeoAnimator.makeDurationToast(this, getString(R.string.errorstartingpurchase), Gravity.CENTER, 0, 0, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BUY_NO_ADS) {
            if (resultCode == RESULT_OK) {
                SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
                sp.edit().putBoolean(PAID_FOR_AD, true).apply();
                GeoAnimator.makeDurationToast(this, getString(R.string.purchasesuccessful), Gravity.CENTER, 0, 0, 1000);
                FragmentManager manager = getSupportFragmentManager();
                BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                        getString(R.string.thanks_for_purchasing), getString(R.string.thanks_for_purchasing_desc),
                        getString(R.string.cool), "", false);
                dialogCancel.show(manager, DIALOG_NONE);
            } else {
                GeoAnimator.makeDurationToast(this, getString(R.string.purchaseunsuccessful), Gravity.CENTER, 0, 0, 1000);
            }
        } else if (requestCode == REQUEST_YOUTUBE && resultCode == RESULT_OK) {
            if (data.getBooleanExtra(YoutubeActivity.EXTRA_TOASTY, true)) {
                GeoAnimator.makeDurationToast(this, getString(R.string.errorvideo), Gravity.CENTER, 0, 0, 1000);
            }
        }
    }

    public void replaceFragmentInActivity(int fragId, Fragment fragment, FragmentManager fragManager, String title) {
        fragManager.beginTransaction()
                .replace(fragId, fragment)
                .commit();
        setTitle(title);
        increaseAdCount(this);
    }

    public void replaceFragmentInActivityWithString(int fragId, Fragment fragment, FragmentManager fragManager, String title,
                                                   String[] serializable, String[] argTags) {
        Bundle args = new Bundle();
        for (int i = 0; i < serializable.length; ++i) {
            args.putString(argTags[i], serializable[i]);
        }

        setTitle(title);

        fragment.setArguments(args);
        fragManager.beginTransaction().replace(fragId, fragment).commit();
    }

    @Override
    public void finishActivity() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(AD_COUNT, adCount);
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

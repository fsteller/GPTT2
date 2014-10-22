package com.credomatic.grpod.ecommerce.android.gptt.testingtool;


import android.app.ActionBar;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.AppGPTT;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.IActivity;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.IFragmentService;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.FragmentServiceHeader;

public class activityGPTT extends FragmentActivity implements IActivity {

    private static final String TAG = activityGPTT.class.getSimpleName();
    //<editor-fold desc="Variables">

    private AppGPTT application = null;
    private IFragmentService mainFragment = null;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private String[] mServicesTitles;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gptt);

        mTitle = mDrawerTitle = getTitle();
        mServicesTitles = getResources().getStringArray(R.array.services);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mServicesTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        final ActionBar ab = getActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                          /* host Activity */
                mDrawerLayout,                 /* DrawerLayout object */
                R.drawable.ic_drawer,          /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gtt, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        final boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        final boolean isServiceRunning = application.isHttpServiceRunning();

        updateHttpServerItem(menu.findItem(R.id.action_service), isServiceRunning);

        // If the nav drawer is open, hide action items related to the content view
        menu.findItem(R.id.action_service).setVisible(!drawerOpen);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        menu.findItem(R.id.action_restoreDefaultValues).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // The action bar index/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_websearch:

                // create intent to perform web search for this planet
                final CharSequence title = getActionBar() != null ? getActionBar().getTitle() : "GPTT";
                final Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, title);

                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_service:
                httpServiceAction(item);
                return true;
            case R.id.action_restoreDefaultValues:
                if (mainFragment != null) {
                    mainFragment.restoreDefaultValues();
                    mainFragment.reloadValues();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(final CharSequence title) {
        mTitle = title;
        if (getActionBar() != null)
            getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void mapNewServiceFragment(final IFragmentService fragmentService) {
        mainFragment = fragmentService;
    }

    @Override
    protected void onResume() {
        super.onResume();
        application = (AppGPTT) getApplication();
        registerReceiver(new HttpResponseBroadcastReceiver(),
                new IntentFilter(HttpResponseBroadcastReceiver.BrowserRedirectBroadcast));
    }

    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private void selectItem(final int position) {
        // update the main content by replacing fragments
        final Bundle args = new Bundle();
        final Fragment fragment = new FragmentServiceHeader();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        args.putInt(IFragmentService.ARG_SERVICE_ID, position);
        fragment.setArguments(args);

        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        setTitle(mServicesTitles[position]);
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void httpServiceAction(final MenuItem item) {
        final boolean isServiceRunning = application.isHttpServiceRunning();
        updateHttpServerItem(item, !isServiceRunning);

        if (isServiceRunning) {
            application.stopHttpServer();
        } else {
            application.startHttpServer();
            Log.i(TAG, "Http Server started ( " + application.getHttpServerAddress() + " )");
        }
    }

    private void updateHttpServerItem(final MenuItem item, final boolean isServiceRunning) {
        //updates itemView icon and text
        item.setTitle(isServiceRunning ? R.string.action_stopService :
                R.string.action_startService);
        item.setIcon(isServiceRunning ? android.R.drawable.ic_media_pause :
                android.R.drawable.ic_media_play);
    }

    //</editor-fold>

    //<editor-fold desc="Inner Classes">

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(final AdapterView parent, final View view, final int position, final long id) {
            selectItem(position);
        }
    }

    public class HttpResponseBroadcastReceiver extends BroadcastReceiver {

        public static final String HttpResponseText = "HttpResponseText";
        public static final String BrowserRedirectBroadcast = "com.credomatic.gprod.ecommerce.android.browser_redirect";

        @Override
        public void onReceive(final Context context, final Intent intent) {
            //final String action = intent.getAction();
            final String data = intent.getStringExtra(HttpResponseText);
            Toast.makeText(context, data, Toast.LENGTH_LONG).show();
        }
    }

    //</editor-fold>
}

package com.example.wolf.testseries;



import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wolf.testseries.AdapterController.NavDrawerListAdapter;
import com.example.wolf.testseries.CentralController.GlobalController;
import com.example.wolf.testseries.CentralController.KeyboardHideController;
import com.example.wolf.testseries.Controller.TestController;
import com.example.wolf.testseries.Controller.TimeModeController;
import com.example.wolf.testseries.fragmentController.About_Us;
import com.example.wolf.testseries.fragmentController.Category_Option;
import com.example.wolf.testseries.fragmentController.ContactUsController;
import com.example.wolf.testseries.fragmentController.Contact_Us;
import com.example.wolf.testseries.fragmentController.Help;
import com.example.wolf.testseries.fragmentController.Payment_Option;
import com.example.wolf.testseries.fragmentController.FreeFoam_Fragment;
import com.example.wolf.testseries.fragmentController.Learn_Fragment;
import com.example.wolf.testseries.fragmentController.PagesFragment;
import com.example.wolf.testseries.fragmentController.SubjectSelector;
import com.example.wolf.testseries.fragmentController.Time_Fragment;
import com.example.wolf.testseries.fragmentController.WhatsHotFragment;
import com.example.wolf.testseries.modelController.NavDrawerItem;

public class NavigationController extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    public  final static int LEARN_MODE=0;
    public final static int TIME_MODE=1;
    public final static int FREE_FORM=2;
    public final static int PAYMENT_OPTION=3;
    public final static int CONTACT_US=4;
    public final static int ABOUT_US=5;
    public final static int HELP=6;

    public final static int LOG_OUT_OPTION=7;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_controller);

        int displayOption=extractDisplayOption(savedInstanceState);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1), true, "22"));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "22"));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        // What's hot, We  will add a counter here



        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(displayOption);
        }
        new KeyboardHideController(this, findViewById(R.id.drawer_layout));
    }

    private int extractDisplayOption(Bundle savedInstanceState)
    {
        int displayOption;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                displayOption= 0;
            } else {
                displayOption= extras.getInt("displayOption");
            }
        } else {
            displayOption= (int) savedInstanceState.getSerializable("displayOption");
        }
        return displayOption;
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            //TODO
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position)
    {
        stopTimer();
        Log.d("navigation", "navigated to --> "+position);
        // update the main content by replacing fragments
        Fragment fragment=null;
        switch (position) {
            case NavigationController.FREE_FORM:
                fragment = Category_Option.newInstance(FREE_FORM);
                break;
            case NavigationController.TIME_MODE:
                fragment = Category_Option.newInstance(TIME_MODE);
                break;
            case NavigationController.LEARN_MODE:
                fragment =Category_Option.newInstance(LEARN_MODE);
                break;
            case NavigationController.PAYMENT_OPTION:
                fragment = SubjectSelector.newInstance(PAYMENT_OPTION);
                break;

            case NavigationController.CONTACT_US:
                fragment = new ContactUsController();
                break;


            case NavigationController.ABOUT_US:
                fragment = new About_Us();
                break;


            case NavigationController.HELP:
                fragment = new Help();
                break;

            case NavigationController.LOG_OUT_OPTION:
                logOut();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void stopTimer()
    {
        CountDownTimer countDownTimer=TimeModeController.countDownTimer;
        if(countDownTimer==null)
        {
            return;
        }
        countDownTimer.cancel();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed(){
        if(absorbForBackNavigationOnResultPage())
        {
            return;
        }
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else
        {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    private Boolean absorbForBackNavigationOnResultPage()
    {
        if(getActiveFragmentTag()==null)
        {
            return false;
        }
        if(getActiveFragmentTag().equals(TestController.RESULT_PAGE_TAG) || getActiveFragmentTag().equals(TestController.QUESTION_COMPLETION_TAG))
        {
            getFragmentManager().popBackStack(Learn_Fragment.Test_INFO_TAG, 0);
            return true;
        }
        return false;
    }



    private String getActiveFragmentTag() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
        return tag;
    }

    private void logOut()
    {
        clearGenIdFromSharedPreferences();
        Intent intent=new Intent(NavigationController.this,  LoginController.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void clearGenIdFromSharedPreferences()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(GlobalController.credentialsFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove("objectId");
        editor.commit();
    }

}

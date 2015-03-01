package com.example.zhangyu.foodtruck;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class MainActivity extends Activity {

    // Declaring our tabs and the corresponding fragments.
    ActionBar.Tab eventsTab, vendorsTap;
    Fragment eventsFragmentTab = new EventsFragmentTab();
    Fragment vendorsFragmentTab = new VendorsFragmentTab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asking for the default ActionBar element that our platform supports.
        ActionBar actionBar = getActionBar();

        // Screen handling while hiding ActionBar icon.
        actionBar.setDisplayShowHomeEnabled(false);

        // Screen handling while hiding Actionbar title.
        actionBar.setDisplayShowTitleEnabled(false);

        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Setting custom tab icons.
        eventsTab = actionBar.newTab().setIcon(R.drawable.food6);
        vendorsTap = actionBar.newTab().setIcon(R.drawable.history);

        // Setting tab listeners.
        eventsTab.setTabListener(new TabListener(eventsFragmentTab));
        vendorsTap.setTabListener(new TabListener(vendorsFragmentTab));

        // Adding tabs to the ActionBar.
        actionBar.addTab(eventsTab);
        actionBar.addTab(vendorsTap);

    }
}


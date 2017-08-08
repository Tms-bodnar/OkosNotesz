package okosnotesz.hu.okosnotesz;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import okosnotesz.hu.okosnotesz.fragments.AdminExpertsFragment;
import okosnotesz.hu.okosnotesz.fragments.AdminGuestsFragment;
import okosnotesz.hu.okosnotesz.fragments.AdminProductsFragment;
import okosnotesz.hu.okosnotesz.fragments.AdminTreatmentsFragment;
import okosnotesz.hu.okosnotesz.fragments.BookingsFragment;
import okosnotesz.hu.okosnotesz.fragments.SalesFragment;
import okosnotesz.hu.okosnotesz.model.DBHelper;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private boolean mainActivity;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    SharedPreferences sp;
    private SQLiteDatabase db;
    private DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = DBHelper.getHelper(this);
        db = helper.getWritableDatabase();

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean firstStart = sp.getBoolean(getString(R.string.first_start), false);
        if (!firstStart) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean(getString(R.string.first_start), true);
                edit.commit();
                firstStart();
            }
        } else {
            mainActivityStart();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean(getString(R.string.first_start), true);
                edit.commit();
                firstStart();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void mainActivityStart() {
        final SectionsPagerAdapter mSectionsPagerAdapterMain;
        mainActivity = true;
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.calendar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.calendar_focus));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.commercial_pale));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.report_pale));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapterMain = new SectionsPagerAdapter(getSupportFragmentManager(), 3);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapterMain);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                toolbar.setTitle(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.calendar_focus);
                        toolbar.setTitle(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                        break;
                    case 1:
                        tab.setIcon(R.drawable.commercial_focus);
                        toolbar.setTitle(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                        break;
                    case 2:
                        tab.setIcon(R.drawable.report_focus);
                        toolbar.setTitle(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.calendar_pale);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.commercial_pale);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.report_pale);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void firstStart() {
        final SectionsPagerAdapter mSectionsPagerAdapterFirstStart;
        mainActivity = false;
        setContentView(R.layout.first_settings);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_first_settings);
        toolbar.setTitle(R.string.guests);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_first_settings);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.guests_focus));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.expert_pale));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.treatment_pale));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.commercial_pale));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        mSectionsPagerAdapterFirstStart = new SectionsPagerAdapter(getSupportFragmentManager(), 4);

        mViewPager = (ViewPager) findViewById(R.id.containerFirstSettings);
        mViewPager.setAdapter(mSectionsPagerAdapterFirstStart);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                toolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.guests_focus);
                        toolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                    case 1:
                        tab.setIcon(R.drawable.expert_focus);
                        toolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                    case 2:
                        tab.setIcon(R.drawable.treatment_focus);
                        toolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                    case 3:
                        tab.setIcon(R.drawable.commercial_focus);
                        toolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.guests_pale);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.expert_pale);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.treatment_pale);
                        break;
                    case 3:
                        tab.setIcon(R.drawable.commercial_pale);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mainActivity) {
            mainActivityStart();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View alertVIew = inflater.inflate(R.layout.admin_ok_dialog, null);
            TextView tv = (TextView) alertVIew.findViewById(R.id.adminOkTextView);
            tv.setText(R.string.exitAlert);
            builder.setView(alertVIew);
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog ad = builder.create();
            ad.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            firstStart();
        }
        if (id == R.id.action_main_activity) {
            mainActivityStart();
        }
        return super.onOptionsItemSelected(item);
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        int numOfTabs;

        public SectionsPagerAdapter(FragmentManager supportFragmentManager, int i) {
            super(supportFragmentManager);
            this.numOfTabs = i;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(numOfTabs) {
                case 4:
                    switch (position) {
                        case 0:
                            fragment = new AdminGuestsFragment();
                            break;
                        case 1:
                            fragment = new AdminExpertsFragment();
                            break;
                        case 2:
                            fragment = new AdminTreatmentsFragment();
                            break;
                        case 3:
                            fragment = new AdminProductsFragment();
                            break;
                    }
                    break;
                case 3:
                    switch (position) {
                        case 0:
                            fragment = new BookingsFragment();
                            break;
                        case 1:
                            fragment = new SalesFragment();
                            break;
                        case 2:
                            fragment = new BookingsFragment();
                            break;
                    }
                    break;
                default:
                    fragment = new BookingsFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {

            return numOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence pageTitle = "";
            switch (numOfTabs) {
                case 4:
                    switch (position) {
                        case 0:
                            pageTitle = getString(R.string.guests);
                            break;
                        case 1:
                            pageTitle = getString(R.string.expert);
                            break;
                        case 2:
                            pageTitle = getString(R.string.treatment);
                            break;
                        case 3:
                            pageTitle = getString(R.string.goods);
                            break;
                    }
                    break;
                case 3:
                    switch (position) {
                        case 0:
                            pageTitle = getString(R.string.calendar);
                            break;
                        case 1:
                            pageTitle = getString(R.string.commercial);
                            break;
                        case 2:
                            pageTitle = getString(R.string.reports);
                            break;
                    }
                    break;
            }
            return pageTitle;
        }
    }
}
//public static class PlaceholderFragment1 extends Fragment{
//    private static final String ARG_SECTION_NUMBER = "section_number";
//
//    public PlaceholderFragment1() {
//    }
//
//    public static PlaceholderFragment1 newInstance(int sectionNumber){
//        PlaceholderFragment1 fragment = new PlaceholderFragment1();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        View rootView = inflater.inflate(R.layout.first_settings, container, false);
//        return rootView;
//    }
//}
/*

    *//**
 * A placeholder fragment containing a simple view.
 *//*
    public static class PlaceholderFragment extends Fragment {
        *//**
 * The fragment argument representing the section number for this
 * fragment.
 *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        *//**
 * Returns a new instance of this fragment for the given section
 * number.
 *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER, 1);
            switch (sectionNumber){
                case 1:
                    rootView = inflater.inflate(R.layout.bookings, container, false);
                    TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    textView.setText(getString(R.string.booking) + " " + getArguments().getInt(ARG_SECTION_NUMBER));
            }
            return rootView;
        }
    }

    *//**
 * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 *//*
    public class SectionsPagerAdapterMain extends FragmentStatePagerAdapter {

        int numOfTabs;

        public SectionsPagerAdapterMain(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs=numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            numOfTabs = 3;
            return numOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
                CharSequence pageTitle = "";
            switch (position) {
                case 0:
                    pageTitle= getString(R.string.calendar);
                    break;
                case 1:
                    pageTitle= getString(R.string.commercial);
                    break;
                case 2:
                    pageTitle= getString(R.string.reports);
                    break;
            }
            return pageTitle;
        }
    }*/

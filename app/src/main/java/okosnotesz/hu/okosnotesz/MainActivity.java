package okosnotesz.hu.okosnotesz;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import okosnotesz.hu.okosnotesz.adapters.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    private boolean mainActivity;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ViewPager mViewPager;
    SharedPreferences sp;
    Context mContext;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle barDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
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

    private void setnavigationDrawer(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home_calendar:
                        mainActivityStart();
                        break;
                    case R.id.nav_commercial:
                        comActivityStart();
                        break;
                    case R.id.nav_settings:
                        firstStart();
                        break;
                }
                return false;
            }
        });
    }

    private void comActivityStart(){
        PagerAdapter mSectionsPagerAdapterSales;
        mainActivity = false;
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setnavigationDrawer();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        barDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.welcome,R.string.cancel);
        barDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
        mSectionsPagerAdapterSales = new PagerAdapter(mContext, getSupportFragmentManager(), 2);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapterSales);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                tab.setText(mSectionsPagerAdapterSales.getPageTitle(tab.getPosition()));
                switch (tab.getPosition()) {
                    case 1:
                        //  tab.setIcon(R.drawable.calendar_focus);
                        tab.setText(mSectionsPagerAdapterSales.getPageTitle(tab.getPosition()));
                        break;
                    case 0:
                        //tab.setIcon(R.drawable.commercial_focus);
                        tab.setText(mSectionsPagerAdapterSales.getPageTitle(tab.getPosition()));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        // tab.setIcon(R.drawable.calendar_pale);
                        break;
                    case 0:
                        //tab.setIcon(R.drawable.commercial_pale);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.setCurrentItem(0);
    }


    private void mainActivityStart() {
        final PagerAdapter mSectionsPagerAdapterMain;
        mainActivity = true;
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setnavigationDrawer();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        barDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.welcome,R.string.cancel);
        barDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        mSectionsPagerAdapterMain = new PagerAdapter(mContext, getSupportFragmentManager(), 3);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapterMain);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                tab.setText(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                switch (tab.getPosition()) {
                    case 1:
                      //  tab.setIcon(R.drawable.calendar_focus);
                        tab.setText(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                        break;
                    case 0:
                        //tab.setIcon(R.drawable.commercial_focus);
                        tab.setText(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                        break;
                    case 2:
                        //tab.setIcon(R.drawable.report_focus);
                        tab.setText(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                       // tab.setIcon(R.drawable.calendar_pale);
                        break;
                    case 0:
                        //tab.setIcon(R.drawable.commercial_pale);
                        break;
                    case 2:
                        //tab.setIcon(R.drawable.report_pale);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.setCurrentItem(1);
    }



    public void firstStart() {
        final PagerAdapter mSectionsPagerAdapterFirstStart;
        mainActivity = false;
        setContentView(R.layout.first_settings);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setnavigationDrawer();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_first_settings);
        barDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.welcome,R.string.cancel);
        barDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_first_settings);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        mSectionsPagerAdapterFirstStart = new PagerAdapter(mContext, getSupportFragmentManager(), 4);

        mViewPager = (ViewPager) findViewById(R.id.containerFirstSettings);
        mViewPager.setAdapter(mSectionsPagerAdapterFirstStart);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                tab.setText(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                switch (tab.getPosition()) {
                    case 0:

                        tab.setText(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                    case 1:

                        tab.setText(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                    case 2:
                        tab.setText(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                    case 3:
                        tab.setText(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:
                    break;
                    case 3:
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(barDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
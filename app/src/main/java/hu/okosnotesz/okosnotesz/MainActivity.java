package hu.okosnotesz.okosnotesz;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import hu.okosnotesz.okosnotesz.adapters.PagerAdapter;
import hu.okosnotesz.okosnotesz.fragments.CalendarFragment;

public class MainActivity extends AppCompatActivity implements MonthItemClickListener {

    private boolean mainActivity;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ViewPager mViewPager;
    SharedPreferences sp;
    Context mContext;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle barDrawerToggle;
    PagerAdapter weekAdapter;
    Toolbar mToolbar;
    private final int ITEM_NUM = 49/2;
    public int  clickedPosition = 24;
    public static FragmentManager mSupportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mSupportFragmentManager = getSupportFragmentManager();
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

            mainActivityStart(ITEM_NUM);
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
                        mainActivityStart(ITEM_NUM);
                        break;
                    case R.id.nav_commercial:
                        comActivityStart();
                        break;
                    case R.id.nav_reports:
                        reportStart();
                        break;
                    case R.id.nav_settings:
                        firstStart();
                        break;
                    case R.id.nav_about_us:
                        webStart();
                        break;
                }
                return false;
            }
        });
    }

    private void reportStart() {
        PagerAdapter mSectionsPagerAdapterSales;
        mainActivity = false;
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setnavigationDrawer();
        Button btn = (Button) findViewById(R.id.tollbar_button);
        btn.setVisibility(View.INVISIBLE);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        barDrawerToggle = new ActionBarDrawerToggle(this, drawer,mToolbar,R.string.welcome,R.string.cancel);
        barDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        mToolbar.setTitle(R.string.reports);
        mSectionsPagerAdapterSales = new PagerAdapter(mContext, getSupportFragmentManager(), 1, 1);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapterSales);
        mViewPager.setOffscreenPageLimit(1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.report_focus));
        mViewPager.setCurrentItem(0);
    }

    private void webStart() {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        startActivity(intent);

    }

    private void comActivityStart(){
        PagerAdapter mSectionsPagerAdapterSales;
        mainActivity = false;
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Button btn = (Button) findViewById(R.id.tollbar_button);
        btn.setVisibility(View.INVISIBLE);
        setnavigationDrawer();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        barDrawerToggle = new ActionBarDrawerToggle(this, drawer,mToolbar,R.string.welcome,R.string.cancel);
        barDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        mToolbar.setTitle(R.string.commercial);
        mSectionsPagerAdapterSales = new PagerAdapter(mContext, getSupportFragmentManager(), 1, 2);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapterSales);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(0);
    }

    public void mainActivityStart(int itemNum) {
        final PagerAdapter mSectionsPagerAdapterMain;
        mainActivity = true;
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setnavigationDrawer();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        barDrawerToggle = new ActionBarDrawerToggle(this, drawer,mToolbar,R.string.welcome,R.string.cancel);
        barDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setSelectedTabIndicatorColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark, null));
        tabLayout.setVisibility(View.INVISIBLE);
        mSectionsPagerAdapterMain = new PagerAdapter(mContext, getSupportFragmentManager(), 49, 3);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapterMain);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(itemNum);
        mToolbar.setTitle(mSectionsPagerAdapterMain.getPageTitle(mViewPager.getCurrentItem()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mToolbar.setTitle(mSectionsPagerAdapterMain.getPageTitle(mViewPager.getCurrentItem()));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
       Button todayButton = (Button) findViewById(R.id.tollbar_button);
       todayButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mViewPager.getAdapter().getCount()==49) {
                   mViewPager.setCurrentItem(49/2,true);
               }if(mViewPager.getAdapter().getCount()==53){
                   Calendar today = Calendar.getInstance();
                   long todayInMillis = today.getTimeInMillis();
                   weekAdapter = new PagerAdapter(mContext,getSupportFragmentManager(), 53, todayInMillis, 5);
                   mViewPager.setAdapter(weekAdapter);
                   mViewPager.setCurrentItem(53/2,true);
                   mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                       @Override
                       public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                           mToolbar.setTitle(weekAdapter.getPageTitle(mViewPager.getCurrentItem()));
                       }
                       @Override
                       public void onPageSelected(int position) {
                       }
                       @Override
                       public void onPageScrollStateChanged(int state) {
                       }
                   });
               }
           }
       });
//        mViewPager.setCurrentItem();
    }



    public void firstStart() {
        final PagerAdapter mSectionsPagerAdapterFirstStart;
        mainActivity = false;
        setContentView(R.layout.first_settings);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setnavigationDrawer();
        mToolbar = (Toolbar) findViewById(R.id.toolbar_first_settings);
        mToolbar.setTitle(R.string.guests);
        barDrawerToggle = new ActionBarDrawerToggle(this, drawer,mToolbar,R.string.welcome,R.string.cancel);
        barDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_first_settings);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.guests_focus));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.expert_pale));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.treatment_pale));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.commercial_pale));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
        mSectionsPagerAdapterFirstStart = new PagerAdapter(mContext, getSupportFragmentManager(), 4,4);
        mViewPager = (ViewPager) findViewById(R.id.containerFirstSettings);
        mViewPager.setAdapter(mSectionsPagerAdapterFirstStart);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
       // tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                mToolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                //tab.setText(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.guests_focus);
                        mToolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                    case 1:
                        tab.setIcon(R.drawable.expert_focus);
                        mToolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                    case 2:
                        tab.setIcon(R.drawable.treatment_focus);
                        mToolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
                        break;
                    case 3:
                        tab.setIcon(R.drawable.commercial_focus);
                        mToolbar.setTitle(mSectionsPagerAdapterFirstStart.getPageTitle(tab.getPosition()));
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
            mainActivityStart(ITEM_NUM);
        }else if(mViewPager.getAdapter().getCount()==53){
            mainActivityStart(clickedPosition);
        }
        else {
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

    @Override
    protected void onResume() {
        super.onResume();
        }


    @Override
    public void onClick(int position, int i) {
        PagerAdapter adapter = (PagerAdapter) mViewPager.getAdapter();
        clickedPosition = mViewPager.getCurrentItem();
        CalendarFragment calFragment = (CalendarFragment) adapter.instantiateItem(mViewPager,mViewPager.getCurrentItem());
        long dateLong = calFragment.getDayValueInCells().get(position).get(i).getTimeInMillis();
        Log.d("clickmonthpager", "month onclick "+new Date(dateLong)+", pos: " + position + ", i: "+i);
        setViewPager(53, dateLong);
    }

    private void setViewPager(int i, long l) {
            mViewPager.setAdapter(new PagerAdapter(mContext, mSupportFragmentManager, i, l, 5));
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setCurrentItem(53/2);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    mToolbar.setTitle(mViewPager.getAdapter().getPageTitle(mViewPager.getCurrentItem()));
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }

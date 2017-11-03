package okosnotesz.hu.okosnotesz;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
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

    private void mainActivityStart() {
        final PagerAdapter mSectionsPagerAdapterMain;
        mainActivity = true;
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.commercial);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.commercial_pale));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.calendar_focus));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.report_pale));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        mSectionsPagerAdapterMain = new PagerAdapter(mContext, getSupportFragmentManager(), 3);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapterMain);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                toolbar.setTitle(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                switch (tab.getPosition()) {
                    case 1:
                        tab.setIcon(R.drawable.calendar_focus);
                        toolbar.setTitle(mSectionsPagerAdapterMain.getPageTitle(tab.getPosition()));
                        break;
                    case 0:
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
                    case 1:
                        tab.setIcon(R.drawable.calendar_pale);
                        break;
                    case 0:
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
        mViewPager.setCurrentItem(1);
    }


    public void firstStart() {
        final PagerAdapter mSectionsPagerAdapterFirstStart;
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

        mSectionsPagerAdapterFirstStart = new PagerAdapter(mContext, getSupportFragmentManager(), 4);

        mViewPager = (ViewPager) findViewById(R.id.containerFirstSettings);
        mViewPager.setAdapter(mSectionsPagerAdapterFirstStart);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
}
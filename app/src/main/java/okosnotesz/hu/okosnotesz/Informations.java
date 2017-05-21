package okosnotesz.hu.okosnotesz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by user on 2017.05.10..
 */

public class Informations extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informations);
        ViewPager infoViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);

        infoViewPager.setAdapter(new InfoAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(infoViewPager,true);

    }
}

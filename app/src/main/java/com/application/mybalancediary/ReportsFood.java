package com.application.mybalancediary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.application.mybalancediary.ui.watertracker.HistoryWaterFragment;
import com.application.mybalancediary.ui.watertracker.WaterFragment;
import com.application.mybalancediary.ui.watertracker.WaterTracker;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ReportsFood extends AppCompatActivity {

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_reports_food);

        setupViewpager(findViewById(R.id.viewpager));

        tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(findViewById(R.id.viewpager));
    }

    private void setupViewpager(ViewPager viewPager) {
        ViewpagerAdapter adapter = new  ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CaloriesHistory(), "Calories");
        adapter.addFragment(new MacrosHistory(), "Macros");
        viewPager.setAdapter(adapter);
    }

    public class ViewpagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewpagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

    }

}
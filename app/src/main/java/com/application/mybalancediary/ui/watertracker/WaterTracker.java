package com.application.mybalancediary.ui.watertracker;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.application.mybalancediary.HistoryWaterFragment;
import com.application.mybalancediary.R;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class WaterTracker extends Fragment {

    TabLayout tabLayout;
    private Handler mainhandler = new Handler();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_water_tracker, container, false);

        setupViewpager(root.findViewById(R.id.viewpager));

        tabLayout = root.findViewById(R.id.tab);
        tabLayout.setupWithViewPager(root.findViewById(R.id.viewpager));
        // setupIcons();
return root;
    }
    /*  private void setupIcons()
      {
          tabLayout.getTabAt(0).setIcon(R.drawable.ic_water_menu);
          tabLayout.getTabAt(1).setIcon(R.drawable.ic_settings_menu);
      }*/
    private void setupViewpager(ViewPager viewPager) {
        ViewpagerAdapter adapter = new ViewpagerAdapter (getChildFragmentManager());
        adapter.addFragment(new WaterFragment(), "Today");
        adapter.addFragment(new HistoryWaterFragment(), "History");
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

package net.openfiretechnologies.veloxcontrol.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.adapters.CustomPageAdapter;
import net.openfiretechnologies.veloxcontrol.fragments.appearance.Animations;
import net.openfiretechnologies.veloxcontrol.fragments.appearance.GeneralUI;
import net.openfiretechnologies.veloxcontrol.fragments.appearance.Lockscreen;
import net.openfiretechnologies.veloxcontrol.fragments.appearance.Performance;
import net.openfiretechnologies.veloxcontrol.fragments.appearance.Statusbar;

import java.util.ArrayList;
import java.util.List;

public class Appearance extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        View v = layoutInflater.inflate(R.layout.fragment_appearance, viewGroup, false);

        CustomPageAdapter mPageAdapter = new CustomPageAdapter(getChildFragmentManager(), getFragments(), getTitles());

        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.appearance_viewpager);
        mViewPager.setAdapter(mPageAdapter);

        PagerTabStrip mPagerTabStrip = (PagerTabStrip) v.findViewById(R.id.appearance_pagerTabStrip);
        mPagerTabStrip.setBackgroundColor(getResources().getColor(R.color.grey_dark));
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_dark));
        mPagerTabStrip.setDrawFullUnderline(true);

        return v;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();

        fList.add(new Animations());
        fList.add(new GeneralUI());
        fList.add(new Lockscreen());
        fList.add(new Statusbar());
        fList.add(new Performance());

        return fList;
    }

    private List<String> getTitles() {
        List<String> titleList = new ArrayList<>();

        titleList.add(getString(R.string.title_animation_controls));
        titleList.add(getString(R.string.title_ui));
        titleList.add(getString(R.string.alex_lockscreen_category_title));
        titleList.add(getString(R.string.status_bar_title));
        titleList.add(getString(R.string.alex_performance));

        return titleList;
    }
}

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
import net.openfiretechnologies.veloxcontrol.fragments.tasker.Optimization;
import net.openfiretechnologies.veloxcontrol.fragments.tasker.ScreenOff;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 05.11.13.
 */
public class Tasker extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        View v = layoutInflater.inflate(R.layout.fragment_tasker, viewGroup, false);

        CustomPageAdapter mPageAdapter = new CustomPageAdapter(getChildFragmentManager(), getFragments(), getTitles());

        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.tasker_viewpager);
        mViewPager.setAdapter(mPageAdapter);

        PagerTabStrip mPagerTabStrip = (PagerTabStrip) v.findViewById(R.id.tasker_pagerTabStrip);
        mPagerTabStrip.setBackgroundColor(getResources().getColor(R.color.grey_dark));
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_dark));
        mPagerTabStrip.setDrawFullUnderline(true);

        return v;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();

        fList.add(new ScreenOff());
        fList.add(new Optimization());

        return fList;
    }

    private List<String> getTitles() {
        List<String> titleList = new ArrayList<>();

        titleList.add(getString(R.string.tools_tasker_screenoff_title));
        titleList.add(getString(R.string.tools_tasker_optimization_title));

        return titleList;
    }
}

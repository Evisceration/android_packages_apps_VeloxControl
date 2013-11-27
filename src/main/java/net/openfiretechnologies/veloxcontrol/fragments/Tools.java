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
import net.openfiretechnologies.veloxcontrol.fragments.tools.ToolBackup;
import net.openfiretechnologies.veloxcontrol.fragments.tools.ToolFlasher;
import net.openfiretechnologies.veloxcontrol.fragments.tools.ToolFont;

import java.util.ArrayList;
import java.util.List;

public class Tools extends Fragment {

    private CustomPageAdapter mPageAdapter;
    private PagerTabStrip mPagerTabStrip;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        View v = layoutInflater.inflate(R.layout.fragment_tools, viewGroup, false);

        mPageAdapter = new CustomPageAdapter(getChildFragmentManager(), getFragments(), getTitles());

        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.tools_viewpager);
        mViewPager.setAdapter(mPageAdapter);

        mPagerTabStrip = (PagerTabStrip) v.findViewById(R.id.tools_pagerTabStrip);
        mPagerTabStrip.setBackgroundColor(getResources().getColor(R.color.grey_dark));
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_dark));
        mPagerTabStrip.setDrawFullUnderline(true);

        return v;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();

        fList.add(new ToolBackup());
        fList.add(new ToolFlasher());
        fList.add(new ToolFont());

        return fList;
    }

    private List<String> getTitles() {
        List<String> titleList = new ArrayList<>();

        titleList.add(getString(R.string.tools_backup_title));
        titleList.add(getString(R.string.tools_flasher_title));
        titleList.add(getString(R.string.tools_fonts_title));

        return titleList;
    }
}

package com.devspacenine.poolpal.widget;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.fragment.PoolTasksFragment;
import com.devspacenine.poolpal.fragment.TaskSettingsFragment;

/**
 * This is a helper class that implements the management of tabs and all
 * details of connecting a ViewPager with associated TabHost.  It relies on a
 * trick.  Normally a tab host has a simple API for supplying a View or
 * Intent that each tab will show.  This is not sufficient for switching
 * between pages.  So instead we make the content part of the tab host
 * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
 * view to show as the tab content.  It listens to changes in tabs, and takes
 * care of switch to the correct paged in the ViewPager whenever the selected
 * tab changes.
 */
public class ProfileTabsAdapter extends FragmentPagerAdapter
        implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	public static final String TAB_INFO = "info";
	public static final String TAB_TASKS = "tasks";
	public static final String TAB_CHARTS = "charts";
	public static final String TAB_GENERAL_MAINTENANCE = "general_maintenance";

    private final Context mContext;
    private final FragmentManager mFragmentManager;
    private final TabHost mTabHost;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private final ArrayList<TabInfo> mTabsBackStack = new ArrayList<TabInfo>();
    private Fragment mInfoFragment;
    private Fragment mTasksFragment;
    private Fragment mChartsFragment;

    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            if(_args != null && tag != null && !tag.equals("")) {
            	_args.putString("tag", tag);
            }
            args = _args;
        }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    private View prepareTabView(Context context, String text, TabWidget parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_tab, parent, false);
        ((TextView) view.findViewById(R.id.title)).setText(text);
        return view;
    }

    public ProfileTabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mFragmentManager = activity.getSupportFragmentManager();
        mTabHost = tabHost;
        mViewPager = pager;
        mTabHost.setOnTabChangedListener(this);
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mContext));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);
        mTabs.add(info);
        mTabHost.addTab(tabSpec);
        notifyDataSetChanged();
    }

    public void addTab(String title, String tag, Class<?> clss, TabWidget parent) {
        TabHost.TabSpec spec = mTabHost.newTabSpec(tag);
        View view = prepareTabView(mTabHost.getContext(), title, parent);
        spec.setIndicator(view);

        addTab(spec, clss, null);
    }

    public void replaceTab(String newTag, Class<?> clss, Bundle args) {
    	int position = mTabHost.getCurrentTab();
    	TabInfo info = new TabInfo(newTag, clss, args);

    	// Replace the old tabinfo and push it to the backstack array
    	mTabsBackStack.add(mTabs.remove(position));
    	mTabs.add(position, info);
    	mFragmentManager.beginTransaction().remove(mTasksFragment).commit();
    	notifyDataSetChanged();
    }

    public void onBack() {
    	int stackCount = mTabsBackStack.size();
    	if(stackCount > 0) {
    		mTabs.set(mTabHost.getCurrentTab(), mTabsBackStack.remove(stackCount-1));
    		mFragmentManager.beginTransaction().remove(mTasksFragment).commit();
    		notifyDataSetChanged();
    	}else{
    		((FragmentActivity)mContext).finish();
    	}
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        switch(position) {
        case 0:
        	mInfoFragment = Fragment.instantiate(mContext, info.clss.getName(), info.args);
        	return mInfoFragment;
        case 1:
        	mTasksFragment = Fragment.instantiate(mContext, info.clss.getName(), info.args);
        	return mTasksFragment;
        case 2:
        	mChartsFragment = Fragment.instantiate(mContext, info.clss.getName(), info.args);
        	return mChartsFragment;
        default:
        	return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
    	if(object instanceof PoolTasksFragment && !mTabs.get(1).tag.equals(TAB_TASKS)) {
    		return POSITION_NONE;
    	}
    	if(object instanceof TaskSettingsFragment && mTabs.get(1).tag.equals(TAB_TASKS)) {
    		return POSITION_NONE;
    	}
    	return POSITION_UNCHANGED;
    }

    @Override
    public void onTabChanged(String tabId) {
        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        // Unfortunately when TabHost changes the current tab, it kindly
        // also takes care of putting focus on it when not in touch mode.
        // The jerk.
        // This hack tries to prevent this from pulling focus out of our
        // ViewPager.
        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);

        // Clear the backstack
        if(mTabsBackStack.size() > 0) {
        	mTabs.set(1, mTabsBackStack.remove(0));
        	mTabsBackStack.clear();
        	mFragmentManager.beginTransaction().remove(mTasksFragment).commit();
    		notifyDataSetChanged();
        }

        widget.setDescendantFocusability(oldFocusability);
        widget.getChildTabViewAt(position).setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
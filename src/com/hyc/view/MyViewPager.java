package com.hyc.view;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyViewPager  extends PagerAdapter {
	private List<View> views = null;

	public MyViewPager(List<View> views) {
		this.views = views;
	}

	@Override
	public void destroyItem(View view, int arg1, Object object) {
		((ViewPager) view).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View view) {
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public Object instantiateItem(View view, int arg1) {
		((ViewPager) view).addView(views.get(arg1), 0);
		return views.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return view == arg1;
	}

	@Override
	public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {
	}
}

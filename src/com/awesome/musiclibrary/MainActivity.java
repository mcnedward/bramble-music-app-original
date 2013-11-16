package com.awesome.musiclibrary;

/**
 * Edward McNealy Music Library - Main Activity View First View on Application
 * Displays all content of Music Library sorted by the user
 * 
 * Dates: January 1, 2013 April 25, 2013 April 29, 2013
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.awesome.asynctasks.LoadDatabase;
import com.awesome.asynctasks.RetrieveMedia;

public class MainActivity extends FragmentActivity {
	private static String TAG = "MainActivity";

	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);	// Set the window to allow for progress spinner
		setContentView(R.layout.activity_main);

		new LoadDatabase(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		new RetrieveMedia(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new CustomFragmentPagerAdapter());
		mViewPager.setOffscreenPageLimit(4 - 1);
	}

	public class CustomFragmentPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
		final int PAGE_COUNT = 4;
		final int[] tabs = { R.layout.view_artist_layout, R.layout.view_album_layout, R.layout.view_song_layout,
				R.layout.view_genre_layout };
		final String[] titles = { "Artists", "Albums", "Songs", "Genres" };

		public CustomFragmentPagerAdapter() {
			super(getSupportFragmentManager());
		}

		@Override
		public int getCount() {
			return PAGE_COUNT;
		}

		@Override
		public Fragment getItem(int position) {
			return PageFragment.createTab(tabs[position]);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {

		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

	}

	public static class PageFragment extends Fragment {
		public static final String LAYOUT_PAGE = "LAYOUT_PAGE";

		private int layout;

		public static PageFragment createTab(int layout) {
			Bundle args = new Bundle();
			args.putInt(LAYOUT_PAGE, layout);
			PageFragment fragment = new PageFragment();
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			layout = getArguments().getInt(LAYOUT_PAGE);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			return inflater.inflate(layout, container, false);
		}
	}

	public MainActivity() {
		// Constructor
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.library_menu, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}

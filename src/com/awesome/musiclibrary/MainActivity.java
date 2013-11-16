package com.awesome.musiclibrary;

/**
 * Edward McNealy Music Library - Main Activity View First View on Application
 * Displays all content of Music Library sorted by the user
 * 
 * Dates: January 1, 2013 April 25, 2013 April 29, 2013
 */

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.Window;

import com.awesome.adapters.TabsAdapter;
import com.awesome.asynctasks.LoadDatabase;
import com.awesome.asynctasks.RetrieveMedia;
import com.awesome.musiclibrary.viewcontent.ViewAlbumActivity;
import com.awesome.musiclibrary.viewcontent.ViewArtistActivity;
import com.awesome.musiclibrary.viewcontent.ViewGenreActivity;
import com.awesome.musiclibrary.viewcontent.ViewSongActivity;

public class MainActivity extends FragmentActivity {
	private static String TAG = "MainActivity";
	private static final String CACHE = "CachedFiles";

	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);	// Set the window to allow for progress spinner
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		new LoadDatabase(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		new RetrieveMedia(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(actionBar.newTab().setText("Artists"), ViewArtistActivity.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Albums"), ViewAlbumActivity.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Songs"), ViewSongActivity.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Genres"), ViewGenreActivity.class, null);

		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
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
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	/*
	 * @Override public void onBackPressed() { if (mViewPager.getCurrentItem()
	 * == 0) { // If the user is currently looking at the first step, allow the
	 * system to handle the // Back button. This calls finish() on this activity
	 * and pops the back stack. super.onBackPressed(); } else { // Otherwise,
	 * select the previous step.
	 * mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1); } }
	 */
}

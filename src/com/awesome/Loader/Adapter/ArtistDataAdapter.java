package com.awesome.Loader.Adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.awesome.Data.MediaDatabase;
import com.awesome.Data.Source.ArtistDataSource;
import com.awesome.Data.Source.DataSource;
import com.awesome.Dto.Artist;
import com.awesome.Loader.ArtistDataLoader;
import com.awesome.Loader.BaseDataLoader;
import com.awesome.adapters.MyExpandableListAdapter;
import com.awesome.adapters.MyListAdapter;
import com.awesome.musiclibrary.R;

/**
 * This is the class that loads data into the Artist list of the library. The
 * LoaderManager will update the data as it is changed. TODO Should this be a
 * FragmentActity?
 * 
 * @author emcnealy
 * 
 */
public class ArtistDataAdapter extends ListFragment implements
		LoaderManager.LoaderCallbacks<List<Artist>> {
	
	protected static final String TAG = "DataAdapter";
	protected static final boolean DEBUG = true;
	private static final int LOADER_ID = 1;

	private MediaDatabase mDatabase;
	private DataSource<Artist> mDataSource;
	private Context mContext;

	private ExpandableListView mEView;
	private ListView mLView;
	private MyExpandableListAdapter mEAdapter;
	private MyListAdapter mLAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mDataSource = new ArtistDataSource(mDatabase);

		mContext = getActivity();

		mEView = (ExpandableListView) ((Activity) mContext)
				.findViewById(R.id.displayArtists);
		mLView = (ListView) ((Activity) mContext)
				.findViewById(R.id.displayArtists2);
		mEAdapter = new MyExpandableListAdapter(mContext);
		mLAdapter = new MyListAdapter(mContext);

		mEView.setAdapter(mEAdapter);
		mLView.setAdapter(mLAdapter);

		if (DEBUG) {
			Log.d(TAG, "### Calling initLoader! ###");
			if (getLoaderManager().getLoader(LOADER_ID) == null) {
				Log.d(TAG, "### Initializing a new Loader... ###");
			} else {
				Log.d(TAG,
						"### Reconnecting with existing Loader (id " + LOADER_ID + ")... ###");
			}
		}
		// Initialize a Loader with id '1'. If the Loader with this id already
		// exists, then the LoaderManager will reuse the existing Loader.
		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	@Override
	public Loader<List<Artist>> onCreateLoader(int arg0, Bundle arg1) {
		BaseDataLoader<Artist> loader = new ArtistDataLoader(mContext, mDataSource, null, null, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<Artist>> arg0, List<Artist> arg1) {
		if (DEBUG)
			Log.d(TAG, "### onLoadFinished() called! ###");
		// TODO Need to use ArrayAdapter for the Custom Adapters http://www.phloxblog.in/android-custom-loader-load-data-sqlite-database-android-version-1-6/#.U-5URvldWCk
	}

	@Override
	public void onLoaderReset(Loader<List<Artist>> arg0) {
		// TODO Auto-generated method stub

	}

}

package com.awesome.Loader.Adapter;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.awesome.Data.MediaDatabase;
import com.awesome.Data.Source.AlbumDataSource;
import com.awesome.Data.Source.IDataSource;
import com.awesome.Dto.Album;
import com.awesome.Loader.AlbumDataLoader;
import com.awesome.Loader.BaseDataLoader;
import com.awesome.adapters.MediaListAdapter;
import com.awesome.musiclibrary.R;

public class AlbumDataAdapter extends Fragment implements
		LoaderManager.LoaderCallbacks<List<Album>> {

	protected static final String TAG = "DataAdapter";
	private static final int LOADER_ID = new Random().nextInt();

	private MediaDatabase mDatabase;
	private IDataSource<Album> mAlbumDataSource;
	private Context mContext;

	private ListView mLView;
	private MediaListAdapter<Album> mMediaAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.view_album_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mContext = getActivity();
		mDatabase = new MediaDatabase(mContext);
		mAlbumDataSource = new AlbumDataSource(mDatabase.open());

		mLView = (ListView) ((Activity) mContext)
				.findViewById(R.id.displayAlbums);

		mMediaAdapter = new MediaListAdapter<Album>(mContext,
				R.layout.view_album_layout);

		mLView.setAdapter(mMediaAdapter);

		Log.d(TAG, "### Calling initLoader! ###");
		if (getLoaderManager().getLoader(LOADER_ID) == null) {
			Log.d(TAG, "### Initializing a new Loader... ###");
		} else {
			Log.d(TAG, "### Reconnecting with existing Loader (id " + LOADER_ID
					+ ")... ###");
		}
		// Initialize a Loader. If the Loader with this id already
		// exists, then the LoaderManager will reuse the existing Loader.
		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	@Override
	public Loader<List<Album>> onCreateLoader(int arg0, Bundle arg1) {
		BaseDataLoader<Album> loader = new AlbumDataLoader(mContext,
				mAlbumDataSource, null, null, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<Album>> loader, List<Album> data) {
		Log.d(TAG, "### onLoadFinished() called! ###");
		mMediaAdapter.clear();
		for (int x = 0; x < data.size(); x++) {
			Album album = data.get(x);
			mMediaAdapter.setGroup(album);

			mLView.setAdapter(mMediaAdapter);
			mLView.setClickable(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Album>> arg0) {
		mMediaAdapter.clear();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mDatabase.close();
		mAlbumDataSource = null;
		mDatabase = null;
	}

}

package com.awesome.Loader.Adapter;

import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;

import com.awesome.Data.MediaDatabase;
import com.awesome.Data.Source.AlbumDataSource;
import com.awesome.Data.Source.ArtistDataSource;
import com.awesome.Data.Source.DataSource;
import com.awesome.Dto.Album;
import com.awesome.Dto.Artist;
import com.awesome.Loader.ArtistDataLoader;
import com.awesome.Loader.BaseDataLoader;
import com.awesome.adapters.MediaExpandableListAdapter;
import com.awesome.adapters.MediaListAdapter;
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
public class ArtistDataAdapter extends Fragment implements
		LoaderManager.LoaderCallbacks<List<Artist>> {
	
	protected static final String TAG = "DataAdapter";
	protected static final boolean DEBUG = true;
	private static final int LOADER_ID = 1;

	private MediaDatabase mDatabase;
	private DataSource<Artist> mArtistDataSource;
	private Context mContext;

	private ExpandableListView mEView;
	private ListView mLView;
	private MediaExpandableListAdapter mEMediaAdapter;
	private MyListAdapter mLAdapter;
	private ArrayAdapter<Artist> mMediaAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.view_artist_layout, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mDatabase = new MediaDatabase(getActivity());
		//mArtistDataSource = new ArtistDataSource(mDatabase); TODO UNCOMMENT!!

		mContext = getActivity();

		mEView = (ExpandableListView) ((Activity) mContext)
				.findViewById(R.id.displayArtists);
		mLView = (ListView) ((Activity) mContext)
				.findViewById(R.id.displayArtists2);
		mEMediaAdapter = new MediaExpandableListAdapter(mContext);
		mLAdapter = new MyListAdapter(mContext);
		
		mMediaAdapter = new MediaListAdapter<Artist>(mContext, R.layout.view_artist_layout);

		mEView.setAdapter(mEMediaAdapter);
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
		BaseDataLoader<Artist> loader = new ArtistDataLoader(mContext, mArtistDataSource, null, null, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<Artist>> loader, List<Artist> data) {
		if (DEBUG)
			Log.d(TAG, "### onLoadFinished() called! ###");
		mMediaAdapter.clear();
		mEMediaAdapter.clear();
		for (int x = 0; x < data.size(); x++) {
			Artist artist = data.get(x);
			mMediaAdapter.add(artist);
			mEMediaAdapter.setGroup(artist);
			if (artist.getAlbumList() != null && !artist.getAlbumList().isEmpty()) {
				mEMediaAdapter.setChild(x, artist.getAlbumList());
			} else {
				mEMediaAdapter.setChild(x, null);
			}
			
			mEView.setAdapter(mEMediaAdapter);
			mEView.setClickable(true);
			mEView.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition,
						long id) {
					if (view.isSelected() == false) {
						Album album = (Album) ((ExpandableListView) parent).getExpandableListAdapter().getChild(
								groupPosition, childPosition);
						// Get all songs for this album
						//List<Song> songList = mdb.getAllSongsForAlbum(album);
						//album.setSongList(songList);
						//viewDisplaySongsByAlbum(album);
					}
					return false;
				}
			});
			mEMediaAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Artist>> arg0) {
		mMediaAdapter.clear();
		mEMediaAdapter.clear();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mDatabase.close();
		mArtistDataSource = null;
		mDatabase = null;
	}

}

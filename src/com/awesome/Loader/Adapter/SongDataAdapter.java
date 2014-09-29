package com.awesome.Loader.Adapter;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.awesome.Data.MediaDatabase;
import com.awesome.Data.Source.IDataSource;
import com.awesome.Data.Source.SongDataSource;
import com.awesome.Entity.Album;
import com.awesome.Entity.Song;
import com.awesome.Loader.BaseDataLoader;
import com.awesome.Loader.SongDataLoader;
import com.awesome.adapters.MediaListAdapter;
import com.awesome.musiclibrary.MediaPlayerService;
import com.awesome.musiclibrary.R;
import com.awesome.musiclibrary.viewcontent.NowPlayingActivity;

public class SongDataAdapter extends Fragment implements LoaderManager.LoaderCallbacks<List<Song>> {

	protected static final String TAG = "DataAdapter";
	private static final int LOADER_ID = new Random().nextInt();

	private MediaDatabase mDatabase;
	private IDataSource<Song> mSongDataSource;
	private Context mContext;

	private ListView mLView;
	private MediaListAdapter<Song> mMediaAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.view_song_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mContext = getActivity();
		mDatabase = new MediaDatabase(mContext);
		mSongDataSource = new SongDataSource(mDatabase.open());

		mLView = (ListView) ((Activity) mContext).findViewById(R.id.displaySongs);

		mMediaAdapter = new MediaListAdapter<Song>(mContext, R.layout.view_song_layout);

		mLView.setAdapter(mMediaAdapter);

		Log.d(TAG, "### Calling initLoader! ###");
		if (getLoaderManager().getLoader(LOADER_ID) == null) {
			Log.d(TAG, "### Initializing a new Loader... ###");
		} else {
			Log.d(TAG, "### Reconnecting with existing Loader (id " + LOADER_ID + ")... ###");
		}
		// Initialize a Loader. If the Loader with this id already
		// exists, then the LoaderManager will reuse the existing Loader.
		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	@Override
	public Loader<List<Song>> onCreateLoader(int arg0, Bundle arg1) {
		BaseDataLoader<Song> loader = new SongDataLoader(mContext, mSongDataSource, null, null, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<Song>> loader, List<Song> data) {
		Log.d(TAG, "### onLoadFinished() called! ###");
		mMediaAdapter.clear();
		for (int x = 0; x < data.size(); x++) {
			Song song = data.get(x);
			mMediaAdapter.setGroup(song);

			mLView.setAdapter(mMediaAdapter);
			mLView.setClickable(true);
			// Set the single click for songs
			mLView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (view.isSelected() == false) {
						Song song = (Song) parent.getItemAtPosition(position);
						// TODO Change this
						viewNowPlaying(null, song);
						startMediaService(null, song);
					}
					return;
				}
			});
			mMediaAdapter.notifyDataSetChanged();
		}
	}
	
	public void startMediaService(Album album, Song song) {
		Intent playSong = new Intent(mContext, MediaPlayerService.class);
		playSong.putExtra("album", album);
		playSong.putExtra("song", song);
		mContext.startService(playSong);
	}

	public void viewNowPlaying(Album album, Song song) {
		Intent nowPlaying = new Intent(mContext, NowPlayingActivity.class);
		nowPlaying.putExtra("album", album);
		nowPlaying.putExtra("song", song);
		this.startActivity(nowPlaying);
	}

	@Override
	public void onLoaderReset(Loader<List<Song>> arg0) {
		mMediaAdapter.clear();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mDatabase.close();
		mSongDataSource = null;
		mDatabase = null;
	}

}

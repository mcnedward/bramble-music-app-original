package com.awesome.asynctasks;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.awesome.adapters.Refresh;
import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Song;
import com.awesome.utils.MusicDatabase;

public class LoadDatabase extends AsyncTask<Void, Object, Refresh> {
	private static String TAG = "LoadDatabase";

	private List<Artist> artists;
	public List<Album> albums;
	public List<Song> songs;

	private Context context;
	private MusicDatabase db;
	private Refresh refresh;

	public LoadDatabase(Context context) {
		this.context = context;
		db = new MusicDatabase(context);
		refresh = new Refresh(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Refresh doInBackground(Void... object) {
		try {
			Log.i(TAG, "Executing task");
			artists = db.getAllArtists();
			albums = db.getAllAlbums();
			songs = db.getAllSongs();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
		return refresh;
	}

	protected void onPostExecute(Refresh refresh) {
		refresh.refreshLibrary(artists, albums, songs);
		Log.i(TAG, "Task successfully executed");
	}
}

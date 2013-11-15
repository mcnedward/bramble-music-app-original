package com.awesome.asynctasks;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.awesome.adapters.MediaAdapter;
import com.awesome.adapters.Refresh;
import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Genre;
import com.awesome.categories.Song;
import com.awesome.utils.MusicDatabase;

public class RetrieveMedia extends AsyncTask<Void, Integer, Refresh> {
	private static String TAG = "RetrieveMedia";

	private List<Artist> artists;
	public ArrayList<Genre> genres;
	public List<Album> albums;
	public List<Song> songs;

	private MediaAdapter mediaAdapter;
	private MusicDatabase db;
	private Refresh refresh;
	private Context context;
	private ProgressDialog progDialog;

	public RetrieveMedia(Context context) {
		this.context = context;
		mediaAdapter = new MediaAdapter(context);
		db = new MusicDatabase(context);
		refresh = new Refresh(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progDialog = new ProgressDialog(context);
		progDialog.setIndeterminate(false);
		progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progDialog.setTitle("Downloading Music Files...");
		progDialog.setCancelable(true);
		progDialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		progDialog.setProgress(progress[0]);
		Log.i(TAG, "Progress: " + progress[0] + "%");
	}

	@Override
	protected Refresh doInBackground(Void... object) {
		try {
			Log.i(TAG, "Executing task");
			mediaAdapter.retrieveArtistsAndAlbums();
			publishProgress(50);
			mediaAdapter.retrieveSongs();
			publishProgress(70);
			artists = db.getAllArtists();
			publishProgress(80);
			albums = db.getAllAlbums();
			publishProgress(90);
			songs = db.getAllSongs();
			publishProgress(100);
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
		return refresh;
	}

	protected void onPostExecute(Refresh refresh) {
		refresh.refreshLibrary(artists, albums, songs);
		progDialog.dismiss();
		Log.i(TAG, "Task successfully executed");
	}
}

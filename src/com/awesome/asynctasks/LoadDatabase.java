package com.awesome.asynctasks;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.awesome.adapters.Refresh;
import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Song;
import com.awesome.utils.MusicDatabase;

/**
 * AsyncTask used to load media from the database. This should be called as soon as the application is created, so that
 * the
 * music will be available for the user to view as soon as possible.
 * 
 * @author Edward
 * 
 */
public class LoadDatabase extends AsyncTask<Void, Object, Refresh> {
	private static String TAG = "LoadDatabase";

	// Initialize the lists for each category
	private List<Artist> artists;
	public List<Album> albums;
	public List<Song> songs;

	private MusicDatabase db;	// Initialize the MusicDatabase
	private Refresh refresh;	// Initialize the Refresh adapter
	private Context context;

	/**
	 * Constructor for the LoadDatabase AsyncTask. Creates a database object to retrieve media files, and a refresh
	 * adapter to add those media files to their appropriate list views.
	 * 
	 * @param context
	 *            The context of the activity that will run this AsyncTask.
	 */
	public LoadDatabase(Context context) {
		this.context = context;
		db = new MusicDatabase(context);
		refresh = new Refresh(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) context).setProgressBarIndeterminateVisibility(true);	// Show the progress spinner
	}

	@Override
	protected Refresh doInBackground(Void... object) {
		try {
			Log.i(TAG, "Executing task");
			// Get all the media files from the database
			artists = db.getAllArtists();
			// TODO albums = db.getAllAlbums();
			// songs = db.getAllSongs();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
		return refresh;
	}

	protected void onPostExecute(Refresh refresh) {
		refresh.refreshLibrary(artists, albums, songs);	// Refresh the list views
		((Activity) context).setProgressBarIndeterminateVisibility(false);	// Remove the progress spinner
		Log.i(TAG, "Task successfully executed");
	}
}

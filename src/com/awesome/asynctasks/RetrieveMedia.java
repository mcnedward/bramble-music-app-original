package com.awesome.asynctasks;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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

/**
 * AsyncTask used to retrieve media information from the Media Store. This should be called as soon as the application
 * is created, because this will look through all media files on the user's phone, and then retrieve the information for
 * those files. This Task is very time consuming.
 * 
 * @author Edward
 * 
 */
public class RetrieveMedia extends AsyncTask<Void, Integer, Refresh> {
	private static String TAG = "RetrieveMedia";

	// Initialize lists for each category
	private List<Artist> artists;
	public List<Album> albums;
	public List<Song> songs;
	public ArrayList<Genre> genres;

	private MediaAdapter mediaAdapter;
	private MusicDatabase db;
	private Refresh refresh;
	private Context context;
	private ProgressDialog progDialog;

	/**
	 * Constructor for this AsyncTask. Creates a MediaAdapter to retrieve information from the Media Store, a
	 * MusicDatabase to get data after the media information has been retrieved, and a Refresh adapter to refresh the
	 * lists views with the new media data.
	 * 
	 * @param context
	 *            The context of the activity to run this AsyncTask.
	 */
	public RetrieveMedia(Context context) {
		this.context = context;
		mediaAdapter = new MediaAdapter(context);
		db = new MusicDatabase(context);
		refresh = new Refresh(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) context).setProgressBarIndeterminateVisibility(true);	// Show the progress spinner
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		// Update the progress dialog
		Log.i(TAG, "Progress: " + progress[0] + "%");
		// progDialog.setProgress(progress[0]);
	}

	@Override
	protected Refresh doInBackground(Void... object) {
		try {
			Log.i(TAG, "Executing task");
			mediaAdapter.retrieveArtistsAndAlbums();	// Retrieve the information for artists and albums
			// publishProgress(40);
			mediaAdapter.retrieveSongs();				// Retrieve the information for songs
			// publishProgress(70);
			artists = db.getAllArtists();				// Get the new list of artists
			// publishProgress(80);
			albums = db.getAllAlbums();					// Get the new list of albums
			// publishProgress(90);
			songs = db.getAllSongs();					// Get the new list of songs
			// publishProgress(100);
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
		return refresh;
	}

	protected void onPostExecute(Refresh refresh) {
		refresh.refreshLibrary(artists, albums, songs);						// Refresh the library
		((Activity) context).setProgressBarIndeterminateVisibility(false);	// Remove the progress spinner
		Log.i(TAG, "Task successfully executed");
	}
}

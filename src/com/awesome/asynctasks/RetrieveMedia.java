package com.awesome.asynctasks;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.awesome.Data.MediaDatabase;
import com.awesome.Dto.Album;
import com.awesome.Dto.Artist;
import com.awesome.Dto.Genre;
import com.awesome.Dto.Song;
import com.awesome.adapters.Refresh;
import com.awesome.utils.MediaLoader;

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

	private MediaLoader loader;
	private MediaDatabase db;
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
		db = new MediaDatabase(context);
		loader = new MediaLoader(context);
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
		Log.i(TAG, "Progress: " + progress[0] + "%");
	}

	@Override
	protected Refresh doInBackground(Void... object) {
		try {
			Log.i(TAG, "Executing task");
			loader.retrieveMedia();	
			artists = db.getAllArtists();				// Get the new list of artists
			albums = db.getAllAlbums();
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

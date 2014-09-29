package com.awesome.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.awesome.Data.MediaDatabase;
import com.awesome.util.MediaLoader;

/**
 * AsyncTask used to retrieve media information from the Media Store. This should be called as soon as the application
 * is created, because this will look through all media files on the user's phone, and then retrieve the information for
 * those files. This Task is very time consuming.
 * 
 * @author Edward
 * 
 */
public class RetrieveMedia extends AsyncTask<Void, Integer, Void> {
	private static String TAG = "RetrieveMedia";

	private MediaLoader loader;
	private MediaDatabase mDatabase;
	private Context context;

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
		mDatabase = new MediaDatabase(context);
		Log.d(TAG, "### Opening the Media Database for RetrieveMedia ###");
		loader = new MediaLoader(context, mDatabase);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) context).setProgressBarIndeterminateVisibility(true); // Show the progress spinner
	}

	@Override
	protected Void doInBackground(Void... object) {
		Log.i(TAG, "Executing task");
		try {
			loader.retrieveMedia();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		((Activity) context).setProgressBarIndeterminateVisibility(false); // Remove the progress spinner
		Log.i(TAG, "Task successfully executed");
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		Log.i(TAG, "Progress: " + progress[0] + "%");
	}
}

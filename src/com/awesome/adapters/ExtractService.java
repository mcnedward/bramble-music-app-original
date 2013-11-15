package com.awesome.adapters;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ExtractService extends IntentService {
	private static String TAG = "ExtractService";

	private MediaAdapter sdCard; // Initialize the sd card

	public ExtractService() {
		super("ExtractService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		sdCard = new MediaAdapter(getApplicationContext());
		long startTime = System.nanoTime(); // Get the time for extraction start
		Log.i(TAG, "Beginning media file extraction from SD Card.");

		// sdCard.retrieveArtists(); // Get files from sd card
		// sdCard.logFiles(); // Display the Music data in Log Cat

		long endTime = System.nanoTime(); // Get the time for extraction end
		long duration = endTime - startTime;
		Log.i(TAG, "Media file extraction complete. Time ellapsed was: " + duration / 1000000);
	}
}

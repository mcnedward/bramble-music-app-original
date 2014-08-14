package com.awesome.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.awesome.Data.ArtistDataSource;
import com.awesome.Dto.Artist;

/**
 * TODO Change this name later
 * 
 * @author emcnealy
 * 
 */
public class MediaLoader {
	private final static String TAG = "MediaLoader";

	private ArtistDataSource mDataSource;
	private Context context;

	public MediaLoader(Context context) {
		this.context = context;
	}

	public void retrieveArtists() {
		List<Artist> artists = new ArrayList<Artist>();

		try {
			// Set the Uri and columns for extracting artist media data
			final Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
			final String[] artistCols = { MediaStore.Audio.Artists._ID,
					MediaStore.Audio.Artists.ARTIST,
					MediaStore.Audio.Artists.ARTIST_KEY,
					MediaStore.Audio.Artists.NUMBER_OF_ALBUMS };
			final Cursor cursor = context.getContentResolver().query(artistUri,
					artistCols, null, null, null);

			while (cursor.moveToNext()) {
				// Get artist information
				Integer artistId = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
				String artist = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
				String artistKey = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST_KEY));
				Integer numberOfAlbums = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
				// Create a new Artist and add to the list
				Artist a = new Artist(artistId, artist, artistKey,
						numberOfAlbums, null);
				artists.add(a);
			}

			for (Artist artist : artists) {
				mDataSource.insert(artist);
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
	}

}

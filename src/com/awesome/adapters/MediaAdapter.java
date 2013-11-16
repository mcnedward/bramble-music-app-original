package com.awesome.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Song;
import com.awesome.utils.MusicDatabase;

/**
 * An adapter that is used to retrieve information from the Media Store for each media file on the user's device. The
 * methods here are very time consuming, and should be run from an AsyncTask.
 * 
 * @author Edward
 * 
 */
public class MediaAdapter {
	private static String TAG = "MediaAdapter";

	private MusicDatabase db;
	private Context context;

	/**
	 * Empty Constructor
	 */
	public MediaAdapter() {

	}

	/**
	 * Constructor for creating a new MediaAdapter. Creates a MusicDatabase object that will be used for inserting the
	 * media information taken from the Media Store.
	 * 
	 * @param context
	 *            The context of the activity that will use this adapter
	 */
	public MediaAdapter(Context context) {
		db = new MusicDatabase(context);
		this.context = context;
	}

	/**
	 * Retrieves media information for each song. This is the most time consuming method because it needs to run through
	 * every media file, and then attempt to insert that file into the database.
	 * 
	 */
	public void retrieveSongs() {
		List<Song> songList = new ArrayList<Song>();

		Integer titleId = null;
		String title = null;
		String titleKey = null;
		String displayName = null;
		String artist = null;
		String artistKey = null;
		String album = null;
		String albumKey = null;
		String composer = null;
		Integer track = null;
		Integer duration = null;
		Integer year = null;
		Integer dateAdded = null;
		String mimeType = null;
		String data = null;
		Integer isMusic;

		try {
			// Set the Uri and columns for extracting artist media data
			final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			final String[] cols = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.TITLE_KEY, MediaStore.Audio.Media.DISPLAY_NAME,
					MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_KEY, MediaStore.Audio.Media.ALBUM,
					MediaStore.Audio.Media.ALBUM_KEY, MediaStore.Audio.Media.COMPOSER, MediaStore.Audio.Media.TRACK,
					MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.YEAR, MediaStore.Audio.Media.DATE_ADDED,
					MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.IS_MUSIC };

			final Cursor cursor = context.getContentResolver().query(uri, cols, null, null, null);

			while (cursor.moveToNext()) {
				titleId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				titleKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE_KEY));
				displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
				artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				artistKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_KEY));
				album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				albumKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_KEY));
				composer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));
				track = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
				duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				year = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
				dateAdded = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED));
				mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
				data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				isMusic = Integer.parseInt(cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC)));

				// Check if the media file is a music file
				if (isMusic == 1) {
					Song s = new Song(titleId, title, titleKey, displayName, artist, artistKey, album, albumKey,
							composer, track, duration, year, dateAdded, mimeType, data);
					songList.add(s);
				}
			}

			for (Song s : songList) {
				db.insertSong(s);
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
	}

	/**
	 * Retrieves media information for each artist and for each album. This uses the artist id to find the URI for
	 * retrieving album information. This is to ensure that all albums are retrieved.
	 */
	public void retrieveArtistsAndAlbums() {
		List<Artist> artistList = new ArrayList<Artist>();	// List of all artists
		List<Album> albumList = new ArrayList<Album>();		// List of all albums

		try {
			// Set the Uri and columns for extracting artist media data
			final Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
			final String[] artistCols = { MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST,
					MediaStore.Audio.Artists.ARTIST_KEY, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS };
			final Cursor cursor = context.getContentResolver().query(artistUri, artistCols, null, null, null);

			while (cursor.moveToNext()) {
				// Get artist information
				Integer artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
				String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
				String artistKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST_KEY));
				Integer numberOfAlbums = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));

				try {
					// Get the album information for each artist
					final Uri albumUri = MediaStore.Audio.Artists.Albums.getContentUri("external", artistId);
					final String[] albumCols = { MediaStore.Audio.Artists.Albums.ALBUM,
							MediaStore.Audio.Artists.Albums.ALBUM_KEY, MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS,
							MediaStore.Audio.Artists.Albums.FIRST_YEAR, MediaStore.Audio.Artists.Albums.LAST_YEAR,
							MediaStore.Audio.Artists.Albums.ALBUM_ART,
							MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS_FOR_ARTIST };
					final Cursor cursor2 = context.getContentResolver().query(albumUri, albumCols, null, null, null);
					while (cursor2.moveToNext()) {
						// Get album information
						String album = cursor2.getString(cursor2
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM));
						String albumKey = cursor2.getString(cursor2
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_KEY));
						Integer numberOfSongs = cursor2.getInt(cursor2
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS));
						Integer firstYear = cursor2.getInt(cursor2
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.FIRST_YEAR));
						Integer lastYear = cursor2.getInt(cursor2
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.LAST_YEAR));
						String albumArt = cursor2.getString(cursor2
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_ART));
						Integer numberOfSongsForArtist = cursor2.getInt(cursor2
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS_FOR_ARTIST));

						// Create a new album and add it to the total album list and the artist album list
						Album al = new Album(null, album, albumKey, artist, numberOfSongs, firstYear, lastYear,
								albumArt, null);
						albumList.add(al);
					}
				} catch (Exception e) {
					Log.i(TAG, e.getMessage(), e);
				}

				// Create a new Artist and add to the list
				Artist a = new Artist(artistId, artist, artistKey, numberOfAlbums, null);
				artistList.add(a);
			}

			for (Artist a : artistList) {
				db.insertArtist(a);
			}
			for (Album a : albumList) {
				db.insertAlbum(a);
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
	}
}

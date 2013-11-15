package com.awesome.adapters;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Music;
import com.awesome.categories.Song;
import com.awesome.utils.MusicDatabase;

public class MediaAdapter extends ListActivity {
	private static String TAG = "MediaAdapter";

	private MusicDatabase db;
	private Context context;

	public MediaAdapter() {
		// Empty Constructor
	}

	public MediaAdapter(Context context) { // Start the Media Adapter
		db = new MusicDatabase(context);
		this.context = context;
	}

	public List<Artist> retrieveArtists() {
		// Initialize variables for artist
		Integer artistId = null;
		String artist = null;
		String artistKey = null;
		Integer numberOfAlbums = null;
		List<Artist> artistList = new ArrayList<Artist>();

		try {
			// Set the Uri and columns for extracting artist media data
			final Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
			final String[] artistCols = { MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST,
					MediaStore.Audio.Artists.ARTIST_KEY, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS };

			final Cursor cursor = context.getContentResolver().query(artistUri, artistCols, null, null, null);

			while (cursor.moveToNext()) {
				artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
				artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
				artistKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST_KEY));
				numberOfAlbums = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
				if (artistId != 1) {
					// Create a new Artist and add to the list
					Artist a = new Artist(artistId, artist, artistKey, numberOfAlbums, null);
					artistList.add(a);
				}

			}
			Set<Artist> hashedList = new LinkedHashSet<Artist>(artistList);

			for (Artist a : hashedList) {
				db.insertArtist(a);
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		}
		return artistList;
	}

	public List<Album> retrieveAlbums() {
		Integer albumId = null;
		String album = null;
		String albumKey = null;
		String artist = null;
		Integer numberOfSongs = null;
		Integer firstYear = null;
		Integer lastYear = null;
		String albumArt = null;
		List<Album> albumList = new ArrayList<Album>();
		Uri uri;
		/**
		 * Set the Uri and columns for extracting artist media data If an artist
		 * Id is set, then the method is getting albums for an artist Otherwise,
		 * the method is getting all albums
		 */
		try {
			uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
			final String[] cols = { MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM,
					MediaStore.Audio.Albums.ALBUM_KEY, MediaStore.Audio.Albums.ARTIST,
					MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.FIRST_YEAR,
					MediaStore.Audio.Albums.LAST_YEAR, MediaStore.Audio.Albums.ALBUM_ART };

			final Cursor cursor = context.getContentResolver().query(uri, cols, null, null, null);

			while (cursor.moveToNext()) {
				albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
				album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
				albumKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_KEY));
				artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
				numberOfSongs = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
				firstYear = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.FIRST_YEAR));
				lastYear = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.LAST_YEAR));
				albumArt = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));

				Album a = new Album(albumId, album, albumKey, artist, numberOfSongs, firstYear, lastYear, albumArt,
						null);
				// if (!db.albumExists(a)) {
				db.insertAlbum(a);
				albumList.add(a);
				// }
			}

			// Set<Album> hashedList = new LinkedHashSet<Album>(albumList);

			for (Album a : albumList) {

			}

		} catch (Exception ex) {
			Log.i(TAG, ex.getMessage());
			ex.printStackTrace();
		}
		return albumList;
	}

	public List<Song> retrieveSongs() {
		List<Song> songList = new ArrayList<Song>();
		List<Artist> artistList = new ArrayList<Artist>();
		List<Album> albumList = new ArrayList<Album>();

		Set<Artist> hashedArtistList = null;
		Set<Album> hashedAlbumList = null;

		try {
			Integer titleId = null;
			String title = null;
			String titleKey = null;
			String displayName = null;
			Integer artistId = null;
			String artist = null;
			String artistKey = null;
			Integer albumId = null;
			String album = null;
			String albumKey = null;
			Integer track = null;
			Integer duration = null;
			Integer year = null;
			Integer dateAdded = null;
			String mimeType = null;
			String data = null;
			Integer isMusic;

			// Set the Uri and columns for extracting artist media data
			final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			final String[] cols = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.TITLE_KEY, MediaStore.Audio.Media.DISPLAY_NAME,
					MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_KEY,
					MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ALBUM_KEY,
					MediaStore.Audio.Media.TRACK, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.YEAR,
					MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.IS_MUSIC };

			final Cursor cursor = context.getContentResolver().query(uri, cols, null, null, null);

			while (cursor.moveToNext()) {
				titleId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				titleKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE_KEY));
				displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
				artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
				artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				artistKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_KEY));
				albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
				album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				albumKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_KEY));
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
					Song s = new Song(titleId, title, titleKey, displayName, artistId, artist, artistKey, albumId,
							album, albumKey, track, duration, year, dateAdded, mimeType, data);
					songList.add(s);
				}
			}
			Set<Song> hashedList = new LinkedHashSet<Song>(songList);

			for (Song s : hashedList) {
				db.insertSong(s);
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			e.printStackTrace();
		}
		return songList;
	}

	public void getMusic() {
		String title = null;
		String artist = null;
		String album = null;
		String composer = null;
		Integer year = null;
		Integer duration = null;
		Integer trackNumber = null;
		Integer songId = null;
		String titleKey = null;
		Integer artistId = null;
		String artistKey = null;
		Integer albumId = null;
		String albumKey = null;
		List<Music> musicList = new ArrayList<Music>();

		try {
			Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			final String[] cols = { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.COMPOSER, MediaStore.Audio.Media.YEAR,
					MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.TRACK, MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.TITLE_KEY, MediaStore.Audio.Media.ARTIST_ID,
					MediaStore.Audio.Media.ARTIST_KEY, MediaStore.Audio.Media.ALBUM_ID,
					MediaStore.Audio.Media.ALBUM_KEY };

			Log.i(TAG, uri.toString());
			final Cursor cursor = context.getContentResolver().query(uri, cols, null, null, null);

			while (cursor.moveToNext()) {
				title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				composer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));
				year = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
				duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
				songId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				titleKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE_KEY));
				artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
				artistKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_KEY));
				albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
				albumKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_KEY));

				Music music = new Music(title, artist, album, composer, year, duration, trackNumber, "BLANK", songId,
						titleKey, artistId, artistKey, albumId, albumKey);
				musicList.add(music);
			}

			for (Music m : musicList) {
				// Integer musicAlbumId = m.getAlbumId();
				// Integer numberOfSongs = null;
				// Integer firstYear = null;
				// Integer lastYear = null;
				// String albumArt = null;
				// List<Album> albumList = new ArrayList<Album>();
				// try {
				// Uri uri2 = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
				// final String[] cols2 = { MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM,
				// MediaStore.Audio.Albums.ALBUM_KEY, MediaStore.Audio.Albums.ARTIST,
				// MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.FIRST_YEAR,
				// MediaStore.Audio.Albums.LAST_YEAR, MediaStore.Audio.Albums.ALBUM_ART };
				// final String selection = "_id = " + musicAlbumId;
				//
				// final Cursor cursor2 = context.getContentResolver().query(uri2, cols2, selection, null, null);
				//
				// while (cursor2.moveToNext()) {
				// albumId = cursor2.getInt(cursor2.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
				// album = cursor2.getString(cursor2.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
				// albumKey = cursor2.getString(cursor2.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_KEY));
				// artist = cursor2.getString(cursor2.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
				// numberOfSongs = cursor2.getInt(cursor2
				// .getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
				// firstYear = cursor2.getInt(cursor2.getColumnIndexOrThrow(MediaStore.Audio.Albums.FIRST_YEAR));
				// lastYear = cursor2.getInt(cursor2.getColumnIndexOrThrow(MediaStore.Audio.Albums.LAST_YEAR));
				// albumArt = cursor2.getString(cursor2.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
				//
				// Album a = new Album(albumId, album, albumKey, artist, numberOfSongs, firstYear, lastYear,
				// albumArt, null);
				// albumList.add(a);
				// }
				// } catch (Exception e) {
				// Log.i(TAG, e.getLocalizedMessage());
				// e.printStackTrace();
				// }
				db.insertMusic(m);
			}
		} catch (Exception e) {
			Log.i(TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

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

				// Create a list of albums for each individual artist
				List<Album> artistAlbumList = new ArrayList<Album>();

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
						artistAlbumList.add(al);
						albumList.add(al);
					}
				} catch (Exception e) {
					Log.i(TAG, e.getMessage(), e);
				}

				if (artistId != 1) {
					// Create a new Artist and add to the list
					Artist a = new Artist(artistId, artist, artistKey, numberOfAlbums, artistAlbumList);
					artistList.add(a);
				}
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

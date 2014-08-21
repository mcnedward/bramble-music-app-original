package com.awesome.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.awesome.Data.MediaDatabase;
import com.awesome.Data.Source.AlbumDataSource;
import com.awesome.Data.Source.ArtistDataSource;
import com.awesome.Data.Source.IDataSource;
import com.awesome.Data.Source.SongDataSource;
import com.awesome.Dto.Album;
import com.awesome.Dto.Artist;
import com.awesome.Dto.Song;
import com.awesome.Loader.AlbumDataLoader;
import com.awesome.Loader.ArtistDataLoader;
import com.awesome.Loader.SongDataLoader;

/**
 * TODO Change this name later
 * 
 * @author emcnealy
 * 
 */
public class MediaLoader {
	private final static String TAG = "MediaLoader";

	private ArtistDataLoader artistDataLoader;
	private AlbumDataLoader albumDataLoader;
	private SongDataLoader songDataLoader;
	private Context context;

	public MediaLoader(Context context, MediaDatabase mediaDatabase) {
		this.context = context;
		SQLiteDatabase database = mediaDatabase.open();
		IDataSource<Artist> artistDataSource = new ArtistDataSource(database);
		IDataSource<Album> albumDataSource = new AlbumDataSource(database);
		IDataSource<Song> songDataSource = new SongDataSource(database);

		artistDataLoader = new ArtistDataLoader(context, artistDataSource,
				null, null, null, null, null);
		albumDataLoader = new AlbumDataLoader(context, albumDataSource, null,
				null, null, null, null);
		songDataLoader = new SongDataLoader(context, songDataSource, null,
				null, null, null, null);
	}

	public void retrieveMedia() {
		getArtists();
		getAlbums();
	}

	private void getArtists() {
		Cursor cursor = null;
		try {
			// Set the Uri and columns for extracting artist media data
			final Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
			final String[] artistCols = { MediaStore.Audio.Artists._ID,
					MediaStore.Audio.Artists.ARTIST,
					MediaStore.Audio.Artists.ARTIST_KEY,
					MediaStore.Audio.Artists.NUMBER_OF_ALBUMS };
			cursor = context.getContentResolver().query(artistUri, artistCols,
					null, null, null);

			int artistCount = cursor.getCount();
			int x = 1;
			Log.d(TAG, "Number of results for artist retrieval: " + artistCount);
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

				// Create a new Artist and add to the database
				Artist artistEntity = new Artist(artistId, artist, artistKey,
						numberOfAlbums, null);
				artistDataLoader.insert(artistEntity);
				Log.d(TAG, "Starting insert task for " + artist + "....." + x
						+ "/" + artistCount);
				x++;
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			Log.d(TAG, "Done with artists");
		}
	}

	private void getAlbums() {
		Cursor cursor = null;
		try {
			// Get the album information for each artist
			final Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
			final String[] albumCols = { MediaStore.Audio.Albums._ID,
					MediaStore.Audio.Albums.ALBUM,
					MediaStore.Audio.Albums.ALBUM_KEY,
					MediaStore.Audio.Albums.NUMBER_OF_SONGS,
					MediaStore.Audio.Albums.FIRST_YEAR,
					MediaStore.Audio.Albums.LAST_YEAR,
					MediaStore.Audio.Albums.ALBUM_ART,
					MediaStore.Audio.Albums.NUMBER_OF_SONGS_FOR_ARTIST };
			cursor = context.getContentResolver().query(albumUri, albumCols,
					null, null, null);

			int albumCount = cursor.getCount();
			int x = 1;
			Log.d(TAG, "Number of results for album retrieval: " + albumCount);
			while (cursor.moveToNext()) {
				// Get album information
				Integer albumId = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
				String album = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM));
				String albumKey = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_KEY));
				Integer numberOfSongs = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS));
				Integer firstYear = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.FIRST_YEAR));
				Integer lastYear = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.LAST_YEAR));
				String albumArt = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_ART));

				// Create a new album and add it to the total album list
				// and the artist album list
				Album albumEntity = new Album(albumId, album, albumKey, null,
						null, numberOfSongs, firstYear, lastYear, albumArt,
						null);
				albumDataLoader.insert(albumEntity);
				Log.d(TAG, "Starting insert task for  " + album + "....." + x
						+ "/" + albumCount);
				x++;
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			Log.d(TAG, "Done with albums");
		}
	}

	private void getSongs() {
		Cursor songCursor = null;
		try {
			// Set the Uri and columns for extracting artist media data
			final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			final String[] cols = { MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.TITLE_KEY,
					MediaStore.Audio.Media.DISPLAY_NAME,
					MediaStore.Audio.Media.ARTIST_ID,
					MediaStore.Audio.Media.ALBUM_ID,
					MediaStore.Audio.Media.COMPOSER,
					MediaStore.Audio.Media.TRACK,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.YEAR,
					MediaStore.Audio.Media.DATE_ADDED,
					MediaStore.Audio.Media.MIME_TYPE,
					MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.IS_MUSIC };

			final Cursor cursor = context.getContentResolver().query(uri, cols,
					null, null, null);

			int songCount = cursor.getCount();
			int x = 1;
			Log.d(TAG, "Number of results for artist retrieval: " + songCount);
			while (cursor.moveToNext()) {
				Integer titleId = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				String title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				String titleKey = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE_KEY));
				String displayName = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
				Integer artistId = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
				Integer albumId = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
				String composer = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));
				Integer track = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
				Integer duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				Integer year = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
				Integer dateAdded = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED));
				String mimeType = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
				String data = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				Integer isMusic = Integer
						.parseInt(cursor.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC)));

				Song songEntity = new Song(titleId, title, titleKey,
						displayName, artistId, albumId, composer, track,
						duration, year, dateAdded, mimeType, data, isMusic);
				songDataLoader.insert(songEntity);

				Log.d(TAG, "Starting insert task for  " + title + "....." + x
						+ "/" + songCount);
				x++;
			}
		} catch (Exception e) {

		} finally {
			if (songCursor != null && !songCursor.isClosed())
				songCursor.close();
			Log.d(TAG, "Done with albums");
		}
	}

}

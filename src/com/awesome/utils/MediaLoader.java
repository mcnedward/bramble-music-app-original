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
	
	private IDataSource<Artist> artistDataSource;
	private IDataSource<Album> albumDataSource;
	private IDataSource<Song> songDataSource;
	
	private Context context;

	public MediaLoader(Context context, MediaDatabase mediaDatabase) {
		this.context = context;
		SQLiteDatabase database = mediaDatabase.open();
		artistDataSource = new ArtistDataSource(database);
		albumDataSource = new AlbumDataSource(database);
		songDataSource = new SongDataSource(database);

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
		getSongs();
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
				// Create a new Artist and add to the database
				Artist artistEntity = artistDataSource.generateObjectFromCursor(cursor);
				artistDataLoader.insert(artistEntity);
				Log.d(TAG, "Starting insert task for " + artistEntity + "....." + x
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
					MediaStore.Audio.Albums.ARTIST,
					MediaStore.Audio.Albums.NUMBER_OF_SONGS,
					MediaStore.Audio.Albums.FIRST_YEAR,
					MediaStore.Audio.Albums.LAST_YEAR,
					MediaStore.Audio.Albums.ALBUM_ART };
			cursor = context.getContentResolver().query(albumUri, albumCols,
					null, null, null);

			int albumCount = cursor.getCount();
			int x = 1;
			Log.d(TAG, "Number of results for album retrieval: " + albumCount);
			while (cursor.moveToNext()) {
				// Create a new album and add it to the total album list
				// and the artist album list
				Album albumEntity = albumDataSource.generateObjectFromCursor(cursor);
				albumDataLoader.insert(albumEntity);
				Log.d(TAG, "Starting insert task for  " + albumEntity + "....." + x
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
				Song songEntity = songDataSource.generateObjectFromCursor(cursor);
				songDataLoader.insert(songEntity);

				Log.d(TAG, "Starting insert task for  " + songEntity + "....." + x
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

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
import com.awesome.Dto.Album;
import com.awesome.Dto.Artist;
import com.awesome.Loader.ArtistDataLoader;

/**
 * TODO Change this name later
 * 
 * @author emcnealy
 * 
 */
public class MediaLoader {
	private final static String TAG = "MediaLoader";

	private ArtistDataLoader artistDataLoader;
	//private AlbumtDataLoader albumDataSource;
	private Context context;

	public MediaLoader(Context context, MediaDatabase mediaDatabase) {
		this.context = context;
		SQLiteDatabase database = mediaDatabase.open();
		IDataSource<Artist> artistDataSource = new ArtistDataSource(database);
		IDataSource<Album> albumDataSource = new AlbumDataSource(database);
		
		artistDataLoader = new ArtistDataLoader(context, artistDataSource, null, null, null, null, null);
	}

	public void retrieveMedia() {
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
				
				// Create a new Artist and add to the database
				Artist artistEntity = new Artist(artistId, artist, artistKey,
						numberOfAlbums, null);
				artistDataLoader.insert(artistEntity);
				
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

					// Create a new album and add it to the total album list and the artist album list
					Album albumEntity = new Album(null, album, albumKey, artist, numberOfSongs, firstYear, lastYear,
							albumArt, null);
					//albumDataSource.insert(albumEntity);
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
	}

}

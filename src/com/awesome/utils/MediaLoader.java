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
import com.awesome.Loader.AlbumDataLoader;
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
	private AlbumDataLoader albumDataLoader;
	private Context context;

	public MediaLoader(Context context, MediaDatabase mediaDatabase) {
		this.context = context;
		SQLiteDatabase database = mediaDatabase.open();
		IDataSource<Artist> artistDataSource = new ArtistDataSource(database);
		IDataSource<Album> albumDataSource = new AlbumDataSource(database);

		artistDataLoader = new ArtistDataLoader(context, artistDataSource,
				null, null, null, null, null);
		albumDataLoader = new AlbumDataLoader(context, albumDataSource, null, null, null, null, null);
	}

	public void retrieveMedia() {
		Cursor artistCursor = null;
		Cursor albumCursor = null;
		try {
			// Set the Uri and columns for extracting artist media data
			final Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
			final String[] artistCols = { MediaStore.Audio.Artists._ID,
					MediaStore.Audio.Artists.ARTIST,
					MediaStore.Audio.Artists.ARTIST_KEY,
					MediaStore.Audio.Artists.NUMBER_OF_ALBUMS };
			artistCursor = context.getContentResolver().query(artistUri,
					artistCols, null, null, null);

			int artistCount = artistCursor.getCount();
			int x = 1;
			Log.d(TAG, "Number of results for artist retrieval: " + artistCount);
			while (artistCursor.moveToNext()) {
				// Get artist information
				Integer artistId = artistCursor.getInt(artistCursor
						.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
				String artist = artistCursor
						.getString(artistCursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
				String artistKey = artistCursor
						.getString(artistCursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST_KEY));
				Integer numberOfAlbums = artistCursor
						.getInt(artistCursor
								.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));

				// Create a new Artist and add to the database
				Artist artistEntity = new Artist(artistId, artist, artistKey,
						numberOfAlbums, null);
				artistDataLoader.insert(artistEntity);
				Log.d(TAG, "Starting insert task for " + artist + "....." + x
						+ "/" + artistCount);
				x++;

				try {
					// Get the album information for each artist
					final Uri albumUri = MediaStore.Audio.Artists.Albums
							.getContentUri("external", artistId);
					final String[] albumCols = {
							MediaStore.Audio.Albums._ID,
							MediaStore.Audio.Artists.Albums.ALBUM,
							MediaStore.Audio.Artists.Albums.ALBUM_KEY,
							MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS,
							MediaStore.Audio.Artists.Albums.FIRST_YEAR,
							MediaStore.Audio.Artists.Albums.LAST_YEAR,
							MediaStore.Audio.Artists.Albums.ALBUM_ART,
							MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS_FOR_ARTIST };
					albumCursor = context.getContentResolver().query(albumUri,
							albumCols, null, null, null);

					int albumCount = albumCursor.getCount();
					int y = 1;
					Log.d(TAG, "Number of results for album retrieval: "
							+ albumCount);
					while (albumCursor.moveToNext()) {
						// Get album information
						Integer albumId = albumCursor
								.getInt(albumCursor
										.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
						String album = albumCursor
								.getString(albumCursor
										.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM));
						String albumKey = albumCursor
								.getString(albumCursor
										.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_KEY));
						Integer numberOfSongs = albumCursor
								.getInt(albumCursor
										.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS));
						Integer firstYear = albumCursor
								.getInt(albumCursor
										.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.FIRST_YEAR));
						Integer lastYear = albumCursor
								.getInt(albumCursor
										.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.LAST_YEAR));
						String albumArt = albumCursor
								.getString(albumCursor
										.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_ART));

						// Create a new album and add it to the total album list
						// and the artist album list
						Album albumEntity = new Album(albumId, album, albumKey,
								artistId, artist, numberOfSongs, firstYear, lastYear,
								albumArt, null);
						albumDataLoader.insert(albumEntity);
						Log.d(TAG, "Starting insert task for  " + album
								+ "....." + y + "/" + albumCount);
						y++;
					}
				} catch (Exception e) {

				} finally {
					if (albumCursor != null && !albumCursor.isClosed())
						albumCursor.close();
					Log.d(TAG, "Done with albums");
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (artistCursor != null && !artistCursor.isClosed())
				artistCursor.close();
			Log.d(TAG, "Done with artists");
		}
	}

}

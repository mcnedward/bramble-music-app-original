package com.awesome.Data.Source;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.awesome.Data.DatabaseHelper;
import com.awesome.Entity.Album;
import com.awesome.Entity.Artist;
import com.awesome.Entity.Media;
import com.awesome.Entity.Song;

public abstract class MediaDataSource<T extends Media> extends DataSource<T> {
	private static final String TAG = "MediaDataSource";

	private SQLiteDatabase mDatabase;

	public MediaDataSource(SQLiteDatabase database) {
		mDatabase = database;
	}

	@Override
	protected boolean insert(T entity) {
		if (entity == null || entityExists(entity.getId()))
			return false;
		try {
			mDatabase.beginTransaction();
			mDatabase.insertOrThrow(getTableName(), null, generateContentValuesFromEntity(entity));
			mDatabase.setTransactionSuccessful();
			Log.d(TAG, "Successfully inserted " + entity);
		} catch (SQLException e) {
			Log.e(TAG, "Error when trying to insert type " + entity.getClass() + ": " + entity, e);
		} finally {
			mDatabase.endTransaction();
		}
		return false;
	}

	@Override
	public boolean delete(Media entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Media entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<T> read() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> query(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		Cursor cursor = mDatabase.query(getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having,
				orderBy);
		List<T> entities = new ArrayList<T>();
		if (cursorHasValue(cursor)) {
			while (!cursor.isAfterLast()) {
				entities.add((T) generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return entities;
	}

	/**
	 * Runs the provided SQL and returns a {@link Cursor} over the result set.
	 * 
	 * @param sql
	 *            the SQL query. The SQL string must not be ; terminated
	 * @param selectionArgs
	 *            You may include ?s in where clause in the query, which will be replaced by the values from
	 *            selectionArgs. The values will be bound as Strings.
	 * @return A {@link Cursor} object, which is positioned before the first entry. Note that {@link Cursor}s are not
	 *         synchronized, see the documentation for more details.
	 */
	@Override
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		return mDatabase.rawQuery(sql, selectionArgs);
	}

	/**
	 * This checks if an entity with a certain id already exists in the database.
	 * 
	 * @param id
	 *            The id of the entity of check.
	 * @return True if the entity already exists, false otherwise.
	 */
	private boolean entityExists(int id) {
		Cursor cursor = null;
		boolean exists = false;
		try {
			cursor = mDatabase.query(getTableName(), new String[] { "_id" }, "_id = ?",
					new String[] { String.valueOf(id) }, null, null, null);
			if (cursor.getCount() > 0)
				exists = true;
		} catch (Exception e) {
			Log.e(TAG, "Error checking if entity exists with id: " + id, e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return exists;
	}

	/********** GENERATE ENTITIES **********/

	public Artist generateArtist(Cursor cursor) {
		if (cursor == null)
			return null;
		Integer artistId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTIST_ID));
		String artistName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTIST));
		String artistKey = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTIST_KEY));
		Integer numberOfAlbums = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.NUMBER_OF_ALBUMS));

		Artist artist = new Artist(artistId, artistName, artistKey, numberOfAlbums, null);
		return artist;
	}

	public Album generateAlbum(Cursor cursor) {
		if (cursor == null)
			return null;
		Integer albumId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ALBUM_ID));
		String albumName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ALBUM));
		String albumKey = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ALBUM_KEY));
		String albumArtist = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ALBUM_ARTIST));
		Integer numberOfSongs = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.NUMBER_OF_SONGS));
		Integer firstYear = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIRST_YEAR));
		Integer lastYear = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.LAST_YEAR));
		String albumArt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ALBUM_ART));

		Album album = new Album(albumId, albumName, albumKey, albumArtist, numberOfSongs, firstYear, lastYear,
				albumArt, null);
		return album;
	}

	public Song generateSong(Cursor cursor) {
		if (cursor == null)
			return null;
		Integer titleId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_ID));
		String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_TITLE));
		String titleKey = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_KEY));
		String displayName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_DISPLAY_NAME));
		// For artist and album, check if the artist or album exists in this cursor depending on if the view of table
		// was used.
		Integer artistId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_ARTIST_ID));
		int artistIndex = cursor.getColumnIndex(DatabaseHelper.ARTIST);
		String artist = artistIndex == -1 ? null : cursor.getString(artistIndex);
		int artistKeyIndex = cursor.getColumnIndex(DatabaseHelper.ARTIST_KEY);
		String artistKey = artistKeyIndex == -1 ? null : cursor.getString(artistKeyIndex);
		Integer albumId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_ALBUM_ID));
		int albumIndex = cursor.getColumnIndex(DatabaseHelper.ALBUM);
		String album = albumIndex == -1 ? null : cursor.getString(albumIndex);
		int albumKeyIndex = cursor.getColumnIndex(DatabaseHelper.ALBUM_KEY);
		String albumKey = albumKeyIndex == -1 ? null : cursor.getString(albumKeyIndex);
		String composer = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_COMPOSER));
		Integer track = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_TRACK));
		Integer duration = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_DURATION));
		Integer year = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_YEAR));
		Integer dateAdded = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_DATE_ADDED));
		String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_MIME_TYPE));
		String data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_DATA));
		boolean isMusic = (cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_IS_MUSIC)) == 1);
		Song song = new Song(titleId, title, titleKey, displayName, artistId, artist, artistKey, albumId, album, albumKey, composer, track, duration, year,
				dateAdded, mimeType, data, isMusic);
		return song;
	}

	protected String[] getArtistColumns() {
		return new String[] { DatabaseHelper.ARTIST_ID, DatabaseHelper.ARTIST, DatabaseHelper.ARTIST_KEY };
	}
}

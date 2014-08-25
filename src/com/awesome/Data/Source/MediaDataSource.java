package com.awesome.Data.Source;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.awesome.Data.DatabaseHelper;
import com.awesome.Dto.Artist;
import com.awesome.Dto.Media;

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
			if (cursorHasValue(cursor)) {
				cursor.close();
			}
		}
		return exists;
	}

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

	protected String[] getArtistColumns() {
		return new String[] { DatabaseHelper.ARTIST_ID, DatabaseHelper.ARTIST, DatabaseHelper.ARTIST_KEY };
	}
}

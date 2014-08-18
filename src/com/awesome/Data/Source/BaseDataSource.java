package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class BaseDataSource<T> {
	private final static String TAG = "BaseDataSource";

	protected SQLiteDatabase mDatabase;

	public BaseDataSource(SQLiteDatabase database) {
		mDatabase = database;
	}

	/**
	 * Attempts to insert an entity into the database. This method uses
	 * validation to check if the entity exists before adding it to the
	 * database.
	 * 
	 * @param entity
	 *            The entity to insert into the database.
	 * @param id
	 *            The id of the entity. Used to check if the entity already
	 *            exists in the database.
	 * @return False if the entity exists, then true or false depending on if
	 *         the entity was successfully inserted.
	 */
	public boolean insert(T entity, int id) {
		if (entityExists(id))
			return false;
		return insert(entity);
	}

	/**
	 * Attempts to insert an entity into the database. This does not check if an
	 * entity already exists.
	 * 
	 * @param entity
	 *            The entity to insert into the database.
	 * @return True if the entity was inserted, false otherwise.
	 */
	protected final boolean insert(T entity) {
		try {
			mDatabase.beginTransaction();
			mDatabase.insert(getTableName(), null,
					generateContentValuesFromEntity(entity));
			mDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(TAG, "Error when trying to insert " + entity, e);
		} finally {
			mDatabase.endTransaction();
		}
		return false;
	}

	/**
	 * Save an entity in the database.
	 * 
	 * @param entity
	 *            The entity to save to the database.
	 * @return True if the entity was saved, false otherwise.
	 */
	public boolean save(T entity) {
		return insert(entity);
	}

	public abstract boolean delete(T entity);

	public abstract boolean update(T entity);

	public abstract List<T> read();

	public abstract List<T> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy);

	public abstract String[] getAllColumns();

	public abstract T generateObjectFromCursor(Cursor cursor);

	public abstract ContentValues generateContentValuesFromEntity(T entity);

	public abstract String getTableName();

	/**
	 * This checks if an entity with a certain id already exists in the
	 * database.
	 * 
	 * @param id
	 *            The id of the entity of check.
	 * @return True if the entity already exists, false otherwise.
	 */
	protected boolean entityExists(int id) {
		Cursor cursor = null;
		try {
			cursor = mDatabase.query(getTableName(), new String[] { "_id" },
					"_id = ?", new String[] { String.valueOf(id) }, null, null,
					null);
			cursor.moveToFirst();
			if (cursor.getCount() > 0)
				return true;
		} catch (Exception e) {
			Log.e(TAG, "Error checking if entity exists with id: " + id, e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return false;
	}
}

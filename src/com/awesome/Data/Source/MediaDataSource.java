package com.awesome.Data.Source;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
	public List<T> query(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		Cursor cursor = mDatabase.query(getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy);
		List<T> entities = new ArrayList<T>();
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				entities.add((T) generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return entities;
	}
	
	/**
	 * This checks if an entity with a certain id already exists in the
	 * database.
	 * 
	 * @param id
	 *            The id of the entity of check.
	 * @return True if the entity already exists, false otherwise.
	 */
	private boolean entityExists(int id) {
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

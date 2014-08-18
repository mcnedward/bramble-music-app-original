package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.awesome.Data.MediaDatabase;

public abstract class DataSource<T> {

	protected MediaDatabase mMediaDatabase;

	public DataSource(MediaDatabase database) {
		mMediaDatabase = database;
	}

	public abstract boolean insert(T entity);

	public abstract boolean delete(T entity);

	public abstract boolean update(T entity);

	public abstract List<T> read();

	public abstract List<T> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy);

	/**
	 * Get all of the data columns for this DataSource. The first object should
	 * always be the id for that DataSource.
	 * 
	 * @return A String[] of all the columns, with the id set as the first
	 *         value.
	 */
	public abstract String[] getAllColumns();

	public abstract T generateObjectFromCursor(Cursor cursor);

	public abstract ContentValues generateContentValuesFromEntity(T entity);

	public abstract String getTableName();

}

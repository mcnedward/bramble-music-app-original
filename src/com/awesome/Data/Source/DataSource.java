package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class DataSource<T> {

	protected abstract boolean insert(T entity);

	public abstract boolean delete(T entity);

	public abstract boolean update(T entity);

	public abstract List<T> read();

	public abstract List<T> query(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy);

	public abstract String[] getAllColumns();
	
	public abstract String getTableName();

	public abstract T generateObjectFromCursor(Cursor cursor);
	
	public abstract ContentValues generateContentValuesFromEntity(T entity);

}

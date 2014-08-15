package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.awesome.Data.MediaDatabase;

public abstract class DataSource<T> {

	protected MediaDatabase mDatabase;
	
	public DataSource(MediaDatabase database) {
		mDatabase = database;
	}
	
	public abstract boolean insert(T entity);
	public abstract boolean delete(T entity);
	public abstract boolean update(T entity);
	public abstract List<T> read();
	public abstract List<T> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy);
	public abstract String[] getAllColumns();
	public abstract T generateObjectFromCursor(Cursor cursor);
	public abstract ContentValues generateContentValuesFromEntity(T entity);
	
}

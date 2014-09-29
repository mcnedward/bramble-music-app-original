package com.awesome.Data.Source;

import java.util.List;

import android.database.Cursor;

public interface IDataSource<T> {

	public boolean save(T entity);

	public List<T> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy);
	
	public T generateObjectFromCursor(Cursor cursor);

	//public Cursor getCursor(String mSelection, String[] mSelectionArgs, String mGroupBy, String mHaving, String mOrderBy);

}

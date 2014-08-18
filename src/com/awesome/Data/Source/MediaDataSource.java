package com.awesome.Data.Source;

import java.util.List;

import com.awesome.Dto.Media;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class MediaDataSource<T> extends BaseDataSource<Media> {
	private static final String TAG = "MediaDataSource";

	public MediaDataSource(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public boolean save(Media entity) {
		if (entity == null)
			return false;
		try {
			if (!entityExists(entity.getId())) {
				mDatabase.beginTransaction();
			}
		} catch (Exception e) {
			
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
	public List<Media> read() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Media> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

}

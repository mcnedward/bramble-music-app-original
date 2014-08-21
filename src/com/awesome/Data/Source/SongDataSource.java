package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.awesome.Dto.Song;

public class SongDataSource extends MediaDataSource<Song> implements IDataSource<Song> {

	public SongDataSource(SQLiteDatabase database) {
		super(database);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean save(Song entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Song> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Song entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Song entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getAllColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Song generateObjectFromCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentValues generateContentValuesFromEntity(Song entity) {
		// TODO Auto-generated method stub
		return null;
	}

}

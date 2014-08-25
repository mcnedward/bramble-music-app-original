package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.awesome.Data.DatabaseHelper;
import com.awesome.Dto.Album;

public class AlbumDataSource extends MediaDataSource<Album> implements
		IDataSource<Album> {

	public AlbumDataSource(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public boolean save(Album entity) {
		return insert(entity);
	}

	@Override
	public List<Album> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		List<Album> albums = query(selection, selectionArgs, groupBy, having,
				orderBy);
		return albums;
	}
	
	/********** GET DATA COLUMNS AND OBJECTS **********/

	@Override
	public String[] getAllColumns() {
		return new String[] { DatabaseHelper.ALBUM_ID, DatabaseHelper.ALBUM, DatabaseHelper.ALBUM_KEY, DatabaseHelper.ALBUM_ARTIST,
				DatabaseHelper.NUMBER_OF_SONGS, DatabaseHelper.FIRST_YEAR, DatabaseHelper.LAST_YEAR, DatabaseHelper.ALBUM_ART };
	}
	
	@Override
	public String getTableName() {
		return DatabaseHelper.ALBUMS_TABLE;
	}

	@Override
	public Album generateObjectFromCursor(Cursor cursor) {
		if (cursor == null)
			return null;
		Integer albumId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ALBUM_ID));
		String albumName = cursor
				.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ALBUM));
		String albumKey = cursor.getString(cursor
				.getColumnIndexOrThrow(DatabaseHelper.ALBUM_KEY));
		String albumArtist = cursor.getString(cursor
				.getColumnIndexOrThrow(DatabaseHelper.ALBUM_ARTIST));
		Integer numberOfSongs = cursor.getInt(cursor
				.getColumnIndexOrThrow(DatabaseHelper.NUMBER_OF_SONGS));
		Integer firstYear = cursor.getInt(cursor
				.getColumnIndexOrThrow(DatabaseHelper.FIRST_YEAR));
		Integer lastYear = cursor.getInt(cursor
				.getColumnIndexOrThrow(DatabaseHelper.LAST_YEAR));
		String albumArt = cursor.getString(cursor
				.getColumnIndexOrThrow(DatabaseHelper.ALBUM_ART));

		Album album = new Album(albumId, albumName, albumKey, albumArtist,
				numberOfSongs, firstYear, lastYear, albumArt, null);
		return album;
	}

	@Override
	public ContentValues generateContentValuesFromEntity(Album entity) {
		if (entity == null)
			return null;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.ALBUM_ID, entity.getId());
		values.put(DatabaseHelper.ALBUM, entity.getAlbum());
		values.put(DatabaseHelper.ALBUM_KEY, entity.getAlbumKey());
		values.put(DatabaseHelper.ALBUM_ARTIST, entity.getArtist());
		values.put(DatabaseHelper.NUMBER_OF_SONGS, entity.getNumberOfSongs());
		values.put(DatabaseHelper.FIRST_YEAR, entity.getFirstYear());
		values.put(DatabaseHelper.LAST_YEAR, entity.getLastYear());
		values.put(DatabaseHelper.ALBUM_ART, entity.getAlbumArt());
		return values;
	}
	
	@Override
	public boolean delete(Album entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Album entity) {
		// TODO Auto-generated method stub
		return false;
	}

}

package com.awesome.Data.Source;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.awesome.Data.MediaDatabase;
import com.awesome.Dto.Album;

public class AlbumDataSource extends DataSource<Album> {

	/** Album Table Variables **/
	final private static String TABLE_NAME = "Albums";
	final private static String ALBUM_ID = "_id";
	final private static String ALBUM = "Album";
	final private static String ALBUM_KEY = "AlbumKey";
	final private static String ALBUM_ARTIST = "Artist";
	final private static String NUMBER_OF_SONGS = "NumberOfSongs";
	final private static String FIRST_YEAR = "MinYear";
	final private static String LAST_YEAR = "MaxYear";
	final private static String ALBUM_ART = "AlbumArt";

	public AlbumDataSource(MediaDatabase database) {
		super(database);
	}

	@Override
	public boolean insert(Album entity) {
		if (entity == null)
			return false;
		long result = mDatabase.insert(TABLE_NAME, null,
				generateContentValuesFromEntity(entity));
		return result != -1;
	}

	@Override
	public boolean delete(Album entity) {
		if (entity == null)
			return false;
		int result = mDatabase.delete(TABLE_NAME,
				ALBUM_ID + " = " + entity.getId(), null);
		return result != 0;
	}

	@Override
	public boolean update(Album entity) {
		if (entity == null)
			return false;
		int result = mDatabase.update(TABLE_NAME,
				generateContentValuesFromEntity(entity), ALBUM_ID + " = "
						+ entity.getId(), null);
		return result != 0;
	}

	@Override
	public List<Album> read() {
		Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
				null, null, null, null);
		List<Album> albums = new ArrayList<Album>();
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				albums.add(generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();

		}
		return albums;
	}

	@Override
	public List<Album> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection,
				selectionArgs, groupBy, having, orderBy);
		List<Album> albums = new ArrayList<Album>();
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				albums.add(generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return albums;
	}

	@Override
	public String[] getAllColumns() {
		return new String[] { ALBUM_ID, ALBUM, ALBUM_KEY, ALBUM_ARTIST,
				NUMBER_OF_SONGS, FIRST_YEAR, LAST_YEAR, ALBUM_ART };
	}

	@Override
	public Album generateObjectFromCursor(Cursor cursor) {
		if (cursor == null)
			return null;
		Integer albumId = cursor.getInt(cursor.getColumnIndexOrThrow(ALBUM_ID));
		String albumName = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM));
		String albumKey = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM_KEY));
		String albumArtist = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM_ARTIST));
		Integer numberOfSongs = cursor.getInt(cursor.getColumnIndexOrThrow(NUMBER_OF_SONGS));
		Integer firstYear = cursor.getInt(cursor.getColumnIndexOrThrow(FIRST_YEAR));
		Integer lastYear = cursor.getInt(cursor.getColumnIndexOrThrow(LAST_YEAR));
		String albumArt = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM_ART));
		
		Album album = new Album(albumId, albumName, albumKey, albumArtist, numberOfSongs, firstYear, lastYear,
				albumArt, null);
		return album;
	}

	@Override
	public ContentValues generateContentValuesFromEntity(Album entity) {
		if (entity == null)
			return null;
		ContentValues values = new ContentValues();
		values.put(ALBUM, entity.getAlbum());
		values.put(ALBUM_KEY, entity.getAlbumKey());
		values.put(ALBUM_ARTIST, entity.getArtist());
		values.put(NUMBER_OF_SONGS, entity.getNumberOfSongs());
		values.put(FIRST_YEAR, entity.getFirstYear());
		values.put(LAST_YEAR, entity.getLastYear());
		values.put(ALBUM_ART, entity.getAlbumArt());
		return values;
	}

}

package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.awesome.Data.DatabaseHelper;
import com.awesome.Entity.Song;

public class SongDataSource extends MediaDataSource<Song> implements IDataSource<Song> {

	public SongDataSource(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public boolean save(Song entity) {
		return insert(entity);
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
	public List<Song> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		return query(selection, selectionArgs, groupBy, having, orderBy);
	}

	@Override
	public String[] getAllColumns() {
		return new String[] { DatabaseHelper.SONG_ID, DatabaseHelper.SONG_TITLE, DatabaseHelper.SONG_KEY,
				DatabaseHelper.SONG_DISPLAY_NAME, DatabaseHelper.SONG_ARTIST_ID, DatabaseHelper.SONG_ALBUM_ID,
				DatabaseHelper.SONG_COMPOSER, DatabaseHelper.SONG_TRACK, DatabaseHelper.SONG_DURATION,
				DatabaseHelper.SONG_YEAR, DatabaseHelper.SONG_DATE_ADDED, DatabaseHelper.SONG_MIME_TYPE,
				DatabaseHelper.SONG_DATA, DatabaseHelper.SONG_IS_MUSIC };
	}

	@Override
	public String getTableName() {
		return DatabaseHelper.SONGS_TABLE;
	}

	@Override
	public Song generateObjectFromCursor(Cursor cursor) {
		if (cursor == null)
			return null;
		Integer titleId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_ID));
		String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_TITLE));
		String titleKey = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_KEY));
		String displayName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_DISPLAY_NAME));
		Integer artistId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_ARTIST_ID));
		Integer albumId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_ALBUM_ID));
		String composer = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_COMPOSER));
		Integer track = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_TRACK));
		Integer duration = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_DURATION));
		Integer year = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_YEAR));
		Integer dateAdded = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_DATE_ADDED));
		String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_MIME_TYPE));
		String data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_DATA));
		boolean isMusic = (cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.SONG_IS_MUSIC)) == 1);
		Song song = new Song(titleId, title, titleKey, displayName, artistId, albumId, composer, track, duration, year,
				dateAdded, mimeType, data, isMusic);
		return song;
	}

	@Override
	public ContentValues generateContentValuesFromEntity(Song entity) {
		if (entity == null)
			return null;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.SONG_ID, entity.getId());
		values.put(DatabaseHelper.SONG_TITLE, entity.getTitle());
		values.put(DatabaseHelper.SONG_KEY, entity.getTitleKey());
		values.put(DatabaseHelper.SONG_DISPLAY_NAME, entity.getDisplayName());
		values.put(DatabaseHelper.SONG_ARTIST_ID, entity.getArtistId());
		values.put(DatabaseHelper.SONG_ALBUM_ID, entity.getAlbumId());
		values.put(DatabaseHelper.SONG_COMPOSER, entity.getComposer());
		values.put(DatabaseHelper.SONG_TRACK, entity.getTrack());
		values.put(DatabaseHelper.SONG_DURATION, entity.getDuration());
		values.put(DatabaseHelper.SONG_YEAR, entity.getYear());
		values.put(DatabaseHelper.SONG_DATE_ADDED, entity.getDateAdded());
		values.put(DatabaseHelper.SONG_MIME_TYPE, entity.getMimeType());
		values.put(DatabaseHelper.SONG_DATA, entity.getData());
		values.put(DatabaseHelper.SONG_IS_MUSIC, entity.isMusic());
		return values;
	}

}

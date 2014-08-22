package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.awesome.Dto.Artist;
import com.awesome.Dto.Song;

public class SongDataSource extends MediaDataSource<Song> implements
		IDataSource<Song> {

	/** Song Table Variables **/
	public final static String SONGS_TABLE = "Songs";
	public final static String SONG_ID = "_id";
	public final static String SONG_TITLE = "Title";
	public final static String SONG_KEY = "TitleKey";
	public final static String SONG_DISPLAY_NAME = "DisplayName";
	public final static String SONG_ARTIST_ID = "ArtistId";
	public final static String SONG_ALBUM_ID = "AlbumId";
	public final static String SONG_COMPOSER = "Composer";
	public final static String SONG_TRACK = "Track";
	public final static String SONG_DURATION = "Duration";
	public final static String SONG_YEAR = "Year";
	public final static String SONG_DATE_ADDED = "DateAdded";
	public final static String SONG_MIME_TYPE = "MimeType";
	public final static String SONG_DATA = "Data";
	public final static String SONG_IS_MUSIC = "IsMusic";

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
	public List<Song> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		return query(selection, selectionArgs, groupBy, having, orderBy);
	}

	@Override
	public String[] getAllColumns() {
		return new String[] { SONG_ID, SONG_TITLE, SONG_KEY, SONG_DISPLAY_NAME,
				SONG_ARTIST_ID, SONG_ALBUM_ID, SONG_COMPOSER, SONG_TRACK,
				SONG_DURATION, SONG_YEAR, SONG_DATE_ADDED, SONG_MIME_TYPE,
				SONG_DATA, SONG_IS_MUSIC };
	}
	
	@Override
	public String getTableName() {
		return SONGS_TABLE;
	}

	@Override
	public Song generateObjectFromCursor(Cursor cursor) {
		if (cursor == null)
			return null;
		Integer titleId = cursor.getInt(cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
		String title = cursor.getString(cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
		String titleKey = cursor
				.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE_KEY));
		String displayName = cursor
				.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
		Integer artistId = cursor
				.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
		Integer albumId = cursor
				.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
		String composer = cursor
				.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));
		Integer track = cursor.getInt(cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
		Integer duration = cursor
				.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
		Integer year = cursor.getInt(cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
		Integer dateAdded = cursor
				.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED));
		String mimeType = cursor
				.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
		String data = cursor.getString(cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
		boolean isMusic = (cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC)) == 1);
		Song song = new Song(titleId, title, titleKey, displayName, artistId, albumId, composer, track, duration, year, dateAdded, mimeType, data, isMusic);
		return song;
	}

	@Override
	public ContentValues generateContentValuesFromEntity(Song entity) {
		if (entity == null)
			return null;
		ContentValues values = new ContentValues();
		values.put(SONG_ID, entity.getId());
		values.put(SONG_TITLE, entity.getTitle());
		values.put(SONG_KEY, entity.getTitleKey());
		values.put(SONG_DISPLAY_NAME, entity.getDisplayName());
		values.put(SONG_ARTIST_ID, entity.getArtistId());
		values.put(SONG_ALBUM_ID, entity.getAlbumId());
		values.put(SONG_COMPOSER, entity.getComposer());
		values.put(SONG_TRACK, entity.getTrack());
		values.put(SONG_DURATION, entity.getDuration());
		values.put(SONG_YEAR, entity.getYear());
		values.put(SONG_DATE_ADDED, entity.getDateAdded());
		values.put(SONG_MIME_TYPE, entity.getMimeType());
		values.put(SONG_DATA, entity.getData());
		values.put(SONG_IS_MUSIC, entity.isMusic());
		return values;
	}

}

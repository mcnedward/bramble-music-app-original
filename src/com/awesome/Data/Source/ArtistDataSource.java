package com.awesome.Data.Source;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.awesome.Data.MediaDatabase;
import com.awesome.Dto.Artist;

public class ArtistDataSource extends DataSource<Artist> {

	/** Artist Table Variables **/
	private final static String TABLE_NAME = "Artists";
	private final static String ARTIST_ID = "_id";
	private final static String ARTIST = "Artist";
	private final static String ARTIST_KEY = "ArtistKey";
	private final static String NUMBER_OF_ALBUMS = "NumberOfAlbums";

	public ArtistDataSource(MediaDatabase database) {
		super(database);
	}

	/********** CRUD OPERATIONS **********/
	
	@Override
	public boolean insert(Artist entity) {
		if (entity == null)
			return false;
		long result = mDatabase.insert(TABLE_NAME, null,
				generateContentValuesFromEntity(entity));
		return result != -1;
	}

	@Override
	public boolean delete(Artist entity) {
		if (entity == null)
			return false;
		int result = mDatabase.delete(TABLE_NAME,
				ARTIST_ID + " = " + entity.getId(), null);
		return result != 0;
	}

	@Override
	public boolean update(Artist entity) {
		if (entity == null)
			return false;
		int result = mDatabase.update(TABLE_NAME,
				generateContentValuesFromEntity(entity), ARTIST_ID + " = "
						+ entity.getId(), null);
		return result != 0;
	}
	
	/********** GET QUERIES **********/
	
	

	/********** READ QUERIES **********/
	
	@Override
	public List<Artist> read() {
		Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
				null, null, null, null);
		List<Artist> artists = new ArrayList<Artist>();
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				artists.add(generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return artists;
	}

	@Override
	public List<Artist> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection,
				selectionArgs, groupBy, having, orderBy);
		List<Artist> artists = new ArrayList<Artist>();
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				artists.add(generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return artists;
	}

	/********** GET DATA COLUMNS AND OBJECTS **********/
	
	@Override
	public String[] getAllColumns() {
		return new String[] { ARTIST_ID, ARTIST, ARTIST_KEY, NUMBER_OF_ALBUMS };
	}

	@Override
	public Artist generateObjectFromCursor(Cursor cursor) {
		if (cursor == null)
			return null;
		Integer artistId = cursor.getInt(cursor
				.getColumnIndexOrThrow(ARTIST_ID));
		String artistName = cursor.getString(cursor
				.getColumnIndexOrThrow(ARTIST));
		String artistKey = cursor.getString(cursor
				.getColumnIndexOrThrow(ARTIST_KEY));
		Integer numberOfAlbums = cursor.getInt(cursor
				.getColumnIndexOrThrow(NUMBER_OF_ALBUMS));

		// List<Album> albumList = getAllAlbumsForArtist(artistName);

		Artist artist = new Artist(artistId, artistName, artistKey,
				numberOfAlbums, null);
		return artist;
	}

	@Override
	public ContentValues generateContentValuesFromEntity(Artist entity) {
		if (entity == null)
			return null;
		ContentValues values = new ContentValues();
		values.put(ARTIST, entity.getArtist());
		values.put(ARTIST_KEY, entity.getArtistKey());
		values.put(NUMBER_OF_ALBUMS, entity.getNumberOfAlbums());
		return values;
	}

}

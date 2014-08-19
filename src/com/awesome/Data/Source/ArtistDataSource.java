package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.awesome.Dto.Artist;

public class ArtistDataSource extends MediaDataSource<Artist> implements IDataSource<Artist> {

	/** Artist Table Variables **/
	private final static String TABLE_NAME = "Artists";
	private final static String ARTIST_ID = "_id";
	private final static String ARTIST = "Artist";
	private final static String ARTIST_KEY = "ArtistKey";
	private final static String NUMBER_OF_ALBUMS = "NumberOfAlbums";

	public ArtistDataSource(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public boolean save(Artist entity) {
		return insert(entity);
	}
	
	@Override
	public List<Artist> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		List<Artist> artists = query(selection, selectionArgs, groupBy, having, orderBy);
		return artists;
	}

	/********** GET DATA COLUMNS AND OBJECTS **********/

	@Override
	public String[] getAllColumns() {
		return new String[] { ARTIST_ID, ARTIST, ARTIST_KEY, NUMBER_OF_ALBUMS };
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
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
		values.put(ARTIST_ID, entity.getId());
		values.put(ARTIST, entity.getArtist());
		values.put(ARTIST_KEY, entity.getArtistKey());
		values.put(NUMBER_OF_ALBUMS, entity.getNumberOfAlbums());
		return values;
	}

	@Override
	public boolean delete(Artist entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Artist entity) {
		// TODO Auto-generated method stub
		return false;
	}
}

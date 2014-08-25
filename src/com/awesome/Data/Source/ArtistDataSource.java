package com.awesome.Data.Source;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.awesome.Data.DatabaseHelper;
import com.awesome.Dto.Album;
import com.awesome.Dto.Artist;

public class ArtistDataSource extends MediaDataSource<Artist> implements IDataSource<Artist> {

	public ArtistDataSource(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public boolean save(Artist entity) {
		return insert(entity);
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

	@Override
	public List<Artist> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		List<Artist> artists = query(selection, selectionArgs, groupBy, having, orderBy);

		return artists;
	}

	public List<Album> getAlbums(Artist artist) {
		String sql = "SELECT * FROM artist_album_info(" + artist.getId() + ")";
		String[] selectionArgs = new String[] { String.valueOf(artist.getId()) };
		Cursor cursor = rawQuery(sql, selectionArgs);
		if (cursorHasValue(cursor)) {
			return null;
		}
		return null;
	}

	/********** GET DATA COLUMNS AND OBJECTS **********/

	@Override
	public String[] getAllColumns() {
		return new String[] { DatabaseHelper.ARTIST_ID, DatabaseHelper.ARTIST, DatabaseHelper.ARTIST_KEY,
				DatabaseHelper.NUMBER_OF_ALBUMS };
	}

	@Override
	public String getTableName() {
		return DatabaseHelper.ARTISTS_TABLE;
	}

	@Override
	public Artist generateObjectFromCursor(Cursor cursor) {
		if (cursor == null)
			return null;
		Integer artistId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTIST_ID));
		String artistName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTIST));
		String artistKey = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTIST_KEY));
		Integer numberOfAlbums = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.NUMBER_OF_ALBUMS));

		Artist artist = new Artist(artistId, artistName, artistKey, numberOfAlbums, null);
		return artist;
	}

	@Override
	public ContentValues generateContentValuesFromEntity(Artist entity) {
		if (entity == null)
			return null;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.ARTIST_ID, entity.getId());
		values.put(DatabaseHelper.ARTIST, entity.getArtist());
		values.put(DatabaseHelper.ARTIST_KEY, entity.getArtistKey());
		values.put(DatabaseHelper.NUMBER_OF_ALBUMS, entity.getNumberOfAlbums());
		return values;
	}
}

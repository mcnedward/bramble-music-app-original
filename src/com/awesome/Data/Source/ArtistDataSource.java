package com.awesome.Data.Source;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.awesome.Data.DatabaseHelper;
import com.awesome.Entity.Album;
import com.awesome.Entity.Artist;
import com.awesome.Entity.Song;

public class ArtistDataSource extends MediaDataSource<Artist> implements IDataSource<Artist> {

	private AlbumDataSource albumDataSource;
	
	public ArtistDataSource(SQLiteDatabase database) {
		super(database);
		albumDataSource  = new AlbumDataSource(database);
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
//		for (Artist artist : artists) { // TODO What type of for loop works best here?
//			getAlbumsForArtist(artist);
//		}
		return artists;
	}
	
	public List<Album> getAlbums(Artist artist) {
		String sql = "SELECT * FROM " + DatabaseHelper.ALBUMS_TABLE + " WHERE " + DatabaseHelper.ALBUM_ARTIST_ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(artist.getId()) };
		Cursor cursor = rawQuery(sql, selectionArgs);
		List<Album> albumList = new ArrayList<Album>();
		if (cursorHasValue(cursor)) {
			while (!cursor.isAfterLast()) {
				Album album = generateAlbum(cursor);
				if (album != null) {
					List<Song> songs = albumDataSource.getSongsForAlbum(album);
					//album.setSongList(songs);
				}
				albumList.add(album);
				cursor.moveToNext();
			}
			cursor.close();
		}
		if (albumList != null && !albumList.isEmpty())
			artist.setAlbumList(albumList);
		return albumList;
	}
	
	/**
	 * This is used to get all the albums for a certain artist. If there are albums available for this artist, they will
	 * be automatically added to that artist's album list.
	 * 
	 * @param artist
	 *            The artist to check for albums.
	 * @return The list of albums for this artist.
	 */
	public List<Album> getAlbumsForArtist(Artist artist) {
		String sql = "SELECT DISTINCT al.* FROM " + DatabaseHelper.ALBUMS_TABLE
				+ " al LEFT JOIN audio_info ai ON ai.artist_id = ? WHERE ai.album_id = al." + DatabaseHelper.ALBUM_ID;
		String[] selectionArgs = new String[] { String.valueOf(artist.getId()) };
		Cursor cursor = rawQuery(sql, selectionArgs);
		List<Album> albumList = new ArrayList<Album>();
		if (cursorHasValue(cursor)) {
			while (!cursor.isAfterLast()) {
				Album album = generateAlbum(cursor);
				if (album != null) {
					List<Song> songs = albumDataSource.getSongsForAlbum(album);
					//album.setSongList(songs);
				}
				albumList.add(album);
				cursor.moveToNext();
			}
			cursor.close();
		}
		if (albumList != null && !albumList.isEmpty())
			artist.setAlbumList(albumList);
		return albumList;
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
		getAlbums(artist);
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

package com.awesome.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Song;

@SuppressWarnings("deprecation")
public class MusicDatabase {
	private static String TAG = "MusicDatabase";

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private Context context;

	/** Artist Table Variables **/
	final private static String DATABASE_TABLE_ARTISTS = "Artists";
	final private static String ARTIST_ID = "_id";
	final private static String ARTIST = "Artist";
	final private static String ARTIST_KEY = "ArtistKey";
	final private static String NUMBER_OF_ALBUMS = "NumberOfAlbums";

	/** Album Table Variables **/
	final private static String DATABASE_TABLE_ALBUMS = "Albums";
	final private static String ALBUM_ID = "_id";
	final private static String ALBUM = "Album";
	final private static String ALBUM_KEY = "AlbumKey";
	final private static String ALBUM_ARTIST = "Artist";
	final private static String NUMBER_OF_SONGS = "NumberOfSongs";
	final private static String FIRST_YEAR = "MinYear";
	final private static String LAST_YEAR = "MaxYear";
	final private static String ALBUM_ART = "AlbumArt";

	/** Song Table Variables **/
	final private static String DATABASE_TABLE_SONGS = "Songs";
	final private static String SONG_ID = "_id";
	final private static String SONG_TITLE = "Title";
	final private static String SONG_KEY = "TitleKey";
	final private static String SONG_DISPLAY_NAME = "DisplayName";
	final private static String SONG_ARTIST = "Artist";
	final private static String SONG_ARTIST_KEY = "ArtistKey";
	final private static String SONG_ALBUM = "Album";
	final private static String SONG_ALBUM_KEY = "AlbumKey";
	final private static String SONG_COMPOSER = "Composer";
	final private static String SONG_TRACK = "Track";
	final private static String SONG_DURATION = "Duration";
	final private static String SONG_YEAR = "Year";
	final private static String SONG_DATE_ADDED = "DateAdded";
	final private static String SONG_MIME_TYPE = "MimeType";
	final private static String SONG_DATA = "Data";
	final private static String SONG_IS_MUSIC = "IsMusic";

	public MusicDatabase() {

	}

	public MusicDatabase(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public MusicDatabase openToRead() throws android.database.SQLException {
		database = dbHelper.getReadableDatabase();
		return this;
	}

	public MusicDatabase open() throws android.database.SQLException {
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/********** INSERT QUERIES **********/

	@SuppressWarnings("deprecation")
	public void insertArtist(Artist artist) {
		// Check if the artist is already in the database
		if (artistExists(artist)) {
			return;
		} else {
			open();	// Open the database
			// Create a single InsertHelper to handle this set of insertions.
			InsertHelper ih = new InsertHelper(database, DATABASE_TABLE_ARTISTS);

			// Get the Artist information
			final String artistName = artist.getArtist();
			final String artistKey = artist.getArtistKey();
			final Integer artistNumberOfAlbums = artist.getNumberOfAlbums();

			// Get the numeric indexes for each of the columns that we're
			// updating
			final int artistColumn = ih.getColumnIndex(ARTIST);
			final int artistKeyColumn = ih.getColumnIndex(ARTIST_KEY);
			final int numberOfAlbumsColumn = ih.getColumnIndex(NUMBER_OF_ALBUMS);
			@SuppressWarnings("unused")
			int x = 0;

			try {
				// Get the InsertHelper ready to insert a single row
				ih.prepareForInsert();

				// Add the data for each column
				ih.bind(artistColumn, artistName);
				ih.bind(artistKeyColumn, artistKey);
				ih.bind(numberOfAlbumsColumn, artistNumberOfAlbums);

				// Insert the row into the database.
				ih.execute();
				x++;
			} finally {
				ih.close();
				close();	// Close the database
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void insertAlbum(Album album) {
		// Check if the album is already in the database
		if (albumExists(album)) {
			return;
		} else {
			open();
			// Create a single InsertHelper to handle this set of insertions.
			InsertHelper ih = new InsertHelper(database, DATABASE_TABLE_ALBUMS);

			// Get the Album information
			final String albumName = album.getAlbum();
			final String albumKey = album.getAlbumKey();
			final String albumArtist = album.getArtist();
			final Integer albumNumberOfSongs = album.getNumberOfSongs();
			final Integer albumFirstYear = album.getFirstYear();
			final Integer albumLastYear = album.getLastYear();
			final String albumArt = album.getAlbumArt();

			// Get the numeric indexes for each of the columns that we're
			// updating
			final int albumNameColumn = ih.getColumnIndex(ALBUM);
			final int albumKeyColumn = ih.getColumnIndex(ALBUM_KEY);
			final int albumArtistColumn = ih.getColumnIndex(ALBUM_ARTIST);
			final int albumNumberOfSongsColumn = ih.getColumnIndex(NUMBER_OF_SONGS);
			final int albumFirstYearColumn = ih.getColumnIndex(FIRST_YEAR);
			final int albumLastYearColumn = ih.getColumnIndex(LAST_YEAR);
			final int albumArtColumn = ih.getColumnIndex(ALBUM_ART);

			@SuppressWarnings("unused")
			int x = 0;

			try {
				// Get the InsertHelper ready to insert a single row
				ih.prepareForInsert();

				// Add the data for each column
				ih.bind(albumNameColumn, albumName);
				ih.bind(albumKeyColumn, albumKey);
				ih.bind(albumArtistColumn, albumArtist);
				ih.bind(albumNumberOfSongsColumn, albumNumberOfSongs);
				ih.bind(albumFirstYearColumn, albumFirstYear);
				ih.bind(albumLastYearColumn, albumLastYear);
				ih.bind(albumArtColumn, albumArt);

				// Insert the row into the database.
				ih.execute();
				x++;
			} finally {
				ih.close();
				close();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void insertSong(Song song) {
		// Check if the song is already in the database
		if (songExists(song)) {
			return;
		} else {
			open();
			// Create a single InsertHelper to handle this set of insertions.
			InsertHelper ih = new InsertHelper(database, DATABASE_TABLE_SONGS);

			// Get the Album information
			final String songTitle = song.getTitle();
			final String songKey = song.getTitleKey();
			final String songDisplayName = song.getDisplayName();
			final String songArtistKey = song.getArtistKey();
			final String songAlbumKey = song.getAlbumKey();
			final String songComposer = song.getComposer();
			final Integer songTrack = song.getTrack();
			final Integer songDuration = song.getDuration();
			final Integer songYear = song.getYear();
			final Integer songDateAdded = song.getDateAdded();
			final String songMimeType = song.getMimeType();
			final String songData = song.getData();

			// Get the numeric indexes for each of the columns that we're updating
			final int songTitleColumn = ih.getColumnIndex(SONG_TITLE);
			final int songKeyColumn = ih.getColumnIndex(SONG_KEY);
			final int songDisplayNameColumn = ih.getColumnIndex(SONG_DISPLAY_NAME);
			final int songArtistKeyColumn = ih.getColumnIndex(SONG_ARTIST_KEY);
			final int songAlbumKeyColumn = ih.getColumnIndex(SONG_ALBUM_KEY);
			final int songComposerComlumn = ih.getColumnIndex(SONG_COMPOSER);
			final int songTrackColumn = ih.getColumnIndex(SONG_TRACK);
			final int songDurationColumn = ih.getColumnIndex(SONG_DURATION);
			final int songYearColumn = ih.getColumnIndex(SONG_YEAR);
			final int songDateAddedColumn = ih.getColumnIndex(SONG_DATE_ADDED);
			final int songMimeTypeColumn = ih.getColumnIndex(SONG_MIME_TYPE);
			final int songDataColumn = ih.getColumnIndex(SONG_DATA);

			@SuppressWarnings("unused")
			int x = 0;

			try {
				// Get the InsertHelper ready to insert a single row
				ih.prepareForInsert();

				// Add the data for each column
				ih.bind(songTitleColumn, songTitle);
				ih.bind(songKeyColumn, songKey);
				ih.bind(songDisplayNameColumn, songDisplayName);
				ih.bind(songArtistKeyColumn, songArtistKey);
				ih.bind(songAlbumKeyColumn, songAlbumKey);
				ih.bind(songComposerComlumn, songComposer);
				ih.bind(songTrackColumn, songTrack);
				ih.bind(songDurationColumn, songDuration);
				ih.bind(songYearColumn, songYear);
				ih.bind(songDateAddedColumn, songDateAdded);
				ih.bind(songMimeTypeColumn, songMimeType);
				ih.bind(songDataColumn, songData);

				// Insert the row into the database.
				ih.execute();
				x++;
			} finally {
				ih.close();
				close();
			}
		}
	}

	/********** SELECT QUERIES **********/

	public List<Artist> getAllArtists() {
		List<Artist> artistList = new ArrayList<Artist>();

		open();
		Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_ARTISTS + " ORDER BY " + ARTIST
				+ " COLLATE NOCASE ASC", null);
		try {
			while (c.moveToNext()) {
				Integer artistId = c.getInt(c.getColumnIndexOrThrow(ARTIST_ID));
				String artistName = c.getString(c.getColumnIndexOrThrow(ARTIST));
				String artistKey = c.getString(c.getColumnIndexOrThrow(ARTIST_KEY));
				Integer numberOfAlbums = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_ALBUMS));

				List<Album> albumList = getAllAlbumsForArtist(artistName);

				Artist artist = new Artist(artistId, artistName, artistKey, numberOfAlbums, albumList);
				artistList.add(artist);
			}

			if (artistList.isEmpty()) {
				Log.i(TAG, "No artists");
				close();
				return null;
			} else
				return artistList;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
	}

	public List<Album> getAllAlbums() {
		List<Album> albumList = new ArrayList<Album>();

		open();
		Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_ALBUMS + " ORDER BY " + ALBUM, null);
		try {
			while (c.moveToNext()) {
				Integer albumId = c.getInt(c.getColumnIndexOrThrow(ALBUM_ID));
				String albumName = c.getString(c.getColumnIndexOrThrow(ALBUM));
				String albumKey = c.getString(c.getColumnIndexOrThrow(ALBUM_KEY));
				String albumArtist = c.getString(c.getColumnIndexOrThrow(ALBUM_ARTIST));
				Integer numberOfSongs = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_SONGS));
				Integer firstYear = c.getInt(c.getColumnIndexOrThrow(FIRST_YEAR));
				Integer lastYear = c.getInt(c.getColumnIndexOrThrow(LAST_YEAR));
				String albumArt = c.getString(c.getColumnIndexOrThrow(ALBUM_ART));
				// List<Song> songList = getAllSongsForAlbum(albumKey);
				Album album = new Album(albumId, albumName, albumKey, albumArtist, numberOfSongs, firstYear, lastYear,
						albumArt, null);
				albumList.add(album);
			}
			if (albumList.isEmpty()) {
				Log.i(TAG, "No albums");
				return null;
			} else
				return albumList;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
	}

	public List<Song> getAllSongs() {
		List<Song> songList = new ArrayList<Song>();

		open();
		Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_SONGS + " ORDER BY " + SONG_TITLE, null);
		try {
			while (c.moveToNext()) {
				Integer songId = c.getInt(c.getColumnIndexOrThrow(SONG_ID));
				String songTitle = c.getString(c.getColumnIndexOrThrow(SONG_TITLE));
				String songKey = c.getString(c.getColumnIndexOrThrow(SONG_KEY));
				String songDisplayName = c.getString(c.getColumnIndexOrThrow(SONG_DISPLAY_NAME));
				String songArtist = c.getString(c.getColumnIndexOrThrow(SONG_ARTIST));
				String songArtistKey = c.getString(c.getColumnIndexOrThrow(SONG_ARTIST_KEY));
				String songAlbum = c.getString(c.getColumnIndexOrThrow(SONG_ALBUM));
				String songAlbumKey = c.getString(c.getColumnIndexOrThrow(SONG_ALBUM_KEY));
				String songComposer = c.getString(c.getColumnIndexOrThrow(SONG_COMPOSER));
				Integer songTrack = c.getInt(c.getColumnIndexOrThrow(SONG_TRACK));
				Integer songDuration = c.getInt(c.getColumnIndexOrThrow(SONG_DURATION));
				Integer songYear = c.getInt(c.getColumnIndexOrThrow(SONG_YEAR));
				Integer songDateAdded = c.getInt(c.getColumnIndexOrThrow(SONG_DATE_ADDED));
				String songMimeType = c.getString(c.getColumnIndexOrThrow(SONG_MIME_TYPE));
				String songData = c.getString(c.getColumnIndexOrThrow(SONG_DATA));
				Song song = new Song(songId, songTitle, songKey, songDisplayName, songArtist, songArtistKey, songAlbum,
						songAlbumKey, songComposer, songTrack, songDuration, songYear, songDateAdded, songMimeType,
						songData);
				songList.add(song);
			}
			if (songList.isEmpty()) {
				Log.i(TAG, "No songs");
				return null;
			} else
				return songList;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
	}

	public List<Album> getAllAlbumsForArtist(String artist) {
		List<Album> albumList = new ArrayList<Album>();

		open();
		Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_ALBUMS + " WHERE " + ALBUM_ARTIST
				+ " = ? ORDER BY " + ALBUM, new String[] { artist });
		try {
			while (c.moveToNext()) {
				Integer albumId = c.getInt(c.getColumnIndexOrThrow(ALBUM_ID));
				String albumName = c.getString(c.getColumnIndexOrThrow(ALBUM));
				String albumKey = c.getString(c.getColumnIndexOrThrow(ALBUM_KEY));
				String albumArtist = c.getString(c.getColumnIndexOrThrow(ALBUM_ARTIST));
				Integer numberOfSongs = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_SONGS));
				Integer firstYear = c.getInt(c.getColumnIndexOrThrow(FIRST_YEAR));
				Integer lastYear = c.getInt(c.getColumnIndexOrThrow(LAST_YEAR));
				String albumArt = c.getString(c.getColumnIndexOrThrow(ALBUM_ART));
				// List<Song> songList = getAllSongsForAlbum(albumKey);
				Album album = new Album(albumId, albumName, albumKey, albumArtist, numberOfSongs, firstYear, lastYear,
						albumArt, null);
				albumList.add(album);
			}
			if (albumList.isEmpty()) {
				return null;
			} else
				return albumList;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
	}

	public List<Song> getAllSongsForAlbum(String albumKey) {
		List<Song> songList = new ArrayList<Song>();

		open();
		Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_SONGS + " WHERE " + SONG_ALBUM_KEY
				+ " = ? ORDER BY " + SONG_TRACK, new String[] { albumKey });
		try {
			while (c.moveToNext()) {
				Integer songId = c.getInt(c.getColumnIndexOrThrow(SONG_ID));
				String songTitle = c.getString(c.getColumnIndexOrThrow(SONG_TITLE));
				String songKey = c.getString(c.getColumnIndexOrThrow(SONG_KEY));
				String songDisplayName = c.getString(c.getColumnIndexOrThrow(SONG_DISPLAY_NAME));
				String songArtist = c.getString(c.getColumnIndexOrThrow(SONG_ARTIST));
				String songArtistKey = c.getString(c.getColumnIndexOrThrow(SONG_ARTIST_KEY));
				String songAlbum = c.getString(c.getColumnIndexOrThrow(SONG_ALBUM));
				String songAlbumKey = c.getString(c.getColumnIndexOrThrow(SONG_ALBUM_KEY));
				String songComposer = c.getString(c.getColumnIndexOrThrow(SONG_COMPOSER));
				Integer songTrack = c.getInt(c.getColumnIndexOrThrow(SONG_TRACK));
				Integer songDuration = c.getInt(c.getColumnIndexOrThrow(SONG_DURATION));
				Integer songYear = c.getInt(c.getColumnIndexOrThrow(SONG_YEAR));
				Integer songDateAdded = c.getInt(c.getColumnIndexOrThrow(SONG_DATE_ADDED));
				String songMimeType = c.getString(c.getColumnIndexOrThrow(SONG_MIME_TYPE));
				String songData = c.getString(c.getColumnIndexOrThrow(SONG_DATA));
				Song song = new Song(songId, songTitle, songKey, songDisplayName, songArtist, songArtistKey, songAlbum,
						songAlbumKey, songComposer, songTrack, songDuration, songYear, songDateAdded, songMimeType,
						songData);
				songList.add(song);
			}
			if (songList.isEmpty()) {
				Log.i(TAG, "No songs");
				return null;
			} else
				return songList;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
	}

	/********** SEARCH QUERIES **********/

	public String getAlbumArt(String albumKey) {
		String albumArt = null;

		open();
		Cursor c = database.rawQuery("SELECT " + ALBUM_ART + " FROM " + DATABASE_TABLE_ALBUMS + " WHERE " + ALBUM_KEY
				+ " = ?", new String[] { albumKey });
		try {
			while (c.moveToNext()) {
				albumArt = c.getString(c.getColumnIndexOrThrow(ALBUM_ART));
			}
			if (albumArt == null) {
				Log.i(TAG, "No album art");
				return null;
			} else
				return albumArt;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
	}

	/********** OBJECT EXISTS QUERIES **********/

	private boolean artistExists(Artist artist) {
		open();
		try {
			Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_ARTISTS + " WHERE " + ARTIST_KEY + " = ?",
					new String[] { artist.getArtistKey() });
			c.moveToFirst();
			if (c.getCount() > 0) {
				c.close();
				close();
				return true;
			} else
				c.close();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			close();
		}
		return false;
	}

	public boolean albumExists(Album album) {
		open();
		try {
			Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_ALBUMS + " WHERE " + ALBUM_KEY + " = ?",
					new String[] { album.getAlbumKey() });
			c.moveToFirst();
			if (c.getCount() > 0) {
				while (c.moveToNext()) {
					String albumArtist = c.getString(c.getColumnIndexOrThrow(ALBUM_ARTIST));
					if (albumArtist.equals(album.getArtist())) {
						c.close();
						close();
						return true;
					}
				}
				c.close();
				close();
				return false;
			} else
				c.close();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			close();
		}
		return false;
	}

	private boolean songExists(Song song) {
		open();
		try {
			Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_SONGS + " WHERE " + SONG_KEY + " = ?",
					new String[] { song.getTitleKey() });
			c.moveToFirst();
			if (c.getCount() > 0) {
				c.close();
				close();
				return true;
			} else
				c.close();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			close();
		}
		return false;
	}

}

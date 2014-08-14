package com.awesome.Data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.awesome.Dto.Album;
import com.awesome.Dto.Artist;
import com.awesome.Dto.Song;

@SuppressWarnings("deprecation")
public class MediaDatabase {
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

	public MediaDatabase() {

	}

	/**
	 * Creates an instance of MusicDatabase. Used to insert and access data for the application.
	 * 
	 * @param context
	 *            The context of the activity to use this database.
	 */
	public MediaDatabase(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * Open the database for the Music Library for reading only.
	 * 
	 * @return A read-only database.
	 * @throws android.database.SQLException
	 */
	public MediaDatabase openToRead() throws android.database.SQLException {
		database = dbHelper.getReadableDatabase();
		return this;
	}

	/**
	 * Opens the database for the Music Library for writing.
	 * 
	 * @return A writable database.
	 * @throws android.database.SQLException
	 */
	public MediaDatabase open() throws android.database.SQLException {
		database = dbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close the open database object.
	 */
	public void close() {
		dbHelper.close();
	}

	/********** INSERT QUERIES **********/

	/**
	 * Insert an artist into the database.
	 * 
	 * @param artist
	 *            The artist to insert.
	 */
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

	/**
	 * Insert an album into the database.
	 * 
	 * @param album
	 *            The album to insert.
	 */
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

			// Get the numeric indexes for each of the columns that we're updating
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
			} catch (Exception e) {
				Log.i(TAG, e.getMessage(), e);
			} finally {
				ih.close();
				close();
			}
		}
	}

	/**
	 * Inserts a song into the database.
	 * 
	 * @param song
	 *            The song to insert.
	 */
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
			final String songArtist = song.getArtist();
			final String songArtistKey = song.getArtistKey();
			final String songAlbum = song.getAlbum();
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
			final int songArtistColumn = ih.getColumnIndex(SONG_ARTIST);
			final int songArtistKeyColumn = ih.getColumnIndex(SONG_ARTIST_KEY);
			final int songAlbumColumn = ih.getColumnIndex(SONG_ALBUM);
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
				ih.bind(songArtistColumn, songArtist);
				ih.bind(songArtistKeyColumn, songArtistKey);
				ih.bind(songAlbumColumn, songAlbum);
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

	/**
	 * Used to get all artists in the database. A list of all albums by each artist will also be generated here.
	 * 
	 * @return A list of all artists and their albums.
	 */
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
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return artistList;
	}

	/**
	 * Used to get all albums in the database.
	 * 
	 * @return A list of all albums in the database.
	 */
	public List<Album> getAllAlbums() {
		List<Album> albumList = new ArrayList<Album>();
		List<String> artistList = new ArrayList<String>();			// List of all artists that match the album
		List<Integer> numberOfSongsList = new ArrayList<Integer>();	// List of number of songs that match that album

		open();
		Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_ALBUMS + " GROUP BY " + ALBUM_KEY + " ORDER BY "
				+ ALBUM, null);
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

				// Add the album artist and number of songs to the list of all for that album
				artistList.add(albumArtist);
				numberOfSongsList.add(numberOfSongs);
				Album album = new Album(albumId, albumName, albumKey, albumArtist, numberOfSongs, firstYear, lastYear,
						albumArt, null);
				if (artistList.contains(albumArtist)) {
					albumList.add(album);
				}
			}
			if (albumList.isEmpty()) {
				Log.i(TAG, "No albums");
				return null;
			} else
				return albumList;
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return albumList;
	}

	/**
	 * Used to get all songs in the database.
	 * 
	 * @return A list of all songs in the database.
	 */
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
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return songList;
	}

	/**
	 * Used to get all albums by a particular artist.
	 * 
	 * @param artist
	 *            The artist who you want to get albums for.
	 * @return A list of all albums by a particular artist.
	 */
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
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return albumList;
	}

	/**
	 * Used to get all the songs that are contained on an album.
	 * 
	 * @param album
	 *            The album that you want get a list of songs from.
	 * @return A list of songs contained on an album.
	 */
	public List<Song> getAllSongsForAlbum(Album album) {
		List<Song> songList = new ArrayList<Song>();

		open();
		Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_SONGS + " WHERE " + SONG_ALBUM_KEY
				+ " = ? ORDER BY " + SONG_TRACK, new String[] { album.getAlbumKey() });
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
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return songList;
	}

	/********** SEARCH QUERIES **********/

	/**
	 * Used to retrieve album art from the database.
	 * 
	 * @param albumKey
	 *            The key for the album to get artwork for.
	 * @return The path for album art.
	 */
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
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return albumArt;
	}

	/********** OBJECT EXISTS QUERIES **********/

	/**
	 * Method for checking whether an artist exists in the database.
	 * 
	 * @param artist
	 *            The artist to check the database for.
	 * @return True if artist is in database, false if not yet in database.
	 */
	private boolean artistExists(Artist artist) {
		open();
		Cursor c = null;
		try {
			c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_ARTISTS + " WHERE " + ARTIST_KEY + " = ?",
					new String[] { artist.getArtistKey() });
			c.moveToFirst();
			if (c.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return false;
	}

	/**
	 * Method for checking whether an album exists in the database.
	 * If the album to check has an artist that matches in the database, and also has a matching number of songs, then
	 * the album exists.
	 * 
	 * @param album
	 *            The album to check the database for.
	 * @return True if album is in database, false if not yet in database.
	 */
	public boolean albumExists(Album album) {
		open();
		Cursor c = null;
		try {
			c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_ALBUMS + " WHERE " + ALBUM + " = ?",
					new String[] { album.getAlbum() });
			List<String> artistList = new ArrayList<String>();			// List of all artists that match the album
			List<Integer> numberOfSongsList = new ArrayList<Integer>();	// List of number of songs that match that album
			if (c.getCount() > 0) {	// If there are results from query
				while (c.moveToNext()) {
					// Add the album artist and number of songs to the list of all for that album
					String albumArtist = c.getString(c.getColumnIndexOrThrow(ALBUM_ARTIST));
					Integer numberOfSongs = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_SONGS));
					artistList.add(albumArtist);
					numberOfSongsList.add(numberOfSongs);
				}
				// If the album to check is in the artist or number of songs list, then the album exists
				if (artistList.contains(album.getArtist()) && numberOfSongsList.contains(album.getNumberOfSongs())) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return false;
	}

	/**
	 * Method for checking whether a song exists in the database.
	 * 
	 * @param song
	 *            The song to check the database for.
	 * @return True if song is in database, false if not yet in database.
	 */
	private boolean songExists(Song song) {
		open();
		Cursor c = null;
		try {
			c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE_SONGS + " WHERE " + SONG_KEY + " = ?",
					new String[] { song.getTitleKey() });
			c.moveToFirst();
			if (c.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return false;
	}

}

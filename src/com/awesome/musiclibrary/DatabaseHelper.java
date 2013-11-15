package com.awesome.musiclibrary;

/**
 * Edward McNealy Music Library - DataBaseHelper Class Helper class that is used
 * to connect to the internal database Contains quieres for adding, retrieving,
 * updating, and deleting content
 * 
 * April 25, 2013
 * 
 * Some source files: http://www.sqlite.org/foreignkeys.html#fk_enable%3E
 * http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-
 * applications/
 * http://www.codeproject.com/Articles/119293/Using-SQLite-Database-with-Android
 */

// Should I change all queries to be rawqueires?

/**
 * Code taken from:
 * http://www.reigndesign.com/blog/using-your-own-sqlite-database
 * -in-android-applications/
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Genre;

@SuppressLint("SdCardPath")
@SuppressWarnings("deprecation")
public class DatabaseHelper extends SQLiteOpenHelper {
	private static String TAG = "DatabaseHelper";

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/droid.musiclibrary/databases/";
	// Database Name
	private static String DB_NAME = "library.db";
	// Database Version
	private static final int DB_VERSION = 31;
	// SQLite Database object
	private SQLiteDatabase db;

	// Database table artists
	private static final String DATABASE_TABLE_ARTISTS = "artists";
	private static final String KEY_ARTIST_ROWID = "_id";
	private static final String KEY_ARTIST = "Artist";

	// Database table genres
	private static final String DATABASE_TABLE_GENRES = "genres";
	private static final String KEY_GENRE_ROWID = "_id";
	private static final String KEY_GENRE = "Genre";

	// Database table albums
	private static final String DATABASE_TABLE_ALBUMS = "albums";
	private static final String KEY_ALBUM_ROWID = "_id";
	private static final String KEY_ALBUM = "Album";
	private static final String KEY_ALBUM_ARTISTID = "_artistid";
	private static final String KEY_ALBUM_GENREID = "_genreid";
	private static final String KEY_ALBUM_YEAR = "Year";

	// Database table songs
	private static final String DATABASE_TABLE_SONGS = "songs";
	private static final String KEY_SONG_ROWID = "_id";
	private static final String KEY_SONG = "Song";
	private static final String KEY_SONG_ALBUMID = "_albumid";

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.myContext = context;
	}

	public interface Listener {
		public void progress(int completed, int total);

		public void complete(boolean success, Exception result);
	}

	public void open() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		this.getReadableDatabase();
		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS
				| SQLiteDatabase.CREATE_IF_NECESSARY);
		db.execSQL("PRAGMA foreign_keys=ON;"); // Enable Foreign Keys here so
												// they are enabled each time
												// you open database
	}

	@Override
	public synchronized void close() {
		if (db != null)
			db.close();
		super.close();
	}

	public void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}// end of copyDataBase() method

	@Override
	public void onCreate(SQLiteDatabase db) {
		dropTables(db);
		String createArtistTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_ARTISTS + "(" + KEY_ARTIST_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_ARTIST + " TEXT NOT NULL UNIQUE)";
		db.execSQL(createArtistTable);
		Log.e("MESSAGE", "Creating table for artists: \n" + createArtistTable);

		String createGenreTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_GENRES + "(" + KEY_GENRE_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_GENRE + " TEXT NOT NULL UNIQUE)";
		db.execSQL(createGenreTable);
		Log.e("MESSAGE", "Creating table for genres: \n" + createGenreTable);

		String createAlbumTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_ALBUMS + "(" + KEY_ALBUM_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_ALBUM
				+ " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE, " + KEY_ALBUM_ARTISTID + " INTEGER REFERENCES "
				+ DATABASE_TABLE_ARTISTS + "(" + KEY_ARTIST_ROWID + ") ON DELETE CASCADE ON UPDATE CASCADE, "
				+ KEY_ALBUM_GENREID + " INTEGER REFERENCES " + DATABASE_TABLE_GENRES + "(" + KEY_GENRE_ROWID
				+ ") ON DELETE CASCADE ON UPDATE CASCADE, " + KEY_ALBUM_YEAR + " TEXT)";
		db.execSQL(createAlbumTable);
		Log.e("MESSAGE", "Creating table for albums: \n" + createAlbumTable);

		String createSongTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_SONGS + "(" + KEY_SONG_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_SONG
				+ " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE, " + KEY_SONG_ALBUMID + " INTEGER REFERENCES "
				+ DATABASE_TABLE_ALBUMS + "(" + KEY_ALBUM_ROWID + ") ON DELETE CASCADE ON UPDATE CASCADE)";
		db.execSQL(createSongTable);
		Log.e("MESSAGE", "Creating table for songs: \n" + createSongTable);
	}

	public void dropTables(SQLiteDatabase db) {
		Log.e("MESSAGE", "Droping database tables");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ARTISTS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GENRES);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ALBUMS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SONGS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("MESSAGE", "Droping database tables");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ARTISTS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GENRES);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ALBUMS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SONGS);

		onCreate(db);
	}

	/********** Artist Table Queries **********/

	// Add an artist to artist table
	public long insertArtist(String artist) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ARTIST, artist);
		return db.insertWithOnConflict(DATABASE_TABLE_ARTISTS, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}

	// Delete an artist from table
	public boolean deleteArtist(long rowId) {
		return db.delete(DATABASE_TABLE_ARTISTS, KEY_ARTIST_ROWID + "=?", new String[] { String.valueOf(rowId) }) > 0;
	}

	public boolean artistExists(String artist) {
		open();
		String a = null;
		Cursor c = db
				.rawQuery("SELECT * FROM artists WHERE Artist = ?", new String[] { artist.toLowerCase(Locale.US) });
		while (c.moveToNext()) {
			a = c.getString(0);
		}
		if (a == null) {
			close();
			return false;
		} else {
			close();
			return true;
		}
	}

	// Return all artists from the database
	public ArrayList<String> getAllArtists() {
		open();
		ArrayList<String> artists = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT Artist FROM artists ORDER BY Artist", null);
		while (c.moveToNext()) {
			String artist = c.getString(0);
			artists.add(artist);
		}
		if (artists.isEmpty()) { // Check if the database for artists is empty;
									// if it is, tell the user
			artists.add("No artists");
			return artists;
		}
		close();
		return artists;
	}

	// Returns an artist
	public String getArtist(long rowId) throws SQLException {
		open();
		String artist = null;
		if (rowId == 0)
			return "No artist";
		Cursor c = db
				.rawQuery("SELECT Artist FROM artists WHERE TRIM(_id) = ?", new String[] { String.valueOf(rowId) });
		while (c.moveToNext()) {
			artist = c.getString(0);
		}
		if (artist.equals("")) {
			return "No artist";
		}
		close();
		return artist;
	}

	// Returns an artist id
	public int getArtistID(String artist) throws SQLException {
		open();
		int artistID = 0;
		if (artist == null)
			return 0;
		Cursor c = db.rawQuery("SELECT _id FROM artists WHERE TRIM(Artist) = ?", new String[] { artist });
		while (c.moveToNext()) {
			artistID = c.getInt(0);
		}
		close();
		return artistID;
	}

	// Update an artist
	public boolean updateArtist(long rowId, String artist) {
		ContentValues args = new ContentValues();
		args.put(KEY_ARTIST, artist);
		return db.update(DATABASE_TABLE_ARTISTS, args, KEY_ARTIST_ROWID + "=" + rowId, null) > 0;
	}

	/********** Genre Table Queries *********/

	// Add a genre to genre table
	public long insertGenre(String genre) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_GENRE, genre);
		return db.insertWithOnConflict(DATABASE_TABLE_GENRES, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}

	// Delete a genre from table
	public boolean deleteGenre(int rowId) {
		return db.delete(DATABASE_TABLE_GENRES, KEY_GENRE_ROWID + "=?", new String[] { String.valueOf(rowId) }) > 0;
	}

	// Returns all genres in the database
	public ArrayList<String> getAllGenres() {
		open();
		ArrayList<String> genres = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT Genre FROM genres ORDER BY Genre", null);
		while (c.moveToNext()) {
			String genre = c.getString(0);
			genres.add(genre);
		}
		if (genres.isEmpty()) { // Check if the database for genres is empty; if
								// it is, tell the user
			genres.add("No genres");
			return genres;
		}
		close();
		return genres;
	}

	// Returns a genre
	public String getGenre(long rowId) throws SQLException {
		open();
		String genre = null;
		if (rowId == 0)
			return "No genre";
		Cursor c = db.rawQuery("SELECT Genre FROM genres WHERE TRIM(_id) = ?", new String[] { String.valueOf(rowId) });
		while (c.moveToNext()) {
			genre = c.getString(0);
		}
		if (genre.equals("")) {
			return "No genre";
		}
		close();
		return genre;
	}

	// Returns a genre id
	public int getGenreID(String genre) throws SQLException {
		open();
		int genreID = 0;
		if (genre == null)
			return 0;
		Cursor c = db.rawQuery("SELECT _id FROM genres WHERE TRIM(Genre) = ?", new String[] { genre });
		while (c.moveToNext()) {
			genreID = c.getInt(0);
		}
		close();
		return genreID;
	}

	// Update a genre
	public boolean updateGenre(long rowId, String genre) {
		ContentValues args = new ContentValues();
		args.put(KEY_GENRE, genre);
		return db.update(DATABASE_TABLE_GENRES, args, KEY_GENRE_ROWID + "=" + rowId, null) > 0;
	}

	/********** Album Table Queries **********/

	public void insertAlbum(String album, int artistID, int genreID, String year) {
		open();
		// Create a single InsertHelper to handle this set of insertions.
		InsertHelper ih = new InsertHelper(db, DATABASE_TABLE_ALBUMS);

		// Get the numeric indexes for each of the columns that we're updating
		final int albumColumn = ih.getColumnIndex(KEY_ALBUM);
		final int artistIDColumn = ih.getColumnIndex(KEY_ALBUM_ARTISTID);
		final int genreIDColumn = ih.getColumnIndex(KEY_ALBUM_GENREID);
		final int yearColumn = ih.getColumnIndex(KEY_ALBUM_YEAR);
		@SuppressWarnings("unused")
		int x = 0;

		try {
			// Get the InsertHelper ready to insert a single row
			ih.prepareForInsert();

			// Add the data for each column
			ih.bind(albumColumn, album);
			ih.bind(artistIDColumn, artistID);
			ih.bind(genreIDColumn, genreID);
			ih.bind(yearColumn, year);

			// Insert the row into the database.
			ih.execute();
			x++;
		} finally {
			ih.close();
		}
		close();
	}

	// Delete an album from table
	public boolean deleteAlbum(long rowId) {
		return db.delete(DATABASE_TABLE_ALBUMS, KEY_ALBUM_ROWID + "=?", new String[] { String.valueOf(rowId) }) > 0;
	}

	// Returns all albums in the database
	public ArrayList<String> getAllAlbums() {
		open();
		ArrayList<String> albums = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT Album FROM albums ORDER BY Album", null);
		while (c.moveToNext()) {
			String album = c.getString(0);
			albums.add(album);
		}
		if (albums.isEmpty()) { // Check if the database for albums is empty; if
								// it is, tell the user
			albums.add("No albums");
		}
		close();
		return albums;
	}

	// Returns an album
	public String getAlbum(long rowId) throws SQLException {
		open();
		String album = null;
		if (rowId == 0)
			return "No album";
		Cursor c = db.rawQuery("SELECT Album FROM albums WHERE _id = ?", new String[] { String.valueOf(rowId) });
		while (c.moveToNext()) {
			album = c.getString(0);
		}
		if (album.equals("")) {
			return "No album";
		}
		close();
		return album;
	}

	// Returns an artist id
	public int getAlbumID(String album) throws SQLException {
		open();
		int albumID = 0;
		if (album == null)
			return 0;
		Cursor c = db.rawQuery("SELECT _id FROM albums WHERE TRIM(Album) = ?", new String[] { album });
		while (c.moveToNext()) {
			albumID = c.getInt(0);
		}
		close();
		return albumID;
	}

	// Update an album
	public boolean updateAlbum(long rowId, String album, String artist, String genre, String year) {
		// Remember to re- openDataBase() after getting ids for artist and genre
		ContentValues args = new ContentValues();
		args.put(KEY_ALBUM, album);
		args.put(KEY_ALBUM_ARTISTID, getArtistID(artist));
		open();
		args.put(KEY_ALBUM_GENREID, getGenreID(genre));
		open();
		args.put(KEY_ALBUM_YEAR, year);
		return db.update(DATABASE_TABLE_ALBUMS, args, KEY_ALBUM_ROWID + "=" + rowId, null) > 0;
	}

	/********** Song Table Queries *********/

	// Add a song to song table
	public void insertSong(String song, int albumID) {
		open();
		// Create a single InsertHelper to handle this set of insertions.
		InsertHelper ih = new InsertHelper(db, DATABASE_TABLE_SONGS);

		// Get the numeric indexes for each of the columns that we're updating
		final int titleColumn = ih.getColumnIndex(KEY_SONG);
		final int albumIDColumn = ih.getColumnIndex(KEY_SONG_ALBUMID);
		@SuppressWarnings("unused")
		int x = 0;

		try {
			// Get the InsertHelper ready to insert a single row
			ih.prepareForInsert();

			// Add the data for each column
			ih.bind(titleColumn, song);
			ih.bind(albumIDColumn, albumID);

			// Insert the row into the database.
			ih.execute();
			x++;
		} finally {
			ih.close();
		}
		close();
	}

	// Delete a song from table
	public boolean deleteSong(int rowId) {
		return db.delete(DATABASE_TABLE_SONGS, KEY_SONG_ROWID + "=?", new String[] { Integer.toString(rowId) }) > 0;
	}

	// Returns all songs in the database
	public ArrayList<String> getAllSongs() {
		open();
		ArrayList<String> titles = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT Song FROM songs ORDER BY Song", null);
		while (c.moveToNext()) {
			String title = c.getString(0);
			titles.add(title);
		}
		if (titles.isEmpty()) { // Check if the database for songs is empty; if
								// it is, tell the user
			titles.add("No songs");
		}
		close();
		return titles;
	}

	public ArrayList<String> getAllSongsInAlbum(int albumID) {
		open();
		ArrayList<String> titles = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT Song From songs WHERE TRIM(_albumid) = ?",
				new String[] { String.valueOf(albumID) });
		while (c.moveToNext()) {
			String title = c.getString(0);
			titles.add(title);
		}
		if (titles.isEmpty()) {
			titles.add("No songs");
		}
		close();
		return titles;
	}

	// Returns a song
	public String getSong(long rowId) throws SQLException {
		open();
		String title = null;
		if (rowId == 0)
			return "No song";
		Cursor c = db.rawQuery("SELECT Song FROM songs WHERE TRIM(_id) = ?", new String[] { String.valueOf(rowId) });
		while (c.moveToNext()) {
			title = c.getString(0);
		}
		if (title.equals("")) {
			return "No song";
		}
		close();
		return title;
	}

	// Returns a song id
	public int getSongID(String song) throws SQLException {
		open();
		int songID = 0;
		if (song == null)
			return 0;
		Cursor c = db.rawQuery("SELECT _id FROM songs WHERE TRIM(Song) = ?", new String[] { song });
		while (c.moveToNext()) {
			songID = c.getInt(0);
		}
		close();
		return songID;
	}

	// Update a song
	public boolean updateSong(long rowId, String song, String album) {
		ContentValues args = new ContentValues();
		args.put(KEY_SONG, song);
		args.put(KEY_SONG_ALBUMID, getAlbumID(album));
		open(); // Database must be reopened here because it is closed in
				// getAlbumID()
		return db.update(DATABASE_TABLE_SONGS, args, KEY_SONG_ROWID + "=" + rowId, null) > 0;
	}

	/********** Other Queries **********/

	// Return an array of all artists with all their albums
	public ArrayList<String> getAlbumTitlesByArtist(String artist) {
		int artistID = getArtistID(artist);
		Cursor c = null;
		ArrayList<String> titles = new ArrayList<String>();
		open();
		c = db.rawQuery("SELECT Album FROM albums WHERE _artistid = ? ORDER BY Album",
				new String[] { String.valueOf(artistID) });
		while (c.moveToNext()) {
			String title = c.getString(0);
			titles.add(title);
		}
		if (titles.isEmpty()) { // Check if the database for albums is empty; if
								// it is, tell the user
			titles.add("No albums by this artist yet");
		}
		close();
		return titles;
	}

	// Return an array of all artists with all their albums
	public ArrayList<String> getAlbumTitlesByGenre(String genre) {
		int genreID = getGenreID(genre);
		Cursor c = null;
		ArrayList<String> titles = new ArrayList<String>();
		open();
		c = db.rawQuery("SELECT Album FROM albums WHERE _genreid = ? ORDER BY Album",
				new String[] { String.valueOf(genreID) });
		while (c.moveToNext()) {
			String title = c.getString(0);
			titles.add(title);
		}
		close();
		return titles;
	}

	// Return an Array list of Artist objects
	public ArrayList<Artist> getArtistAndTitles() {
		open();
		int artistId = 0;
		Artist artist = null;
		ArrayList<Artist> artistList = new ArrayList<Artist>();
		ArrayList<String> titles = new ArrayList<String>();
		try {
			Cursor c = db.rawQuery("SELECT Artist FROM artists ORDER BY Artist COLLATE NOCASE", null);
			while (c.moveToNext()) {
				artistId = c.getInt(0);
				String artistName = c.getString(1);
				String artistKey = c.getString(2);
				int numberOfAlbums = c.getInt(3);
				titles = getAlbumTitlesByArtist(artistName);
				if (titles.isEmpty()) { // Check if the database for song titles
										// is empty; if it is, tell the user
					titles.add("No albums by this artist yet");
				}

				/*
				 * artist = new Artist(artistId, artistName, artistKey,
				 * numberOfAlbums);
				 */
				artistList.add(artist);
			}

			if (artistId == 0) {
				String artistName = "No artists";
				// artist = new Artist(artistId, artistName, null, null);
				artistList.add(artist);
				close();
				return artistList;
			}
		} catch (Exception ex) {
			Log.i(TAG, ex.getStackTrace().toString());
		}
		close();
		return artistList;
	}

	// Return an Array list of Album objects
	public Album getAlbumInfo(String title) {
		Album album = null;
		int artistId = 0, genreId = 0;
		String artist, genre, year = null;

		open();
		Cursor c = db.rawQuery("SELECT * FROM albums WHERE Album = ?", new String[] { title });
		while (c.moveToNext()) {
			artistId = c.getInt(c.getColumnIndex("_artistid"));
			genreId = c.getInt(c.getColumnIndex("_genreid"));
			year = c.getString(c.getColumnIndex("Year"));
		}
		artist = getArtist(artistId);
		genre = getGenre(genreId);

		// album = new Album(title, artist, genre, year);
		close();
		return album;
	}

	// Return an Array list of Genre objects
	public ArrayList<Genre> getGenreAndAlbums() {
		Genre genre = null;
		ArrayList<Genre> genreList = new ArrayList<Genre>();
		String genreName = null;
		ArrayList<String> titles = new ArrayList<String>();
		open();
		Cursor c = db.rawQuery("SELECT Genre FROM genres ORDER BY Genre", null);
		while (c.moveToNext()) {
			genreName = c.getString(0);
			titles = getAlbumTitlesByGenre(genreName);
			if (titles.isEmpty()) { // Check if the database for song titles is
									// empty; if it is, tell the user
				titles.add("No albums in this genre yet");
			}

			genre = new Genre(genreName, titles);
			genreList.add(genre);
		}
		if (genreName == null) {
			genreName = "No genres";
			genre = new Genre(genreName, titles);
			genreList.add(genre);
			close();
			return genreList;
		}
		close();
		return genreList;
	}

	// Return the album containing a song
	public String getSongAlbum(String title) {
		int albumID = 0;
		String album = null;

		open();
		Cursor c = db.rawQuery("SELECT _albumid FROM songs WHERE Song = ?", new String[] { title });
		while (c.moveToNext()) {
			albumID = c.getInt(0);
		}
		Cursor c2 = db.rawQuery("SELECT Album FROM albums WHERE _id = ?", new String[] { String.valueOf(albumID) });
		while (c2.moveToNext()) {
			album = c2.getString(0);
		}
		close();
		return album;
	}
}

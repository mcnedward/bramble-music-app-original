package com.awesome.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A helper class that is used to create, update, and delete the database for
 * the Music Library
 * 
 * @author Edward
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static String TAG = "DatabaseAdapter";

	// Database Name
	public static String DB_NAME = "library.db";
	// Database Version - Increment this number in order to upgrade the database
	public static final int DB_VERSION = 18;

	/** Artist Table Variables **/
	public final static String ARTISTS_TABLE = "Artists";
	public final static String ARTIST_ID = "_id";
	public final static String ARTIST = "Artist";
	public final static String ARTIST_KEY = "ArtistKey";
	public final static String NUMBER_OF_ALBUMS = "NumberOfAlbums";

	/** Album Table Variables **/
	public final static String ALBUMS_TABLE = "Albums";
	public final static String ALBUM_ID = "_id";
	public final static String ALBUM = "Album";
	public final static String ALBUM_KEY = "AlbumKey";
	public final static String ALBUM_ARTIST_ID = "ArtistId";
	public final static String ALBUM_ARTIST = "Artist";
	public final static String NUMBER_OF_SONGS = "NumberOfSongs";
	public final static String FIRST_YEAR = "MinYear";
	public final static String LAST_YEAR = "MaxYear";
	public final static String ALBUM_ART = "AlbumArt";

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

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ARTISTS_TABLE + " ( "
				+ ARTIST_ID + " INTEGER PRIMARY KEY NOT NULL, " + ARTIST
				+ " TEXT, " + ARTIST_KEY + " TEXT, " + NUMBER_OF_ALBUMS
				+ " INTEGER)");

		db.execSQL("CREATE TABLE IF NOT EXISTS " + ALBUMS_TABLE + " ("
				+ ALBUM_ID + " PRIMARY KEY NOT NULL, " + ALBUM + " TEXT, " + ALBUM_KEY + " TEXT, ");

		// Primary key does not auto-increment for this table to allow for
		// proper checking of albumExists method
		String createAlbumTable = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY NOT NULL, %s TEXT, %s TEXT, %s INTEGER REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
						ALBUMS_TABLE, ALBUM_ID, ALBUM, ALBUM_KEY,
						ALBUM_ARTIST_ID, ARTISTS_TABLE, ARTIST_ID,
						ALBUM_ARTIST, NUMBER_OF_SONGS, FIRST_YEAR, LAST_YEAR,
						ALBUM_ART);

		String createSongTable = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY NOT NULL, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
						SONGS_TABLE, SONG_ID, SONG_TITLE, SONG_KEY,
						SONG_DISPLAY_NAME, SONG_ARTIST_ID, SONG_ALBUM_ID,
						SONG_COMPOSER, SONG_TRACK, SONG_DURATION, SONG_YEAR,
						SONG_DATE_ADDED, SONG_MIME_TYPE, SONG_DATA,
						SONG_IS_MUSIC);

		//Log.i(TAG, String.format("%s \n %s \n %s", createArtistTable,
		//		createAlbumTable, createSongTable));
		//db.execSQL(createArtistTable);
		db.execSQL(createAlbumTable);
		db.execSQL(createSongTable);
	}

	public void dropTables(SQLiteDatabase db) {
		Log.i(TAG, "Droping database tables");
		db.execSQL("DROP TABLE IF EXISTS " + ARTISTS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ALBUMS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SONGS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Droping database tables");
		db.execSQL("DROP TABLE IF EXISTS " + ARTISTS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ALBUMS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SONGS_TABLE);

		onCreate(db);
	}
}

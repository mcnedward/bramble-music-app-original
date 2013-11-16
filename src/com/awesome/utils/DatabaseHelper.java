package com.awesome.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A helper class that is used to create, update, and delete the database for the Music Library
 * 
 * @author Edward
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static String TAG = "DatabaseAdapter";

	// Database Name
	private static String DB_NAME = "library.db";
	// Database Version
	private static final int DB_VERSION = 9;	// Increment this number in order to upgrade the database

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
		String createArtistTable = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT, %s INTEGER)",
						DATABASE_TABLE_ARTISTS, ARTIST_ID, ARTIST, ARTIST_KEY, NUMBER_OF_ALBUMS);

		// Primary key does not auto-increment for this table to allow for proper checking of albumExists method
		String createAlbumTable = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY NOT NULL, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
						DATABASE_TABLE_ALBUMS, ALBUM_ID, ALBUM, ALBUM_KEY, ALBUM_ARTIST, NUMBER_OF_SONGS, FIRST_YEAR,
						LAST_YEAR, ALBUM_ART);

		String createSongTable = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT)",
						DATABASE_TABLE_SONGS, SONG_ID, SONG_TITLE, SONG_KEY, SONG_DISPLAY_NAME, SONG_ARTIST,
						SONG_ARTIST_KEY, SONG_ALBUM, SONG_ALBUM_KEY, SONG_COMPOSER, SONG_TRACK, SONG_DURATION,
						SONG_YEAR, SONG_DATE_ADDED, SONG_MIME_TYPE, SONG_DATA);

		Log.i(TAG, String.format("%s \n %s \n %s", createArtistTable, createAlbumTable, createSongTable));
		db.execSQL(createArtistTable);
		db.execSQL(createAlbumTable);
		db.execSQL(createSongTable);
	}

	public void dropTables(SQLiteDatabase db) {
		Log.i(TAG, "Droping database tables");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ARTISTS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ALBUMS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SONGS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Droping database tables");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ARTISTS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ALBUMS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SONGS);

		onCreate(db);
	}
}

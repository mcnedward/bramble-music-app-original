package com.awesome.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.util.Log;

/**
 * A helper class that is used to create, update, and delete the database for the Music Library
 * 
 * TODO Maybe this should be static?
 * 
 * @author Edward
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static String TAG = "DatabaseAdapter";

	// Database Name
	public static String DB_NAME = "library.db";
	// Database Version - Increment this number in order to upgrade the database
	public static final int DB_VERSION = 35;

	/** Artist Table Variables **/
	public final static String ARTISTS_TABLE = "Artists";
	public final static String ARTIST_ID = MediaStore.Audio.Artists._ID;
	public final static String ARTIST = MediaStore.Audio.Artists.ARTIST;
	public final static String ARTIST_KEY = MediaStore.Audio.Artists.ARTIST_KEY;
	public final static String NUMBER_OF_ALBUMS = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS;

	/** Album Table Variables **/
	public final static String ALBUMS_TABLE = "Albums";
	public final static String ALBUM_ID = MediaStore.Audio.Albums._ID;
	public final static String ALBUM = MediaStore.Audio.Albums.ALBUM;
	public final static String ALBUM_KEY = MediaStore.Audio.Albums.ALBUM_KEY;
	public final static String ALBUM_ARTIST = MediaStore.Audio.Albums.ARTIST;
	public final static String NUMBER_OF_SONGS = MediaStore.Audio.Albums.NUMBER_OF_SONGS;
	public final static String FIRST_YEAR = MediaStore.Audio.Albums.FIRST_YEAR;
	public final static String LAST_YEAR = MediaStore.Audio.Albums.LAST_YEAR;
	public final static String ALBUM_ART = MediaStore.Audio.Albums.ALBUM_ART;

	/** Song Table Variables **/
	public final static String SONGS_TABLE = "Songs";
	public final static String SONG_ID = MediaStore.Audio.Media._ID;
	public final static String SONG_TITLE = MediaStore.Audio.Media.TITLE;
	public final static String SONG_KEY = MediaStore.Audio.Media.TITLE_KEY;
	public final static String SONG_DISPLAY_NAME = MediaStore.Audio.Media.DISPLAY_NAME;
	public final static String SONG_ARTIST_ID = MediaStore.Audio.Media.ARTIST_ID;
	public final static String SONG_ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID;
	public final static String SONG_COMPOSER = MediaStore.Audio.Media.COMPOSER;
	public final static String SONG_TRACK = MediaStore.Audio.Media.TRACK;
	public final static String SONG_DURATION = MediaStore.Audio.Media.DURATION;
	public final static String SONG_YEAR = MediaStore.Audio.Media.YEAR;
	public final static String SONG_DATE_ADDED = MediaStore.Audio.Media.DATE_ADDED;
	public final static String SONG_MIME_TYPE = MediaStore.Audio.Media.MIME_TYPE;
	public final static String SONG_DATA = MediaStore.Audio.Media.DATA;
	public final static String SONG_IS_MUSIC = MediaStore.Audio.Media.IS_MUSIC;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to access to the application assets and
	 * resources.
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ARTISTS_TABLE + " ( " + ARTIST_ID
				+ " INTEGER PRIMARY KEY NOT NULL, " + ARTIST + " TEXT, " + ARTIST_KEY + " TEXT, " + NUMBER_OF_ALBUMS
				+ " INTEGER)");

		db.execSQL("CREATE TABLE IF NOT EXISTS " + ALBUMS_TABLE + " (" + ALBUM_ID + " INTEGER PRIMARY KEY NOT NULL, "
				+ ALBUM + " TEXT, " + ALBUM_KEY + " TEXT, " + ARTIST + " TEXT, " + NUMBER_OF_SONGS + " INTEGER, "
				+ FIRST_YEAR + " INTEGER, " + LAST_YEAR + " INTEGER, " + ALBUM_ART + " TEXT)");

		db.execSQL("CREATE TABLE IF NOT EXISTS " + SONGS_TABLE + " (" + SONG_ID + " INTEGER PRIMARY KEY NOT NULL, "
				+ SONG_TITLE + " TEXT, " + SONG_KEY + " TEXT, " + SONG_DISPLAY_NAME + " TEXT, " + SONG_ARTIST_ID
				+ " INTEGER, " + SONG_ALBUM_ID + " INTEGER, " + SONG_COMPOSER + " TEXT, " + SONG_TRACK + " INTEGER, "
				+ SONG_DURATION + " INTEGER, " + SONG_YEAR + " INTEGER, " + SONG_DATE_ADDED + " INTEGER, "
				+ SONG_MIME_TYPE + " TEXT, " + SONG_DATA + " TEXT, " + SONG_IS_MUSIC + " INTEGER)");

		db.execSQL("CREATE VIEW IF NOT EXISTS audio_info AS SELECT * FROM " + SONGS_TABLE + " LEFT OUTER JOIN "
				+ ARTISTS_TABLE + " ON " + SONGS_TABLE + "." + SONG_ARTIST_ID + " = " + ARTISTS_TABLE + "." + ARTIST_ID
				+ " LEFT OUTER JOIN " + ALBUMS_TABLE + " ON " + SONGS_TABLE + "." + SONG_ALBUM_ID + " = "
				+ ALBUMS_TABLE + "." + ALBUM_ID);
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

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// TODO THIS IS ONLY FOR DEBUG PURPOSES
		// dropTables(db);
		// onCreate(db);
		if (!db.isReadOnly())
			db.execSQL("PRAGMA foreign_keys=ON;");
	}
}

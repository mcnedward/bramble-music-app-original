package com.awesome.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static String TAG = "DatabaseAdapter";

	// Database Name
	private static String DB_NAME = "library.db";
	// Database Version
	private static final int DB_VERSION = 27;

	/** Artist Table Variables **/
	private static String DATABASE_TABLE_ARTISTS = "artists";
	private static String ARTIST_ID = "_id";
	private static String ARTIST = "artist";
	private static String ARTIST_KEY = "artist_key";
	private static String NUMBER_OF_ALBUMS = "number_of_albums";

	/** Album Table Variables **/
	private static String DATABASE_TABLE_ALBUMS = "albums";
	private static String ALBUM_ID = "_id";
	private static String ALBUM = "album";
	private static String ALBUM_KEY = "album_key";
	private static String ALBUM_ARTIST = "artist";
	private static String NUMBER_OF_SONGS = "numsongs";
	private static String FIRST_YEAR = "minyear";
	private static String LAST_YEAR = "maxyear";
	private static String ALBUM_ART = "album_art";

	/** Song Table Variables **/
	private static String DATABASE_TABLE_SONGS = "songs";
	private static String SONG_ID = "_id";
	private static String SONG_TITLE = "title";
	private static String SONG_KEY = "title_key";
	private static String SONG_DISPLAY_NAME = "_display_name";
	private static String SONG_ARTIST_ID = "artist_id";
	private static String SONG_ARTIST = "artist";
	private static String SONG_ARTIST_KEY = "artist_key";
	private static String SONG_ALBUM_ID = "album_id";
	private static String SONG_ALBUM = "album";
	private static String SONG_ALBUM_KEY = "album_key";
	private static String SONG_TRACK = "track";
	private static String SONG_DURATION = "duration";
	private static String SONG_YEAR = "year";
	private static String SONG_DATE_ADDED = "date_added";
	private static String SONG_MIME_TYPE = "mime_type";
	private static String SONG_DATA = "_data";
	private static String SONG_IS_MUSIC = "is_music";

	/** Music Table Variables **/
	private static String DATABASE_TABLE_MUSIC = "MUSIC";
	private static String MUSIC_ID = "_id";
	private static String MUSIC_TITLE = "Title";
	private static String MUSIC_ARTIST = "Artist";
	private static String MUSIC_ALBUM = "Album";
	private static String MUSIC_COMPOSER = "Composer";
	private static String MUSIC_YEAR = "Year";
	private static String MUSIC_DURATION = "Duration";
	private static String MUSIC_TRACK_NUMBER = "TrackNumber";
	private static String MUSIC_ALBUM_ART = "AlbumArt";
	private static String MUSIC_SONG_ID = "SongId";
	private static String MUSIC_TITLE_KEY = "TitleKey";
	private static String MUSIC_ARTIST_ID = "ArtistId";
	private static String MUSIC_ARTIST_KEY = "ArtistKey";
	private static String MUSIC_ALBUM_ID = "AlbumId";
	private static String MUSIC_ALBUM_KEY = "AlbumKey";

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

		String createAlbumTable = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
						DATABASE_TABLE_ALBUMS, ALBUM_ID, ALBUM, ALBUM_KEY, ALBUM_ARTIST, NUMBER_OF_SONGS, FIRST_YEAR,
						LAST_YEAR, ALBUM_ART);

		String createSongTable = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT)",
						DATABASE_TABLE_SONGS, SONG_ID, SONG_TITLE, SONG_KEY, SONG_DISPLAY_NAME, SONG_ARTIST_ID,
						SONG_ARTIST, SONG_ARTIST_KEY, SONG_ALBUM_ID, SONG_ALBUM, SONG_ALBUM_KEY, SONG_TRACK,
						SONG_DURATION, SONG_YEAR, SONG_DATE_ADDED, SONG_MIME_TYPE, SONG_DATA);

		// TODO Album art not null?
		String createMusicTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_MUSIC + " (" + MUSIC_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + MUSIC_TITLE + " TEXT, " + MUSIC_ARTIST + " TEXT, "
				+ MUSIC_ALBUM + " TEXT, " + MUSIC_COMPOSER + " TEXT, " + MUSIC_YEAR + " INTEGER, " + MUSIC_DURATION
				+ " INTEGER, " + MUSIC_TRACK_NUMBER + " INTEGER, " + MUSIC_ALBUM_ART + " TEXT, " + MUSIC_SONG_ID
				+ " INTEGER, " + MUSIC_TITLE_KEY + " TEXT, " + MUSIC_ARTIST_ID + " INTEGER, " + MUSIC_ARTIST_KEY
				+ " TEXT, " + MUSIC_ALBUM_ID + " INTEGER, " + MUSIC_ALBUM_KEY + " TEXT)";

		Log.i(TAG, String.format("%s \n %s \n %s \n %s", createArtistTable, createAlbumTable, createSongTable,
				createMusicTable));
		db.execSQL(createArtistTable);
		db.execSQL(createAlbumTable);
		db.execSQL(createSongTable);
		db.execSQL(createMusicTable);
	}

	public void dropTables(SQLiteDatabase db) {
		Log.i(TAG, "Droping database tables");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ARTISTS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ALBUMS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SONGS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_MUSIC);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Droping database tables");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ARTISTS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ALBUMS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SONGS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_MUSIC);

		onCreate(db);
	}
}

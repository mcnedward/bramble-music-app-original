package com.awesome.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Music;
import com.awesome.categories.Song;

@SuppressWarnings("deprecation")
public class MusicDatabase {
	private static String TAG = "MusicDatabase";

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private Context context;

	/** Artist Table Variables **/
	static String DATABASE_TABLE_ARTISTS = "artists";
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
		if (artistExists(artist.getArtistKey())) {
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
		if (songExists(song.getTitleKey())) {
			return;
		} else {
			open();
			// Create a single InsertHelper to handle this set of insertions.
			InsertHelper ih = new InsertHelper(database, DATABASE_TABLE_SONGS);

			// Get the Album information
			final String songTitle = song.getTitle();
			final String songKey = song.getTitleKey();
			final String songDisplayName = song.getDisplayName();
			final Integer songArtistId = song.getArtistId();
			final String songArtist = song.getArtist();
			final String songArtistKey = song.getArtistKey();
			final Integer songAlbumId = song.getAlbumId();
			final String songAlbum = song.getAlbum();
			final String songAlbumKey = song.getAlbumKey();
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
			final int songArtistIdColumn = ih.getColumnIndex(SONG_ARTIST_ID);
			final int songArtistColumn = ih.getColumnIndex(SONG_ARTIST);
			final int songArtistKeyColumn = ih.getColumnIndex(SONG_ARTIST_KEY);
			final int songAlbumIdColumn = ih.getColumnIndex(SONG_ALBUM_ID);
			final int songAlbumColumn = ih.getColumnIndex(SONG_ALBUM);
			final int songAlbumKeyColumn = ih.getColumnIndex(SONG_ALBUM_KEY);
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
				ih.bind(songArtistIdColumn, songArtistId);
				ih.bind(songArtistColumn, songArtist);
				ih.bind(songArtistKeyColumn, songArtistKey);
				ih.bind(songAlbumIdColumn, songAlbumId);
				ih.bind(songAlbumColumn, songAlbum);
				ih.bind(songAlbumKeyColumn, songAlbumKey);
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

	@SuppressWarnings("deprecation")
	public void insertMusic(Music music) {
		// Check if the album is already in the database
		if (musicExists(music.getSongId())) {
			return;
		} else {
			open();
			// Create a single InsertHelper to handle this set of insertions.
			InsertHelper ih = new InsertHelper(database, DATABASE_TABLE_MUSIC);

			// Get the Music information
			final String title = music.getTitle();
			final String artist = music.getArtist();
			final String album = music.getAlbum();
			final String composer = music.getComposer();
			final Integer year = music.getYear();
			final Integer duration = music.getDuration();
			final Integer trackNumber = music.getTrackNumber();
			final String albumArt = music.getAlbumArt();
			final Integer songId = music.getSongId();
			final String titleKey = music.getTitleKey();
			final Integer artistId = music.getArtistId();
			final String artistKey = music.getArtistKey();
			final Integer albumId = music.getAlbumId();
			final String albumKey = music.getAlbumKey();

			// Get the numeric indexes for each of the columns that we're updating
			final int titleColumn = ih.getColumnIndex(MUSIC_TITLE);
			final int artistColumn = ih.getColumnIndex(MUSIC_ARTIST);
			final int albumColumn = ih.getColumnIndex(MUSIC_ALBUM);
			final int composerColumn = ih.getColumnIndex(MUSIC_COMPOSER);
			final int yearColumn = ih.getColumnIndex(MUSIC_YEAR);
			final int durationColumn = ih.getColumnIndex(MUSIC_DURATION);
			final int trackNumberColumn = ih.getColumnIndex(MUSIC_TRACK_NUMBER);
			final int albumArtColumn = ih.getColumnIndex(MUSIC_ALBUM_ART);
			final int songIdColumn = ih.getColumnIndex(MUSIC_SONG_ID);
			final int titleKeyColumn = ih.getColumnIndex(MUSIC_TITLE_KEY);
			final int artistIdColumn = ih.getColumnIndex(MUSIC_ARTIST_ID);
			final int artistKeyColumn = ih.getColumnIndex(MUSIC_ARTIST_KEY);
			final int albumIdColumn = ih.getColumnIndex(MUSIC_ALBUM_ID);
			final int albumKeyColumn = ih.getColumnIndex(MUSIC_ALBUM_KEY);

			@SuppressWarnings("unused")
			int x = 0;

			try {
				// Get the InsertHelper ready to insert a single row
				ih.prepareForInsert();

				// Add the data for each column
				ih.bind(titleColumn, title);
				ih.bind(artistColumn, artist);
				ih.bind(albumColumn, album);
				ih.bind(composerColumn, composer);
				ih.bind(yearColumn, year);
				ih.bind(durationColumn, duration);
				ih.bind(trackNumberColumn, trackNumber);
				ih.bind(albumArtColumn, albumArt);
				ih.bind(songIdColumn, songId);
				ih.bind(titleKeyColumn, titleKey);
				ih.bind(artistIdColumn, artistId);
				ih.bind(artistKeyColumn, artistKey);
				ih.bind(albumIdColumn, albumId);
				ih.bind(albumKeyColumn, albumKey);

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
		Cursor c = database.rawQuery("SELECT * FROM artists ORDER BY artist COLLATE NOCASE ASC", null);
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

	public List<Artist> getArtists() {
		List<Artist> artistList = new ArrayList<Artist>();

		open();
		Cursor c = database.rawQuery(
				"SELECT ArtistId, Artist, ArtistKey FROM MUSIC ORDER BY Artist COLLATE NOCASE ASC", null);
		try {
			while (c.moveToNext()) {
				Integer artistId = c.getInt(c.getColumnIndexOrThrow(MUSIC_ARTIST_ID));
				String artist = c.getString(c.getColumnIndexOrThrow(MUSIC_ARTIST));
				String artistKey = c.getString(c.getColumnIndexOrThrow(MUSIC_ARTIST_KEY));
				List<Album> albumList = getAlbumsForArtist(artistId);
				Artist a = new Artist(artistId, artist, artistKey, null, null);
				artistList.add(a);
			}
			Set<Artist> hashedList = new LinkedHashSet<Artist>(artistList);
			artistList = new ArrayList<Artist>(hashedList);
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
		Cursor c = database.rawQuery("SELECT * FROM albums ORDER BY album", null);
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
		Cursor c = database.rawQuery("SELECT * FROM songs ORDER BY title", null);
		try {
			while (c.moveToNext()) {
				Integer songId = c.getInt(c.getColumnIndexOrThrow(SONG_ID));
				String songTitle = c.getString(c.getColumnIndexOrThrow(SONG_TITLE));
				String songKey = c.getString(c.getColumnIndexOrThrow(SONG_KEY));
				String songDisplayName = c.getString(c.getColumnIndexOrThrow(SONG_DISPLAY_NAME));
				Integer songArtistId = c.getInt(c.getColumnIndexOrThrow(SONG_ARTIST_ID));
				String songArtist = c.getString(c.getColumnIndexOrThrow(SONG_ARTIST));
				String songArtistKey = c.getString(c.getColumnIndexOrThrow(SONG_ARTIST_KEY));
				Integer songAlbumId = c.getInt(c.getColumnIndexOrThrow(SONG_ALBUM_ID));
				String songAlbum = c.getString(c.getColumnIndexOrThrow(SONG_ALBUM));
				String songAlbumKey = c.getString(c.getColumnIndexOrThrow(SONG_ALBUM_KEY));
				Integer songTrack = c.getInt(c.getColumnIndexOrThrow(SONG_TRACK));
				Integer songDuration = c.getInt(c.getColumnIndexOrThrow(SONG_DURATION));
				Integer songYear = c.getInt(c.getColumnIndexOrThrow(SONG_YEAR));
				Integer songDateAdded = c.getInt(c.getColumnIndexOrThrow(SONG_DATE_ADDED));
				String songMimeType = c.getString(c.getColumnIndexOrThrow(SONG_MIME_TYPE));
				String songData = c.getString(c.getColumnIndexOrThrow(SONG_DATA));
				Song song = new Song(songId, songTitle, songKey, songDisplayName, songArtistId, songArtist,
						songArtistKey, songAlbumId, songAlbum, songAlbumKey, songTrack, songDuration, songYear,
						songDateAdded, songMimeType, songData);
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

	public List<Album> getAlbumsForArtist(Integer artistId) {
		List<Album> albumList = new ArrayList<Album>();

		open();
		Cursor c = database.rawQuery("SELECT * FROM MUSIC WHERE ArtistId = ? ORDER BY Album",
				new String[] { artistId.toString() });
		try {
			while (c.moveToNext()) {
				Integer albumId = c.getInt(c.getColumnIndexOrThrow(MUSIC_ALBUM_ID));
				String album = c.getString(c.getColumnIndexOrThrow(MUSIC_ALBUM));
				String albumKey = c.getString(c.getColumnIndexOrThrow(MUSIC_ALBUM_KEY));
				String artist = c.getString(c.getColumnIndexOrThrow(MUSIC_ARTIST));
				Integer year = c.getInt(c.getColumnIndexOrThrow(MUSIC_YEAR));
				Album a = new Album(albumId, album, albumKey, artist, null, year, null, null, null);
				albumList.add(a);
			}
			Set<Album> hashedList = new LinkedHashSet<Album>(albumList);
			albumList = new ArrayList<Album>(hashedList);
			if (albumList.isEmpty()) {
				Log.i(TAG, "No albums");
				close();
				return null;
			} else
				return albumList;
		} catch (Exception e) {
			Log.i(TAG, e.getLocalizedMessage());
			e.printStackTrace();
			return null;
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
		Cursor c = database.rawQuery("SELECT * FROM albums WHERE artist = ? ORDER BY album", new String[] { artist });
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
		Cursor c = database.rawQuery("SELECT * FROM songs WHERE album_key = ? ORDER BY track",
				new String[] { albumKey });
		try {
			while (c.moveToNext()) {
				Integer songId = c.getInt(c.getColumnIndexOrThrow(SONG_ID));
				String songTitle = c.getString(c.getColumnIndexOrThrow(SONG_TITLE));
				String songKey = c.getString(c.getColumnIndexOrThrow(SONG_KEY));
				String songDisplayName = c.getString(c.getColumnIndexOrThrow(SONG_DISPLAY_NAME));
				Integer songArtistId = c.getInt(c.getColumnIndexOrThrow(SONG_ARTIST_ID));
				String songArtist = c.getString(c.getColumnIndexOrThrow(SONG_ARTIST));
				String songArtistKey = c.getString(c.getColumnIndexOrThrow(SONG_ARTIST_KEY));
				Integer songAlbumId = c.getInt(c.getColumnIndexOrThrow(SONG_ALBUM_ID));
				String songAlbum = c.getString(c.getColumnIndexOrThrow(SONG_ALBUM));
				String songAlbumKey = c.getString(c.getColumnIndexOrThrow(SONG_ALBUM_KEY));
				Integer songTrack = c.getInt(c.getColumnIndexOrThrow(SONG_TRACK));
				Integer songDuration = c.getInt(c.getColumnIndexOrThrow(SONG_DURATION));
				Integer songYear = c.getInt(c.getColumnIndexOrThrow(SONG_YEAR));
				Integer songDateAdded = c.getInt(c.getColumnIndexOrThrow(SONG_DATE_ADDED));
				String songMimeType = c.getString(c.getColumnIndexOrThrow(SONG_MIME_TYPE));
				String songData = c.getString(c.getColumnIndexOrThrow(SONG_DATA));
				Song song = new Song(songId, songTitle, songKey, songDisplayName, songArtistId, songArtist,
						songArtistKey, songAlbumId, songAlbum, songAlbumKey, songTrack, songDuration, songYear,
						songDateAdded, songMimeType, songData);
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
		Cursor c = database.rawQuery("SELECT album_art FROM albums WHERE album_key = ?", new String[] { albumKey });
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

	private boolean artistExists(String artistKey) {
		open();
		Cursor c = database.rawQuery("SELECT * FROM artists WHERE artist_key = ?", new String[] { artistKey });
		if (c.getCount() > 0) {
			c.close();
			close();
			return true;
		} else {
			c.close();
			close();
			return false;
		}
	}

	public boolean albumExists(Album album) {
		open();
		try {
			Cursor c = database.rawQuery("SELECT * FROM albums WHERE album_key = ?",
					new String[] { album.getAlbumKey() });
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

	private boolean songExists(String songKey) {
		open();
		Cursor c = database.rawQuery("SELECT * FROM songs WHERE title_key = ?", new String[] { songKey });
		if (c.getCount() > 0) {
			c.close();
			close();
			return true;
		} else {
			c.close();
			close();
			return false;
		}
	}

	private boolean musicExists(Integer songId) {
		open();
		Cursor c = database.rawQuery("SELECT * FROM MUSIC WHERE songId = ?", new String[] { songId.toString() });
		if (c.getCount() > 0) {
			c.close();
			close();
			return true;
		} else {
			c.close();
			close();
			return false;
		}
	}

}

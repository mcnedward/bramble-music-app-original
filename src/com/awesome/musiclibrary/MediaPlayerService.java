/**
 * 
 */
package com.awesome.musiclibrary;

import java.io.IOException;
import java.util.Iterator;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.awesome.categories.Album;
import com.awesome.categories.Song;
import com.awesome.musiclibrary.viewcontent.NowPlayingActivity;

/**
 * @author Edward
 * 
 */
public class MediaPlayerService extends Service {
	private static String TAG = "MediaPlayerService";

	private Album album;
	private Song song;
	private String title;
	private String albumTitle;
	private String albumArt;

	/**
	 * @param name
	 */
	public MediaPlayerService(String name) {
		super();
	}

	public MediaPlayerService() {
		super();
	}

	@Override
	public void onCreate() {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// Get the album and song from the bundle
		album = (Album) intent.getSerializableExtra("album");
		song = (Song) intent.getSerializableExtra("song");

		playMedia();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
	}

	/**
	 * This is used to play the media for the current song. A thread is started to display the current progress of the
	 * song as text and in the seek bar.
	 */
	public void playMedia() {
		if (MainActivity.mPlayer == null) {
			MainActivity.mPlayer = new MediaPlayer();
			MainActivity.mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			MainActivity.currentSong = song;
			MainActivity.currentAlbum = album;
		}
		if (!MainActivity.currentSong.getTitleKey().equals(song.getTitleKey())) {
			MainActivity.currentSong = song;
			MainActivity.currentAlbum = album;
			MainActivity.mPlayer.reset();
		}

		title = song.getTitle();
		albumTitle = album.getAlbum();
		albumArt = album.getAlbumArt();

		Uri songUri = Uri.parse(song.getData());

		try {
			MainActivity.mPlayer.setDataSource(this, songUri);
			MainActivity.mPlayer.prepare();
		} catch (IllegalArgumentException e) {
			Log.i(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (SecurityException e) {
			Log.i(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.i(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.i(TAG, e.getMessage(), e);
			e.printStackTrace();
		}
		MainActivity.mPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});

		new Handler(getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				MainActivity.mainSong.setText(title);
				MainActivity.mainAlbum.setText(albumTitle);
				if (albumArt != null) {
					Bitmap imageBitmap = BitmapFactory.decodeFile(albumArt);
					MainActivity.mainAlbumArt.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 60, 60, false));
				} else {
					Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noalbumart);
					MainActivity.mainAlbumArt.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 60, 60, false));
				}
				MainActivity.mainBtnPlayPause.setEnabled(true);
				MainActivity.mainBtnPlayPause.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Perform action on clicks
						if (MainActivity.mainBtnPlayPause.isChecked()) { // Checked - Pause icon visible
							pause();
						} else { // Unchecked - Play icon visible
							play();
						}
					}
				});
				MainActivity.nowPlayingInformationLayout.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						Log.i(TAG, "Now Playing Information Touched!!!");
						Intent nowPlaying = new Intent(MainActivity.context, NowPlayingActivity.class);
						nowPlaying.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						nowPlaying.putExtra("album", MainActivity.currentAlbum);
						nowPlaying.putExtra("song", MainActivity.currentSong);
						MainActivity.context.startActivity(nowPlaying);
						return false;
					}

				});
			}
		});
	}

	/**
	 * Used to format the time of the current media
	 * 
	 * @param millis
	 *            - The time in milliseconds to format
	 * @return - The formatted time
	 */

	private String getTimeString(long millis) {
		int minutes = (int) (millis / (1000 * 60));
		int seconds = (int) ((millis / 1000) % 60);
		return String.format("%d:%02d", minutes, seconds);
	}

	public void play() {
		MainActivity.mPlayer.start();
	}

	public void pause() {
		MainActivity.mPlayer.pause();
	}

	public void playMedia2(Album album) {
		final Iterator<Song> songList = album.getSongList().iterator();
		if (songList.hasNext()) {
			final Uri songUri = Uri.parse(songList.next().getData());

			try {
				MainActivity.mPlayer.setDataSource(getApplicationContext(), songUri);
				MainActivity.mPlayer.prepare();
				MainActivity.mPlayer.start();

				MainActivity.mPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer arg0) {
						try {
							if (songList.hasNext()) {
								final Uri sUri = Uri.parse(songList.next().getData());
								MainActivity.mPlayer.setDataSource(getApplicationContext(), sUri);
								MainActivity.mPlayer.prepare();
								MainActivity.mPlayer.start();
							}
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}

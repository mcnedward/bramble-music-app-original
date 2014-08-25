package com.awesome.musiclibrary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.awesome.Dto.Album;
import com.awesome.Dto.Song;
import com.awesome.musiclibrary.viewcontent.NowPlayingActivity;

/**
 * @author Edward
 * 
 */
public class MediaPlayerService extends Service {
	private static String TAG = "MediaPlayerService";

	private Album album;
	private Song song;
	private List<Song> songList = new ArrayList<Song>();
	private String title;
	private String albumTitle;
	private String albumArt;
	private Context context;

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
		this.context = context;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// Get the album and song from the bundle
		album = (Album) intent.getSerializableExtra("album");
		song = (Song) intent.getSerializableExtra("song");

		songList = album.getSongList();

		playMedia();

		return START_STICKY;
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

		try {
			MainActivity.mPlayer.setDataSource(this, song.getUri());
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
		MainActivity.mPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				next();
			}

		});

		new Handler(getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				updateMainActivity();
				updateNowPlaying();
			}
		});
	}

	public void release() {
		if (MainActivity.mPlayer == null) {
			return;
		}
		if (MainActivity.mPlayer.isPlaying()) {
			MainActivity.mPlayer.stop();
		}
		MainActivity.mPlayer.release();
		MainActivity.mPlayer = null;
	}

	public void updateMainActivity() {
		MainActivity.mainSong.setText(title);
		MainActivity.mainAlbum.setText(albumTitle);
		if (albumArt != null) {
			Bitmap imageBitmap = BitmapFactory.decodeFile(albumArt);
			MainActivity.mainAlbumArt.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 60, 60, false));
		} else {
			Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noalbumart);
			MainActivity.mainAlbumArt.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 60, 60, false));
		}
		MainActivity.btnPlayPause.setEnabled(true);
		MainActivity.btnPlayPause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on clicks
				if (MainActivity.btnPlayPause.isChecked()) { // Checked - Pause icon visible
					pause();
				} else { // Unchecked - Play icon visible
					play();
				}
			}
		});
		MainActivity.btnPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				previous();
			}
		});
		MainActivity.btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next();
			}
		});
		MainActivity.nowPlayingInformationLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i(TAG, "Now Playing Information Touched!!!");
				Intent nowPlaying = new Intent(MainActivity.mContext, NowPlayingActivity.class);
				nowPlaying.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				nowPlaying.putExtra("album", MainActivity.currentAlbum);
				nowPlaying.putExtra("song", MainActivity.currentSong);
				MainActivity.mContext.startActivity(nowPlaying);
				return false;
			}
		});
	}

	public void updateNowPlaying() {
		//NowPlayingActivity.txtArtist.setText(song.getArtist());
		NowPlayingActivity.txtTitle.setText(song.getTitle());
		NowPlayingActivity.btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next();
			}
		});
		NowPlayingActivity.btnPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				previous();
			}
		});
	}

	public void play() {
		MainActivity.mPlayer.start();
	}

	public void pause() {
		MainActivity.mPlayer.pause();
	}

	public void next() {	// TODO Should player stop when next() is called on last song?
		int currentPosition = songList.indexOf(song);
		if (currentPosition < songList.size() && currentPosition != songList.size() - 1) {
			song = songList.get(currentPosition + 1);
			playMedia();
		} else if (MainActivity.mPlayer.isLooping()) {
			song = songList.get(0);
			playMedia();
		}
	}

	public void previous() {
		int currentPosition = songList.indexOf(song);
		if (currentPosition > songList.size() && currentPosition != songList.size() + 1) {
			song = songList.get(currentPosition - 1);
			playMedia();
		} else if (MainActivity.mPlayer.isLooping()) {
			song = songList.get(songList.size() - 1);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
	}

}

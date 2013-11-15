package com.awesome.musiclibrary.viewcontent;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.awesome.asynctasks.PlayMediaTask;
import com.awesome.categories.Album;
import com.awesome.categories.Song;
import com.awesome.musiclibrary.R;
import com.awesome.utils.MusicDatabase;

public class NowPlayingActivity extends Activity {
	private static String TAG = "NowPlaying";

	private MediaPlayer mPlayer;			// Instance of the MediaPlayer
	private ToggleButton btnPlayPause;		// ToggleButton for playing and pausing
	private SeekBar mSeekBar;				// Seek bar for displaying the progress of the track
	private TextView txtCurrentTrackTime;	// TextView for displaying the current time of the track
	private TextView txtDuration;			// TextView for displaying the duration of the track
	private Activity activity;				// Instance of the NowPlayingActivity activity

	// Called as soon as this view is opened
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.now_playing);

		activity = this;	// Set the activity to NowPlayingActivity
		MusicDatabase mdb = new MusicDatabase(this);	// Instantiate the database

		mSeekBar = (SeekBar) findViewById(R.id.seekBar);

		// Get the album and song from the bundle
		Album album = (Album) getIntent().getSerializableExtra("album");
		Song song = (Song) getIntent().getSerializableExtra("song");

		new PlayMediaTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, song);

		// Set the title, artist name, and album art
		TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
		TextView txtArtist = (TextView) findViewById(R.id.txtArtist);
		ImageView imgAlbumArt = (ImageView) findViewById(R.id.imgAlbumArtNowPlaying);
		txtTitle.setText(song.getTitle());
		txtArtist.setText(song.getArtist());

		String albumArt = mdb.getAlbumArt(song.getAlbumKey());
		if (albumArt != null) {
			File imageFile = new File(albumArt);
			Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
			imgAlbumArt.setImageBitmap(imageBitmap);
		}
	}

	/**
	 * This is used to play the media for the current song. A thread is started to display the current progress of the
	 * song as text and in the seek bar.
	 */
	public void playMedia(Uri songUri) {
		try {
			mPlayer.setDataSource(getApplicationContext(), songUri);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Start a new thread to check the current duration and display the time and seek bar progress
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (mPlayer != null && mPlayer.getCurrentPosition() < mPlayer.getDuration()) {
					// Sleep for 100 milliseconds
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// Set the seek bar max position, current position, and change listener
					mSeekBar.setMax(mPlayer.getDuration());
					mSeekBar.setProgress(mPlayer.getCurrentPosition());
					mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						@Override
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
							if (fromUser == true) {
								mPlayer.seekTo(seekBar.getProgress());
							} else {
								// Do nothing
							}
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}
					});
					// Start a new thread on the NowPlayingActivity UI
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// Get the current time and total duration and display on the UI
							String currentTime = getTimeString(mPlayer.getCurrentPosition());
							String duration = getTimeString(mPlayer.getDuration());
							Log.i(TAG, String.valueOf(currentTime));

							// Find the TextViews from the NowPlayingActivity and update UI
							txtCurrentTrackTime = (TextView) activity.findViewById(R.id.txtCurrentTrackTime);
							txtCurrentTrackTime.setText(currentTime);

							txtDuration = (TextView) activity.findViewById(R.id.txtNowPlayingDuration);
							txtDuration.setText(String.valueOf(duration));
						}
					});
				}
			}
		}).start();
	}

	public void play() {
		mPlayer.start();
	}

	public void pause() {
		mPlayer.pause();
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
}

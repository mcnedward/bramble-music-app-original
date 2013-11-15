package com.awesome.asynctasks;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.awesome.categories.Song;
import com.awesome.musiclibrary.R;

public class PlayMediaTask extends AsyncTask<Song, Integer, Void> {
	private static String TAG = "PlayMediaTask";

	private MediaPlayer mPlayer;			// Instance of the MediaPlayer
	private ToggleButton btnPlayPause;		// ToggleButton for playing and pausing
	private SeekBar mSeekBar;				// Seek bar for displaying the progress of the track
	private TextView txtCurrentTrackTime;	// TextView for displaying the current time of the track
	private TextView txtDuration;			// TextView for displaying the duration of the track
	private Context context;

	public PlayMediaTask(Context context) {
		this.context = context;

		mPlayer = new MediaPlayer();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mSeekBar = (SeekBar) ((Activity) context).findViewById(R.id.seekBar);

		btnPlayPause = (ToggleButton) ((Activity) context).findViewById(R.id.btnPlayPause);

		btnPlayPause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on clicks
				if (btnPlayPause.isChecked()) { // Checked - Pause icon visible
					pause();
				} else { // Unchecked - Play icon visible
					play();
				}
			}
		});
	}

	@Override
	protected Void doInBackground(Song... songList) {
		Log.i(TAG, "Starting Play Media Task");

		Song song = songList[0];

		final Uri songUri = Uri.parse(song.getData());

		playMedia(songUri);
		return null;
	}

	/**
	 * This is used to play the media for the current song. A thread is started to display the current progress of the
	 * song as text and in the seek bar.
	 */
	public void playMedia(Uri songUri) {
		try {
			mPlayer.setDataSource(context, songUri);
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
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// Get the current time and total duration and display on the UI
							String currentTime = getTimeString(mPlayer.getCurrentPosition());
							String duration = getTimeString(mPlayer.getDuration());
							Log.i(TAG, String.valueOf(currentTime));

							// Find the TextViews from the NowPlayingActivity and update UI
							txtCurrentTrackTime = (TextView) ((Activity) context)
									.findViewById(R.id.txtCurrentTrackTime);
							txtCurrentTrackTime.setText(currentTime);

							txtDuration = (TextView) ((Activity) context).findViewById(R.id.txtNowPlayingDuration);
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

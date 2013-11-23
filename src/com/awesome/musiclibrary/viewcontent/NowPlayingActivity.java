package com.awesome.musiclibrary.viewcontent;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.awesome.categories.Album;
import com.awesome.categories.Song;
import com.awesome.musiclibrary.MainActivity;
import com.awesome.musiclibrary.R;
import com.awesome.utils.MusicDatabase;

public class NowPlayingActivity extends Activity {
	private static String TAG = "NowPlaying";

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

		btnPlayPause = (ToggleButton) findViewById(R.id.btnPlayPause);

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

		new Thread(new Runnable() {
			@Override
			public void run() {
				// if (MainActivity.mPlayer.isPlaying()) {
				while (MainActivity.mPlayer != null
						&& MainActivity.mPlayer.getCurrentPosition() < MainActivity.mPlayer.getDuration()) {
					// Sleep for 100 milliseconds
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// Set the seek bar max position, current position, and change listener
					mSeekBar.setMax(MainActivity.mPlayer.getDuration());
					mSeekBar.setProgress(MainActivity.mPlayer.getCurrentPosition());
					mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						@Override
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
							if (fromUser == true) {
								MainActivity.mPlayer.seekTo(seekBar.getProgress());
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
							String currentTime = getTimeString(MainActivity.mPlayer.getCurrentPosition());
							String duration = getTimeString(MainActivity.mPlayer.getDuration());
							Log.i(TAG, String.valueOf(currentTime));

							// Find the TextViews from the NowPlayingActivity and update UI
							txtCurrentTrackTime = (TextView) activity.findViewById(R.id.txtCurrentTrackTime);
							txtCurrentTrackTime.setText(currentTime);

							txtDuration = (TextView) activity.findViewById(R.id.txtNowPlayingDuration);
							txtDuration.setText(String.valueOf(duration));
						}
					});
				}
				// }
			}
		}).start();
	}

	public void play() {
		MainActivity.mPlayer.start();
	}

	public void pause() {
		MainActivity.mPlayer.pause();
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

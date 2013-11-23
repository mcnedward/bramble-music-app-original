package com.awesome.musiclibrary.viewcontent;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.awesome.adapters.MyListAdapter;
import com.awesome.categories.Album;
import com.awesome.categories.Song;
import com.awesome.musiclibrary.MediaPlayerService;
import com.awesome.musiclibrary.R;

public class DisplaySongsActivity extends Activity {

	// Initializes all variables here
	private ListView listView;
	private Activity activity;
	private Context context;

	// Called as soon as this view is opened
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_album_content);

		activity = this;
		this.context = this;

		// Get the album from the bundle of the main activity
		final Album album = (Album) getIntent().getSerializableExtra("album");
		TextView albumTitle = (TextView) findViewById(R.id.txtAlbumTitle);
		TextView albumArtist = (TextView) findViewById(R.id.txtAlbumArtist);
		albumTitle.setText(album.getAlbum());
		albumArtist.setText(album.getArtist());

		List<Song> songList = album.getSongList();

		ImageView imgAlbumArt = (ImageView) findViewById(R.id.imgAlbumArt);
		String albumArt = album.getAlbumArt();
		if (albumArt != null) {
			File imageFile = new File(album.getAlbumArt());
			Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
			imgAlbumArt.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 200, 200, false));
		} else {
			Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.noalbumart);
			imgAlbumArt.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 200, 200, false));
		}

		ListView displaySongs = (ListView) findViewById(R.id.displaySongs3);
		MyListAdapter songAdapter = new MyListAdapter(this);

		final Intent playSong = new Intent(this, MediaPlayerService.class);
		playSong.putExtra("album", album);

		if (songList != null) {
			for (Song song : songList) {
				songAdapter.setGroup(song);
				songAdapter.isDisplaySongs(true);
				displaySongs.setAdapter(songAdapter);

				// Set the single click for album
				displaySongs.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if (view.isSelected() == false) {
							Song song = (Song) parent.getItemAtPosition(position);
							// TODO Change this
							viewNowPlaying(album, song);
							playSong.putExtra("song", song);
							activity.startService(playSong);
						}
						return;
					}
				});
			}
		} else {
			songAdapter.setGroup(null);
			displaySongs.setAdapter(songAdapter);
		}
	}

	public void viewNowPlaying(Album album, Song song) {
		Intent nowPlaying = new Intent(this, NowPlayingActivity.class);
		nowPlaying.putExtra("album", album);
		nowPlaying.putExtra("song", song);
		this.startActivity(nowPlaying);
	}
}

package com.awesome.musiclibrary.editcontent;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.awesome.musiclibrary.MainActivity;
import com.awesome.musiclibrary.R;

public class EditSongActivity extends Activity {

	// Initializes all variables here
	private ArrayAdapter<String> adapterAlbum;
	private Spinner spinAlbums;
	private EditText txtEditTrack;
	private ArrayList<String> albumList = new ArrayList<String>();
	private String currentTrack, currentAlbum;
	String activity; // String for the activity user is coming from

	// Called as soon as this view is opened
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_song_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		currentTrack = getIntent().getExtras().getString("title");
		activity = getIntent().getExtras().getString("activity");

		// Initialize TextViews and Spinner
		txtEditTrack = (EditText) findViewById(R.id.txtEditTrack);
		txtEditTrack.setText(currentTrack);
		spinAlbums = (Spinner) findViewById(R.id.spinEditAlbums);

		// Set the Album drop down box to be populated by all Albums in the database
		spinAlbums = (Spinner) findViewById(R.id.spinEditAlbums);
		adapterAlbum = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, albumList);
		adapterAlbum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinAlbums.setAdapter(adapterAlbum);
		int spinnerPositionAlbum = adapterAlbum.getPosition(currentAlbum);
		spinAlbums.setSelection(spinnerPositionAlbum);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.content_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed
			// in the Action Bar.
			Intent parentActivityIntent = new Intent(this, MainActivity.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void editSong(View view) {
		String newTrack = txtEditTrack.getText().toString().trim();
		String newAlbum = spinAlbums.getSelectedItem().toString().trim();

		if (newTrack.equals("")) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "You need to enter a new song title.", Toast.LENGTH_SHORT);
			toast.show();
		} else if (newAlbum.equals("")) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "You need to select an album.", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Context context = getApplicationContext();
			if (currentTrack.equals(newTrack) && currentAlbum.equals(newAlbum)) {
				Toast toast = Toast.makeText(context, "The song was not changed.", Toast.LENGTH_SHORT);
				toast.show(); // Display toast
			} else {
				Toast toast = Toast.makeText(context, "The song " + currentTrack + " has been changed to " + newTrack
						+ ".", Toast.LENGTH_SHORT);
				toast.show(); // Display toast
			}
			this.finish(); // Close the Activity
		}
	}
}
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

public class EditAlbumActivity extends Activity {

	// Initializes all variables here
	private ArrayAdapter<String> adapterArtist, adapterGenre;
	private Spinner spinEditArtist, spinEditGenre;
	private EditText txtEditAlbum, txtEditYear;
	String currentAlbum, currentArtist, currentGenre, currentYear; // String for
																	// the Album
																	// name
	String activity; // String for the activity user is coming from
	MainActivity mainActivity = new MainActivity(); // Initialize an instance of
													// the Main Activity

	// Called as soon as this view is opened
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_album_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Get the album from the bundle of the Main Activity
		currentAlbum = getIntent().getExtras().getString("albumName");
		activity = getIntent().getExtras().getString("activity");

		txtEditAlbum = (EditText) findViewById(R.id.txtEditAlbum);
		txtEditYear = (EditText) findViewById(R.id.txtEditYear);
		txtEditAlbum.setText(currentAlbum);
		txtEditYear.setText(currentYear);

		// Populate the spinners
		ArrayList<String> artistList = null;
		spinEditArtist = (Spinner) findViewById(R.id.spinEditArtist);
		adapterArtist = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, artistList);
		adapterArtist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinEditArtist.setAdapter(adapterArtist);
		int spinnerPositionArtist = adapterArtist.getPosition(currentArtist);
		spinEditArtist.setSelection(spinnerPositionArtist);

		ArrayList<String> genreList = null;
		spinEditGenre = (Spinner) findViewById(R.id.spinEditGenre);
		adapterGenre = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genreList);
		adapterGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinEditGenre.setAdapter(adapterGenre);
		int spinnerPositionGenre = adapterGenre.getPosition(currentGenre);
		spinEditGenre.setSelection(spinnerPositionGenre);
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

	public void editAlbum(View view) {
		String newAlbum = txtEditAlbum.getText().toString().trim(); // Set the
																	// new album
																	// name
		String newArtist = spinEditArtist.getSelectedItem().toString().trim(); // Set
																				// the
																				// new
																				// artist
		String newGenre = spinEditGenre.getSelectedItem().toString().trim(); // Set
																				// the
																				// new
																				// genre
		String newYear = txtEditYear.getText().toString().trim(); // Set the new
																	// year

		if (newAlbum.equals("")) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "You need to enter a new album name.", Toast.LENGTH_SHORT);
			toast.show();
		} else if (newArtist.equals("")) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "You need to select a new artist.", Toast.LENGTH_SHORT);
			toast.show();
		} else if (newGenre.equals("")) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "You need to select a new genre.", Toast.LENGTH_SHORT);
			toast.show();
		} else if (newYear.equals("")) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "You need to enter a new year.", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Context context = getApplicationContext();
			if (currentAlbum.equals(newAlbum) && currentArtist.equals(newArtist) && currentGenre.equals(newGenre)
					&& currentYear.equals(newYear)) {
				Toast toast = Toast.makeText(context, "The album was not changed.", Toast.LENGTH_SHORT);
				toast.show(); // Display toast
			} else {
				Toast toast = Toast.makeText(context, "The album " + currentAlbum + " has been changed.",
						Toast.LENGTH_SHORT);
				toast.show(); // Display toast
			}
			this.finish(); // Close the Activity
		}
	}
}

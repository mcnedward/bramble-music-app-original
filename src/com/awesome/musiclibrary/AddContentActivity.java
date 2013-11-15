package com.awesome.musiclibrary;

/**
 * Source for some code: https://code.google.com/p/android-custom-tabs/
 */

import java.util.ArrayList;

import com.awesome.adapters.Control;
import com.awesome.adapters.TabsAdapter;
import com.awesome.musiclibrary.R;
import com.awesome.musiclibrary.addcontent.AddAlbumActivity;
import com.awesome.musiclibrary.addcontent.AddArtistActivity;
import com.awesome.musiclibrary.addcontent.AddGenreActivity;
import com.awesome.musiclibrary.addcontent.AddSongActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddContentActivity extends FragmentActivity {

	// Initializes all variables here
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private EditText addArtist, addGenre, addTitle, addYear;
	private Spinner spinArtist, spinGenre, spinAlbum;
	private DatabaseHelper dbh;
	private Control control;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_content_layout);

		final ActionBar actionBar = getActionBar(); // Display the Action Bar
													// for user to go home by
													// pressing icon
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		dbh = new DatabaseHelper(this); // Start the database helper
		control = new Control(this); // Start the Control

		mViewPager = (ViewPager) findViewById(R.id.contentPager);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(actionBar.newTab().setText("Artist"),
				AddArtistActivity.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Genre"),
				AddGenreActivity.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Album"),
				AddAlbumActivity.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Song"),
				AddSongActivity.class, null);

		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt(
					"tab", 0));
		}
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
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	// This will allow pressing the Back button to move to the previous tab,
	// instead of closing the fragment
	/*
	 * @Override public void onBackPressed() { if (mViewPager.getCurrentItem()
	 * == 0) { // If the user is currently looking at the first step, allow the
	 * system to handle the // Back button. This calls finish() on this activity
	 * and pops the back stack. super.onBackPressed(); } else { // Otherwise,
	 * select the previous step.
	 * mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1); } }
	 */

	/********** Add Methods **********/

	public void addArtist(View view) {
		Context context = getApplicationContext();
		String artist = null; // Initialize the artist string
		ArrayList<String> currentArtists = dbh.getAllArtists(); // Initialize
																// the list of
																// current
																// artists

		addArtist = (EditText) findViewById(R.id.txtAddArtist); // Get the
																// EditText for
																// add artist
		artist = addArtist.getText().toString().trim(); // Set the artist based
														// on user input

		if (artist.equals("")) { // If the artist text box was empty or blank...
			Toast toast = Toast.makeText(context,
					"You need to enter an artist name.", Toast.LENGTH_SHORT);
			toast.show();
		} else if (currentArtists.contains(artist)) { // If the artist already
														// exists...
			Toast toast = Toast.makeText(context,
					"That artist is already in the library.",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Toast toast = Toast.makeText(context, "The artist " + artist
					+ " has been added.", Toast.LENGTH_SHORT);
			toast.show(); // Display toast

			dbh.open();
			dbh.insertArtist(artist); // Insert the artist into the database
			dbh.close();
			addArtist.setText(""); // Clear the Add Artist text field
			// control.refreshArtistList(context); // Refresh the table list of
			// all artists
			control.refreshSpinners();
			hideKeypad(context);
		}
	}

	public void addGenre(View view) {
		Context context = getApplicationContext();
		String genre = null;
		ArrayList<String> currentGenres = dbh.getAllGenres();

		addGenre = (EditText) findViewById(R.id.txtAddGenre);
		genre = addGenre.getText().toString().trim();

		if (genre.equals("")) {
			Toast toast = Toast.makeText(context, "You need to enter a genre.",
					Toast.LENGTH_SHORT);
			toast.show();
		} else if (currentGenres.contains(genre)) { // If the genre already
													// exists...
			Toast toast = Toast
					.makeText(context, "That genre is already in the library.",
							Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Toast toast = Toast.makeText(context, "The genre " + genre
					+ " has been added.", Toast.LENGTH_SHORT);
			toast.show(); // Display toast

			dbh.open(); // Open the database
			dbh.insertGenre(genre); // Insert the genre into the database
			dbh.close(); // Close the database
			addGenre.setText(""); // Clear the genre text box
			control.refreshGenreList(dbh.getGenreAndAlbums()); // Refresh the
																// table list of
																// all genres
			control.refreshSpinners();
			hideKeypad(context);
		}
	}

	public void addAlbum(View view) {
		Context context = getApplicationContext();
		String album = null;
		int artistID = 0;
		int genreID = 0;
		String year = null;
		ArrayList<String> currentAlbums = dbh.getAllAlbums();

		addTitle = (EditText) findViewById(R.id.txtAddTitle);
		addYear = (EditText) findViewById(R.id.txtAddYear);
		spinArtist = (Spinner) findViewById(R.id.spinArtist);
		spinGenre = (Spinner) findViewById(R.id.spinGenre);
		album = addTitle.getText().toString().trim(); // Set the album title

		if (album.equals("")) {
			Toast toast = Toast.makeText(context, "You need to enter a title.",
					Toast.LENGTH_SHORT);
			toast.show();
		} else if (spinArtist.getSelectedItem().toString().trim().equals("")) {
			Toast toast = Toast.makeText(context,
					"You need to select an artist.", Toast.LENGTH_SHORT);
			toast.show();
		} else if (spinGenre.getSelectedItem().toString().trim().equals("")) {
			Toast toast = Toast.makeText(context,
					"You need to select a genre.", Toast.LENGTH_SHORT);
			toast.show();
		} else if (addYear.getText().toString().trim().equals("")) {
			Toast toast = Toast.makeText(context, "You need to enter a year.",
					Toast.LENGTH_SHORT);
			toast.show();
		} else if (currentAlbums.contains(album)) {
			Toast toast = Toast
					.makeText(context, "That album is already in the library.",
							Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Toast toast = Toast.makeText(context, "The album " + album
					+ " has been added.", Toast.LENGTH_SHORT);
			toast.show(); // Display toast

			// Set the artist and genre ID to add
			artistID = dbh.getArtistID(spinArtist.getSelectedItem().toString()
					.trim());
			genreID = dbh.getGenreID(spinGenre.getSelectedItem().toString()
					.trim());
			dbh.insertAlbum(album, artistID, genreID, year); // Insert the album
																// information
			addTitle.setText("");
			addYear.setText(""); // Clear the text boxes
			// control.refreshAlbumList(dbh.getAllAlbums()); // Refresh the list
			// of albums
			control.refreshSpinners();
			hideKeypad(context);
		}
	}

	public void addSongs(View view) {
		Context context = getApplicationContext();
		String album = null;
		String song = null;

		spinAlbum = (Spinner) findViewById(R.id.spinAlbum);
		album = spinAlbum.getSelectedItem().toString().trim();

		addTitle = (EditText) findViewById(R.id.txtAddSongTitle);
		song = addTitle.getText().toString().trim();

		int albumID = dbh.getAlbumID(album);
		ArrayList<String> currentSongs = dbh.getAllSongsInAlbum(albumID);

		if (album.equals("")) {
			Toast toast = Toast.makeText(context,
					"You need to select an artist.", Toast.LENGTH_SHORT);
			toast.show();
		} else if (song.equals("")) {
			Toast toast = Toast.makeText(context, "You need to enter a title.",
					Toast.LENGTH_SHORT);
			toast.show();
		} else if (currentSongs.contains(song)) {
			Toast toast = Toast.makeText(context,
					"This album already contains that song.",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Toast toast = Toast.makeText(context, "The song " + song
					+ " has been add to the album " + album + ".",
					Toast.LENGTH_LONG);
			toast.show();

			dbh.insertSong(song, albumID);
			addTitle.setText("");
			// control.refreshSongList(dbh.getAllSongs());
			control.refreshSpinners();
			hideKeypad(context);
		}
	}

	/********* End Add Methods **********/

	// This hides the keypad after adding content
	private void hideKeypad(Context context) {
		InputMethodManager inputManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}

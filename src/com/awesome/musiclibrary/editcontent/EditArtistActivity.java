package com.awesome.musiclibrary.editcontent;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.awesome.musiclibrary.MainActivity;
import com.awesome.musiclibrary.R;

public class EditArtistActivity extends Activity {

	// Initializes all variables here
	private EditText txtEditArtist;
	private String currentArtist; // String for the artist name

	// Called as soon as this view is opened
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_artist_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		currentArtist = getIntent().getExtras().getString("artistName");

		// Set the current artist in the text view
		txtEditArtist = (EditText) findViewById(R.id.txtEditArtist);
		txtEditArtist.setText(currentArtist);
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

	public void editArtist(View view) {
		String newArtist = txtEditArtist.getText().toString().trim();

		if (newArtist.equals("")) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "You need to enter a new artist name.", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Context context = getApplicationContext();
			if (currentArtist.equals(newArtist)) {
				Toast toast = Toast.makeText(context, "The artist was not changed.", Toast.LENGTH_SHORT);
				toast.show(); // Display toast
			} else {
				Toast toast = Toast.makeText(context, "The artist " + currentArtist + " has been changed to "
						+ newArtist + ".", Toast.LENGTH_SHORT);
				toast.show(); // Display toast
			}
			this.finish(); // Close the Activity
		}
	}
}

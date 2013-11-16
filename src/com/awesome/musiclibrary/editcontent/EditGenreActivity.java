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

public class EditGenreActivity extends Activity {

	// Initializes all variables here
	private EditText txtEditGenre;
	private String currentGenre; // String for the genre name

	// Called as soon as this view is opened
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_genre_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		currentGenre = getIntent().getExtras().getString("genreName");

		// Set the current artist in the text view
		txtEditGenre = (EditText) findViewById(R.id.txtEditGenre);
		txtEditGenre.setText(currentGenre);
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

	public void editGenre(View view) {
		String newGenre = txtEditGenre.getText().toString().trim();

		if (newGenre.equals("")) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "You need to enter a new Genre name.", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Context context = getApplicationContext();
			if (currentGenre.equals(newGenre)) {
				Toast toast = Toast.makeText(context, "The genre was not changed.", Toast.LENGTH_SHORT);
				toast.show(); // Display toast
			} else {
				Toast toast = Toast.makeText(context, "The genre " + currentGenre + " has been changed to " + newGenre
						+ ".", Toast.LENGTH_SHORT);
				toast.show(); // Display toast
			}
			this.finish(); // Close the Activity
		}
	}
}

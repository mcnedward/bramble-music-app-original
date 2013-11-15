package com.awesome.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Genre;
import com.awesome.categories.Song;
import com.awesome.musiclibrary.DatabaseHelper;
import com.awesome.musiclibrary.DisplaySongsActivity;
import com.awesome.musiclibrary.R;
import com.awesome.musiclibrary.editcontent.EditAlbumActivity;
import com.awesome.musiclibrary.editcontent.EditArtistActivity;
import com.awesome.musiclibrary.editcontent.EditGenreActivity;
import com.awesome.musiclibrary.editcontent.EditSongActivity;

public class Control extends Activity {

	private DatabaseHelper dbh;
	private Context context;

	public Control() {

	}

	public Control(Context context) {
		dbh = new DatabaseHelper(context);
		this.context = context;
	}

	/********** Refresh Methods **********/

	// Method for listing all the artists and their albums in the expandable
	// list view
	public void refreshArtistList(List<Artist> artists) {
		// Initialize the List Views here
		ExpandableListView listView = null;
		ListView listView2 = null;

		// Initialize the Layouts containing the List Views
		LinearLayout expViewArtist = null;
		LinearLayout viewArtist = null;

		// Initialize the adapters
		MyExpandableListAdapter expAdapter = new MyExpandableListAdapter(context);
		MyListAdapter adapter = new MyListAdapter(context);

		// Check the context and display then assign the List Views to their
		// appropriate Views
		String activity = context.toString();
		if (activity.contains("MainActivity")) {
			listView = (ExpandableListView) ((Activity) context).findViewById(R.id.displayArtists);
			listView2 = (ListView) ((Activity) context).findViewById(R.id.displayArtists2);

			expViewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.expViewArtist);
			viewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.viewArtist);
		}
		if (activity.contains("AddContentActivity")) {
			listView = (ExpandableListView) ((Activity) context).findViewById(R.id.displayArtists3);
			listView2 = (ListView) ((Activity) context).findViewById(R.id.displayArtists4);

			expViewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.expViewArtist2);
			viewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.viewArtist2);
		}

		listView.setAdapter(expAdapter); // Clear the Exp List
		// Display the list view with "No artists" message if there are no
		// artists

		// ArrayList<Artist> artistList = dbh.getArtistAndTitles();
		if (artists.get(0).getArtist().equals("No artists")) {
			expViewArtist.setVisibility(LinearLayout.GONE);
			viewArtist.setVisibility(LinearLayout.HORIZONTAL);

			adapter.setGroup("No artists");
			listView2.setAdapter(adapter);
			// Reset the click listeners
			listView2.setOnItemClickListener(null);
			listView2.setOnItemLongClickListener(null);
		} else {
			for (Iterator<Artist> i = artists.iterator(); i.hasNext();) {
				// Initialize the variables used to create each individual
				// artist and their titles
				for (int x = 0; x < artists.size(); x++) { // Loop through
															// artists in list
															// and set the
															// proper index
					Artist a; // Initialize the individual artist
					final String artistName; // Initialize the artist name
					ArrayList<String> titles = new ArrayList<String>(); // Initialize
																		// the
																		// array
																		// to
																		// hold
																		// artist's
																		// album
					// titles

					a = (Artist) i.next(); // Set the artist to the next artist
											// in the current iteration
					artistName = a.getArtist(); // Set artist name
					// titles = a.getTitles(); // Set the albums by artist
					expAdapter.setGroup(a); // Set the artist parent
											// drop down
					// expAdapter.setChild(x, titles); // Set the album child drop
					// down
					listView.setAdapter(expAdapter);
					listView.setClickable(true);

					listView.setOnChildClickListener(new OnChildClickListener() {
						@Override
						public boolean onChildClick(ExpandableListView parent, View view, int groupPosition,
								int childPosition, long id) {
							if (((TextView) view).isSelected() == false) {
								String title = (String) ((ExpandableListView) parent).getExpandableListAdapter()
										.getChild(groupPosition, childPosition);
								if (!title.equals("No albums by this artist yet")
										&& !title.equals("No albums in this genre yet")) {
									viewDisplaySongsByAlbum(title);
								}
							}
							return false;
						}
					});

					// Set the long click listener for when selecting an album
					// or artist
					listView.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
							if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
								int groupPosition = ExpandableListView.getPackedPositionGroup(id);
								int childPosition = ExpandableListView.getPackedPositionChild(id);

								if (((TextView) view).isSelected() == false) {
									String title = (String) ((ExpandableListView) parent).getExpandableListAdapter()
											.getChild(groupPosition, childPosition);
									if (!title.equals("No albums by this artist yet")) {
										showOptions(dbh.getAlbumID(title), title, 1); // Show options menu;
																						// the second
																						// param (1) is the
																						// type album
										return true;
									}
								}
								return true;
							} else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
								int groupPosition = ExpandableListView.getPackedPositionGroup(id);

								if (((TextView) view).isSelected() == false) {
									String artist = (String) ((ExpandableListView) parent).getExpandableListAdapter()
											.getGroup(groupPosition);
									if (!artist.equals("No artists")) {
										showOptions(dbh.getArtistID(artist), artist, 0); // The first param
																							// (0) is the
																							// type artist
										return true;
									}
								}
							}
							return false;
						}
					});
				}
			}
			adapter.notifyDataSetChanged();
			expAdapter.notifyDataSetChanged();
		}
	}

	// Method for listing all the albums in the list view
	public void refreshAlbumList(List<Album> albums) {
		// Initialize the List View
		ListView listView = null;
		// Initialize the Adapter
		MyListAdapter adapter = new MyListAdapter(context);

		// Check the context and display then assign the List Views to their
		// appropriate Views
		String activity = context.toString();
		if (activity.contains("MainActivity")) {
			listView = (ListView) ((Activity) context).findViewById(R.id.displayAlbums);
		}
		if (activity.contains("AddContentActivity")) {
			listView = (ListView) ((Activity) context).findViewById(R.id.displayAlbums2);
		}

		listView.setAdapter(adapter); // Clear the adapter
		for (int x = 0; x < albums.size(); x++) { // Loop through artists in
													// list and set the proper
													// index
			adapter.setGroup(albums.get(x).getAlbum());
			listView.setAdapter(adapter);

			// Set the single click for album
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (((TextView) view).isSelected() == false) {
						String title = (String) parent.getItemAtPosition(position);
						if (!title.equals("No albums by this artist yet") && !title.equals("No albums")) {
							viewDisplaySongsByAlbum(title);
						}
					}
					return;
				}
			});

			// Set the long click for album
			listView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					if (((TextView) view).isSelected() == false) {
						String title = (String) parent.getItemAtPosition(position);
						if (!title.equals("No albums")) {
							showOptions(dbh.getAlbumID(title), title, 1); // Show
																			// options
																			// menu;
																			// the
																			// second
																			// param
																			// (1)
																			// is
																			// the
																			// type
																			// album
							return true;
						}
					}
					return false;
				}
			});
		}
	}

	// Method for listing all the genres and their albums in the expandable list
	// view
	public void refreshGenreList(List<Genre> genreList) {
		// Initialize the List Views here
		ExpandableListView listView = null;
		ListView listView2 = null;

		// Initialize the Layouts containing the List Views
		LinearLayout expViewArtist = null;
		LinearLayout viewArtist = null;

		// Initialize the adapters
		MyExpandableListAdapter expAdapter = new MyExpandableListAdapter(context);
		MyListAdapter adapter = new MyListAdapter(context);

		// Check the context and display then assign the List Views to their
		// appropriate Views
		String activity = context.toString();
		if (activity.contains("MainActivity")) {
			listView = (ExpandableListView) ((Activity) context).findViewById(R.id.displayGenres);
			listView2 = (ListView) ((Activity) context).findViewById(R.id.displayGenres2);

			expViewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.expViewGenre);
			viewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.viewGenre);
		}
		if (activity.contains("AddContentActivity")) {
			listView = (ExpandableListView) ((Activity) context).findViewById(R.id.displayGenres3);
			listView2 = (ListView) ((Activity) context).findViewById(R.id.displayGenres4);

			expViewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.expViewGenre2);
			viewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.viewGenre2);
		}

		if (expAdapter != null && listView != null) {
			listView.setAdapter(expAdapter); // Clear the Exp List
		}
		// Display the list view with "No genres" message if there are no genres
		if (genreList.get(0).getGenre().equals("No genres")) {
			expViewArtist.setVisibility(LinearLayout.GONE);
			viewArtist.setVisibility(LinearLayout.HORIZONTAL);

			adapter.setGroup("No genres");
			listView2.setAdapter(adapter);
			// Reset the click listeners
			listView2.setOnItemClickListener(null);
			listView2.setOnItemLongClickListener(null);
		} else if (listView != null) {
			for (Iterator<Genre> i = genreList.iterator(); i.hasNext();) {
				// Initialize the variables used to create each individual genre
				// and their albums
				for (int x = 0; x < genreList.size(); x++) { // Loop through
																// genres in
																// list and set
																// the proper
																// index
					Genre g; // Initialize the individual genre
					final String genre; // Initialize the genre
					ArrayList<String> albums = new ArrayList<String>(); // Initialize
																		// the
																		// array
																		// to
																		// hold
																		// artist's
																		// album
					// titles

					g = (Genre) i.next(); // Set the genre to the next genre in
											// the current iteration
					genre = g.getGenre(); // Set genre
					albums = g.getAlbums(); // Set the albums by artist
					// expAdapter.setGroup(genre); // Set the genre parent drop
					// down
					// expAdapter.setChild(x, albums); // Set the album child drop
					// down
					listView.setAdapter(expAdapter);
					listView.setClickable(true);

					listView.setOnChildClickListener(new OnChildClickListener() {
						@Override
						public boolean onChildClick(ExpandableListView parent, View view, int groupPosition,
								int childPosition, long id) {
							if (((TextView) view).isSelected() == false) {
								String title = (String) ((ExpandableListView) parent).getExpandableListAdapter()
										.getChild(groupPosition, childPosition);
								if (!title.equals("No albums by this artist yet")
										&& !title.equals("No albums in this genre yet")) {
									viewDisplaySongsByAlbum(title);
									return true;
								}
							}
							return false;
						}
					});

					listView.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
							if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
								int groupPosition = ExpandableListView.getPackedPositionGroup(id);
								int childPosition = ExpandableListView.getPackedPositionChild(id);

								if (((TextView) view).isSelected() == false) {
									String title = (String) ((ExpandableListView) parent).getExpandableListAdapter()
											.getChild(groupPosition, childPosition);
									if (!title.equals("No albums in this genre yet")) {
										showOptions(dbh.getAlbumID(title), title, 1); // Show options menu;
																						// the third
																						// param (2) is the
																						// type genre
										return true;
									}
								}
								return true;
							} else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
								int groupPosition = ExpandableListView.getPackedPositionGroup(id);

								if (((TextView) view).isSelected() == false) {
									String genre = (String) ((ExpandableListView) parent).getExpandableListAdapter()
											.getGroup(groupPosition);
									if (!genre.equals("No genres")) {
										showOptions(dbh.getGenreID(genre), genre, 2); // The third param
																						// (2) is the type
																						// genre
										return true;
									}
								}
							}
							return false;
						}
					});
				}
			}
		}
	}

	// Method for listing all the songs in the list view
	public void refreshSongList(List<Song> songs) {
		// Initialize the List View
		ListView listView = null;
		// Initialize the Adapter
		MyListAdapter adapter = new MyListAdapter(context);

		// Check the context and display then assign the List Views to their
		// appropriate Views
		String activity = context.toString();
		if (activity.contains("MainActivity")) {
			listView = (ListView) ((Activity) context).findViewById(R.id.displaySongs);
		}
		if (activity.contains("AddContentActivity")) {
			listView = (ListView) ((Activity) context).findViewById(R.id.displaySongs2);
		}
		if (activity.contains("DisplaySongsActivity")) {
			listView = (ListView) ((Activity) context).findViewById(R.id.displaySongs3);
		}

		listView.setAdapter(adapter); // Clear the adapter
		for (int x = 0; x < songs.size(); x++) { // Loop through artists in list
													// and set the proper index
			adapter.setGroup(songs.get(x).getTitle());
			listView.setAdapter(adapter);
			listView.setClickable(true);

			// Set the single click for song
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (((TextView) view).isSelected() == false) {
						String title = (String) parent.getItemAtPosition(position);
						if (!title.equals("No songs")) {
							// This will be the viewSongs method eventually once
							// I implement it
						}
					}
					return;
				}
			});

			listView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					if (((TextView) view).isSelected() == false) {
						String title = (String) parent.getItemAtPosition(position);
						if (!title.equals("No songs")) {
							showOptions(dbh.getSongID(title), title, 3); // Show
																			// options
																			// menu
							return true;
						}
					}
					return false;
				}
			});
		}
	}

	// Method to populate the artist and genre drop downs in AddAlbums
	public void refreshSpinners() {
		Spinner spinArtist = (Spinner) ((Activity) context).findViewById(R.id.spinArtist);
		ArrayList<String> artistList = dbh.getAllArtists(); // Return all the
															// artists in the
															// database

		if (!artistList.isEmpty()) {
			// Set the artist drop down box to be populated by all artists in
			// the database
			ArrayAdapter<String> adapterArtist = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_dropdown_item, artistList);
			adapterArtist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinArtist.setAdapter(adapterArtist);
		}

		Spinner spinGenre = (Spinner) ((Activity) context).findViewById(R.id.spinGenre);
		ArrayList<String> genreList = dbh.getAllGenres(); // Return all the
															// genres in the
															// database

		if (!genreList.isEmpty()) {
			// Set the genre drop down box to be populated by all genres in the
			// database
			ArrayAdapter<String> adapterGenre = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_dropdown_item, genreList);
			adapterGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinGenre.setAdapter(adapterGenre);
		}

		Spinner spinAlbum = (Spinner) ((Activity) context).findViewById(R.id.spinAlbum);
		ArrayList<String> albumList = dbh.getAllAlbums();

		if (!albumList.isEmpty()) {
			// Set the artist drop down box to be populated by all artists in
			// the database
			ArrayAdapter<String> adapterAlbum = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_dropdown_item, albumList);
			adapterAlbum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinAlbum.setAdapter(adapterAlbum);
		}
	}

	/********** End Refresh Methods **********/

	/********** View Methods **********/

	public void viewDisplaySongsByAlbum(String album) {
		Intent displaySongs = new Intent(context, DisplaySongsActivity.class);
		displaySongs.putExtra("albumTitle", album);
		context.startActivity(displaySongs);
	}

	public void viewEdit(String catagory, int type) {
		switch (type) {
		case 0: // Artist
			Intent editArtist = new Intent(context, EditArtistActivity.class);
			editArtist.putExtra("artistName", catagory);
			editArtist.putExtra("activity", "MainActivity");
			context.startActivity(editArtist);
			return;
		case 1: // Album
			Intent editAlbum = new Intent(context, EditAlbumActivity.class);
			editAlbum.putExtra("albumName", catagory);
			editAlbum.putExtra("activity", "MainActivity");
			context.startActivity(editAlbum);
			return;
		case 2: // Genre
			Intent editGenre = new Intent(context, EditGenreActivity.class);
			editGenre.putExtra("genreName", catagory);
			editGenre.putExtra("activity", "MainActivity");
			context.startActivity(editGenre);
			return;
		case 3: // Song
			Intent editSong = new Intent(context, EditSongActivity.class);
			editSong.putExtra("title", catagory);
			editSong.putExtra("activity", "MainActivity");
			context.startActivity(editSong);
			return;
		}
	}

	/********** Pop up menus **********/

	// Throw a pop up menu up for when the user selects an album title
	public void showOptions(final int rowId, final String title, final int type) {
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
		helpBuilder.setTitle(title); // Set the title for the options popup
		LayoutInflater inflater = ((Activity) context).getLayoutInflater(); // Inflate
																			// the
																			// popup
																			// menu
																			// layout
		View options = inflater.inflate(R.layout.popup_options, null);
		helpBuilder.setView(options);

		final AlertDialog helpDialog = helpBuilder.create(); // Show the options
																// menu
		helpDialog.show();

		TextView edit = (TextView) options.findViewById(R.id.edit); // Find the
																	// text view
																	// for Edit
		edit.setOnClickListener(new View.OnClickListener() { // Set the
																// onClickListener
																// for the Edit
																// option
			@Override
			public void onClick(View v) { // This will start the new intent for
											// Edit view
				viewEdit(title, type); // Show the Edit activity
				helpDialog.dismiss(); // Close the options menu
				return;
			}
		});

		TextView delete = (TextView) options.findViewById(R.id.delete); // Find
																		// the
																		// text
																		// view
																		// for
																		// Delete
		delete.setOnClickListener(new View.OnClickListener() { // Set the
																// onClickListener
																// for the
																// Delete option
			@Override
			public void onClick(View v) {
				showConfirm(rowId, title, type); // Show the confirmation dialog
				helpDialog.dismiss(); // Close the options menu
				return;
			}
		});

	}

	// Throw a confirmation dialog for when user attempts to delete
	public void showConfirm(final int rowId, final String title, int type) {
		AlertDialog.Builder confirm = new AlertDialog.Builder(context);
		AlertDialog confirmDialog;
		switch (type) {
		case 0:
			confirm.setTitle("Really?");
			confirm.setMessage("Are you sure you want to delete the artist " + title
					+ "?\nAll content by them will also be deleted.");
			confirm.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { // Delete
																			// the
																			// artist
																			// if
																			// user
																			// confirms
					dbh.open();
					dbh.deleteArtist(rowId);
					dbh.close();

					Toast toast = Toast.makeText(context, "The artist " + title + " has been deleted.",
							Toast.LENGTH_SHORT);
					toast.show();

					// refreshArtistList(dbh.getArtistAndTitles(),
					// context);
					return;
				}
			});
			confirm.setNegativeButton("Cancel", new OnClickListener() { // Do
						// nothing
						// if
						// user
						// cancels
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast toast = Toast.makeText(context, "The artist " + title + " was not deleted.",
									Toast.LENGTH_SHORT);
							toast.show();
							return;
						}
					});
			confirmDialog = confirm.create(); // Show the confirm dialog
			confirmDialog.show();
			return;
		case 1:
			confirm.setTitle("Really?");
			confirm.setMessage("Are you sure you want to delete the album " + title
					+ "?\nAll songs in this album will be removed.");
			confirm.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { // Delete
																			// the
																			// album
																			// if
																			// user
																			// confirms
					dbh.open();
					dbh.deleteAlbum(rowId);
					dbh.close();

					Toast toast = Toast.makeText(context, "The album " + title + " has been deleted.",
							Toast.LENGTH_SHORT);
					toast.show();

					// Check the current tab; refreshGenreList only
					// called if on Genre tab
					// String currentTab =
					// mTabsAdapter.getItem(mViewPager.getCurrentItem()).toString();
					// if (!currentTab.contains("ViewGenreActivity")) {
					refreshArtistList(dbh.getArtistAndTitles());
					// refreshAlbumList(dbh.getAllAlbums());
					// }
					// else {
					refreshGenreList(dbh.getGenreAndAlbums());
					// }
					return;
				}
			});
			confirm.setNegativeButton("Cancel", new OnClickListener() { // Do
						// nothing
						// if
						// user
						// cancels
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast toast = Toast.makeText(context, "The album " + title + " was not deleted.",
									Toast.LENGTH_SHORT);
							toast.show();
							return;
						}
					});
			confirmDialog = confirm.create(); // Show the confirm dialog
			confirmDialog.show();
			return;
		case 2:
			confirm.setTitle("Really?");
			confirm.setMessage("Are you sure you want to delete the genre " + title
					+ "?\nAll songs associated with this genre will be removed.");
			confirm.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { // Delete
																			// the
																			// genre
																			// if
																			// user
																			// confirms
					dbh.open();
					dbh.deleteGenre(rowId);
					dbh.close();
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, "The genre " + title + " has been deleted.",
							Toast.LENGTH_SHORT);
					toast.show();
					// refreshGenreList(dbh.getGenreAndAlbums(),
					// context);
					return;
				}
			});
			confirm.setNegativeButton("Cancel", new OnClickListener() { // Do
						// nothing
						// if
						// user
						// cancels
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast toast = Toast.makeText(context, "The genre " + title + " was not deleted.",
									Toast.LENGTH_SHORT);
							toast.show();
							return;
						}
					});
			confirmDialog = confirm.create(); // Show the confirm dialog
			confirmDialog.show();
			return;
		case 3:
			confirm.setTitle("Really?");
			confirm.setMessage("Are you sure you want to delete the song " + title + "?");
			confirm.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { // Delete
																			// the
																			// song
																			// if
																			// user
																			// confirms
					dbh.open();
					dbh.deleteSong(rowId);
					dbh.close();

					Toast toast = Toast.makeText(context, "The song " + title + " has been deleted.",
							Toast.LENGTH_SHORT);
					toast.show();
					// refreshSongList(dbh.getAllSongs());
					return;
				}
			});
			confirm.setNegativeButton("Cancel", new OnClickListener() { // Do
						// nothing
						// if
						// user
						// cancels
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast toast = Toast.makeText(context, "The song " + title + " was not deleted.",
									Toast.LENGTH_SHORT);
							toast.show();
							return;
						}
					});
			confirmDialog = confirm.create(); // Show the confirm dialog
			confirmDialog.show();
			return;
		}
	}
}

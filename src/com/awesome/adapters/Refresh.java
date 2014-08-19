package com.awesome.adapters;

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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.Data.MediaDatabase3;
import com.awesome.Dto.Album;
import com.awesome.Dto.Artist;
import com.awesome.Dto.Song;
import com.awesome.musiclibrary.MainActivity;
import com.awesome.musiclibrary.R;
import com.awesome.musiclibrary.editcontent.EditAlbumActivity;
import com.awesome.musiclibrary.editcontent.EditArtistActivity;
import com.awesome.musiclibrary.editcontent.EditGenreActivity;
import com.awesome.musiclibrary.editcontent.EditSongActivity;
import com.awesome.musiclibrary.viewcontent.DisplaySongsActivity;
import com.awesome.musiclibrary.viewcontent.NowPlayingActivity;

/**
 * An adapter that is used for refreshing the list views used to display the contents of the Music Library. The pop-up
 * menus for the list views are also created here.
 * 
 * @author Edward
 * 
 */
public class Refresh extends Activity {

	private Context context;
	private MainActivity mainActivity;
	private MediaDatabase3 mdb;

	/**
	 * Empty constructor for the Refresh adapter
	 */
	public Refresh() {

	}

	/**
	 * Create a new instance of the Refresh adapter
	 * 
	 * @param context
	 *            THe context of the activity to use this adapter
	 */
	public Refresh(Context context) {
		this.context = context;
		mdb = new MediaDatabase3(context);

	}

	/**
	 * Refresh the list of all artists in the Music Library
	 * 
	 * @param artistList
	 *            A list of all artists to display
	 */
	public void refreshArtists(List<Artist> artistList) {
		// Initialize the List Views here
		ExpandableListView exlvArtists = (ExpandableListView) ((Activity) context).findViewById(R.id.displayArtists);
		ListView lvArtists = (ListView) ((Activity) context).findViewById(R.id.displayArtists2);

		// Initialize the adapters
		MediaExpandableListAdapter exlaArtists = new MediaExpandableListAdapter(context);
		MyListAdapter laArtists = new MyListAdapter(context);

		// Clear the adapters
		exlvArtists.setAdapter(exlaArtists);
		lvArtists.setAdapter(laArtists);

		if (artistList != null && !artistList.isEmpty()) {
			int index = 0;
			for (Artist artist : artistList) {
				exlaArtists.setGroup(artist);

				if (artist.getAlbumList() != null && !artist.getAlbumList().isEmpty()) {
					exlaArtists.setChild(index, artist.getAlbumList());
				} else {
					exlaArtists.setChild(index, null);
				}
				index++;
			}

			exlvArtists.setAdapter(exlaArtists);
			exlvArtists.setClickable(true);
			exlvArtists.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition,
						long id) {
					if (view.isSelected() == false) {
						Album album = (Album) ((ExpandableListView) parent).getExpandableListAdapter().getChild(
								groupPosition, childPosition);
						// Get all songs for this album
						List<Song> songList = mdb.getAllSongsForAlbum(album);
						album.setSongList(songList);
						viewDisplaySongsByAlbum(album);
					}
					return false;
				}
			});

			// Set the long click listener for when selecting an album or artist
			exlvArtists.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
						int groupPosition = ExpandableListView.getPackedPositionGroup(id);
						int childPosition = ExpandableListView.getPackedPositionChild(id);

						if (view.isSelected() == false) {
							Album title = (Album) ((ExpandableListView) parent).getExpandableListAdapter().getChild(
									groupPosition, childPosition);
							// Show options menu; the second parameter (1) is the type album
							showOptions(title.getId(), title.getAlbum(), 1);
							return true;
						}
						return true;
					} else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
						int groupPosition = ExpandableListView.getPackedPositionGroup(id);

						if (view.isSelected() == false) {
							Artist artist = (Artist) ((ExpandableListView) parent).getExpandableListAdapter().getGroup(
									groupPosition);
							if (!artist.equals("No artists")) {
								// The first parameter (0) is the type artist
								showOptions(artist.getId(), artist.getArtist(), 0);
								return true;
							}
						}
					}
					return false;
				}
			});
			exlaArtists.notifyDataSetChanged();	// Refresh the list
		} else {
			LinearLayout expViewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.expViewArtist);
			expViewArtist.setVisibility(LinearLayout.GONE);
			LinearLayout viewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.viewArtist);
			viewArtist.setVisibility(LinearLayout.HORIZONTAL);
			laArtists.setGroup(null);
			lvArtists.setAdapter(laArtists);
		}
	}

	/**
	 * Refresh the list of all albums in the Music Library
	 * 
	 * @param albumList
	 *            A list of all albums to display
	 */
	public void refreshAlbums(List<Album> albumList) {
		// Initialize the List View here
		ListView lvAlbums = (ListView) ((Activity) context).findViewById(R.id.displayAlbums);

		// Initialize the adapter
		MyListAdapter laAlbums = new MyListAdapter(context);

		// Clear the adapter
		lvAlbums.setAdapter(laAlbums);

		if (albumList != null && !albumList.isEmpty()) {
			for (Album album : albumList) {
				laAlbums.setGroup(album);
				lvAlbums.setAdapter(laAlbums);

				// Set the single click for album
				lvAlbums.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if (view.isSelected() == false) {
							Album album = (Album) parent.getItemAtPosition(position);
							// Get all songs in album
							List<Song> songList = mdb.getAllSongsForAlbum(album);
							album.setSongList(songList);
							viewDisplaySongsByAlbum(album);
						}
						return;
					}
				});

				// Set the long click for album
				lvAlbums.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
						if (view.isSelected() == false) {
							Album album = (Album) parent.getItemAtPosition(position);
							// Show options menu; the second param (1) the type album
							showOptions(album.getId(), album.getAlbum(), 1);
							return true;
						}
						return false;
					}
				});
				laAlbums.notifyDataSetChanged();
			}
		} else {
			laAlbums.setGroup(null);
			lvAlbums.setAdapter(laAlbums);
			laAlbums.notifyDataSetChanged();
		}
	}

	/**
	 * Refresh the list of all songs in the Music Library
	 * 
	 * @param songList
	 *            A list of all songs to display
	 */
	public void refreshSongs(List<Song> songList) {
		// Initialize the List View here
		ListView lvSongs = (ListView) ((Activity) context).findViewById(R.id.displaySongs);

		// Initialize the adapter
		MyListAdapter laSongs = new MyListAdapter(context);

		// Clear the adapter
		lvSongs.setAdapter(laSongs);

		if (songList != null && !songList.isEmpty()) {
			for (Song song : songList) {
				laSongs.setGroup(song);
				lvSongs.setAdapter(laSongs);

				// Set the single click for album
				lvSongs.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if (view.isSelected() == false) {
							Song song = (Song) parent.getItemAtPosition(position);
							// TODO Change this
							viewNowPlaying(song);
						}
						return;
					}
				});
				laSongs.notifyDataSetChanged();
			}
		} else {
			laSongs.setGroup(null);
			lvSongs.setAdapter(laSongs);
			laSongs.notifyDataSetChanged();
		}
	}

	/**
	 * Refresh the entire library all at once. Fills all list views with the appropriate catagory
	 * 
	 * @param artistList
	 *            A list of all artists to display
	 * @param albumList
	 *            A list of all albums to display
	 * @param songList
	 *            A list of all songs to displays
	 */
	public void refreshLibrary(List<Artist> artistList, List<Album> albumList, List<Song> songList) {
		refreshArtists(artistList);
		refreshAlbums(albumList);
		refreshSongs(songList);
	}

	/********** View Methods **********/

	/**
	 * Opens the activity for viewing all songs in an album
	 * 
	 * @param album
	 *            The album that you want to view
	 */
	public void viewDisplaySongsByAlbum(Album album) {
		Intent displaySongs = new Intent(context, DisplaySongsActivity.class);
		displaySongs.putExtra("album", album);
		context.startActivity(displaySongs);
	}

	/**
	 * Opens the activity for viewing the currently playing song
	 * 
	 * @param song
	 *            The song to play
	 */
	public void viewNowPlaying(Song song) {
		Intent nowPlaying = new Intent(context, NowPlayingActivity.class);
		nowPlaying.putExtra("song", song);
		context.startActivity(nowPlaying);
	}

	/**
	 * Opens the activity for editing a certain category.
	 * 
	 * @param category
	 *            The category that you want to edit. This will be the artist name, album name, genre type, or song.
	 *            title
	 * @param type
	 *            The type that you want to edit. 0 = Artist; 1 = Album; 2 = Song; 3 = Genre.
	 */
	public void viewEdit(String category, int type) {
		switch (type) {
		case 0: // Artist
			Intent editArtist = new Intent(context, EditArtistActivity.class);
			editArtist.putExtra("artistName", category);
			editArtist.putExtra("activity", "MainActivity");
			context.startActivity(editArtist);
			return;
		case 1: // Album
			Intent editAlbum = new Intent(context, EditAlbumActivity.class);
			editAlbum.putExtra("albumName", category);
			editAlbum.putExtra("activity", "MainActivity");
			context.startActivity(editAlbum);
			return;
		case 2:
			// Song
			Intent editSong = new Intent(context, EditSongActivity.class);
			editSong.putExtra("title", category);
			editSong.putExtra("activity", "MainActivity");
			context.startActivity(editSong);
			return;
		case 3: // Genre
			Intent editGenre = new Intent(context, EditGenreActivity.class);
			editGenre.putExtra("genreName", category);
			editGenre.putExtra("activity", "MainActivity");
			context.startActivity(editGenre);
			return;
		}
	}

	/**
	 * Throw a pop up menu for when the user long presses on an item in a list view. This will allow the user to select
	 * between options such as editing the selection or deleting the selection.
	 * 
	 * @param rowId
	 *            The id for the item that you want to choose an option for.
	 * @param title
	 *            The title for the pop up menu. This will be the artist name, album name, genre type, or song.
	 * @param type
	 *            The type that you want to choose an option for. 0 = Artist; 1 = Album; 2 = Song; 3 = Genre.
	 */
	public void showOptions(final int rowId, final String title, final int type) {
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
		helpBuilder.setTitle(title); // Set the title for the options popup
		LayoutInflater inflater = ((Activity) context).getLayoutInflater(); // Inflate the popup menu layout
		View options = inflater.inflate(R.layout.popup_options, null);
		helpBuilder.setView(options);

		final AlertDialog helpDialog = helpBuilder.create(); // Show the options menu
		helpDialog.show();

		TextView edit = (TextView) options.findViewById(R.id.edit); // Find the text view for Edit
		edit.setOnClickListener(new View.OnClickListener() { 		// Set the onClickListener for the Edit option
			@Override
			public void onClick(View v) { 	// This will start the new intent for Edit view
				viewEdit(title, type); 		// Show the Edit activity
				helpDialog.dismiss(); 		// Close the options menu
				return;
			}
		});

		TextView delete = (TextView) options.findViewById(R.id.delete); // Find the text view for Delete
		delete.setOnClickListener(new View.OnClickListener() { // Set the onClickListener for the Delete option
			@Override
			public void onClick(View v) {
				showConfirm(rowId, title, type); // Show the confirmation dialog
				helpDialog.dismiss(); // Close the options menu
				return;
			}
		});

	}

	/**
	 * Throw a confirmation dialog for when user attempts to delete.
	 * 
	 * @param rowId
	 *            The id for the item that you want to delete.
	 * @param title
	 *            The title for the pop up menu. This will be the artist name, album name, genre type, or song.
	 * @param type
	 *            The type that you want to delete. 0 = Artist; 1 = Album; 2 = Song; 3 = Genre.
	 */
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
				public void onClick(DialogInterface dialog, int which) { // Delete the artist if user confirms
					// mdb.deleteArtist(rowId);

					Toast toast = Toast.makeText(context, "The artist " + title + " has been deleted.",
							Toast.LENGTH_SHORT);
					toast.show();

					// refreshArtistList(dbh.getArtistAndTitles(),
					// context);
					return;
				}
			});
			confirm.setNegativeButton("Cancel", new OnClickListener() { // Do nothing if user cancels
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
				public void onClick(DialogInterface dialog, int which) { // Delete the album if user confirms
					// mdb.deleteAlbum(rowId);

					Toast toast = Toast.makeText(context, "The album " + title + " has been deleted.",
							Toast.LENGTH_SHORT);
					toast.show();

					// Check the current tab; refreshGenreList only
					// called if on Genre tab
					// String currentTab =
					// mTabsAdapter.getItem(mViewPager.getCurrentItem()).toString();
					// if (!currentTab.contains("ViewGenreActivity")) {
					// refreshArtistList(mdb.getArtistAndTitles());
					// refreshAlbumList(dbh.getAllAlbums());
					// }
					// else {
					// refreshGenreList(mdb.getGenreAndAlbums());
					// }
					return;
				}
			});
			confirm.setNegativeButton("Cancel", new OnClickListener() { // Do nothing if user cancels
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
				public void onClick(DialogInterface dialog, int which) { // Delete the genre if user confirms
					// mdb.deleteGenre(rowId);
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, "The genre " + title + " has been deleted.",
							Toast.LENGTH_SHORT);
					toast.show();
					// refreshGenreList(dbh.getGenreAndAlbums(),
					// context);
					return;
				}
			});
			confirm.setNegativeButton("Cancel", new OnClickListener() { // Do nothing if user cancels
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
				public void onClick(DialogInterface dialog, int which) { // Delete the song if the user confirms
					// mdb.deleteSong(rowId);

					Toast toast = Toast.makeText(context, "The song " + title + " has been deleted.",
							Toast.LENGTH_SHORT);
					toast.show();
					// refreshSongList(dbh.getAllSongs());
					return;
				}
			});
			confirm.setNegativeButton("Cancel", new OnClickListener() { // Do nothing if user cancels
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

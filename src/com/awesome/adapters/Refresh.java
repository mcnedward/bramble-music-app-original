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

import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.categories.Song;
import com.awesome.musiclibrary.DisplaySongsActivity;
import com.awesome.musiclibrary.R;
import com.awesome.musiclibrary.editcontent.EditAlbumActivity;
import com.awesome.musiclibrary.editcontent.EditArtistActivity;
import com.awesome.musiclibrary.editcontent.EditGenreActivity;
import com.awesome.musiclibrary.editcontent.EditSongActivity;
import com.awesome.musiclibrary.viewcontent.NowPlayingActivity;
import com.awesome.utils.MusicDatabase;

public class Refresh extends Activity {

	private Context context;
	MusicDatabase mdb;

	public Refresh() {

	}

	public Refresh(Context context) {
		this.context = context;
		mdb = new MusicDatabase(context);
	}

	public void refreshArtists(List<Artist> artistList) {
		// Initialize the List Views here
		ExpandableListView exlvArtists = (ExpandableListView) ((Activity) context).findViewById(R.id.displayArtists);
		ListView lvArtists = (ListView) ((Activity) context).findViewById(R.id.displayArtists2);

		// Initialize the adapters
		MyExpandableListAdapter exlaArtists = new MyExpandableListAdapter(context);
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
							// Show options menu; the second param (1) is the type album
							showOptions(title.getAlbumId(), title.getAlbum(), 1);
							return true;
						}
						return true;
					} else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
						int groupPosition = ExpandableListView.getPackedPositionGroup(id);

						if (view.isSelected() == false) {
							Artist artist = (Artist) ((ExpandableListView) parent).getExpandableListAdapter().getGroup(
									groupPosition);
							if (!artist.equals("No artists")) {
								showOptions(artist.getArtistId(), artist.getArtist(), 0); // The first param (0) is the
																							// type artist
								return true;
							}
						}
					}
					return false;
				}
			});
		} else {
			LinearLayout expViewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.expViewArtist);
			expViewArtist.setVisibility(LinearLayout.GONE);
			LinearLayout viewArtist = (LinearLayout) ((Activity) context).findViewById(R.id.viewArtist);
			viewArtist.setVisibility(LinearLayout.HORIZONTAL);
			laArtists.setGroup(null);
			lvArtists.setAdapter(laArtists);
		}

		exlaArtists.notifyDataSetChanged();
		laArtists.notifyDataSetChanged();
	}

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
							showOptions(album.getAlbumId(), album.getAlbum(), 1);
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
		}
	}

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
		}
	}

	public void refreshLibrary(List<Artist> artistList, List<Album> albumList, List<Song> songList) {
		refreshArtists(artistList);
		refreshAlbums(albumList);
		refreshSongs(songList);

	}

	/********** View Methods **********/

	public void viewDisplaySongsByAlbum(Album album) {
		Intent displaySongs = new Intent(context, DisplaySongsActivity.class);
		displaySongs.putExtra("album", album);
		context.startActivity(displaySongs);
	}

	public void viewNowPlaying(Song song) {
		Intent nowPlaying = new Intent(context, NowPlayingActivity.class);
		nowPlaying.putExtra("song", song);
		context.startActivity(nowPlaying);
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

	// Throw a pop up menu up for when the user selects an album title
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

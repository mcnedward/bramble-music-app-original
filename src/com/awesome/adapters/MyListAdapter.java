package com.awesome.adapters;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesome.Dto.Album;
import com.awesome.Dto.Song;
import com.awesome.musiclibrary.R;

/**
 * Class that sets the list items in a customizable list view
 * Used for displaying albums and songs mainly
 * 
 * @author Edward
 * @created April 25, 2013
 * 
 */
public class MyListAdapter extends BaseAdapter {
	final private static String TAG = "MyListAdapter";
	private Context context;
	private Boolean isDisplaySong = false;

	private ArrayList<Object> groups = new ArrayList<Object>();

	public MyListAdapter(Context context) {
		this.context = context;
	}

	public void setGroup(Object group) {
		if (group == null) {
			groups = null;
		} else {
			groups.add(group);
		}
	}

	/**
	 * This method creates a generic view for displaying items in the list view
	 * 
	 * @return TextView
	 */
	public TextView getGenericView() {
		// Layout parameters for the ExpandableListView
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 98);

		TextView tv = new TextView(this.context);
		tv.setLayoutParams(lp);
		tv.setMinimumWidth(550); // Sets the width of the text view for the list
		// tv.setLongClickable(true);
		tv.setTextSize(20);
		tv.setTypeface(null, Typeface.BOLD);

		// Center the text vertically
		tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		// Set the text starting position
		tv.setPadding(45, 0, 0, 0);
		return tv;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getCount() {
		if (groups == null) {
			return 0;
		} else {
			return groups.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return groups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = null;

		try {
			Object object = getItem(position);
			if (object == null) {
				itemView = getGenericView();
				((TextView) itemView).setText("No media");
			} else if (object.getClass() == Album.class) {
				Album album = (Album) getItem(position);
				TextView txtName;
				ImageView albumArtView;

				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.listview_item, null, false);

				albumArtView = (ImageView) itemView.findViewById(R.id.albumArt);
				txtName = (TextView) itemView.findViewById(R.id.txtAlbum);

				txtName.setTextSize(20);
				txtName.setTypeface(null, Typeface.BOLD);

				txtName.setText(album.getAlbum());
				String albumArt = album.getAlbumArt();
				if (albumArt != null) {
					// Create the album art bitmap and scale it to fit properly and avoid over using memory
					File imageFile = new File(album.getAlbumArt());
					Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
					albumArtView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 92, 92, false));
				} else {
					Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.noalbumart);
					albumArtView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 92, 92, false));
				}
			} else if (object.getClass() == Song.class) {
				Song song = (Song) getItem(position);
				itemView = getGenericView();

				if (isDisplaySong) {
					((TextView) itemView).setText(song.getTitle());
					((TextView) itemView).setTypeface(null, Typeface.NORMAL);
					((TextView) itemView).setPadding(20, 0, 20, 0);
				} else {
					((TextView) itemView).setText(song.getTitle());
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}

		return itemView;
	}

	/**
	 * This method is used to determine whether the current list view is from the DisplaySongs Activity. If it is, then
	 * the list view will display the track number of the song along with the title
	 * 
	 * @param isDisplaySong
	 * @return boolean isDisplaySong
	 */
	public Boolean isDisplaySongs(Boolean isDisplaySong) {
		this.isDisplaySong = isDisplaySong;
		return this.isDisplaySong;
	}
}

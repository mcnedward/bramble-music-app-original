package com.awesome.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.awesome.Entity.Album;
import com.awesome.Entity.Artist;
import com.awesome.musiclibrary.R;
import com.awesome.util.art.ImageLoaderTask;
import com.awesome.util.art.LoadingHolder;
import com.awesome.util.view.ArtistView;

public class MediaGridAdapter<T> extends BaseAdapter {
	private static String TAG = "Adapter";

	private Context mContext;
	private LayoutInflater _inflator;

	private Boolean isDisplaySong = false;
	private List<T> groups = new ArrayList<T>();

	public MediaGridAdapter(Context context) {
		mContext = context;
		_inflator = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (groups == null) {
			return 0;
		} else {
			return groups.size();
		}
	}

	public void setGroup(T group) {
		if (group == null) {
			groups = null;
		} else {
			groups.add(group);
		}
		notifyDataSetChanged();
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
		View view = convertView;
		ArtistView artistView;
		TextView textView;

		try {
			Artist artist = (Artist) getItem(position);

			if (view == null) {
				view = _inflator.inflate(R.layout.gridview_item, parent, false);
				view.setTag(R.id.artistImage, view.findViewById(R.id.artistImage));
				view.setTag(R.id.artistText, view.findViewById(R.id.artistText));
			}

			artistView = (ArtistView) view.getTag(R.id.artistImage);
			textView = (TextView) view.getTag(R.id.artistText);

			artistView.setMinimumHeight(250);

			Album album = artist.getAlbumList().get(0);
			String albumArt = album.getAlbumArt();

			load(albumArt, artistView);
			textView.setText(artist.toString());
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		}
		return view;
	}

	/**
	 * This method creates a generic view for displaying items in the list view
	 * 
	 * @return TextView
	 */
	public TextView getGenericView() {
		TextView tv = new TextView(mContext);
		tv.setLayoutParams(new GridView.LayoutParams(250, 150));
		// tv.setLongClickable(true);
		tv.setTextSize(10);
		tv.setTypeface(null, Typeface.BOLD);
		tv.setPadding(10, 10, 10, 10);
		return tv;
	}

	public void load(String albumArt, ArtistView artistView) {
		if (cancelPotentialLoad(albumArt, artistView)) {
			ImageLoaderTask task = new ImageLoaderTask(artistView);
			LoadingHolder holder = new LoadingHolder(task);
			artistView.setLoadingHolder(holder);
			task.execute(albumArt);
		}
	}

	private boolean cancelPotentialLoad(String albumArt, ArtistView artistView) {
		ImageLoaderTask task = ImageLoaderTask.getImageLoaderTask(artistView);

		if (task != null) {
			String taskAlbumArt = task.getTaskAlbumArt();
			if ((taskAlbumArt == null) || (!taskAlbumArt.equals(albumArt))) {
				task.cancel(true);
			} else {
				return false;
			}
		}
		return true;
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

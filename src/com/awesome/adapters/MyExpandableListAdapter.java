package com.awesome.adapters;

/**
 * Edward McNealy Music Library - MyExpandableListAdapter Class Class that sets
 * the parent and child items in the drop down boxes located on Main Activity
 * View
 * 
 * April 25, 2013
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesome.categories.Album;
import com.awesome.categories.Artist;
import com.awesome.musiclibrary.R;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context;

	private List<Artist> groups = new ArrayList<Artist>();
	private List<List<Album>> children = new ArrayList<List<Album>>();

	public MyExpandableListAdapter(Context context) {
		this.context = context;
	}

	public void setGroup(Artist group) {
		groups.add(group);
	}

	// Method to change the parent object in exp list on update
	public void changeGroup(Artist oldGroup, Artist newGroup) {
		if (groups.contains(oldGroup)) {
			groups.remove(oldGroup);
			groups.add(newGroup);
		}
	}

	public void setChild(int index, List<Album> child) {
		if (child == null) {
			child = new ArrayList<Album>();
			child.add(null);
			children.add(index, child);
		} else {
			children.add(index, child);
		}
	}

	@Override
	public Album getChild(int groupPosition, int childPosition) {
		return children.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		Album album = getChild(groupPosition, childPosition);
		TextView txtName;
		ImageView albumArtView;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.listview_item, null, false);

		itemView.setBackgroundColor(context.getResources().getColor(R.color.WhiteSmoke));

		txtName = (TextView) itemView.findViewById(R.id.txtAlbum);
		albumArtView = (ImageView) itemView.findViewById(R.id.albumArt);

		txtName.setTextSize(20);
		if (album == null) {
			txtName.setText("No albums for this artist");
		} else {
			txtName.setText(album.getAlbum());

			String albumArt = album.getAlbumArt();
			if (albumArt != null) {
				File imageFile = new File(album.getAlbumArt());
				Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
				albumArtView.setImageBitmap(imageBitmap);
			}
		}

		return itemView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return children.get(groupPosition).size();
	}

	@Override
	public Artist getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

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
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		TextView artistView = getGenericView();
		artistView.setText(getGroup(groupPosition).getArtist());
		return artistView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}

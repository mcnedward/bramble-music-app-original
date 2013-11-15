package com.awesome.musiclibrary.addcontent;

import com.awesome.adapters.Control;
import com.awesome.musiclibrary.DatabaseHelper;
import com.awesome.musiclibrary.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddArtistActivity extends Fragment {

	// Initializes all variables here
	private DatabaseHelper dbh;
	private Control control;
	private Context context;

	@Override
	// You can access other activity content in this onCreateView
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.add_artist_layout, container, false);
	}

	// Called as soon as this view is opened
	@Override
	public void onStart() {
		super.onStart();

		dbh = new DatabaseHelper(getActivity()); // Start the database helper
		control = new Control(context); // Start the Control
		// control.refreshArtistList(context);
	}

	// This is used to Attach the AddContentActivity to this current fragment so
	// that the ACA context can be used here
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}
}

package com.awesome.categories;

import com.awesome.musiclibrary.DatabaseHelper;

import android.content.Context;

public class FillData {

	private DatabaseHelper dbh;
	private Context context;

	public FillData() {

	}

	public FillData(Context context) {
		this.context = context;
	}

	public void populate() {
		dbh = new DatabaseHelper(context);
		String[] artists = { "Silverstein", "Blink-182", "Asking Alexandria",
				"I See Stars", "We Came As Romans", "Attack Attack!",
				"Bullet For My Valentine", "Saosin", "Red Jumpsuit Apparatus" };
		for (int i = 0; i < artists.length; i++) {
			dbh.open();
			dbh.insertArtist(artists[i]);
			dbh.close();
		}

		String[] genres = { "Post-hardcore", "Punk", "Metal", "Rock",
				"Electronicore" };
		for (int i = 0; i < genres.length; i++) {
			dbh.open();
			dbh.insertGenre(genres[i]);
			dbh.close();
		}

		String[] albums = { "When Broken is Easily Fixed",
				"Discovering the Waterfront", "Rescue", "Dude Ranch",
				"Blink-182", "Stand Up and Scream", "Reckless and Relentless",
				"End of the World Party", "Digital Renegade",
				"To Plant a Seed", "Understanding What We've Grown to Be",
				"The Poison", "Scream, Aim, Fire", "Don't You Fake It" };
		dbh.open();
		dbh.insertAlbum(albums[0], 1, 1, "2003");
		dbh.insertAlbum(albums[1], 1, 1, "2005");
		dbh.insertAlbum(albums[2], 1, 1, "2011");
		dbh.insertAlbum(albums[3], 2, 2, "2003");
		dbh.insertAlbum(albums[4], 2, 2, "2007");
		dbh.insertAlbum(albums[5], 3, 3, "2003");
		dbh.insertAlbum(albums[6], 3, 3, "2005");
		dbh.insertAlbum(albums[7], 4, 5, "2005");
		dbh.insertAlbum(albums[8], 4, 5, "2005");
		dbh.insertAlbum(albums[9], 5, 5, "2005");
		dbh.insertAlbum(albums[10], 5, 5, "2005");
		dbh.insertAlbum(albums[11], 7, 4, "2005");
		dbh.insertAlbum(albums[12], 7, 4, "2005");
		dbh.insertAlbum(albums[13], 9, 2, "2005");
		dbh.close();
		dbh.open();
		dbh.insertSong("Smashed Into Pieces", 1);
		dbh.insertSong("My Herione", 2);
		dbh.insertSong("Intervention", 3);
		dbh.insertSong("Dammit", 4);
		dbh.insertSong("M & M's", 5);
		dbh.insertSong("The Final Episode(Let's Change the Channel", 6);
		dbh.insertSong("Closure", 7);
		dbh.insertSong("End of the World Party", 8);
		dbh.insertSong("Endless Sky", 9);
		dbh.insertSong("We Are The Reasons", 10);
		dbh.insertSong("A War Inside", 11);
		dbh.insertSong("Cries in Vain", 12);
		dbh.insertSong("Hearts Burst Into Fire", 13);
		dbh.insertSong("Atrophy", 14);
		dbh.close();
	}

}

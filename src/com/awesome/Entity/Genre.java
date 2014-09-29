package com.awesome.Entity;

import java.util.ArrayList;

public class Genre {

	private String genre;
	private ArrayList<String> albums;

	public Genre() {

	}

	public Genre(String Genre, ArrayList<String> albums) {
		this.genre = Genre;
		this.albums = albums;
	}

	public String getGenre() {
		return genre;
	}

	public ArrayList<String> getAlbums() {
		return albums;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setTitle(ArrayList<String> title) {
		this.albums = title;
	}

}

package com.awesome.categories;

public class MusicFile {

	private String artist, genre, album, year, song;

	public MusicFile() {
		// Empty constructor
	}

	public MusicFile(String artist, String genre, String album, String year,
			String song) {
		this.artist = artist;
		this.genre = genre;
		this.album = album;
		this.year = year;
		this.song = song;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtist() {
		return artist;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getGenre() {
		return genre;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAlbum() {
		return album;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getYear() {
		return year;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public String getSong() {
		return song;
	}
}

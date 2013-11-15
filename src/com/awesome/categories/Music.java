package com.awesome.categories;

public class Music {

	private String title = null;
	private String artist = null;
	private String album = null;
	private String composer = null;
	private Integer year = null;
	private Integer duration = null;
	private Integer trackNumber = null;
	private String albumArt = null;
	private Integer songId = null;
	private String titleKey = null;
	private Integer artistId = null;
	private String artistKey = null;
	private Integer albumId = null;
	private String albumKey = null;

	public Music() {

	}

	public Music(String title, String artist, String album, String composer, Integer year, Integer duration,
			Integer trackNumber, String albumArt, Integer songId, String titleKey, Integer artistId, String artistKey,
			Integer albumId, String albumKey) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.composer = composer;
		this.year = year;
		this.duration = duration;
		this.trackNumber = trackNumber;
		this.albumArt = albumArt;
		this.songId = songId;
		this.titleKey = titleKey;
		this.artistId = artistId;
		this.artistKey = artistKey;
		this.albumId = albumId;
		this.albumKey = albumKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(Integer trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getAlbumArt() {
		return albumArt;
	}

	public void setAlbumArt(String albumArt) {
		this.albumArt = albumArt;
	}

	public Integer getSongId() {
		return songId;
	}

	public void setSongId(Integer songId) {
		this.songId = songId;
	}

	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	public Integer getArtistId() {
		return artistId;
	}

	public void setArtistId(Integer artistId) {
		this.artistId = artistId;
	}

	public String getArtistKey() {
		return artistKey;
	}

	public void setArtistKey(String artistKey) {
		this.artistKey = artistKey;
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public String getAlbumKey() {
		return albumKey;
	}

	public void setAlbumKey(String albumKey) {
		this.albumKey = albumKey;
	}

}

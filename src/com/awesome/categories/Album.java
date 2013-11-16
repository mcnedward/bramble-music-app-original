package com.awesome.categories;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {

	/**
	 * Default serial version id
	 */
	private static final long serialVersionUID = 1L;
	private Integer albumId;
	private String album;
	private String albumKey;
	private String artist;
	private Integer numberOfSongs;
	private Integer firstYear;
	private Integer lastYear;
	private String albumArt;
	private List<Song> songList;

	public Album() {

	}

	public Album(Integer albumId, String album, String albumKey, String artist, Integer numberOfSongs,
			Integer firstYear, Integer lastYear, String albumArt, List<Song> songList) {
		this.albumId = albumId;
		this.album = album;
		this.albumKey = albumKey;
		this.artist = artist;
		this.numberOfSongs = numberOfSongs;
		this.firstYear = firstYear;
		this.lastYear = lastYear;
		this.albumArt = albumArt;
		this.songList = songList;
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAlbumKey() {
		return albumKey;
	}

	public void setAlbumKey(String albumKey) {
		this.albumKey = albumKey;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Integer getNumberOfSongs() {
		return numberOfSongs;
	}

	public void setNumberOfSongs(Integer numberOfSongs) {
		this.numberOfSongs = numberOfSongs;
	}

	public Integer getFirstYear() {
		return firstYear;
	}

	public void setFirstYear(Integer firstYear) {
		this.firstYear = firstYear;
	}

	public Integer getLastYear() {
		return lastYear;
	}

	public void setLastYear(Integer lastYear) {
		this.lastYear = lastYear;
	}

	public String getAlbumArt() {
		return albumArt;
	}

	public void setAlbumArt(String albumArt) {
		this.albumArt = albumArt;
	}

	public List<Song> getSongList() {
		return songList;
	}

	public void setSongList(List<Song> songList) {
		this.songList = songList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((album == null) ? 0 : album.hashCode());
		result = prime * result + ((albumArt == null) ? 0 : albumArt.hashCode());
		result = prime * result + ((albumId == null) ? 0 : albumId.hashCode());
		result = prime * result + ((albumKey == null) ? 0 : albumKey.hashCode());
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((firstYear == null) ? 0 : firstYear.hashCode());
		result = prime * result + ((lastYear == null) ? 0 : lastYear.hashCode());
		result = prime * result + ((numberOfSongs == null) ? 0 : numberOfSongs.hashCode());
		result = prime * result + ((songList == null) ? 0 : songList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (album == null) {
			if (other.album != null)
				return false;
		} else if (!album.equals(other.album))
			return false;
		if (albumArt == null) {
			if (other.albumArt != null)
				return false;
		} else if (!albumArt.equals(other.albumArt))
			return false;
		if (albumId == null) {
			if (other.albumId != null)
				return false;
		} else if (!albumId.equals(other.albumId))
			return false;
		if (albumKey == null) {
			if (other.albumKey != null)
				return false;
		} else if (!albumKey.equals(other.albumKey))
			return false;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (firstYear == null) {
			if (other.firstYear != null)
				return false;
		} else if (!firstYear.equals(other.firstYear))
			return false;
		if (lastYear == null) {
			if (other.lastYear != null)
				return false;
		} else if (!lastYear.equals(other.lastYear))
			return false;
		if (numberOfSongs == null) {
			if (other.numberOfSongs != null)
				return false;
		} else if (!numberOfSongs.equals(other.numberOfSongs))
			return false;
		if (songList == null) {
			if (other.songList != null)
				return false;
		} else if (!songList.equals(other.songList))
			return false;
		return true;
	}

}

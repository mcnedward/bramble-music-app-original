package com.awesome.categories;

/**
 * Edward McNealy
 * Music Library - Artist Class
 * Used to instantiate an instance of an artist
 * 
 * April 25, 2013
 */
import java.util.List;

public class Artist {

	private Integer artistId;
	private String artist;
	private String artistKey;
	private Integer numberOfAlbums;
	private List<Album> albumList;

	public Artist() {

	}

	public Artist(Integer artistId, String artist, String artistKey, Integer numberOfAlbums, List<Album> albumList) {
		this.artistId = artistId;
		this.artist = artist;
		this.artistKey = artistKey;
		this.numberOfAlbums = numberOfAlbums;
		this.albumList = albumList;
	}

	public Integer getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtistKey() {
		return artistKey;
	}

	public void setArtistKey(String artistKey) {
		this.artistKey = artistKey;
	}

	public Integer getNumberOfAlbums() {
		return numberOfAlbums;
	}

	public void setNumberOfAlbums(int numberOfAlbums) {
		this.numberOfAlbums = numberOfAlbums;
	}

	public List<Album> getAlbumList() {
		return albumList;
	}

	public void setAlbumList(List<Album> albumList) {
		this.albumList = albumList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((albumList == null) ? 0 : albumList.hashCode());
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((artistId == null) ? 0 : artistId.hashCode());
		result = prime * result + ((artistKey == null) ? 0 : artistKey.hashCode());
		result = prime * result + ((numberOfAlbums == null) ? 0 : numberOfAlbums.hashCode());
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
		Artist other = (Artist) obj;
		if (albumList == null) {
			if (other.albumList != null)
				return false;
		} else if (!albumList.equals(other.albumList))
			return false;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (artistId == null) {
			if (other.artistId != null)
				return false;
		} else if (!artistId.equals(other.artistId))
			return false;
		if (artistKey == null) {
			if (other.artistKey != null)
				return false;
		} else if (!artistKey.equals(other.artistKey))
			return false;
		if (numberOfAlbums == null) {
			if (other.numberOfAlbums != null)
				return false;
		} else if (!numberOfAlbums.equals(other.numberOfAlbums))
			return false;
		return true;
	}

}

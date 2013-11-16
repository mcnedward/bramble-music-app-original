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

	/**
	 * Empty constructor.
	 */
	public Artist() {

	}

	/**
	 * Constructor for creating a new Artist object.
	 * 
	 * @param artistId
	 *            The id for the artist.
	 * @param artist
	 *            The name of the artist.
	 * @param artistKey
	 *            A key for the artist, used for searching, sorting, and grouping.
	 * @param numberOfAlbums
	 *            The number of albums released by this artist.
	 * @param albumList
	 *            A list of all albums by this artist.
	 */
	public Artist(Integer artistId, String artist, String artistKey, Integer numberOfAlbums, List<Album> albumList) {
		this.artistId = artistId;
		this.artist = artist;
		this.artistKey = artistKey;
		this.numberOfAlbums = numberOfAlbums;
		this.albumList = albumList;
	}

	/**
	 * @return The artistId.
	 */
	public Integer getArtistId() {
		return artistId;
	}

	/**
	 * @param artistId
	 *            The artistId to set.
	 */
	public void setArtistId(Integer artistId) {
		this.artistId = artistId;
	}

	/**
	 * @return The artist.
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist
	 *            The artist to set.
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * @return The artistKey.
	 */
	public String getArtistKey() {
		return artistKey;
	}

	/**
	 * @param artistKey
	 *            The artistKey to set.
	 */
	public void setArtistKey(String artistKey) {
		this.artistKey = artistKey;
	}

	/**
	 * @return The numberOfAlbums.
	 */
	public Integer getNumberOfAlbums() {
		return numberOfAlbums;
	}

	/**
	 * @param numberOfAlbums
	 *            The numberOfAlbums to set.
	 */
	public void setNumberOfAlbums(Integer numberOfAlbums) {
		this.numberOfAlbums = numberOfAlbums;
	}

	/**
	 * @return The albumList.
	 */
	public List<Album> getAlbumList() {
		return albumList;
	}

	/**
	 * @param albumList
	 *            The albumList to set.
	 */
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

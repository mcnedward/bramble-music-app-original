package com.awesome.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * A class for containing all the information about an album.
 * 
 * @author Edward
 * 
 */
public class Album extends Media implements Serializable {

	/**
	 * Default serial version id
	 */
	private static final long serialVersionUID = 1L;
	private String album;
	private String albumKey;
	private Integer artistId;
	private String artist;
	private Integer numberOfSongs;
	private Integer firstYear;
	private Integer lastYear;
	private String albumArt;
	private List<Song> songList;

	/**
	 * Empty constructor.
	 */
	public Album() {

	}

	/**
	 * Constructor for creating a new Album object.
	 * 
	 * @param album
	 *            The name of the album.
	 * @param albumKey
	 *            A key for the album, used for searching, sorting, and
	 *            grouping.
	 * @param artistId
	 *            The id for the artist of the album.
	 * @param artist
	 *            The artist of the album.
	 * @param numberOfSongs
	 *            The number of songs on the album.
	 * @param firstYear
	 *            The first year in which songs on the album were released.
	 * @param lastYear
	 *            The last year in which songs on the album were released.
	 * @param albumArt
	 *            The path to the art for this album.
	 * @param songList
	 *            A list of all songs on this album.
	 */
	public Album(Integer albumId, String album, String albumKey, String artist,
			Integer numberOfSongs, Integer firstYear, Integer lastYear,
			String albumArt, List<Song> songList) {
		id = albumId;
		this.album = album;
		this.albumKey = albumKey;
		this.artist = artist;
		this.numberOfSongs = numberOfSongs;
		this.firstYear = firstYear;
		this.lastYear = lastYear;
		this.albumArt = albumArt;
		this.songList = songList;
	}

	/**
	 * @return The album.
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * @param album
	 *            The album to set.
	 */
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * @return The albumKey.
	 */
	public String getAlbumKey() {
		return albumKey;
	}

	/**
	 * @param albumKey
	 *            The albumKey to set.
	 */
	public void setAlbumKey(String albumKey) {
		this.albumKey = albumKey;
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
	 * @return the artistId
	 */
	public Integer getArtistId() {
		return artistId;
	}

	/**
	 * @param artistId the artistId to set
	 */
	public void setArtistId(Integer artistId) {
		this.artistId = artistId;
	}

	/**
	 * @return The numberOfSongs.
	 */
	public Integer getNumberOfSongs() {
		return numberOfSongs;
	}

	/**
	 * @param numberOfSongs
	 *            The numberOfSongs to set.
	 */
	public void setNumberOfSongs(Integer numberOfSongs) {
		this.numberOfSongs = numberOfSongs;
	}

	/**
	 * @return The firstYear.
	 */
	public Integer getFirstYear() {
		return firstYear;
	}

	/**
	 * @param firstYear
	 *            The firstYear to set.
	 */
	public void setFirstYear(Integer firstYear) {
		this.firstYear = firstYear;
	}

	/**
	 * @return The lastYear.
	 */
	public Integer getLastYear() {
		return lastYear;
	}

	/**
	 * @param lastYear
	 *            The lastYear to set.
	 */
	public void setLastYear(Integer lastYear) {
		this.lastYear = lastYear;
	}

	/**
	 * @return The albumArt.
	 */
	public String getAlbumArt() {
		return albumArt;
	}

	/**
	 * @param albumArt
	 *            The albumArt to set.
	 */
	public void setAlbumArt(String albumArt) {
		this.albumArt = albumArt;
	}

	/**
	 * @return The songList.
	 */
	public List<Song> getSongList() {
		return songList;
	}

	/**
	 * @param songList
	 *            The songList to set.
	 */
	public void setSongList(List<Song> songList) {
		this.songList = songList;
	}

	@Override
	public String toString() {
		return album;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((album == null) ? 0 : album.hashCode());
		result = prime * result
				+ ((albumArt == null) ? 0 : albumArt.hashCode());
		result = prime * result
				+ ((albumKey == null) ? 0 : albumKey.hashCode());
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result
				+ ((firstYear == null) ? 0 : firstYear.hashCode());
		result = prime * result
				+ ((lastYear == null) ? 0 : lastYear.hashCode());
		result = prime * result
				+ ((numberOfSongs == null) ? 0 : numberOfSongs.hashCode());
		result = prime * result
				+ ((songList == null) ? 0 : songList.hashCode());
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

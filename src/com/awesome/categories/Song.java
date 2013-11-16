package com.awesome.categories;

import java.io.Serializable;

public class Song implements Serializable {

	/**
	 * Default serial id
	 */
	private static final long serialVersionUID = 1L;
	private Integer titleId;
	private String title;
	private String titleKey;
	private String displayName;
	private String artist;
	private String artistKey;
	private String album;
	private String albumKey;
	private String composer;
	private Integer track;
	private Integer duration;
	private Integer year;
	private Integer dateAdded;
	private String mimeType;
	private String data;

	public Song() {

	}

	/**
	 * Constructor used to create a new Song object.
	 * 
	 * @param titleId
	 *            The id for the song.
	 * @param title
	 *            The title of the song.
	 * @param titleKey
	 *            A key for the song, used for searching, sorting, and grouping.
	 * @param displayName
	 *            The display name of the song.
	 * @param artist
	 *            The artist of the song.
	 * @param artistKey
	 *            A key for the artist, used for searching, sorting, and grouping.
	 * @param album
	 *            The album on which the song appears.
	 * @param albumKey
	 *            A key for the album, used for searching, sorting, and grouping.
	 * @param composer
	 *            The composer of the song.
	 * @param track
	 *            The track number of the song.
	 * @param duration
	 *            The duration of the song.
	 * @param year
	 *            The year the song was released.
	 * @param dateAdded
	 *            The time the song was added to the media provider.
	 * @param mimeType
	 *            The MIME type for the song.
	 * @param data
	 *            The data stream for the song.
	 */
	public Song(Integer titleId, String title, String titleKey, String displayName, String artist, String artistKey,
			String album, String albumKey, String composer, Integer track, Integer duration, Integer year,
			Integer dateAdded, String mimeType, String data) {
		this.titleId = titleId;
		this.title = title;
		this.titleKey = titleKey;
		this.displayName = displayName;
		this.artist = artist;
		this.artistKey = artistKey;
		this.album = album;
		this.albumKey = albumKey;
		this.composer = composer;
		this.track = track;
		this.duration = duration;
		this.year = year;
		this.dateAdded = dateAdded;
		this.mimeType = mimeType;
		this.data = data;
	}

	/**
	 * @return The titleId.
	 */
	public Integer getTitleId() {
		return titleId;
	}

	/**
	 * @param titleId
	 *            The titleId to set.
	 */
	public void setTitleId(Integer titleId) {
		this.titleId = titleId;
	}

	/**
	 * @return The title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return The titleKey.
	 */
	public String getTitleKey() {
		return titleKey;
	}

	/**
	 * @param titleKey
	 *            The titleKey to set.
	 */
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	/**
	 * @return The displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	 * @return The composer.
	 */
	public String getComposer() {
		return composer;
	}

	/**
	 * @param composer
	 *            The composer to set.
	 */
	public void setComposer(String composer) {
		this.composer = composer;
	}

	/**
	 * @return The track.
	 */
	public Integer getTrack() {
		return track;
	}

	/**
	 * @param track
	 *            The track to set.
	 */
	public void setTrack(Integer track) {
		this.track = track;
	}

	/**
	 * @return The duration.
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            The duration to set.
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * @return The year.
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year
	 *            The year to set.
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return The dateAdded.
	 */
	public Integer getDateAdded() {
		return dateAdded;
	}

	/**
	 * @param dateAdded
	 *            The dateAdded to set.
	 */
	public void setDateAdded(Integer dateAdded) {
		this.dateAdded = dateAdded;
	}

	/**
	 * @return The mimeType.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType
	 *            The mimeType to set.
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return The data.
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            The data to set.
	 */
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((albumKey == null) ? 0 : albumKey.hashCode());
		result = prime * result + ((artistKey == null) ? 0 : artistKey.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((dateAdded == null) ? 0 : dateAdded.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((titleId == null) ? 0 : titleId.hashCode());
		result = prime * result + ((titleKey == null) ? 0 : titleKey.hashCode());
		result = prime * result + ((track == null) ? 0 : track.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		Song other = (Song) obj;
		if (albumKey == null) {
			if (other.albumKey != null)
				return false;
		} else if (!albumKey.equals(other.albumKey))
			return false;
		if (artistKey == null) {
			if (other.artistKey != null)
				return false;
		} else if (!artistKey.equals(other.artistKey))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (dateAdded == null) {
			if (other.dateAdded != null)
				return false;
		} else if (!dateAdded.equals(other.dateAdded))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (mimeType == null) {
			if (other.mimeType != null)
				return false;
		} else if (!mimeType.equals(other.mimeType))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (titleId == null) {
			if (other.titleId != null)
				return false;
		} else if (!titleId.equals(other.titleId))
			return false;
		if (titleKey == null) {
			if (other.titleKey != null)
				return false;
		} else if (!titleKey.equals(other.titleKey))
			return false;
		if (track == null) {
			if (other.track != null)
				return false;
		} else if (!track.equals(other.track))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

}

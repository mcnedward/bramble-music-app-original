package com.awesome.Dto;

import java.io.Serializable;

import android.net.Uri;

public class Song extends Media implements Serializable {

	/**
	 * Default serial id
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String titleKey;
	private String displayName;
	private Integer artistId;
	private Integer albumId;
	private String composer;
	private Integer track;
	private Integer duration;
	private Integer year;
	private Integer dateAdded;
	private String mimeType;
	private String data;
	private Boolean isMusic;

	public Song() {

	}

	public Song(Integer titleId, String title, String titleKey,
			String displayName, Integer artistId, Integer albumId,
			String composer, Integer track, Integer duration, Integer year,
			Integer dateAdded, String mimeType, String data, Boolean isMusic) {
		id = titleId;
		this.title = title;
		this.titleKey = titleKey;
		this.displayName = displayName;
		this.artistId = artistId;
		this.albumId = albumId;
		this.composer = composer;
		this.track = track;
		this.duration = duration;
		this.year = year;
		this.dateAdded = dateAdded;
		this.mimeType = mimeType;
		this.data = data;
		this.isMusic = isMusic;
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
	 * @return The albumId.
	 */
	public Integer getAlbumId() {
		return albumId;
	}

	/**
	 * @param albumId
	 *            The albumId to set.
	 */
	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
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

	public Boolean isMusic() {
		return isMusic;
	}

	public void setIsMusic(Boolean isMusic) {
		this.isMusic = isMusic;
	}

	/**
	 * This will return the song data as a uri, to be used for setting the data source of the media player.
	 * 
	 * @return The song data as a uri.
	 */
	public Uri getUri() {
		return Uri.parse(data);
	}
	
	@Override
	public String toString() {
		return displayName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((albumId == null) ? 0 : albumId.hashCode());
		result = prime * result
				+ ((artistId == null) ? 0 : artistId.hashCode());
		result = prime * result
				+ ((composer == null) ? 0 : composer.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((dateAdded == null) ? 0 : dateAdded.hashCode());
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result
				+ ((mimeType == null) ? 0 : mimeType.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((titleKey == null) ? 0 : titleKey.hashCode());
		result = prime * result + ((track == null) ? 0 : track.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Song other = (Song) obj;
		if (albumId == null) {
			if (other.albumId != null)
				return false;
		} else if (!albumId.equals(other.albumId))
			return false;
		if (artistId == null) {
			if (other.artistId != null)
				return false;
		} else if (!artistId.equals(other.artistId))
			return false;
		if (composer == null) {
			if (other.composer != null)
				return false;
		} else if (!composer.equals(other.composer))
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

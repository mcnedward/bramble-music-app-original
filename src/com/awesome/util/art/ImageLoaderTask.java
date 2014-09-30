package com.awesome.util.art;

import java.io.File;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.awesome.util.view.ArtworkView;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

	private String taskAlbumArt;
	private final WeakReference<ArtworkView> artworkViewReference;

	public ImageLoaderTask(ArtworkView gridItemView) {
		artworkViewReference = new WeakReference<ArtworkView>(gridItemView);
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		taskAlbumArt = params[0];
		Bitmap imageBitmap = null;
		if (taskAlbumArt != null) {
			// Create the album art bitmap and scale it to fit properly and avoid over using memory
			File imageFile = new File(taskAlbumArt);
			imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		}
		return imageBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (artworkViewReference != null) {
			ArtworkView artworkView = artworkViewReference.get();
			ImageLoaderTask imageLoaderTask = getImageLoaderTask(artworkView);
			if (imageLoaderTask != null) {
				if (this == imageLoaderTask) {
					if (artworkView != null) {
						artworkView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 250, false));
					}
				}
			}
		}
	}

	public String getTaskAlbumArt() {
		return taskAlbumArt;
	}
	
	public static ImageLoaderTask getImageLoaderTask(ArtworkView artworView) {
		ImageLoaderTask task = null;
		LoadingHolder holder = artworView.getLoadingHolder();
		if (holder != null) {
			task = holder.getTask();
		}
		return task;
	}

}

package com.awesome.util.art;

import java.lang.ref.WeakReference;

public class LoadingHolder {
	
	private final WeakReference<ImageLoaderTask> taskReference;
	
	public LoadingHolder(ImageLoaderTask task) {
		taskReference = new WeakReference<ImageLoaderTask>(task);
	}
	
	public ImageLoaderTask getTask() {
		return taskReference.get();
	}

}

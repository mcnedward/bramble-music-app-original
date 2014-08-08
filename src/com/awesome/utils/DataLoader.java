package com.awesome.utils;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Source: http://www.javacodegeeks.com/2013/08/android-custom-loader-to-load-data-directly-from-sqlite-database.html
 * @author emcnealy
 *
 */
public abstract class DataLoader<E extends List<?>> extends AsyncTaskLoader<E> {
	
	protected E mLastDataList = null;
	
	public DataLoader(Context context) {
		super(context);
	}
	
	protected abstract E buildList();
	
	/**
	 *  Runs on worker thread to load data.
	 *  Delegates the real work to concrete subclass' buildCursor() method
	 */
	@Override
	public E loadInBackground() {
		return buildList();
	}
	
	/**
	 * Runs on UI thread, routing results from background thread to whatever is using dataList
	 */
	@Override
	public void deliverResult(E dataList) {
		if (isReset()) {
			// An async query came in while the loader is stopped
			emptyDataList(dataList);
			return;
		}
		E oldDataList = mLastDataList;
		mLastDataList = dataList;
		if (isStarted()) {
			super.deliverResult(dataList);
		}
		if (oldDataList != null && oldDataList != dataList && oldDataList.size() > 0) {
			emptyDataList(oldDataList);
		}
	}
	
	/**
	 * Starts an asynchronous load of the list data. When the result is ready, the callbacks will be called on the UI
	 * thread. If a previous load has been completed and is still valid, the result may be passed to the
	 * callbacks immediately.
	 * Must be called from the UI thread.
	 */
	
}

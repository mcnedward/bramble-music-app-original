package com.awesome.Loader;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.awesome.Data.Source.DataSource;

/**
 * Source:
 * http://www.phloxblog.in/android-custom-loader-load-data-sqlite-database
 * -android-version-1-6/#.U-0I5PldWCk
 * 
 * @author emcnealy
 * 
 * @param <T>
 */
public abstract class BaseDataLoader<T> extends AsyncTaskLoader<List<T>> {

	protected List<T> mDataList = null;
	protected abstract List<T> buildList();

	public BaseDataLoader(Context context, DataSource<T> dataSource) {
		super(context);
	}

	/**
	 * Runs on a worker thread, loading in the data. Delegates the real work to
	 * concrete subclass' buildList() method.
	 */
	@Override
	public List<T> loadInBackground() {
		return buildList();
	}

	/**
	 * Runs on the UI thread, routing the results from the background thread to
	 * whatever is using the dataList.
	 */
	@Override
	public void deliverResult(List<T> dataList) {
		if (isReset()) {
			emptyDataList(dataList);
			return;
		}
		List<T> oldDataList = mDataList;
		mDataList = dataList;
		if (isStarted()) {
			super.deliverResult(dataList);
		}
		if (oldDataList != null && oldDataList != dataList
				&& oldDataList.size() > 0) {
			emptyDataList(oldDataList);
		}
	}

	/**
	 * Starts an asynchronous load of the list data. When the result is ready
	 * the callbacks will be called on the UI thread. If a previous load has
	 * been completed and is still valid, the result may be passed to the
	 * callbacks immediately. Must be called from the UI thread.
	 */
	@Override
	protected void onStartLoading() {
		if (mDataList != null) {
			deliverResult(mDataList);
		}
		if (takeContentChanged() || mDataList == null || mDataList.size() == 0) {
			forceLoad();
		}
	}

	/**
	 * Must be called from the UI thread, triggered by a call to stopLoading().
	 */
	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	/**
	 * Must be called from the UI thread, triggered by a call to cancel(). Here,
	 * we make sure our Cursor is closed, if it still exists and is not already
	 * closed.
	 */
	@Override
	public void onCanceled(List<T> dataList) {
		if (dataList != null & dataList.size() > 0)
			emptyDataList(dataList);
	}

	/**
	 * Must be called from the UI thread, triggered by a call to reset(). Here,
	 * we make sure our Cursor is closed, if it still exists and is not already
	 * closed.
	 */
	@Override
	protected void onReset() {
		super.onReset();
		// Ensure the loader is stopped
		onStopLoading();
		if (mDataList != null && mDataList.size() > 0) {
			emptyDataList(mDataList);
		}
		mDataList = null;
	}

	protected void emptyDataList(List<T> dataList) {
		if (dataList != null && dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				dataList.remove(i);
			}
		}
	}

}

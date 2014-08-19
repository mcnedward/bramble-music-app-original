package com.awesome.Loader.Task;

import android.os.AsyncTask;
import android.support.v4.content.Loader;

import com.awesome.Data.Source.IDataSource;

/**
 * @author Edward
 *
 * @param <T1>
 * @param <T2>
 * @param <T3>
 */
public abstract class DataChangeTask<T1, T2, T3> extends AsyncTask<T1, T2, T3> {
	
	private Loader<?> mLoader;
	protected T1 mEntity;
	protected IDataSource<T1> mDataSource;
	
	public DataChangeTask(Loader<?> loader, T1 entity, IDataSource<T1> dataSource) {
		mLoader = loader;
		mEntity = entity;
		mDataSource = dataSource;
	}
	
	@Override
	protected void onPostExecute(T3 param) {
		mLoader.onContentChanged();
	}

}

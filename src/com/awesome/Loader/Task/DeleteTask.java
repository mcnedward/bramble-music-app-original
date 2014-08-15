package com.awesome.Loader.Task;

import android.support.v4.content.Loader;

import com.awesome.Data.Source.DataSource;

public class DeleteTask<T> extends DataChangeTask<T, Void, Void> {
	
	public DeleteTask(Loader<?> loader, T entity, DataSource<T> dataSource) {
		super(loader, entity, dataSource);
	}
	
	@Override
	protected Void doInBackground(T... params) {
		mDataSource.delete(mEntity);
		return null;
	}

}

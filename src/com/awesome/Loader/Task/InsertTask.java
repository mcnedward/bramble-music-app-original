package com.awesome.Loader.Task;

import android.support.v4.content.Loader;

import com.awesome.Data.Source.IDataSource;

public class InsertTask<T> extends DataChangeTask<T, Void, Void> {
	
	public InsertTask(Loader<?> loader, T entity, IDataSource<T> dataSource) {
		super(loader, entity, dataSource);
	}
	
	@Override
	protected Void doInBackground(T... params) {
		mDataSource.save(mEntity);
		return null;
	}

}

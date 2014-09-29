package com.awesome.Loader;

import java.util.List;

import android.content.Context;

import com.awesome.Data.Source.IDataSource;
import com.awesome.Entity.Album;
import com.awesome.Loader.Task.DeleteTask;
import com.awesome.Loader.Task.InsertTask;
import com.awesome.Loader.Task.UpdateTask;

public class AlbumDataLoader extends BaseDataLoader<Album> {
	
	private IDataSource<Album> mDataSource;
	private String mSelection;
	private String[] mSelectionArgs;
	private String mGroupBy;
	private String mHaving;
	private String mOrderBy;
	
	public AlbumDataLoader(Context context, IDataSource<Album> dataSource,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		super(context, dataSource);
		mDataSource = dataSource;
		mSelection = selection;
		mSelectionArgs = selectionArgs;
		mGroupBy = groupBy;
		mHaving = having;
		mOrderBy = orderBy;
	}

	@Override
	protected List<Album> buildList() {
		return mDataSource.read(mSelection, mSelectionArgs,
				mGroupBy, mHaving, mOrderBy);
	}
	
	public void insert(Album entity) {
		new InsertTask<Album>(this, entity, mDataSource).execute();
	}

	public void update(Album entity) {
		new UpdateTask<Album>(this, entity, mDataSource).execute();
	}

	public void delete(Album entity) {
		new DeleteTask<Album>(this, entity, mDataSource).execute();
	}

}

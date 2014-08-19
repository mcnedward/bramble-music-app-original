package com.awesome.Loader;

import java.util.List;

import android.content.Context;

import com.awesome.Data.Source.IDataSource;
import com.awesome.Dto.Song;
import com.awesome.Loader.Task.DeleteTask;
import com.awesome.Loader.Task.InsertTask;
import com.awesome.Loader.Task.UpdateTask;

public class SongDataLoader extends BaseDataLoader<Song> {

	private IDataSource<Song> mDataSource;
	private String mSelection;
	private String[] mSelectionArgs;
	private String mGroupBy;
	private String mHaving;
	private String mOrderBy;

	public SongDataLoader(Context context, IDataSource<Song> dataSource,
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
	protected List<Song> buildList() {
		return mDataSource.read(mSelection, mSelectionArgs,
				mGroupBy, mHaving, mOrderBy);
	}
	
	public void insert(Song entity) {
		new InsertTask<Song>(this, entity, mDataSource).execute();
	}

	public void update(Song entity) {
		new UpdateTask<Song>(this, entity, mDataSource).execute();
	}

	public void delete(Song entity) {
		new DeleteTask<Song>(this, entity, mDataSource).execute();
	}

}


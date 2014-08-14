package com.awesome.Loader;

import java.util.List;

import android.content.Context;

import com.awesome.Data.DataSource;
import com.awesome.Dto.Artist;

public class ArtistDataLoader extends BaseDataLoader<Artist> {

	private DataSource<Artist> mDataSource;
	private String mSelection;
	private String[] mSelectionArgs;
	private String mGroupBy;
	private String mHaving;
	private String mOrderBy;

	public ArtistDataLoader(Context context, DataSource<Artist> dataSource,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		super(context);
		mDataSource = dataSource;
		mSelection = selection;
		mSelectionArgs = selectionArgs;
		mGroupBy = groupBy;
		mHaving = having;
		mOrderBy = orderBy;
	}

	@Override
	protected List<Artist> buildList() {
		return (List<Artist>) mDataSource.read(mSelection, mSelectionArgs,
				mGroupBy, mHaving, mOrderBy);
	}

	public void insert(Artist entity) {
		new InsertTask(this).execute(entity);
	}

	public void update(Artist entity) {
		new UpdateTask(this).execute(entity);
	}

	public void delete(Artist entity) {
		new DeleteTask(this).execute(entity);
	}

	private class InsertTask extends ContentChangeTask<Artist, Void, Void> {
		InsertTask(ArtistDataLoader loader) {
			super(loader);
		}
		
		@Override
		protected Void doInBackground(Artist... params) {
			mDataSource.insert(params[0]);
			return null;
		}
	}
	
	private class UpdateTask extends ContentChangeTask<Artist, Void, Void> {
		UpdateTask(ArtistDataLoader loader) {
			super(loader);
		}
		
		@Override
		protected Void doInBackground(Artist... params) {
			mDataSource.update(params[0]);
			return null;
		}
	}
	
	private class DeleteTask extends ContentChangeTask<Artist, Void, Void> {
		DeleteTask(ArtistDataLoader loader) {
			super(loader);
		}
		
		@Override
		protected Void doInBackground(Artist... params) {
			mDataSource.delete(params[0]);
			return null;
		}
	}

}

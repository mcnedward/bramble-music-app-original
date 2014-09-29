package com.awesome.Loader;

import java.util.List;

import android.content.Context;

import com.awesome.Data.Source.ArtistDataSource;
import com.awesome.Data.Source.IDataSource;
import com.awesome.Entity.Album;
import com.awesome.Entity.Artist;
import com.awesome.Loader.Task.DeleteTask;
import com.awesome.Loader.Task.GetAlbumsForArtistTask;
import com.awesome.Loader.Task.InsertTask;
import com.awesome.Loader.Task.UpdateTask;

public class ArtistDataLoader extends BaseDataLoader<Artist> {

	private ArtistDataSource mDataSource;
	private String mSelection;
	private String[] mSelectionArgs;
	private String mGroupBy;
	private String mHaving;
	private String mOrderBy;

	public ArtistDataLoader(Context context, IDataSource<Artist> dataSource,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		super(context, dataSource);
		mDataSource = (ArtistDataSource) dataSource;
		mSelection = selection;
		mSelectionArgs = selectionArgs;
		mGroupBy = groupBy;
		mHaving = having;
		mOrderBy = orderBy;
	}

	@Override
	protected List<Artist> buildList() {
		return mDataSource.read(mSelection, mSelectionArgs,
				mGroupBy, mHaving, mOrderBy);
	}
	
	public void insert(Artist entity) {
		new InsertTask<Artist>(this, entity, mDataSource).execute();
	}

	public void update(Artist entity) {
		new UpdateTask<Artist>(this, entity, mDataSource).execute();
	}

	public void delete(Artist entity) {
		new DeleteTask<Artist>(this, entity, mDataSource).execute();
	}
	
	public void updateAlbumList(Artist entity, Album album) {
		
	}

	public void getAlbumsForArtist(Artist entity) {
		new GetAlbumsForArtistTask<Artist>(this, entity, mDataSource).execute();
	}
}

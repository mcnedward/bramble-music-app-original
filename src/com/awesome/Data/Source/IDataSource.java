package com.awesome.Data.Source;

import java.util.List;

public interface IDataSource<T> {

	public boolean save(T entity);

	public List<T> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy);

}

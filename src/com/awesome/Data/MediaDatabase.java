package com.awesome.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MediaDatabase {
	private static String TAG = "MediaDatabase";

	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mDatabase;

	public MediaDatabase() {

	}

	/**
	 * Creates an instance of MediaDatabase. Used to insert and access data for
	 * the application.
	 * 
	 * @param context
	 *            The context of the activity to use this database.
	 */
	public MediaDatabase(Context context) {
		mDBHelper = new DatabaseHelper(context);
		open();
	}

	/**
	 * Open the database for the Music Library for reading only.
	 * 
	 * @return A read-only database.
	 * @throws android.database.SQLException
	 */
	public SQLiteDatabase openToRead() throws android.database.SQLException {
		mDatabase = mDBHelper.getReadableDatabase();
		return mDatabase;
	}

	/**
	 * Opens the database for the Music Library for writing.
	 * 
	 * @return A writable database.
	 * @throws android.database.SQLException
	 */
	public SQLiteDatabase open() throws android.database.SQLException {
		mDatabase = mDBHelper.getWritableDatabase();
		return mDatabase;
	}
	
	/**
	 * Close the open database object.
	 */
	public void close() {
		if (mDBHelper != null)
			mDBHelper.close();
	}

	public long insert(String table, String nullColumnHack, ContentValues values) {
		try {
			mDatabase.beginTransaction();
			long result = mDatabase.insert(table, null, values);
			mDatabase.setTransactionSuccessful();
			return result;
		} catch (Exception e) {
			Log.d(TAG,
					"Problem with inserting into " + table + ";\n"
							+ e.getLocalizedMessage());
			return -1;
		} finally {
			mDatabase.endTransaction();
		}
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		try {
			mDatabase.beginTransaction();
			int result = mDatabase.delete(table, whereClause, whereArgs);
			mDatabase.setTransactionSuccessful();
			return result;
		} catch (Exception e) {
			Log.d(TAG, "Problem with deleting from " + table + " "
					+ whereClause + ";\n" + e.getLocalizedMessage());
			return -1;
		} finally {
			mDatabase.endTransaction();
		}
	}

	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		try {
			mDatabase.beginTransaction();
			int result = mDatabase
					.update(table, values, whereClause, whereArgs);
			mDatabase.setTransactionSuccessful();
			return result;
		} catch (Exception e) {
			Log.d(TAG, "Problem with updating from " + table + " "
					+ whereClause + ";\n" + e.getLocalizedMessage());
			return -1;
		} finally {
			mDatabase.endTransaction();
		}
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		try {
			Cursor cursor = mDatabase.query(table, columns, selection,
					selectionArgs, groupBy, having, orderBy);
			return cursor;
		} catch (Exception e) {
			Log.d(TAG, "Problem executing query for " + table);
			return null;
		}
	}
}

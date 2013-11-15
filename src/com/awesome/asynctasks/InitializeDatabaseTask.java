package com.awesome.asynctasks;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.awesome.musiclibrary.DatabaseHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class InitializeDatabaseTask extends AsyncTask<Void, Integer, Exception> {

	public static final int DATABASE_COPY_BUFFER = 4096;

	private Context context;
	private final File destination;
	private final int resourceName;
	private final Integer iterations;
	private final DatabaseHelper dbh;
	private ProgressDialog progressDialog;
	private File dbFile;

	public InitializeDatabaseTask(Context context, File destination,
			int resourceName, int expectedSize, DatabaseHelper dbh) {
		this.context = context;
		this.destination = destination;
		this.resourceName = resourceName;
		this.iterations = (Integer.valueOf(expectedSize + DATABASE_COPY_BUFFER
				- 1) / DATABASE_COPY_BUFFER);
		this.dbh = dbh;
	}

	@Override
	protected Exception doInBackground(Void... params) {
		try {
			deleteFilesInDestinationDirectory();

			File tempFile = new File(destination.getAbsolutePath() + ".tmp");
			copyDatabaseToTempFile(tempFile);

			tempFile.renameTo(destination);
		} catch (Exception e) {
			deleteFilesInDestinationDirectory();
			return e;
		}

		return null;
	}

	private void deleteFilesInDestinationDirectory() {
		File directory = destination.getParentFile();
		if (directory.exists()) {
			File[] files = directory.listFiles();
			for (File file : files) {
				file.delete();
			}
		}
	}

	private void copyDatabaseToTempFile(File tempFile) throws IOException {
		File dir = destination.getParentFile();
		if (!dir.exists()) {
			dir.mkdir();
		}

		InputStream input = null;
		FileOutputStream output = null;

		try {
			input = context.getResources().openRawResource(resourceName);
			output = new FileOutputStream(tempFile);
			byte[] buffer = new byte[DATABASE_COPY_BUFFER];

			int progress = 0;
			for (;;) {
				int nRead = input.read(buffer);
				if (nRead <= 0) {
					break;
				}

				output.write(buffer, 0, nRead);
				progress++;

				publishProgress(Integer.valueOf(progress));
			}
		} finally {
			safeClose(input);
			safeClose(output);
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// dbh.progress(values[0], iterations);
	}

	@Override
	protected void onPostExecute(Exception result) {
		// dbh.setupComplete(result);
	}

	private void safeClose(Closeable item) {
		try {
			if (item != null) {
				item.close();
			}
		} catch (Exception ignored) {
		}
	}

}

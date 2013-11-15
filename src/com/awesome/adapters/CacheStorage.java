package com.awesome.adapters;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * @author Edward
 * 
 *         Source:
 *         http://stackoverflow.com/questions/6580418/how-to-store-images
 *         -in-cache-memory
 * 
 */

public class CacheStorage {
	private static String TAG = "CacheStorage";
	private static CacheStorage INSTANCE = null;
	private HashMap cacheMap;
	private HashMap bitmapMap;
	private static final String cacheDir = "/Android/data/droid.musiclibrary/cache/";
	private static final String CACHE_FILENAME = ".cache";

	private CacheStorage() {
		cacheMap = new HashMap();
		bitmapMap = new HashMap();
		File fullCacheDir = new File(Environment.getExternalStorageDirectory()
				.toString(), cacheDir);
		if (!fullCacheDir.exists()) {
			Log.i(TAG, "Directory does not exist...");
			cleanCacheStart();
			return;
		}
		try {
			ObjectInputStream is = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(new File(
							fullCacheDir.toString(), CACHE_FILENAME))));
			cacheMap = (HashMap) is.readObject();
			is.close();
		} catch (StreamCorruptedException e) {
			Log.i(TAG, "Corrupted stream...");
			cleanCacheStart();
		} catch (FileNotFoundException e) {
			Log.i(TAG, "File not found...");
		} catch (IOException e) {
			Log.i(TAG, "Input/Output Error...");
		} catch (ClassNotFoundException e) {
			Log.i(TAG, "Class not found...");
			cleanCacheStart();
		}
	}

	private void cleanCacheStart() {
		cacheMap = new HashMap();
		File fullCacheDir = new File(Environment.getExternalStorageState()
				.toString(), cacheDir);
		fullCacheDir.mkdirs();
		File noMedia = new File(fullCacheDir.toString(), ".nomedia");
		try {
			noMedia.createNewFile();
			Log.i(TAG, "Cache created");
		} catch (IOException e) {
			Log.i(TAG, "Could not create .nomedia file");
			e.printStackTrace();
		}
	}

	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CacheStorage();
		}
	}

	public static CacheStorage getInstance() {
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}

	public void saveCacheFile(String cacheUri, Bitmap image) {
		File fullCacheDir = new File(Environment.getExternalStorageDirectory()
				.toString(), cacheDir);
		String fileLocalName = new SimpleDateFormat("ddMMyyhhmmssSSS")
				.format(new java.util.Date()) + ".PNG";
		File fileUri = new File(fullCacheDir.toString(), fileLocalName);
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(fileUri);
			image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			cacheMap.put(cacheUri, image);
			Log.i(TAG, String.format(
					"Saved file %s (which is now %s) correctly", cacheUri,
					fileUri.toString()));
			bitmapMap.put(cacheUri, image);
			ObjectOutputStream os = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(new File(
							fullCacheDir.toString(), CACHE_FILENAME))));
			os.writeObject(cacheMap);
			os.close();
		} catch (FileNotFoundException e) {
			Log.i(TAG,
					String.format("Error: File %s was not found...", cacheUri));
			e.printStackTrace();
		} catch (IOException e) {
			Log.i(TAG, "Error: File could not be stuffed...");
			e.printStackTrace();
		}
	}

	public Bitmap getCacheFile(String cacheUri) {
		if (bitmapMap.containsKey(cacheUri))
			return (Bitmap) bitmapMap.get(cacheUri);
		if (!cacheMap.containsKey(cacheMap))
			return null;
		String fileLocalName = (String) cacheMap.get(cacheUri);
		File fullCacheDir = new File(Environment.getExternalStorageDirectory()
				.toString(), cacheDir);
		File fileUri = new File(fullCacheDir.toString(), fileLocalName);
		if (!fileUri.exists())
			return null;
		Log.i(TAG,
				String.format("File %s has been found in the cache.", cacheUri));
		Bitmap bitmap = BitmapFactory.decodeFile(fileUri.toString());
		bitmapMap.put(cacheUri, bitmap);
		return bitmap;

	}
}

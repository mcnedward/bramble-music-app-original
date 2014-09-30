package com.awesome.util.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.awesome.musiclibrary.R;
import com.awesome.util.art.LoadingHolder;

public class ArtworkView extends ImageView {

	private LoadingHolder loadingHolder;
	private Bitmap defaultAlbumArt;

	public ArtworkView(Context context) {
		super(context);
		loadDefaultArt(context);
	}

	public ArtworkView(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadDefaultArt(context);
	}

	public ArtworkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		loadDefaultArt(context);
	}
	
	private void loadDefaultArt(Context context) {
		defaultAlbumArt = BitmapFactory.decodeResource(context.getResources(), R.drawable.noalbumart);
		setImageBitmap(Bitmap.createScaledBitmap(defaultAlbumArt, 100, 250, false));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
	}
	
	public void setLoadingHolder(LoadingHolder loadingHolder) {
		this.loadingHolder = loadingHolder;
		setImageBitmap(Bitmap.createScaledBitmap(defaultAlbumArt, 100, 250, false));
	}
	
	public LoadingHolder getLoadingHolder() {
		return loadingHolder;
	}

}

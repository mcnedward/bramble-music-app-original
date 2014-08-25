package com.awesome.musiclibrary;

/**
 * Edward McNealy Music Library - Main Activity View First View on Application
 * Displays all content of Music Library sorted by the user
 * 
 * Dates: January 1, 2013 April 25, 2013 April 29, 2013
 */

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.awesome.Dto.Album;
import com.awesome.Dto.Song;
import com.awesome.Loader.Adapter.AlbumDataAdapter;
import com.awesome.Loader.Adapter.ArtistDataAdapter;
import com.awesome.Loader.Adapter.SongDataAdapter;
import com.awesome.asynctasks.RetrieveMedia;
import com.awesome.musiclibrary.viewcontent.DisplaySongsActivity;
import com.awesome.musiclibrary.viewcontent.NowPlayingActivity;

public class MainActivity extends FragmentActivity {
	private static String TAG = "MainActivity";

	public static ViewPager mViewPager;
	public static PagerAdapter mPagerAdapter;

	public static AsyncTask<Song, Integer, Void> playMediaTask;
	public static MediaPlayer mPlayer = null;
	public static IntentService mediaPlayerService;
	public static ToggleButton btnPlayPause;
	public static Button btnPrevious;
	public static Button btnNext;
	public static ImageView mainAlbumArt;
	public static TextView mainSong;
	public static TextView mainAlbum;
	public static LinearLayout nowPlayingInformationLayout;
	public static Song currentSong;
	public static Album currentAlbum;

	public static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the window to allow for progress spinner
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);

		// new
		// LoadDatabase(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		new RetrieveMedia(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		mContext = getApplicationContext();

		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(4 - 1);

		btnPlayPause = (ToggleButton) findViewById(R.id.mainBtnPlayPause);
		btnPrevious = (Button) findViewById(R.id.mainBtnPrevious);
		btnNext = (Button) findViewById(R.id.mainBtnNext);
		mainAlbumArt = (ImageView) findViewById(R.id.mainNowPlayingAlbumArt);
		mainSong = (TextView) findViewById(R.id.mainNowPlayingSong);
		mainAlbum = (TextView) findViewById(R.id.mainNowPlayingAlbum);
		nowPlayingInformationLayout = (LinearLayout) findViewById(R.id.nowPlayingInformationLayout);

		/**
		 * If there is no media playing, set the play/pause button to be disable and set the album art for currently
		 * playing to be blank. If there is media playing, get the current media information and set the text views and
		 * album art. Also update the layout for currently playing information to start the Now Playing activity when
		 * touched.
		 */
		if (mPlayer == null || !mPlayer.isPlaying()) {
			btnPlayPause.setEnabled(false);
			Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noalbumart);
			MainActivity.mainAlbumArt.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 60, 60, false));
		} else {
			btnPlayPause.setEnabled(true);
			MainActivity.mainSong.setText(currentSong.getTitle());
			MainActivity.mainAlbum.setText(currentAlbum.getAlbum());
			if (currentAlbum.getAlbumArt() != null) {
				Bitmap imageBitmap = BitmapFactory.decodeFile(currentAlbum.getAlbumArt());
				MainActivity.mainAlbumArt.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 60, 60, false));
			} else {
				Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noalbumart);
				MainActivity.mainAlbumArt.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 60, 60, false));
			}
			MainActivity.nowPlayingInformationLayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Log.i(TAG, "Now Playing Information Touched!!!");
					Intent nowPlaying = new Intent(MainActivity.mContext, NowPlayingActivity.class);
					nowPlaying.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					nowPlaying.putExtra("album", MainActivity.currentAlbum);
					nowPlaying.putExtra("song", MainActivity.currentSong);
					MainActivity.mContext.startActivity(nowPlaying);
					return false;
				}

			});
		}
	}

	public MainActivity() {
		// Constructor
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void viewDisplaySongsByAlbum(Album album) {
		Intent displaySongs = new Intent(mContext, DisplaySongsActivity.class);
		displaySongs.putExtra("album", album);
		mContext.startActivity(displaySongs);
	}

	public class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
		final int PAGE_COUNT = 4;
		private List<Fragment> fragments = new ArrayList<Fragment>();
		final int[] tabs = { R.layout.view_artist_layout, R.layout.view_album_layout, R.layout.view_song_layout,
				R.layout.view_genre_layout };
		final String[] titles = { "Artists", "Albums", "Songs", "Genres" };
		private FragmentManager fm;

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
			for (String title : titles) {
				switch (title) {
				case "Artists":
					fragments.add(new ArtistDataAdapter());
					break;
				case "Albums":
					fragments.add(new AlbumDataAdapter());
					break;
				case "Songs":
					fragments.add(new SongDataAdapter());
				default:
					fragments.add(PageFragment.createTab(tabs[2]));
					break;
				}
			}
		}

		@Override
		public int getCount() {
			return PAGE_COUNT;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {

		}

	}

	public static class PageFragment extends Fragment {
		public static final String LAYOUT_PAGE = "LAYOUT_PAGE";

		private int layout;

		public static PageFragment createTab(int layout) {
			Bundle args = new Bundle();
			args.putInt(LAYOUT_PAGE, layout);
			PageFragment fragment = new PageFragment();
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			layout = getArguments().getInt(LAYOUT_PAGE);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			return inflater.inflate(layout, container, false);
		}
	}
}

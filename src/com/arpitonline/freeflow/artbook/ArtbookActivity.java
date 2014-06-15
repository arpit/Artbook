package com.arpitonline.freeflow.artbook;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Choreographer.FrameCallback;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arpitonline.freeflow.artbook.data.DribbbleDataAdapter;
import com.arpitonline.freeflow.artbook.layouts.DribbbleQuiltLayout;
import com.arpitonline.freeflow.artbook.models.DribbbleFeed;
import com.arpitonline.freeflow.artbook.models.DribbbleFetch;
import com.arpitonline.freeflow.artbook.models.Shot;
import com.comcast.freeflow.animations.interpolators.EaseInOutQuintInterpolator;
import com.comcast.freeflow.core.AbsLayoutContainer;
import com.comcast.freeflow.core.AbsLayoutContainer.OnItemClickListener;
import com.comcast.freeflow.core.FreeFlowContainer;
import com.comcast.freeflow.core.FreeFlowContainer.OnScrollListener;
import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.layouts.FreeFlowLayout;
import com.comcast.freeflow.layouts.HLayout;
import com.comcast.freeflow.layouts.VGridLayout;
import com.comcast.freeflow.layouts.VLayout;
import com.comcast.freeflow.utils.ViewUtils;
import com.squareup.picasso.Picasso;

public class ArtbookActivity extends Activity implements OnClickListener,
		android.widget.AdapterView.OnItemClickListener, OnItemClickListener {

	public static final String TAG = "ArtbookActivity";

	private FrameLayout containerFrame;
	private FreeFlowContainer container;
	private VGridLayout grid;
	private DribbbleQuiltLayout custom;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private DribbbleFetch fetch;
	private int itemsPerPage = 25;
	private int pageIndex = 1;

	DribbbleDataAdapter adapter;

	ViewGroup detailsView;

	FreeFlowLayout[] layouts;
	int currLayoutIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artbook);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		containerFrame = (FrameLayout) findViewById(R.id.frame);
		container = (FreeFlowContainer) findViewById(R.id.container);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.open_drawer,
				R.string.close_drawer) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, new String[] { "Popular",
						"Everyone", "Debuts" }));
		mDrawerList.setOnItemClickListener(this);
		CardIncomingAnimation anim = new CardIncomingAnimation();

		anim.animateIndividualCellsSequentially = false;
		anim.animateAllSetsSequentially = false;
		anim.oldCellsRemovalAnimationDuration = 300;
		anim.newCellsAdditionAnimationDurationPerCell = 300;
		anim.cellPositionTransitionAnimationDuration = 250;

		container.setLayoutAnimator(anim);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		// findViewById(R.id.load_more).setOnClickListener(this);
		custom = new DribbbleQuiltLayout();
		grid = new VGridLayout();

		int columnCount = getResources().getInteger(R.integer.column_count);

		VGridLayout.LayoutParams params = new VGridLayout.LayoutParams(size.x
				/ columnCount, (int) ((size.x / columnCount) * 0.75));
		grid.setLayoutParams(params);

		layouts = new FreeFlowLayout[] { grid, custom };

		adapter = new DribbbleDataAdapter(this);

		container.setLayout(layouts[currLayoutIndex]);
		container.setOnItemClickListener(this);
		container.setAdapter(adapter);
		fetch = new DribbbleFetch();
		selectSource(0);

		if (1 == 1) {
			detailsView = (ViewGroup) LayoutInflater.from(this).inflate(
					R.layout.shot_details, null);
			
			FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams((int) ViewUtils.dipToPixels(this, 760),
					
					FrameLayout.LayoutParams.WRAP_CONTENT );
			fl.gravity = Gravity.CENTER;
			detailsView.setLayoutParams(fl);
			containerFrame.addView(detailsView);
			detailsView.setVisibility(View.GONE);

		}

	}

	private void selectSource(int idx) {
		String url = "";
		adapter.clear();
		switch (idx) {
		case 0:
			url = DribbbleFetch.getPopularURL(itemsPerPage, pageIndex);
			break;
		case 1:
			url = DribbbleFetch.getEveryoneURL(itemsPerPage, pageIndex);
			break;
		case 2:
			url = DribbbleFetch.getDebutsURL(itemsPerPage, pageIndex);
			break;
		}

		Log.d(TAG, "Loading: " + url);

		fetch.load(this, url);
	}

	public void onDataLoaded(DribbbleFeed feed) {
		Log.d(TAG, "Feed: " + feed.getShots());
		Log.d(TAG, "photo: " + feed.getShots().get(0).getImage_teaser_url());
		adapter.update(feed);
		container.dataInvalidated();

		container.addScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(FreeFlowContainer container) {
				Log.d(TAG, "scroll percent " + container.getScrollPercentY());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.artbook, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case (R.id.action_change_layout):
			currLayoutIndex++;
			if (currLayoutIndex == layouts.length) {
				currLayoutIndex = 0;
			}
			container.setLayout(layouts[currLayoutIndex]);

			break;
		case (R.id.action_about):
			Intent about = new Intent(this, AboutActivity.class);
			startActivity(about);
			break;
		default:
			return super.onOptionsItemSelected(item);

		}

		return true;

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "Loading data");
		pageIndex++;
		fetch.load(this, DribbbleFetch.getPopularURL(itemsPerPage, pageIndex));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectSource(position);
		mDrawerLayout.closeDrawers();
	}

	public void onDataFailed() {
		Toast.makeText(this, "Error loading Dribbble data", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onItemClick(AbsLayoutContainer parent, FreeFlowItem proxy) {

		Shot s = (Shot) adapter.getSection(0).getDataAtIndex(proxy.itemIndex);

		ImageView imgView = (ImageView) detailsView.findViewById(R.id.shot_img);
		detailsView.findViewById(R.id.details_done).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						detailsView
								.animate()
								.translationY(2*containerFrame.getHeight())
								.setDuration(500)
								.setInterpolator(
										new EaseInOutQuintInterpolator()).setListener(new AnimatorListener() {
											
											@Override
											public void onAnimationStart(Animator animation) {
												
											}
											
											@Override
											public void onAnimationRepeat(Animator animation) {
												
											}
											
											@Override
											public void onAnimationEnd(Animator animation) {
												detailsView.setVisibility(View.GONE);
												detailsView.setTranslationY(0);
												detailsView.animate().setListener(null);
											}
											
											@Override
											public void onAnimationCancel(Animator animation) {
												
											}
										});
					}
				});

		((TextView) detailsView.findViewById(R.id.shot_title)).setText(s
				.getTitle());
		// ((TextView)detailsView.findViewById(R.id.shot_desc)).setText(s.get);

		Picasso.with(this).load(s.getImage_url()).into(imgView);
		detailsView.setTranslationY(2 * containerFrame.getHeight());
		detailsView.setRotation(45f);
		detailsView.setVisibility(View.VISIBLE);
		detailsView.animate().translationY(0).rotation(0).setDuration(500)
				.setInterpolator(new EaseInOutQuintInterpolator());

	}
}

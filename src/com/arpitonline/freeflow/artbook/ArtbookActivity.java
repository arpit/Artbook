package com.arpitonline.freeflow.artbook;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import com.comcast.freeflow.layouts.VGridLayout;
import com.comcast.freeflow.utils.ViewUtils;
import com.crashlytics.android.Crashlytics;

public class ArtbookActivity extends DetailsCapableActivity implements
		OnClickListener, android.widget.AdapterView.OnItemClickListener,
		OnItemClickListener {

	public static final String TAG = "ArtbookActivity";

	private FrameLayout containerFrame;
	private View loadingIndicator;
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

	FreeFlowLayout[] layouts;
	int currLayoutIndex = 0;

	protected ViewGroup detailsView;

	boolean newact;
	Point size;
	
	int columnCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_artbook);

		loadingIndicator = findViewById(R.id.loading);
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

		findViewById(R.id.about_tf).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent about = new Intent(ArtbookActivity.this,
								AboutActivity.class);
						startActivity(about);
					}
				});

		CardIncomingAnimation anim = new CardIncomingAnimation();

		anim.animateIndividualCellsSequentially = false;
		anim.animateAllSetsSequentially = false;
		anim.oldCellsRemovalAnimationDuration = 300;
		anim.newCellsAdditionAnimationDurationPerCell = 300;
		anim.cellPositionTransitionAnimationDuration = 250;

		container.setLayoutAnimator(anim);

		Display display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);

		// findViewById(R.id.load_more).setOnClickListener(this);
		custom = new DribbbleQuiltLayout();
		grid = new VGridLayout();
		columnCount = getResources().getInteger(R.integer.column_count);
		
		int itemWidth = size.x / columnCount;

		VGridLayout.LayoutParams params = new VGridLayout.LayoutParams(itemWidth, (int) (itemWidth * 0.75));
		grid.setLayoutParams(params);

		layouts = new FreeFlowLayout[] { grid, custom };
		layoutIcons = new Drawable[]{
               	getResources().getDrawable(R.drawable.ic_quilt),
               	getResources().getDrawable(R.drawable.grid)
				};

		adapter = new DribbbleDataAdapter(this);

		container.setLayout(layouts[currLayoutIndex]);
		container.setOnItemClickListener(this);
		container.setAdapter(adapter);
		
		
		
		fetch = new DribbbleFetch();
		selectSource(0);
		newact = getResources().getBoolean(R.bool.start_new_activity);
		if (newact) {

		} else {
			detailsView = (ViewGroup) LayoutInflater.from(this).inflate(
					R.layout.embedded_details, container, false);
			
			
			
			detailsView.findViewById(R.id.panel_close).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					closeDetails(true);
				}
			});
			
			detailsView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// trap the event
				}
			});
			FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
					2*itemWidth,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			fl.gravity = Gravity.RIGHT;
			detailsView.setLayoutParams(fl);
			containerFrame.addView(detailsView);
			detailsView.setVisibility(View.INVISIBLE);

		}

		container.addScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(FreeFlowContainer container) {
				if (container.getScrollPercentY() > .95 && !fetch.isLoading()) {

					pageIndex++;
					showLoading();
					fetch.load(ArtbookActivity.this, DribbbleFetch
							.getPopularURL(itemsPerPage, pageIndex));

				}
			}
		});

	}

	private void showLoading() {
		loadingIndicator.setVisibility(View.VISIBLE);
		loadingIndicator.setTranslationY(loadingIndicator.getHeight());
		loadingIndicator.animate().translationY(0).setDuration(250);
	}

	private void hideLoading() {
		loadingIndicator.animate().translationY(loadingIndicator.getHeight())
				.setDuration(250).setListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animator animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animator animation) {
						loadingIndicator.setVisibility(View.GONE);
						loadingIndicator.animate().setListener(null);

					}

					@Override
					public void onAnimationCancel(Animator animation) {
						loadingIndicator.setVisibility(View.GONE);
						loadingIndicator.animate().setListener(null);

					}
				});
	}

	private void selectSource(int idx) {
		String url = "";
		pageIndex = 1;
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

		fetch.load(this, url);
	}

	public void onDataLoaded(DribbbleFeed feed) {
		hideLoading();
		adapter.update(feed);
		container.dataInvalidated();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.artbook, menu);
		return true;
	}
	
	private Drawable[] layoutIcons;

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
			if(layouts[currLayoutIndex] == grid){
				Log.d(TAG, "Grid ItemWidth: "+grid.getItemWidth());
			}
			
			
			
			item.setIcon(layoutIcons[currLayoutIndex]);

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
		// close clicked
		closeDetails(true);
	}
	
	private void openDetails(){
		
		if(areDetailsShowing){
			return;
		}
		areDetailsShowing = true;
		
		final int w = size.x - detailsView.getWidth();
		
		ViewGroup.LayoutParams p = container.getLayoutParams();
		p.width = w;
		container.setLayoutParams(p);
		container.layoutChanged();
		
		
		detailsView.setVisibility(View.VISIBLE);
		
		detailsView.setTranslationY(2 * containerFrame.getHeight());
		
		detailsView.animate().translationY(0)
		.setDuration(500)
		.setInterpolator(new EaseInOutQuintInterpolator());
		
		
	}

	private void closeDetails(boolean withAnimation) {
		if(!areDetailsShowing){
			return;
		}
		areDetailsShowing = false;
		
		ViewGroup.LayoutParams p = container.getLayoutParams();
		p.width = size.x;
		container.setLayoutParams(p);
		container.layoutChanged();
		
		if(withAnimation){
			detailsView.animate().translationY(2 * containerFrame.getHeight())
			.setDuration(500)
			.setInterpolator(new EaseInOutQuintInterpolator())
			.setListener(new AnimatorListener() {

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
		else{
			detailsView.setVisibility(View.GONE);
			detailsView.setTranslationY(0);
			detailsView.animate().setListener(null);
		}
		
		
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

	boolean areDetailsShowing = false;

	@Override
	public void onItemClick(AbsLayoutContainer parent, FreeFlowItem proxy) {
		Shot s = (Shot) adapter.getSection(0).getDataAtIndex(proxy.itemIndex);
		if (newact) {
			Intent intent = new Intent(this, DetailsActivity.class);
			ArrayList<Shot> shots = new ArrayList<Shot>();
			for (int i = 0; i < adapter.getSection(0).getDataCount(); i++) {
				shots.add((Shot) adapter.getSection(0).getDataAtIndex(i));
			}
			
			/* set the data on the application. Otherwise you might get a failed
			 * Binder transaction cause the data is too much to send across the 
			 * process boundary. 
			 */
			((ArtbookApplication)getApplication()).setShots(shots);

			intent.putExtra("selectedIndex", proxy.itemIndex);
			startActivity(intent);
		}

		else {
			openDetails();
			renderShot(detailsView, s);
		}

	}

	@Override
	public void onBackPressed() {
		if (areDetailsShowing) {
			closeDetails(true);
			return;
		}
		super.onBackPressed();

	}

}

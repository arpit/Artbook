package com.arpitonline.freeflow.artbook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.arpitonline.freeflow.artbook.data.DribbbleDataAdapter;
import com.arpitonline.freeflow.artbook.layouts.DribbbleQuiltLayout;
import com.arpitonline.freeflow.artbook.models.DribbbleFeed;
import com.arpitonline.freeflow.artbook.models.DribbbleFetch;
import com.comcast.freeflow.core.AbsLayoutContainer;
import com.comcast.freeflow.core.AbsLayoutContainer.OnItemClickListener;
import com.comcast.freeflow.core.FreeFlowContainer;
import com.comcast.freeflow.core.FreeFlowContainer.OnScrollListener;
import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.layouts.FreeFlowLayout;
import com.comcast.freeflow.layouts.HLayout;
import com.comcast.freeflow.layouts.VGridLayout;
import com.comcast.freeflow.layouts.VLayout;

public class ArtbookActivity extends Activity implements OnClickListener, android.widget.AdapterView.OnItemClickListener {

	public static final String TAG = "ArtbookActivity";

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artbook);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
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
                R.layout.drawer_list_item, new String[]{"Popular","Everyone","Debuts"}));
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

		// Vertical Layout
		VLayout vlayout = new VLayout();
		VLayout.LayoutParams params2 = new VLayout.LayoutParams(size.x);
		vlayout.setLayoutParams(params2);

		// HLayout
		HLayout hlayout = new HLayout();
		hlayout.setLayoutParams(new HLayout.LayoutParams(size.x));

		layouts = new FreeFlowLayout[] { grid, vlayout, custom };

		adapter = new DribbbleDataAdapter(this);

		container.setLayout(layouts[currLayoutIndex]);
		container.setAdapter(adapter);
		fetch = new DribbbleFetch();
		selectSource(0);

	}
	
	private void selectSource(int idx){
		String url = "";
		adapter.clear();
		switch(idx){
		case 0 : url = DribbbleFetch.getPopularURL(itemsPerPage, pageIndex); break;
		case 1 : url = DribbbleFetch.getEveryoneURL(itemsPerPage, pageIndex); break;
		case 2 : url = DribbbleFetch.getDebutsURL(itemsPerPage, pageIndex); break;
		}
		
		Log.d(TAG, "Loading: "+url);

		fetch.load(this, url);
	}

	public void onDataLoaded(DribbbleFeed feed) {
		Log.d(TAG, "Feed: "+feed.getShots());
		Log.d(TAG, "photo: " + feed.getShots().get(0).getImage_teaser_url());
		adapter.update(feed);
		container.dataInvalidated();
		container.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AbsLayoutContainer parent,
					FreeFlowItem proxy) {

			}
		});

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
	
	public void onDataFailed(){
		Toast.makeText(this, "Error loading Dribbble data", Toast.LENGTH_SHORT).show();
	}
}

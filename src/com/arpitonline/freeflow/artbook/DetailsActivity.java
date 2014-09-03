package com.arpitonline.freeflow.artbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arpitonline.freeflow.artbook.models.Shot;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends DetailsCapableActivity {

	private ViewPager pager;
	private PhotosDetailsListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.shot_details);
		// Shot s = getIntent().getParcelableExtra("shot");
		// renderShot(s);
		// this.setTitle(s.getTitle());

		setContentView(R.layout.photo_details_adaper);
		ArrayList<Shot> shots = ((ArtbookApplication)getApplication()).getShots();
		adapter = new PhotosDetailsListAdapter(shots);
		pager = (ViewPager) findViewById(R.id.pages_holder);
		pager.setAdapter(adapter);
		pager.setCurrentItem(getIntent().getIntExtra("selectedIndex", 0));
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}
	
	
	


	public class PhotosDetailsListAdapter extends PagerAdapter {

		private ArrayList<Shot> shots;

		public PhotosDetailsListAdapter(ArrayList<Shot> shots) {
			this.shots = shots;
		}
		
		public Shot getItem(int index){
			return shots.get(index);
		}

		@Override
		public int getCount() {
			return shots.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ViewGroup) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = container.getContext();

			ViewGroup v = (ViewGroup) LayoutInflater.from(context).inflate(
					R.layout.shot_details, container, false);
			renderShot(v, shots.get(position));
			((ViewPager) container).addView(v, 0);

			return v;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
	}


	@Override
	public Shot getSelectedShot() {
		return adapter.getItem(pager.getCurrentItem());
	}
}

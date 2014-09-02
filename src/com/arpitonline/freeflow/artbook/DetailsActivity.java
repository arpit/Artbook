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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final  Shot s = adapter.getItem(pager.getCurrentItem());
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    case R.id.open_in_browser:
	    	String url = s.getUrl();
	    	Intent i = new Intent(Intent.ACTION_VIEW);
	    	i.setData(Uri.parse(url));
	    	startActivity(i);
	    	return true;
	    	
	    case R.id.share:
	    	new AsyncTask<Void, Integer, Void>() {
	    		
				@Override
				protected Void doInBackground(Void... params) {
					Bitmap b = null;
					try {
						b = Picasso.with(DetailsActivity.this).load(s.getImage_url()).get();
					} catch (IOException e1) {
						e1.printStackTrace();
						return null;
					}
					String url = s.getImage_url();
					String exten = url.substring(url.lastIndexOf(".")+1);
					
					Log.d("share", "extension: "+exten);
					
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			    	sharingIntent.setType("image/"+exten);
			    	sharingIntent.putExtra(Intent.EXTRA_TITLE, s.getTitle());
			    	sharingIntent.putExtra(Intent.EXTRA_SUBJECT, s.getTitle());
			    	String desc = Html.fromHtml(s.getDescription()).toString();
			    	
			    	sharingIntent.putExtra(Intent.EXTRA_TEXT, desc);
			    	String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Artbook";
			    	File dir = new File(file_path);
			    	if(!dir.exists())
			    		dir.mkdirs();
			    	
			    	
			    	
			    	
			    	
			    	File file = new File(dir, "image."+exten);
			    	FileOutputStream fOut;
			    	try {
			    		fOut = new FileOutputStream(file);
			    		b.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			    		fOut.flush();
			    		fOut.close();
			    		
			    		Log.d("share", "share file: "+file.getAbsolutePath());
			    		
			    		sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
				    	startActivity(sharingIntent);
			    		
			    	} catch (Exception e) {
			    		e.printStackTrace();
			    	}
					return null;
				}
	    		
			}.execute();
	    }
	    return super.onOptionsItemSelected(item);
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
}

package com.arpitonline.freeflow.artbook;

import java.util.ArrayList;

import com.arpitonline.freeflow.artbook.models.Shot;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailsActivity extends DetailsCapableActivity {

	private ViewPager pager;
	private PagerAdapter adapter;

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
	}

	public class PhotosDetailsListAdapter extends PagerAdapter {

		private ArrayList<Shot> shots;

		public PhotosDetailsListAdapter(ArrayList<Shot> shots) {
			this.shots = shots;
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

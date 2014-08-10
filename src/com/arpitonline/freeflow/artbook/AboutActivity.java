package com.arpitonline.freeflow.artbook;

import com.crashlytics.android.internal.p;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		final String[] links = new String[]{
			"",
			"https://dribbble.com/about",
			"https://github.com/arpit/Artbook",
			"https://github.com/Comcast/FreeFlow",
			"http://arpitonline.com/blog/",
			"https://twitter.com/arpit",
			"https://plus.google.com/+ArpitMathur/posts"	
		};
		
		final String[] titles = new String[] {
				"Application Version",
				"About Dribbble.com",
				"Artbook Source",
				"Built with FreeFlow",
				"Made by Arpit Mathur",
				"Follow me on Twitter",
				"Connect on Google Plus",
		};
		
		
		PackageInfo pinfo;
		String info;
		try {
			pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
			info = ""+pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			info = "Version info unavailable";
		}
		
		
		final String[] labels = new String[] {
				info,
				"https://dribbble.com/about",
				getResources().getString(R.string.artbook_source_txt),
				getResources().getString(R.string.freeflow_source_txt),
				"Visit my blog",
				getResources().getString(R.string.follow_me_twitter),
				getResources().getString(R.string.follow_me_google_plus) };

		ListView lv = (ListView) findViewById(R.id.about_list);
		lv.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = LayoutInflater.from(parent.getContext())
							.inflate(android.R.layout.simple_list_item_2,
									parent, false);
				}
				ViewGroup v = (ViewGroup) convertView;
				TextView t = (TextView) v.findViewById(android.R.id.text1);
				t.setTextSize(18);
				t.setTypeface(null, Typeface.BOLD);
				t.setText(titles[position]);
				
				TextView t2 = (TextView) v.findViewById(android.R.id.text2);
				t2.setText(labels[position]);
				
				return v;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return labels[position];
			}

			@Override
			public int getCount() {
				return labels.length;
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0) return;
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(links[position]));
				startActivity(browserIntent);
				
				
			}
		});

	}

	private void makeLinkable(int id, final String url) {
		findViewById(id).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
	}

}

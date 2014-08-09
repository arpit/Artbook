package com.arpitonline.freeflow.artbook;

import android.os.Bundle;

import com.arpitonline.freeflow.artbook.models.Shot;

public class DetailsActivity extends DetailsCapableActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shot_details);
		Shot s = getIntent().getParcelableExtra("shot");
		renderShot(s);
		this.setTitle(s.getTitle());
		
	}
}

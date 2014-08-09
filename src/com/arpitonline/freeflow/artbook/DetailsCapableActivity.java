package com.arpitonline.freeflow.artbook;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arpitonline.freeflow.artbook.models.Shot;
import com.squareup.picasso.Picasso;

public class DetailsCapableActivity extends Activity {
	


	protected void renderShot(Shot s){
		ImageView imgView = (ImageView) findViewById(R.id.shot_img);
		
		View tf = findViewById(R.id.shot_title);
		if(tf != null){
			((TextView)tf).setText(s.getTitle());
		}
		Picasso.with(this).load(s.getImage_url()).into(imgView);
		
		TextView desc = ((TextView)findViewById(R.id.shot_desc));
		
		if(s.getDescription() != null){
			desc.setText(Html.fromHtml(s.getDescription()));
			desc.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
		
	}

}

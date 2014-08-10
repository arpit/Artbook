package com.arpitonline.freeflow.artbook;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arpitonline.freeflow.artbook.models.Shot;
import com.squareup.picasso.Picasso;

public class DetailsCapableActivity extends Activity {
	


	protected void renderShot(ViewGroup parent, Shot s){
		Typeface customFont = Typeface.createFromAsset(getAssets(), "RobotoSlab-Regular.ttf");
	  	
		ImageView imgView = (ImageView) parent.findViewById(R.id.shot_img);
		Picasso.with(this).load(s.getImage_url()).into(imgView);
		
		TextView desc = ((TextView)parent.findViewById(R.id.shot_desc));
		
		if(s.getDescription() != null){
			desc.setText(Html.fromHtml(s.getDescription()));
			desc.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
		ImageView profileImg = (ImageView) parent.findViewById(R.id.profile_img);
		Picasso.with(this).load(s.getPlayer().getAvatar_url()).into(profileImg);
		
		TextView a1  = (TextView)parent.findViewById(R.id.avatar_t_1);
		TextView a2  = (TextView)parent.findViewById(R.id.avatar_t_2);
		
		a1.setTypeface(customFont);
		
		a1.setText(s.getPlayer().getUsername());
		a2.setText(s.getPlayer().getLocation());
		
	    
		View v = parent.findViewById(R.id.shot_title_view);
		
		if(v != null){
			((TextView)v).setTypeface(customFont);
			((TextView)v).setText(s.getTitle());
		}
		else{
			this.setTitle(s.getTitle());
		}
		
	}

}

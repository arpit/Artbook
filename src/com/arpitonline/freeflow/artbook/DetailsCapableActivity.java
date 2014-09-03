package com.arpitonline.freeflow.artbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arpitonline.freeflow.artbook.models.Shot;
import com.squareup.picasso.Picasso;

public abstract class DetailsCapableActivity extends Activity {
	


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
	
	abstract public Shot getSelectedShot();
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final  Shot s = getSelectedShot();
		final Context c = this;
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
						b = Picasso.with(c).load(s.getImage_url()).get();
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
			return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}

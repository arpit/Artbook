package com.arpitonline.freeflow.artbook.models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.arpitonline.freeflow.artbook.ArtbookActivity;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class DribbbleFetch {
	
	public static final String TAG = "DribbbleFetch";

	
	public void load(final ArtbookActivity caller, String url) {
		
		new AsyncTask<String, Void, String>() {

			OkHttpClient client = new OkHttpClient();

			private Exception exception;

			protected String doInBackground(String... urls) {
				try {
					return get(new URL(urls[0]));

				} catch (Exception e) {
					this.exception = e;
					Log.e(TAG, "Exception: " + e);
					return null;
				}
			}

			protected void onPostExecute(String data) {
				DribbbleFeed feed  = new Gson().fromJson(data, DribbbleFeed.class);
				if(feed == null){
					caller.onDataFailed();
				}
				caller.onDataLoaded(feed);
			}

			String get(URL url) throws IOException {
				HttpURLConnection connection = client.open(url);
				InputStream in = null;
				try {
					in = connection.getInputStream();
					byte[] response = readFully(in);
					return new String(response, "UTF-8");
				} finally {
					if (in != null)
						in.close();
				}
			}

			byte[] readFully(InputStream in) throws IOException {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				for (int count; (count = in.read(buffer)) != -1;) {
					out.write(buffer, 0, count);
				}
				return out.toByteArray();
			}

		}.execute(url);
	}
	
	public static String getPopularURL(int itemsPerPage, int pageIndex){
		return "http://api.dribbble.com/shots/popular?per_page="+itemsPerPage+"&page="+pageIndex;
	}
	public static String getEveryoneURL(int itemsPerPage, int pageIndex){
		return "http://api.dribbble.com/shots/everyone?per_page="+itemsPerPage+"&page="+pageIndex;
	}
	public static String getDebutsURL(int itemsPerPage, int pageIndex){
		return "http://api.dribbble.com/shots/debuts?per_page="+itemsPerPage+"&page="+pageIndex;
	}

}

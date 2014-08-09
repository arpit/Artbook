package com.arpitonline.freeflow.artbook;

import java.util.ArrayList;

import com.arpitonline.freeflow.artbook.models.Shot;

import android.app.Application;

public class ArtbookApplication extends Application {

	private ArrayList<Shot> shots;
	public void setShots(ArrayList<Shot> shots) {
		this.shots = shots;
	}
	
	public ArrayList<Shot> getShots(){
		return shots;
	}

}

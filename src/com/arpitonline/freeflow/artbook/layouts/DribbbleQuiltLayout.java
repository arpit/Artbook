package com.arpitonline.freeflow.artbook.layouts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.graphics.Rect;
import android.util.Log;

import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.core.Section;
import com.comcast.freeflow.core.SectionedAdapter;
import com.comcast.freeflow.layouts.FreeFlowLayout;
import com.comcast.freeflow.layouts.FreeFlowLayout.FreeFlowLayoutParams;
import com.comcast.freeflow.layouts.FreeFlowLayoutBase;
import com.comcast.freeflow.utils.ViewUtils;

public class DribbbleQuiltLayout extends FreeFlowLayoutBase implements FreeFlowLayout {

	private static final String TAG = "ArtbookLayout";

	private int largeItemWidth;
	private int largeItemHeight;
	private int regularItemWidth;
	private int regularItemHeight;


	@Override
	public void setDimensions(int measuredWidth, int measuredHeight) {
		super.setDimensions(measuredWidth, measuredHeight);
		largeItemWidth = measuredWidth / 2;
		largeItemHeight = (int)(0.75 * largeItemWidth);
		regularItemWidth = measuredWidth / 4;
		regularItemHeight = ((int)(0.75 * regularItemWidth));

	}

	private HashMap<Object, FreeFlowItem> map;
	private Section s;
	
	@Override
	public void prepareLayout(){
		Log.d(TAG, "prepare layout!!!");
		map = new HashMap<Object, FreeFlowItem>();
		s = itemsAdapter.getSection(0);
		int rowIndex;
		Log.d(TAG, "prepare layout for: "+s.getDataCount());
		for (int i = 0; i < s.getDataCount(); i++) {
			rowIndex = i / 5;

			FreeFlowItem p = new FreeFlowItem();
			p.isHeader = false;
			p.itemIndex = i;
			p.itemSection = 0;
			p.data = s.getDataAtIndex(i);

			Rect r = new Rect();

			switch (i % 5) {
			case (0):
				r.left = 0;
				r.top = rowIndex * largeItemHeight;
				r.right = largeItemWidth;
				r.bottom = r.top + largeItemHeight;
				
				if(rowIndex % 2 != 0){
					r.offset(largeItemWidth, 0);
				}
				
				
				break;

			case (1):
				r.left = largeItemWidth;
				r.right = largeItemWidth + regularItemWidth;
				r.top = rowIndex * largeItemHeight;
				r.bottom = r.top + regularItemHeight;
				
				if(rowIndex % 2 != 0){
					r.offset(-largeItemWidth, 0);
				}
				
				break;

			case (2):
				r.left = 3 * regularItemWidth;
				r.right = width;
				r.top = rowIndex * largeItemHeight;
				r.bottom = r.top + regularItemHeight;
				
				if(rowIndex % 2 != 0){
					r.offset(-largeItemWidth, 0);
				}
				
				break;

			case (3):
				r.left = largeItemWidth;
				r.right = largeItemWidth + regularItemWidth;
				r.top = rowIndex * largeItemHeight + regularItemHeight;
				r.bottom = r.top + regularItemHeight;
				if(rowIndex % 2 != 0){
					r.offset(-largeItemWidth, 0);
				}
				break;

			case (4):
				r.left = 3 * regularItemWidth;
				r.right = width;
				r.top = rowIndex * largeItemHeight + regularItemHeight;
				r.bottom = r.top + regularItemHeight;
				if(rowIndex % 2 != 0){
					r.offset(-largeItemWidth, 0);
				}
				break;

			default:
				break;
			}
			p.frame = r;
			map.put(s.getDataAtIndex(i), p);
		}
	}

	@Override
	public HashMap<Object, FreeFlowItem> getItemProxies(
			int viewPortLeft, int viewPortTop) {

		Rect viewport = new Rect(viewPortLeft, 
								viewPortTop, 
								viewPortLeft + width, 
								viewPortTop + height);
		
		//Log.d(TAG, "Viewport: "+viewPortLeft+", "+viewPortTop+", "+viewport.width()+","+viewport.height());
		HashMap<Object, FreeFlowItem> ret = new HashMap<Object, FreeFlowItem>();

		Iterator<Entry<Object, FreeFlowItem>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, FreeFlowItem> pairs = it.next();
			FreeFlowItem p = (FreeFlowItem) pairs.getValue();
			if ( Rect.intersects(p.frame, viewport) ) {
				ret.put(pairs.getKey(), p);
			}
		}
		return ret;
		
	}

	@Override
	public FreeFlowItem getFreeFlowItemForItem(Object item) {
		Log.d(TAG, " returing item: " + map.get(item));
		return map.get(item);
	}

	@Override
	public int getContentWidth() {
		return 0;
	}

	@Override
	public int getContentHeight() {
		return s.getDataCount() / 5 * largeItemHeight;
	}

	@Override
	public FreeFlowItem getItemAt(float x, float y) {
		return (FreeFlowItem) ViewUtils.getItemAt(map, (int) x, (int) y);
	}

	@Override
	public void setLayoutParams(FreeFlowLayoutParams params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean verticalScrollEnabled() {
		return true;
	}
	
	@Override
	public boolean horizontalScrollEnabled(){
		return false;
	}
}

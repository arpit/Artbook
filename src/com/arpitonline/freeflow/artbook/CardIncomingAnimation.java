package com.arpitonline.freeflow.artbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.MotionEvent;
import android.view.View;

import com.comcast.freeflow.animations.DefaultLayoutAnimator;
import com.comcast.freeflow.animations.interpolators.EaseInOutQuintInterpolator;
import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.utils.MathUtils;

public class CardIncomingAnimation extends DefaultLayoutAnimator{

	public HashMap<View, PropertyValuesHolder> yAnims;
	
	@Override
	protected AnimatorSet getItemsAddedAnimation(List<FreeFlowItem> added) {

		appearingSet = new AnimatorSet();
		yAnims = new HashMap<View, PropertyValuesHolder>();

		appearingSet.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		ArrayList<Animator> addedAnims = new ArrayList<Animator>();
		for (FreeFlowItem proxy : added) {
			proxy.view.setRotation(45f);

			float y = proxy.view.getY();
			proxy.view.setY(y + 2400f);

			PropertyValuesHolder a1 = PropertyValuesHolder.ofFloat(
					View.ROTATION, 0);
			PropertyValuesHolder a2 = PropertyValuesHolder.ofFloat(View.Y, y);
			
			yAnims.put(proxy.view, a2);
			
			
			ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(
					proxy.view, a1, a2);
			anim.setDuration(MathUtils.randRange(800, 2000));
			anim.setInterpolator(new EaseInOutQuintInterpolator());
			addedAnims.add(anim);
		}

		appearingSet.playTogether(addedAnims);

		return appearingSet;
	}
	
	@Override
	public void cancel() {
		if(appearingSet != null && appearingSet.isRunning()){
			appearingSet.cancel();
			for( Animator s : appearingSet.getChildAnimations()){
				ObjectAnimator a = (ObjectAnimator)s;
				a.cancel();
				View v = (View)a.getTarget();
				
				v.setTranslationY(0);
				v.setTranslationX(0);
				v.setRotation(0);
			}
		}
	}
	
	@Override
	public void onContainerTouchDown(MotionEvent event) {
		cancel();
	}

}

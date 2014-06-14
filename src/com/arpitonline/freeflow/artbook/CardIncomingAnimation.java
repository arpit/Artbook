package com.arpitonline.freeflow.artbook;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

import com.comcast.freeflow.animations.DefaultLayoutAnimator;
import com.comcast.freeflow.animations.interpolators.EaseInOutQuintInterpolator;
import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.utils.MathUtils;

public class CardIncomingAnimation extends DefaultLayoutAnimator{

	@Override
	protected AnimatorSet getItemsAddedAnimation(List<FreeFlowItem> added) {

		appearingSet = new AnimatorSet();

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
			ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(
					proxy.view, a1, a2);

			anim.setDuration(MathUtils.randRange(800, 2000));
			anim.setInterpolator(new EaseInOutQuintInterpolator());
			addedAnims.add(anim);
		}

		appearingSet.playTogether(addedAnims);

		return appearingSet;
	}

}

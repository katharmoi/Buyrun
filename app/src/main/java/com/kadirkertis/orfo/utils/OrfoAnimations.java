package com.kadirkertis.orfo.utils;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;

import com.kadirkertis.orfo.R;

/**
 * Created by Kadir Kertis on 3.4.2017.
 */

public class OrfoAnimations {

    public static Animation fromAtoB(float fromX, float fromY, float toX, float toY, Animation.AnimationListener l, int speed) {


        Animation fromAtoB = new TranslateAnimation(
                Animation.ABSOLUTE, //from xType
                fromX,
                Animation.ABSOLUTE, //to xType
                toX,
                Animation.ABSOLUTE, //from yType
                fromY,
                Animation.ABSOLUTE, //to yType
                toY
        );

        fromAtoB.setDuration(speed);
        fromAtoB.setInterpolator(new AnticipateOvershootInterpolator(1.0f));


        if (l != null)
            fromAtoB.setAnimationListener(l);
        return fromAtoB;
    }

    public static void revealEffectShow(final Context context, final View view) {
        int cx = view.getWidth();
        int cy = view.getHeight();
        int finalRadius = view.getWidth();

        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setDuration(context.getResources().getInteger(R.integer.animation_duration));
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

}

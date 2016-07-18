package com.yn.framework.animation;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;


/**
 * Created by youjiannuo on 15/7/21.
 */
public class Animation {


    public static void alpha(View view, int time) {
        ObjectAnimator.ofFloat(view, "alpha", 1, 0)
                .setDuration(time).start();
    }

    public static void alpha(View view, int time, ValueAnimator.AnimatorListener l) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0)
                .setDuration(time);
        animator.addListener(l);
        animator.start();
    }

    public static void alpha0To1(View view, int time, ValueAnimator.AnimatorListener l) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0, 1)
                .setDuration(time);
        animator.addListener(l);
        animator.start();
    }

    public static void alpha(View view, float from, float to, int time) {
        ObjectAnimator.ofFloat(view, "alpha", from, to)
                .setDuration(time).start();
    }

    public static void rotateNow(View v, float rotate) {
        rotate(v, 0, rotate, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void rotate(View v, float startRote, float endRote, long time) {
        ObjectAnimator//
                .ofFloat(v, "rotation", startRote, endRote)//
                .setDuration(time)//
                .start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void translationY(View v, float y, int time) {
        ObjectAnimator
                .ofFloat(v, "TranslationY", y)
                .setDuration(time)//
                .start();
    }

    public static void translationY(View v, float fromY, float toY, int time, Animator.AnimatorListener l) {
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofFloat(v, "TranslationY", fromY, toY)
                .setDuration(time);
        if (l != null) {
            objectAnimator.addListener(l);
        }
        objectAnimator.start();
    }

    public static ValueAnimator valueAnimator(float from, float to, int time, ValueAnimator.AnimatorUpdateListener l) {
        return valueAnimator(from, to, time, l, null);
    }

    public static ValueAnimator valueAnimator(float from, float to, int time, ValueAnimator.AnimatorUpdateListener l, Animator.AnimatorListener listener) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(l);
        valueAnimator.start();
        if (listener != null) {
            valueAnimator.addListener(listener);
        }
        return valueAnimator;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void translationX(View v, float x, int time, Animator.AnimatorListener l) {
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(v, "TranslationX", x);
        animator.setDuration(time);
        if (l != null)
            animator.addListener(l);
        animator.start();
    }

    public static void translationX(View v, float fromX, float toX, int time) {
        ObjectAnimator
                .ofFloat(v, "TranslationX", fromX, toX)
                .setDuration(time)//
                .start();

    }


    public static TranslateAnimation Translate(View v, int fromX, int toX, int fromY, int toY, int time, android.view.animation.Animation.AnimationListener l) {
        if (v == null) return null;
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        translateAnimation.setDuration(time);
        if (l != null)
            translateAnimation.setAnimationListener(l);
        translateAnimation.setFillAfter(true);
        v.startAnimation(translateAnimation);
        return translateAnimation;
    }

    public static android.view.animation.Animation startAnimation(View v, int id) {
        return startAnimation(v, id, null);
    }

    public static android.view.animation.Animation startAnimation(View v, int id, android.view.animation.Animation.AnimationListener l) {
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(v.getContext(), id);
        if (l != null)
            animation.setAnimationListener(l);
        v.startAnimation(animation);
        return animation;
    }

    public static class AnimationObjectListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    public static class AnimationListener implements android.view.animation.Animation.AnimationListener {

        @Override
        public void onAnimationStart(android.view.animation.Animation animation) {

        }

        @Override
        public void onAnimationEnd(android.view.animation.Animation animation) {

        }

        @Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {

        }
    }


}

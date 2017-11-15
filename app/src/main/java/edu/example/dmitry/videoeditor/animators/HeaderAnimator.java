package edu.example.dmitry.videoeditor.animators;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by dmitry on 09.11.17.
 */

public class HeaderAnimator {
    private static HeaderAnimator instance;
    private HeaderAnimator() {}
    private AnimatorSet animatorSet;

    public static HeaderAnimator getInstance() {
        if (instance == null ){
            instance = new HeaderAnimator();
        }
        return instance;
    }
    public void startMove(View view) {
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }
        int viewHeight = view.getHeight();
        ObjectAnimator sideAnim;
        sideAnim = ObjectAnimator.ofFloat(view
                , "y"
                ,  - viewHeight
                , - viewHeight  * 0.5f);
        sideAnim.setDuration(150);
        animatorSet = new AnimatorSet();
        animatorSet.play(sideAnim);
        animatorSet.start();
    }

    public void move(View view) {
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }
        int viewHeight = view.getHeight();
        ObjectAnimator sideAnim;
        sideAnim = ObjectAnimator.ofFloat(view
                , "y"
                , -viewHeight
                , -viewHeight  * 0.5f);
        sideAnim.setDuration(150);
        animatorSet = new AnimatorSet();
        animatorSet.play(sideAnim);
        animatorSet.start();
    }

}

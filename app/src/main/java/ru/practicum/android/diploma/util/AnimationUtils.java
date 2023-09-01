package ru.practicum.android.diploma.util;

import android.animation.Animator;
import androidx.annotation.NonNull;

public class AnimationUtils {

    public static abstract class AnimationEndListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(@NonNull Animator animation) {
            // Do nothing
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animation) {
            // Do nothing
        }

        @Override
        public void onAnimationRepeat(@NonNull Animator animation) {
            // Do nothing
        }
    }
}

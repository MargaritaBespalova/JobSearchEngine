package ru.practicum.android.diploma.util

import android.animation.Animator

class AnimationUtils {

    abstract class AnimationEndListener : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            // Do nothing
        }

        override fun onAnimationCancel(animation: Animator) {
            // Do nothing
        }

        override fun onAnimationRepeat(animation: Animator) {
            // Do nothing
        }
    }
}

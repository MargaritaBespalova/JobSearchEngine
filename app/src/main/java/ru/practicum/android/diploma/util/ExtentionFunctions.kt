package ru.practicum.android.diploma.util

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY_400_MILLIS = 400L
const val DELAY_2000_MILLIS = 2000L

val <T> T.thisName: String
    get() = this!!::class.simpleName ?: "Unknown class"

fun <T> delayedAction(
    delayMillis: Long = DELAY_2000_MILLIS,
    coroutineScope: CoroutineScope,
    deferredUsing: Boolean = true,
    action: (T) -> Unit,
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (deferredUsing) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || deferredUsing) {
            debounceJob = coroutineScope.launch {
                Log.d("delayedAction", "delayedAction: job = $debounceJob   query = $param", )
                delay(delayMillis)
                action(param)
            }
        }
    }
}

fun ImageView.setImage(url: String, placeholder: Int, cornerRadius: Int) {
    Glide
        .with(this.context)
        .load(url)
        .placeholder(placeholder)
        .transform(CenterInside(), RoundedCorners(cornerRadius))
        .into(this)
}
package ru.practicum.android.diploma.app

import android.app.Application
import ru.practicum.android.diploma.Logger
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.LoggerImpl
import ru.practicum.android.diploma.di.DaggerAppComponent
import ru.practicum.android.diploma.util.thisName
import javax.inject.Inject

class App: Application() {
    @Inject
    lateinit var logger: Logger
    val component by lazy(LazyThreadSafetyMode.NONE) {
        DaggerAppComponent.factory()
            .create(
                context = this,
                baseUrl = resources.getString(R.string.base_url)
            )
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
        logger.log(thisName, "onCreate() $logger")
    }
}
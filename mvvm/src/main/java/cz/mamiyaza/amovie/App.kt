package cz.mamiyaza.amovie

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class of the MVP.
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
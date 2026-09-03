package de.vegnpunk.mentalmatics

import android.app.Application
import de.vegnpunk.mentalmatics.ui.di.initKoin
import org.koin.android.ext.koin.androidContext

class MentalmaticsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MentalmaticsApplication)
        }
    }
}

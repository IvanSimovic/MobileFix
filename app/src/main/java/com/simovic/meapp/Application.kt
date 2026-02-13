package com.simovic.meapp

import android.app.Application
import com.simovic.meapp.app.BuildConfig
import com.simovic.meapp.birthday.featureBirthDayModules
import com.simovic.meapp.feature.album.featureAlbumModules
import com.simovic.meapp.livefeed.featureLiveFeedModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import timber.log.Timber

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@Application)

            modules(appModule)
            modules(featureAlbumModules)
            modules(featureLiveFeedModules)
            modules(featureBirthDayModules)
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

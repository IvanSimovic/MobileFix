package com.simovic.mobilefix

import android.app.Application
import com.simovic.mobilefix.app.BuildConfig
import com.simovic.mobilefix.feature.album.featureAlbumModules
import com.simovic.mobilefix.feature.favourite.featureFavouriteModules
import com.simovic.mobilefix.feature.settings.featureSettingsModules
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
            modules(featureFavouriteModules)
            modules(featureAlbumModules)
            modules(featureSettingsModules)
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

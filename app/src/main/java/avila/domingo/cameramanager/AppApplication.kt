package avila.domingo.cameramanager

import android.app.Application
import avila.domingo.cameramanager.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            androidLogger()
            modules(
                appModule,
                activityModule,
                viewModelModule,
                useCaseModule,
                cameraModule,
//                flashModule,
                scheduleModule,
                mapperModule
            )
        }
    }
}
package br.com.fernando.servicestudy

import android.app.Application
import android.util.Log
import br.com.fernando.servicestudy.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ServiceStudyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Log.d("ServiceStudyApplication", "onCreate")

        startKoin {
            androidContext(this@ServiceStudyApplication)
            androidLogger()
            modules(appModule)
        }
    }
}
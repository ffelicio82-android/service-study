package br.com.fernando.servicestudy.di

import android.app.NotificationManager
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<NotificationManager> {
        androidContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}
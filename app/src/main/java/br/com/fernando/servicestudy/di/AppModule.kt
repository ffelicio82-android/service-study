package br.com.fernando.servicestudy.di

import android.app.NotificationManager
import android.content.Context
import br.com.fernando.servicestudy.utils.GsonHelper
import br.com.fernando.servicestudy.utils.SystemUserCheck
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<NotificationManager> {
        androidContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    single<SystemUserCheck> { SystemUserCheck(context = androidContext()) }
    single<GsonHelper> { GsonHelper }
}
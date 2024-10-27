package br.com.fernando.servicestudy.di

import br.com.fernando.servicestudy.workers.schedulers.QueueScheduler
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val workerModule = module {
    single<QueueScheduler> { QueueScheduler(context = androidApplication()) }
}
package br.com.fernando.servicestudy.workers.schedulers

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.com.fernando.servicestudy.workers.CheckUpdateWorker
import java.util.concurrent.TimeUnit


object CheckUpdateScheduler {
    private const val TAG : String = "check_update_scheduler_Fernando"

    fun scheduleImmediateCheckUpdate(context : Context) {
        val checkUpdateWorker = OneTimeWorkRequestBuilder<CheckUpdateWorker>()
            .build()
        WorkManager.getInstance(context).enqueue(checkUpdateWorker)

        Log.d(TAG, "CheckUpdateScheduler: scheduleImmediateCheckUpdate")
    }

    fun schedulePeriodicCheckUpdate(context : Context) {
        val checkUpdateWorker = PeriodicWorkRequestBuilder<CheckUpdateWorker>(15, TimeUnit.MINUTES)
            .setInitialDelay(5, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TAG,
            ExistingPeriodicWorkPolicy.UPDATE,
            checkUpdateWorker
        )

        Log.d(TAG, "CheckUpdateScheduler: schedulePeriodicCheckUpdate")
    }
}
package br.com.fernando.servicestudy.workers.schedulers

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import br.com.fernando.servicestudy.workers.DownloadWorker
import br.com.fernando.servicestudy.workers.FetchInstalledAppsWorker
import br.com.fernando.servicestudy.workers.FetchManufacturerWorker
import br.com.fernando.servicestudy.workers.SaveDataWorker
import br.com.fernando.servicestudy.workers.UpdateAppWorker

class QueueScheduler(private val context : Context) {
    fun schedule() {
        WorkManager.getInstance(context)
            .beginUniqueWork(
                "FETCH_INSTALLED_APPS_AND_MANUFACTURER",
                ExistingWorkPolicy.REPLACE,
                listOf(
                    OneTimeWorkRequest.from(FetchInstalledAppsWorker::class.java),
                    OneTimeWorkRequest.from(FetchManufacturerWorker::class.java)
                )
            )
            .then(OneTimeWorkRequest.from(DownloadWorker::class.java))
            .then(OneTimeWorkRequest.from(SaveDataWorker::class.java))
            .then(OneTimeWorkRequest.from(UpdateAppWorker::class.java))
            .enqueue()

        Log.d(TAG, "QueueScheduler: schedule")
    }

    companion object {
        private const val TAG : String = "queue_scheduler"
    }
}
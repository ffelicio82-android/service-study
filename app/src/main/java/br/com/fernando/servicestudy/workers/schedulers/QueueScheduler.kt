package br.com.fernando.servicestudy.workers.schedulers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.fernando.servicestudy.workers.DownloadWorker
import br.com.fernando.servicestudy.workers.FetchInstalledAppsWorker
import br.com.fernando.servicestudy.workers.SaveDataWorker
import br.com.fernando.servicestudy.workers.UpdateAppWorker

object QueueScheduler {
    private const val TAG : String = "queue_scheduler_Fernando"

    fun schedule(context : Context, data : Data) {
        val fetchInstalledAppsWorker : OneTimeWorkRequest = OneTimeWorkRequestBuilder<FetchInstalledAppsWorker>()
            .setInputData(data)
            .build()

        val downloadWorkerRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(data)
            .build()

        val saveDataWorkerRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<SaveDataWorker>()
            .setInputData(data)
            .build()

        val updateAppWorkerRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<UpdateAppWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(context)
            .beginWith(downloadWorkerRequest)
            .then(saveDataWorkerRequest)
            .then(updateAppWorkerRequest)
            .enqueue()

        Log.d(TAG, "QueueScheduler: schedule")
    }
}
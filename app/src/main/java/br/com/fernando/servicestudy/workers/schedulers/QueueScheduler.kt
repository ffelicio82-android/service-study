package br.com.fernando.servicestudy.workers.schedulers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.fernando.servicestudy.workers.DownloadWorker
import br.com.fernando.servicestudy.workers.SaveDataWorker
import br.com.fernando.servicestudy.workers.UpdateAppWorker

object QueueScheduler {
    private const val TAG : String = "queue_scheduler_Fernando"

    fun schedule(context : Context, data : Data) {
        val downloadWorkerRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(data)
            .build()

        val saveDataWorkerRequest = OneTimeWorkRequestBuilder<SaveDataWorker>()
            .setInputData(data)
            .build()

        val updateAppWorkerRequest = OneTimeWorkRequestBuilder<UpdateAppWorker>()
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
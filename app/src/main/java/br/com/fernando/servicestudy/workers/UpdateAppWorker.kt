package br.com.fernando.servicestudy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class UpdateAppWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val inputData: Data = inputData
                val data: String? = inputData.getString("url-info")
                Log.d(
                    TAG,
                    "doWork: UpdateAppWorker ${Date(System.currentTimeMillis())} | Data from worker: $data"
                )
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "UpdateAppWorkerFernando"
    }
}
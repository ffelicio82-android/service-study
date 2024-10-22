package br.com.fernando.servicestudy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import br.com.fernando.domain.GetUrlsFromCloudFrontUseCase
import br.com.fernando.domain.entities.MyData
import br.com.fernando.servicestudy.workers.schedulers.QueueScheduler
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date

class CheckUpdateWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {
    private val getUrlsFromCloudFrontUseCase: GetUrlsFromCloudFrontUseCase by inject()

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "doWork: CheckUpdateWorker ${Date(System.currentTimeMillis())}")

                val list : List<MyData> = getUrlsFromCloudFrontUseCase.execute()
                val data : String = Gson().toJson(list)
                val workerData : Data = Data.Builder().putString("url-info", data).build()

                // schedule other workers
                QueueScheduler.schedule(context = applicationContext, data = workerData)

                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "CheckUpdateWorkerFernando"
    }
}
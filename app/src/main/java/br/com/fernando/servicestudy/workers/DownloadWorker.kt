package br.com.fernando.servicestudy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import br.com.fernando.domain.entities.AppInfo
import br.com.fernando.domain.entities.DeviceData
import br.com.fernando.servicestudy.utils.GsonHelper
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date

class DownloadWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {
    private val gsonHelper : GsonHelper by inject()
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val inputData: Data = inputData
                val installedApps : String? = inputData.getString("installed-apps")
                val deviceData : String? = inputData.getString("device-data")

                val type = object : TypeToken<List<AppInfo>>() {}.type
                Log.d(TAG, "doWork: DownloadWorker ${Date(System.currentTimeMillis())} | Data from worker (apps-data): ${gsonHelper.fromJsonList<AppInfo>(installedApps, type)}")
                Log.d(TAG, "doWork: DownloadWorker ${Date(System.currentTimeMillis())} | Data from worker (device-data): ${gsonHelper.fromJson(deviceData, DeviceData::class.java)}")

                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "DownloadWorkerFernando"
    }
}
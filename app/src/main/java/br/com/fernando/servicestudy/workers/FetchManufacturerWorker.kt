package br.com.fernando.servicestudy.workers

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.fernando.domain.entities.DeviceData
import br.com.fernando.servicestudy.utils.GsonHelper
import com.google.gson.Gson
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date

class FetchManufacturerWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams), KoinComponent {
    private val gsonHelper : GsonHelper by inject()

    override fun doWork(): Result {

        val deviceData = DeviceData(
            imei = "1234567890",
            carrier = "Vivo",
            latitude = 23.456789,
            longitude = 45.678901,
            apps = listOf()
        )

        Log.d(TAG, "doWork: FetchManufacturerWorker ${Date(System.currentTimeMillis())} | Data to next worker: $deviceData")

        val data : String = gsonHelper.toJson(deviceData) ?: ""
        val workerData : Data = Data.Builder().putString("device-data", data).build()
        return Result.success(workerData)
    }

    companion object {
        private const val TAG = "FetchManufacturerWorker"
    }
}
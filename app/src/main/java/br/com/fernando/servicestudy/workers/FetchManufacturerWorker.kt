package br.com.fernando.servicestudy.workers

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.fernando.domain.entities.DeviceData
import br.com.fernando.servicestudy.utils.GsonHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date

class FetchManufacturerWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams), KoinComponent {
    private val gsonHelper : GsonHelper by inject()
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var locationManager: LocationManager

    override fun doWork(): Result {
        return try {
            telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val location : Location? = getLocation()

            val deviceData = DeviceData(
                imei = getImei(),
                carrier = getCarrier(),
                latitude = location?.latitude,
                longitude = location?.longitude
            )

            Log.d(TAG, "doWork: FetchManufacturerWorker ${Date(System.currentTimeMillis())} | Data to next worker: $deviceData")

            val data : String = gsonHelper.toJson(deviceData) ?: ""
            val workerData : Data = Data.Builder().putString("device-data", data).build()

            Result.success(workerData)
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun getImei(): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                telephonyManager.imei ?: "Unknown"
            } else {
                telephonyManager.deviceId ?: "Unknown"
            }
        } catch (e: SecurityException) {
            "Permission Denied"
        }
    }

    private fun getCarrier(): String {
        return telephonyManager.networkOperatorName ?: "Unknown"
    }

    private fun getLocation(): Location? {
        return try {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } catch (e: SecurityException) {
            null
        }
    }

    companion object {
        private const val TAG = "FetchManufacturerWorker"
    }
}
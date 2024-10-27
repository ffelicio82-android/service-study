package br.com.fernando.servicestudy.workers

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.fernando.domain.entities.AppInfo
import br.com.fernando.servicestudy.utils.GsonHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date

class FetchInstalledAppsWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams), KoinComponent {
    private val gsonHelper : GsonHelper by inject()
    private lateinit var packageManager: PackageManager

    override fun doWork(): Result {
        packageManager = context.packageManager

        val installedApps : List<AppInfo> = packageManager
            .getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { appInfo -> !isSystemOrVendorApp(appInfo.packageName) }
            .map { applicationInfo -> applicationInfo.toAppInfo() }

        Log.d(TAG, "doWork: FetchInstalledAppsWorker ${Date(System.currentTimeMillis())} | Data to next worker: $installedApps")

        val data : String = gsonHelper.toJson(installedApps) ?: ""
        val workerData : Data = Data.Builder().putString("installed-apps", data).build()
        return Result.success(workerData)
    }

    private fun itIsSystemApp(appInfo: ApplicationInfo): Boolean {
        return (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
    }

    private fun isSystemOrVendorApp(packageName : String) : Boolean {
        return packageName.trim().lowercase().contains("android") ||
               packageName.trim().lowercase().contains("mediatek")
    }

    private fun fetchPackageInfo(packageName : String) : PackageInfo? {
        return try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun fetchVersionCode(packageName: String): Int {
        val packageInfo: PackageInfo = fetchPackageInfo(packageName = packageName) ?: return 0

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toInt()
        } else {
            packageInfo.versionCode
        }
    }

    private fun ApplicationInfo.toAppInfo() : AppInfo {
        return AppInfo(
            name = packageManager.getApplicationLabel(this).toString(),
            packageName = this.packageName,
            versionName = fetchPackageInfo(this.packageName)?.versionName ?: "",
            versionCode = fetchVersionCode(this.packageName),
            isSystemApp = itIsSystemApp(this)
        )
    }

    companion object {
        private const val TAG = "FetchInstalledAppsWorker"
    }
}
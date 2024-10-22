package br.com.fernando.servicestudy.workers

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class FetchInstalledAppsWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private lateinit var packageManager: PackageManager

    override fun doWork(): Result {
        packageManager = context.packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val appInfoList = mutableListOf<String>()

        for (appInfo in installedApps) {
            // Verificação para saber se é um aplicativo do sistema
//            test

            val itIsSystemApp : String = if(itIsSystemApp(appInfo)) "App de sistema" else "Não é app de sistema"

            // Nome do pacote
            val packageName = appInfo.packageName

            // Pega as informações do pacote incluindo a versão
            val packageInfo: PackageInfo = fetchPackageInfo(packageName = packageName) ?: continue

            // Nome do aplicativo (nome de exibição)
            val appName = packageManager.getApplicationLabel(appInfo).toString()

            // Versão do aplicativo (exibida para o usuário)
            val versionName = packageInfo.versionName

            // Código da versão (geralmente um número incrementado)
            val versionCode = fetchVersionCode(packageInfo = packageInfo)

            appInfoList.add("$appName ($packageName) - Versão: $versionName, Código: $versionCode")
            Log.d("FetchInstalledApps", "($itIsSystemApp) : $appName ($packageName) - Versão: $versionName, Código: $versionCode, appInfoFlags: ${appInfo.flags} (${ApplicationInfo.FLAG_SYSTEM})")

        }

        return Result.success()
    }

    private fun itIsSystemApp(appInfo: ApplicationInfo): Boolean {
        return (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
    }

    private fun fetchPackageInfo(packageName : String) : PackageInfo? {
        return try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun fetchVersionCode(packageInfo: PackageInfo): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toInt()
        } else {
            packageInfo.versionCode
        }
    }
}
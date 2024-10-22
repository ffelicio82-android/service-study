package br.com.fernando.servicestudy.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.com.fernando.servicestudy.workers.CheckUpdateWorker
import br.com.fernando.servicestudy.workers.FetchInstalledAppsWorker
import br.com.fernando.servicestudy.workers.schedulers.CheckUpdateScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class UpdateService : Service() {
    private val notificationManager: NotificationManager by inject()
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // To inform O.S. that the service is running
        sendNotification()

        serviceScope.launch {
            while (isActive) {
                Log.d(TAG, "serviceScope-fernando: Service is running")

                val fetchInstalledAppsWorker = OneTimeWorkRequestBuilder<FetchInstalledAppsWorker>()
                    .build()

                WorkManager.getInstance(applicationContext).enqueue(fetchInstalledAppsWorker)

                delay(CHECK_INTERVAL)
            }
        }

        Log.d(TAG, "onStartCommand: ${intent?.`package`.toString()} flags: $flags startId: $startId")
        return START_STICKY
    }

    override fun onBind(intent : Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Destroyed Service")
    }

    private fun sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notification",
                NotificationManager.IMPORTANCE_NONE
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Criar a notificação
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID).build()
        startForeground(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TAG = "UpdateService-fernando"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "my_channel_id"
        private const val CHECK_INTERVAL = 1 * 60 * 1000L
    }
}
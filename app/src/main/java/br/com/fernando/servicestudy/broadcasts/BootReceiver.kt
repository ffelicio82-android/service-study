package br.com.fernando.servicestudy.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import br.com.fernando.servicestudy.services.UpdateService
import br.com.fernando.servicestudy.workers.schedulers.CheckUpdateScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context : Context?, intent : Intent?) {
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                val updateServiceIntent = Intent(context, UpdateService::class.java)
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                        context?.startForegroundService(updateServiceIntent)
                    }
                    else -> context?.startService(updateServiceIntent)
                }

//                context?.let {
//                    CheckUpdateScheduler.scheduleImmediateCheckUpdate(it)
//                    CheckUpdateScheduler.schedulePeriodicCheckUpdate(it)
//                }

                Log.d(TAG, "BootReceiver: onReceive")
            }
        }
    }

    companion object {
        private const val TAG = "BootReceiver-fernando"
    }
}
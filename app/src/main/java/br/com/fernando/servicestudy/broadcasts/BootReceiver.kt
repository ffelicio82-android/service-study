package br.com.fernando.servicestudy.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import br.com.fernando.servicestudy.services.UpdateService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context : Context, intent : Intent) {
        when (intent.action) {
            // operation to be performed when the device is rebooted
            Intent.ACTION_BOOT_COMPLETED -> {
                val serviceIntent = Intent(context, UpdateService::class.java)
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                    return
                }
                context.startService(serviceIntent)

                Log.d(TAG, "Serviço destruído")
            }
        }
    }

    companion object {
        private const val TAG = "BootReceiver-fernando"
    }
}
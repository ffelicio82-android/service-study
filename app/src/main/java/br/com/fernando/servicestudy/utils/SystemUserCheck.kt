package br.com.fernando.servicestudy.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process
import android.util.Log

class SystemUserCheck(private val context: Context) {
    companion object {
        private const val SYSTEM_ID : Int = 1000
    }

    fun isSystemUser() : Boolean {
        Log.d("SystemUserCheck", "isSystemUser: ${Process.myUid() == SYSTEM_ID}")
        Log.d("SystemUserCheck", "isSystemUser: ${Process.myUid()}")
        return Process.myUid() == SYSTEM_ID
    }

    fun requireSystemPrivileges() {
        if (!isSystemUser()) {
            throw SecurityException("Esta operação requer privilégios de sistema (UID 1000)")
        }
    }

    fun hasSystemPrivileges(): Boolean {
        // Verifica se tem permissões de sistema
        val hasInstallPermission = permissionIsGranted(Manifest.permission.INSTALL_PACKAGES)
        Log.d("SystemUserCheck", "hasInstallPermission: $hasInstallPermission")

        val hasUninstallPermission = permissionIsGranted(Manifest.permission.DELETE_PACKAGES)
        Log.d("SystemUserCheck", "hasUninstallPermission: $hasUninstallPermission")

        val hasLocationPermission = permissionIsGranted(Manifest.permission.ACCESS_FINE_LOCATION)
        Log.d("hasLocationPermission", "hasInstallPermission: $hasLocationPermission")

        return hasInstallPermission && hasUninstallPermission && hasLocationPermission
    }

    private fun permissionIsGranted(permission : String) : Boolean {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
}
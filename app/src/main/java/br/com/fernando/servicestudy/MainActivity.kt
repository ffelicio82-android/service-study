package br.com.fernando.servicestudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.fernando.servicestudy.utils.SystemUserCheck
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val systemUserCheck : SystemUserCheck by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        systemUserCheck.isSystemUser()
//        systemUserCheck.requireSystemPrivileges()
        systemUserCheck.hasSystemPrivileges()
    }
}
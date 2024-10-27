package br.com.fernando.domain.entities

data class AppInfo(
    val name: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Int,
    var isSystemApp: Boolean = false
)

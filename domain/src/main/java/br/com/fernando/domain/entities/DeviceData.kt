package br.com.fernando.domain.entities

data class DeviceData(
    val imei: String,
    val carrier: String,
    var latitude: Double?,
    var longitude: Double?,
    var apps : List<AppInfo>?,
)

package br.com.fernando.domain.entities

data class DeviceData(
    val imei: String,
    val carrier: String,
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0,
    var apps : List<AppInfo> = listOf<AppInfo>(),
)

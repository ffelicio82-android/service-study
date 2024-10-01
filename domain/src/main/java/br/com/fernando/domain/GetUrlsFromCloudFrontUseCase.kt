package br.com.fernando.domain

import br.com.fernando.domain.entities.MyData

class GetUrlsFromCloudFrontUseCase {
    suspend fun execute() : List<MyData> {
        return listOf(
            MyData(1, "https://apk-link-1"),
            MyData(2, "https://apk-link-2"),
            MyData(3, "https://apk-link-3"),
            MyData(4, "https://apk-link-4"),
        )
    }
}
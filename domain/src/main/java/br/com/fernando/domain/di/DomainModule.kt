package br.com.fernando.domain.di

import br.com.fernando.domain.GetUrlsFromCloudFrontUseCase
import org.koin.dsl.module

val domainModule = module {
    single<GetUrlsFromCloudFrontUseCase> { GetUrlsFromCloudFrontUseCase() }
}
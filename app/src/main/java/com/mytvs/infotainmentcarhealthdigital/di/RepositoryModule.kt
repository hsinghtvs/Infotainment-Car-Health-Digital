package com.mytvs.infotainmentcarhealthdigital.di

import com.mytvs.infotainmentcarhealthdigital.api.VehicleDetailApiResponse
import com.mytvs.infotainmentcarhealthdigital.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideLoginRepository(
        vehicleDetailApiResponse: VehicleDetailApiResponse
    ): MainRepository =
        MainRepository(
            vehicleDetailApiResponse = vehicleDetailApiResponse
        )
}


package com.example.infotainment_car_health_digital.di

import com.example.infotainment_car_health_digital.api.VehicleDetailApiResponse
import com.example.infotainment_car_health_digital.repository.MainRepository
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


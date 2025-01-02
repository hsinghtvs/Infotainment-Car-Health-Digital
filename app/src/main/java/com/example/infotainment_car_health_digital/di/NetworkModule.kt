package com.example.infotainment_car_health_digital.di

import android.content.Context
import com.example.infotainment_car_health_digital.api.VehicleDetailApiResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAlertApiResponse(
    ): VehicleDetailApiResponse {
        val builder = Retrofit.Builder()
            .baseUrl("https://mytvsapp-connect.tvs.in/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return builder.create(VehicleDetailApiResponse::class.java)
    }



    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }
}



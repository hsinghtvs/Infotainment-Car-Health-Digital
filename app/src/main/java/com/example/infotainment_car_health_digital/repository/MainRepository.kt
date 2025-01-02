package com.example.infotainment_car_health_digital.repository

import android.util.Log
import com.example.infotainment_car_health_digital.api.VehicleDetailApiResponse
import com.example.infotainment_car_health_digital.di.DataState
import com.example.infotainment_car_health_digital.di.parse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    val vehicleDetailApiResponse: VehicleDetailApiResponse
) {
    fun getServiceHistoryResponse(
        order_id: String
    ) = flow {
        vehicleDetailApiResponse.getServiceHistoryResponse(
            client_code = "MvxW3k482v2o",
            token = "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.MRMs599lWLRZS6SJISaQNnz0HeGW5GMrl_kEeueD5sc97LXMS0QaTPxZ-QcTci0W_gke7QCQdu8d9ujdaHzNuwdlYomjSzEx_z2TbFqnZH0pnSldowBqxbBIqiuB3V_xECyTBLeUuBdMm46r3lgO9Z7CtznbRRQAmRHEt7zD66detDjViHiSkOzAY0hCtiN1etBAKUJtaMz8eE4vbHQfzuoJZkMVY9a7E5q77ucbsUbDPmURKgzAbIWl2xbKMpfYzuTUmOFhG--f54Y1zCaraJBmsk8CYoI5ynFVYO_ipQZuDaDGhWnAMbP1vdFO-FR-JfOTLOLfEZQzjMAWbpoCdw.cjRyMjKItoioT4Erl2tNkQ.hpHvimvs3kZemNjhx9NioAFudr0vvRiS_GkFHKTImjel5SiC4DUvt1Jj73Xo1tQvRob-IArKTZZjl4ZSNDJGUw.dKDBPuuj40FEhDqOObO8fw",
            order_id = order_id
        ).parse {
            emit(DataState.success(it))
        }
    }.catch { error ->
        Log.d("TAG", "sendData: $error ")
        emit(DataState.error(error.message))
    }

    fun getInventoryDetail(
        order_id: String,
        type: String
    ) = flow {
        vehicleDetailApiResponse.getInventoryDetail(
            client_code = "MvxW3k482v2o",
            token = "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.MRMs599lWLRZS6SJISaQNnz0HeGW5GMrl_kEeueD5sc97LXMS0QaTPxZ-QcTci0W_gke7QCQdu8d9ujdaHzNuwdlYomjSzEx_z2TbFqnZH0pnSldowBqxbBIqiuB3V_xECyTBLeUuBdMm46r3lgO9Z7CtznbRRQAmRHEt7zD66detDjViHiSkOzAY0hCtiN1etBAKUJtaMz8eE4vbHQfzuoJZkMVY9a7E5q77ucbsUbDPmURKgzAbIWl2xbKMpfYzuTUmOFhG--f54Y1zCaraJBmsk8CYoI5ynFVYO_ipQZuDaDGhWnAMbP1vdFO-FR-JfOTLOLfEZQzjMAWbpoCdw.cjRyMjKItoioT4Erl2tNkQ.hpHvimvs3kZemNjhx9NioAFudr0vvRiS_GkFHKTImjel5SiC4DUvt1Jj73Xo1tQvRob-IArKTZZjl4ZSNDJGUw.dKDBPuuj40FEhDqOObO8fw",
            order_id = order_id,
            type = type
        ).parse {
            emit(DataState.success(it))
        }
    }.catch { error ->
        Log.d("TAG", "sendData: $error ")
        emit(DataState.error(error.message))
    }


    fun getEstimateDetail(
        order_id: String,
        type: String
    ) = flow {
        vehicleDetailApiResponse.getEstimateDetail(
            client_code = "MvxW3k482v2o",
            token = "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.MRMs599lWLRZS6SJISaQNnz0HeGW5GMrl_kEeueD5sc97LXMS0QaTPxZ-QcTci0W_gke7QCQdu8d9ujdaHzNuwdlYomjSzEx_z2TbFqnZH0pnSldowBqxbBIqiuB3V_xECyTBLeUuBdMm46r3lgO9Z7CtznbRRQAmRHEt7zD66detDjViHiSkOzAY0hCtiN1etBAKUJtaMz8eE4vbHQfzuoJZkMVY9a7E5q77ucbsUbDPmURKgzAbIWl2xbKMpfYzuTUmOFhG--f54Y1zCaraJBmsk8CYoI5ynFVYO_ipQZuDaDGhWnAMbP1vdFO-FR-JfOTLOLfEZQzjMAWbpoCdw.cjRyMjKItoioT4Erl2tNkQ.hpHvimvs3kZemNjhx9NioAFudr0vvRiS_GkFHKTImjel5SiC4DUvt1Jj73Xo1tQvRob-IArKTZZjl4ZSNDJGUw.dKDBPuuj40FEhDqOObO8fw",
            order_id = order_id,
            type = type
        ).parse {
            emit(DataState.success(it))
        }
    }.catch { error ->
        Log.d("TAG", "sendData: $error ")
        emit(DataState.error(error.message))
    }
}
package com.imb.jobtop.network.api

import retrofit2.http.GET
import retrofit2.http.Path

interface VacancyApi {


    @GET("13645/version/10074?access_key=$accessKey")
    suspend fun getEconomyVacancy(): Any

    @GET("{dataSet_id}/version/{vacancy_id}?access_key=$accessKey")
    suspend fun getVacancy(
        @Path("dataSet_id") dataSet_id: Int,
        @Path("vacancy_id") vacancyId: Int
    ): Any
}

private const val accessKey = "dfe4029fd751aea455164e329ac3fa9b"


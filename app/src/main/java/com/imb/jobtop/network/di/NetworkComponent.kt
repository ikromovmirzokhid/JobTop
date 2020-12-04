package com.imb.jobtop.network.di

import android.content.Context
import com.imb.jobtop.network.api.VacancyApi
import com.imb.jobtop.network.client.UnsafeOkHttpClient
import com.orhanobut.hawk.Hawk
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Component(modules = [NetworkModule::class])
@Singleton
interface NetworkComponent {

    fun api(): Retrofit

    fun vacancyApi(): VacancyApi

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(context: Context): Builder

        fun build(): NetworkComponent
    }
}


@Module
abstract class NetworkModule {

    companion object {
        private const val BASE_URL = "https://data.gov.uz/uz/api/v1/json/"

        @Provides
        @Singleton
        fun provideApi(context: Context): Retrofit {
            Hawk.init(context).build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    UnsafeOkHttpClient.getUnsafeOkHttpClient()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(ChuckInterceptor(context))
                        .build()
                )

                .build()
        }


        @Provides
        @Singleton
        fun provideVacancyApi(retrofit: Retrofit): VacancyApi =
            retrofit.create(VacancyApi::class.java)

    }
}

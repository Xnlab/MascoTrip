package com.mascot.app.data.remote

import com.mascot.app.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Retrofit 네트워크 모듈
 * 
 * 역할:
 * - Retrofit 인스턴스 생성 및 제공
 * - QuestApi 인터페이스 구현체 제공
 * - 네트워크 설정 (타임아웃, URL 등)
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    /**
     * OkHttpClient 제공
     * 
     * GPT 호출 때문에 타임아웃을 넉넉히 설정 (60초)
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Retrofit 인스턴스 제공
     * 
     * BASE_URL은 BuildConfig에서 가져옴 (빌드 타입별로 다른 값 사용 가능)
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * QuestApi 인터페이스 구현체 제공
     */
    @Provides
    @Singleton
    fun provideQuestApi(retrofit: Retrofit): QuestApi {
        return retrofit.create(QuestApi::class.java)
    }
}

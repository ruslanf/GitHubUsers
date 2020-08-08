package studio.bz_soft.githubusers.data.http

import android.content.Context
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import studio.bz_soft.githubusers.BuildConfig
import studio.bz_soft.githubusers.data.models.*
import java.io.File
import java.util.*

class ApiClientImpl(
    private val apiURL: String,
    private val appContext: Context
): ApiClient {

    private val retrofitClient by lazy { createRetrofitClient(apiURL) }
    private val apiClient by lazy { retrofitClient.create(DataApiInterface::class.java) }

    private val cacheSize = (10 * 1024 * 1024).toLong()
    private val httpCacheDirectory = File(appContext.cacheDir, "offlineCache")
    private val httpCache = Cache(httpCacheDirectory, cacheSize)

    private val logger = run {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE }
    }

    private val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .tlsVersions(TlsVersion.TLS_1_2)
        .cipherSuites(
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
        )
        .build()

    private fun httpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .cache(httpCache)
            .connectionSpecs(Collections.singletonList(connectionSpec))
            .addNetworkInterceptor(cacheInterceptor())
            .addInterceptor(offlineCacheInterceptor(appContext))
            .addInterceptor(githubInterceptor())
            .addInterceptor(logger)
            .build()

    private fun createRetrofitClient(apiURL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient())
            .build()

    override suspend fun getUsersList(): List<UsersModel> = apiClient.getUsers()

    override suspend fun getUserInfo(userName: String): UserModel = apiClient.getUserInfo(userName)
}
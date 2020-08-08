package studio.bz_soft.githubusers.data.http

import okhttp3.Interceptor

const val ACCEPT_HEADER = "Accept"
const val GITHUB_HEADER_VALUE = "application/vnd.github.v3+json"

fun githubInterceptor(): Interceptor = Interceptor { chain ->
    var request = chain.request()

    request = request.newBuilder().apply {
        addHeader(ACCEPT_HEADER, GITHUB_HEADER_VALUE)
    }.build()

    chain.proceed(request)
}
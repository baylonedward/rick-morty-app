package com.android.component.rickmorty_api_component.utils

import okhttp3.Interceptor
import okhttp3.OkHttpClient

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */

fun OkHttpClient.Builder.loggingInterceptor(): OkHttpClient.Builder {
  val loggingInterceptor = Interceptor {
    val chainRequest = it.request()
    //request
    val requestTime = System.nanoTime()
    println(
      String.format(
        "Sending request %s on %s%n%s",
        chainRequest.url(),
        it.connection(),
        chainRequest.headers()
      )
    )
    //response
    val response = it.proceed(chainRequest)
    val responseTime = System.nanoTime()
    println(
      String.format(
        "Received response for %s in %.1fms%n",
        chainRequest.url(),
        (responseTime - requestTime) / 1e6
      )
    )
    //log network response
    val networkResponse = response.networkResponse()
    println(networkResponse)
    return@Interceptor response
  }

  this.addInterceptor(loggingInterceptor)
  return this
}
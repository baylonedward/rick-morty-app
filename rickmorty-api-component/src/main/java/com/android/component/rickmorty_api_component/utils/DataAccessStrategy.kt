package com.android.component.rickmorty_api_component.utils

import com.android.component.rickmorty_api_component.utils.Resource.Status.ERROR
import com.android.component.rickmorty_api_component.utils.Resource.Status.SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */

@ExperimentalCoroutinesApi
fun <T, A> performGetOperation(
  databaseQuery: () -> Flow<T>,
  networkCall: suspend () -> Resource<A>,
  saveCallResult: suspend (A) -> Unit
): Flow<Resource<T>> = flow {
  emit(Resource.loading())
  val source = databaseQuery.invoke().map { Resource.success(it) }
  val responseStatus = networkCall.invoke()
  when (responseStatus.status) {
    SUCCESS -> responseStatus.data?.also { saveCallResult(it) }
    ERROR -> emit(Resource.error(responseStatus.message!!))
  }
  emitAll(source)
}.flowOn(Dispatchers.IO)
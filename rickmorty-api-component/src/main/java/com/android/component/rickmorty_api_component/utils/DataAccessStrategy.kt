package com.android.component.rickmorty_api_component.utils

import com.android.component.rickmorty_api_component.utils.Resource.Status.ERROR
import com.android.component.rickmorty_api_component.utils.Resource.Status.SUCCESS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */

@ExperimentalCoroutinesApi
fun <T, A> performGetOperation(
  databaseQuery: () -> Flow<T>,
  networkCall: suspend () -> Resource<A>,
  saveCallResult: suspend (A) -> Unit
): Flow<Resource<T>> =
  flow {
    emit(Resource.loading())
    val source = databaseQuery.invoke().map { Resource.success(it) }
    emitAll(source)

    val responseStatus = networkCall.invoke()
    if (responseStatus.status == SUCCESS) {
      responseStatus.data?.also { saveCallResult(it) }
    } else if (responseStatus.status == ERROR) {
      emit(Resource.error(responseStatus.message!!))
      emitAll(source)
    }
  }
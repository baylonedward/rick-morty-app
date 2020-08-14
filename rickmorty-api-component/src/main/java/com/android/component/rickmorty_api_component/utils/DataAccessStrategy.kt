package com.android.component.rickmorty_api_component.utils

import com.android.component.rickmorty_api_component.utils.Resource.Status.ERROR
import com.android.component.rickmorty_api_component.utils.Resource.Status.SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */

@ExperimentalCoroutinesApi
fun <T, A> performGetOperation(
  databaseQuery: () -> Flow<T>,
  networkCall: suspend () -> Resource<A>,
  saveCallResult: suspend (A) -> Unit,
  networkCallBack: (suspend (A) -> Unit)? = null
): Flow<Resource<T>> = channelFlow {
  send(Resource.loading())
  launch(Dispatchers.IO) {
    databaseQuery.invoke().map { Resource.success(it) }.collect {
      send(it)
    }
  }
  val responseStatus = networkCall.invoke()
  when (responseStatus.status) {
    SUCCESS -> {
      responseStatus.data?.also {
        saveCallResult(it)
        networkCallBack?.invoke(it)
      }
    }
    ERROR -> send(Resource.error(responseStatus.message!!))
  }
}.flowOn(Dispatchers.IO)
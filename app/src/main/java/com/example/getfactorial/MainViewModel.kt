package com.example.getfactorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.math.BigInteger
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainViewModel : ViewModel() {
private val coroutineScope= CoroutineScope(Dispatchers.Main+CoroutineName("My coroutine scope"))
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state


   fun calculate(value: String?) {
        _state.value = Progress
        if (value.isNullOrBlank()) {
            _state.value = Error
            return
        }
     //   with viewModelScope
//        viewModelScope.launch {
//            val number = value.toLong()
//            val result = factorial(number)
//            _state.value = Result(value = result)
//        }
        // with coroutineScope
        coroutineScope.launch {
            val number = value.toLong()
            val result = factorial(number)
            _state.value = Result(value = result)
        }

    }

    //work incorrect
//    private fun factorial(number:Long):BigInteger{
//        var result = BigInteger.ONE
//        for(i in 1..number){
//            result=result.multiply(BigInteger.valueOf(i))
//
//        }
//         return result
//    }
    //first  variant with suspendCoroutine
// private suspend fun  factorial(number:Long):String{
//   return suspendCoroutine {
//        thread {
//            var result = BigInteger.ONE
//            for (i in 1..number) {
//                result = result.multiply(BigInteger.valueOf(i))
//
//            }
//            it.resumeWith(kotlin.Result.success(result.toString()))
//        }
//    }
//
//}

// second variant with withContext
    private suspend fun factorial(number: Long): String {
        return withContext(Dispatchers.Default) {
            var result = BigInteger.ONE
            for (i in 1..number) {
                result = result.multiply(BigInteger.valueOf(i))
            }
            result.toString()
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}

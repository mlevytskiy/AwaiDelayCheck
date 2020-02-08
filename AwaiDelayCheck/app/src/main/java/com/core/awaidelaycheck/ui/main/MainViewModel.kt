package com.core.awaidelaycheck.ui.main

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.lang.Exception

class MainViewModel : ViewModel() {

    @Transient
    private val supervisor = SupervisorJob()
    @Transient
    private var scope = CoroutineScope(Dispatchers.IO + supervisor)

    private var time1: Long = 0

    init {

    }

    fun testFun() {
        startBgJob {
            startCalcTime()
            val someOperation1 = async {
                delay(1000)
            }
            someOperation1.await()
            delay(2000)
            finishCalcTime("testFun1: ")
        }
    }

    fun testFun2() {
        startBgJob {
            startCalcTime()
            val someOperation1 = async {
                delay(1000)
            }
            val someOperation2 = async {
                delay(2000)
            }
            someOperation1.await()
            someOperation2.await()
            finishCalcTime("testFun2: ")
        }
    }

    private fun startCalcTime() {
        time1 = SystemClock.elapsedRealtime()
    }

    private fun finishCalcTime(text: String) {
        val time2 = SystemClock.elapsedRealtime()
        Log.i("testr", text + "timeDuration=" + (time2-time1))
    }

    fun startBgJob(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                //ignore
            }
        })
    }

}

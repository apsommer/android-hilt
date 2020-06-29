package com.example.android.hilt.data

import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject

// @ActivityScoped means that this object is tied to the parent activity lifecycle - annotation not needed (but allowed) since defined in the module
// Hilt must know how to provide instances of this type, therefore @Inject is used on constructor
class LoggerInMemoryDataSource @Inject constructor(): LoggerDataSource {

    private val logs = LinkedList<Log>()

    override fun addLog(msg: String) {
        logs.addFirst(Log(msg, System.currentTimeMillis()))
    }

    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        callback(logs)
    }

    override fun removeLogs() {
        logs.clear()
    }
}
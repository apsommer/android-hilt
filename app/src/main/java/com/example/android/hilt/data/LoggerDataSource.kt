package com.example.android.hilt.data

// common interface for logger data sources: local db, or in-memory list
// moving the functionality into an interface allows multiple implementations of the same "logger" functionality
interface LoggerDataSource {
    fun addLog(msg: String)
    fun getAllLogs(callback: (List<Log>) -> Unit)
    fun removeLogs()
}
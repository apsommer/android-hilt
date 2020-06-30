package com.example.android.hilt.contentprovider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.example.android.hilt.data.Log
import com.example.android.hilt.data.LogDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent

// implementation of this class is not important ... just using it as example to show @EntryPoint annotation

private const val LOGS_TABLE = "logs"
private const val AUTHORITY = "com.example.android.hilt.provider"
private const val CODE_LOGS_DIR = 1
private const val CODE_LOGS_ITEM = 2

class LogsContentProvider: ContentProvider() {

    // @EntryPoint is need here because the hilt provided @AndroidEntryPoint does not exist for this
    // framework class. An interface and its implementation are created here to get hilt dependencies.

    // An entry point is an interface with an accessor method for each binding type we desire (including
    // its qualifier if applicable) @InstallIn is also required to indicate the correct container. This
    // interface and implementation should added inside the class that uses it (best practice).

    @InstallIn(ApplicationComponent::class) // DAO is in the app-level
    @EntryPoint // used to inject dependencies in classes not supported by Hilt (i.e., ContentProvider)
    interface LogsContentProviderEntryPoint {
        fun logDao(): LogDao
    }

    // EntryPointAccessors has static methods to access each container, and is passed the component
    // instance (superclass of this) or the @AndroidEntryPoint object that acts as the component.
    // The method, parameter, and @InstallIn annotation all must align to the same containers.

    private fun getLogDao(appContext: Context): LogDao {
        val hiltEntryPoint = EntryPointAccessors.fromApplication( // EntryPointAccessors expose containers
            appContext,
            LogsContentProviderEntryPoint::class.java)
        return hiltEntryPoint.logDao() // returned from the app-level container
    }

    private val matcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, LOGS_TABLE, CODE_LOGS_DIR)
        addURI(AUTHORITY, "$LOGS_TABLE/*", CODE_LOGS_ITEM)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code: Int = matcher.match(uri)
        return if (code == CODE_LOGS_DIR || code == CODE_LOGS_ITEM) {
            val appContext = context?.applicationContext ?: throw IllegalStateException()
            val logDao: LogDao = getLogDao(appContext) // getLogDao from Hilt container

            val cursor: Cursor? = if (code == CODE_LOGS_DIR) {
                logDao.selectAllLogsCursor()
            } else {
                logDao.selectLogById(ContentUris.parseId(uri))
            }
            cursor?.setNotificationUri(appContext.contentResolver, uri)
            cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }
}
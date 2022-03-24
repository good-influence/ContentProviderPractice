package com.example.myprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.example.myprovider.db.AppDatabase
import com.example.myprovider.db.Result
import com.example.myprovider.db.ResultDao
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.*
import org.threeten.bp.LocalDate
import kotlin.coroutines.resume

class MyProvider : ContentProvider() {

    companion object {
        private const val TAG = "MyProvider "
        private const val AUTHORITY = "com.example.myprovider"

        val URI_RESULT: Uri = Uri.parse("content://$AUTHORITY/result")
        const val TABLE_NAME = "result"
        const val CODE_RESULT_ITEM = 1
        const val CODE_RESULT_DIR = 2

        var uriMatcher: UriMatcher? = null

        // DB
        private lateinit var appDatabase: AppDatabase
        private var resultDao: ResultDao? = null

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher!!.addURI(AUTHORITY, "result", CODE_RESULT_ITEM)
            uriMatcher!!.addURI(
                AUTHORITY, "result/*", CODE_RESULT_DIR
            )

        }
    }

    override fun onCreate(): Boolean {
        appDatabase = AppDatabase.getInstance(context!!)
        resultDao = appDatabase.resultDao()

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher?.match(uri)) {
            CODE_RESULT_DIR -> {
                val cursor: Cursor = resultDao!!.getAll()
                cursor.setNotificationUri(context!!.contentResolver, uri)
                cursor
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        /*
            보통 bundle 에 전송할 데이터를 담아서 전송하지만,
            doSomething 시나리오
            : A 앱에서 B앱(MyProvider)의 doSomething 메소드 실행 -> 다른 서버와 통신을 통해 받은 결과 값을 DB에 저장
              A 앱에서 그 값을 Observe 하는 형태
            getListenerValue 시나리오
            : A 앱에서 B앱의 getListenerValue 메소드 실행 -> 다른 서버 작업 실행 및 서버 작업 결과 listener 에 반환
              listener 에 값이 반환될 때 까지 기다렸다가 bundle 에 담아 반환
         */
        return when (method) {
            "doSomething" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    doSomething()
                }
                // contentResolver 한테 값이 변경되었음을 알림. (A 앱에서 contentResolver 를 통해 이 사실을 알 수 있음)
                context!!.contentResolver.notifyChange(URI_RESULT, null)
                Bundle()
            }
            "getListenerValue" -> {
                Log.d(TAG, "START VIRUS")
                var bundle: Bundle
                runBlocking {
                    bundle = getListenerValue()
                }
                bundle
            }
            else -> {
                Bundle()
            }
        }
    }

    private suspend fun getListenerValue(): Bundle {
        val mapper = jacksonObjectMapper()

        var bundle = Bundle()
        bundle = suspendCancellableCoroutine<Bundle> { continuation ->
            val sampleListener = object : SampleListener {
                override fun onSuccess(result: Result) {
                    val jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result)
                    bundle.putSerializable("result", jsonStr)
                    continuation.resume(bundle)

                }

                override fun onFailure(result: Result) {
                    bundle.putSerializable("result", result.toString())
                    continuation.resume(bundle)
                }
            }
            // TODO 서버 작업
//            startServer(sampleListener)
        }

        return bundle

    }

    private suspend fun doSomething() {
        // TODO 서버 작업
        val netWorkData = Result(1, "sample value", LocalDate.now().toString())
        CoroutineScope(Dispatchers.IO).launch {
            resultDao?.insertOrUpdate(netWorkData)
        }
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher!!.match(uri)) {
            CODE_RESULT_DIR -> "vnd.android.cursor.dir/${AUTHORITY}.$TABLE_NAME"
            CODE_RESULT_ITEM -> "vnd.android.cursor.item/${AUTHORITY}.$TABLE_NAME"
            else -> throw IllegalArgumentException("Unknown URI : $uri")
        }
    }

    /*
        insert, delete, update 는 따로 구현하지 않았음.
     */
    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }
}
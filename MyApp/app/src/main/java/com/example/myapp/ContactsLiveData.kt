package com.example.myapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import java.lang.StringBuilder

class ContactsLiveData(context: Context) : ContentProviderLiveData<StringBuilder>(context, uri) {

    companion object {
        private val uri: Uri = Uri.parse("content://com.example.myprovider/")
        private val uriContent = Uri.parse("content://com.example.myprovider/result")

    }

    @SuppressLint("Range")
    override fun getContentProviderValue(): StringBuilder {
        val cursor = context.contentResolver.query(
            uriContent, null, null, null, null)!!
        val strBuild = StringBuilder()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                strBuild.append(
                    """${cursor.getString(cursor.getColumnIndex("id"))} - ${
                        cursor.getString(
                            cursor.getColumnIndex("result")
                        )
                    }""".trimIndent()
                )
            }
        } else {
            Log.d("ContactsLiveData ", "No Records Found")
        }

        return strBuild
    }
}
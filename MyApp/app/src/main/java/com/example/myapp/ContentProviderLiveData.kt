package com.example.myapp

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import androidx.lifecycle.MutableLiveData

abstract class ContentProviderLiveData<T>(
    val context: Context,
    private val uri: Uri
) : MutableLiveData<T>() {

    private lateinit var observer: ContentObserver

    override fun onActive() {
        observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                postValue(getContentProviderValue())
            }
        }
        context.contentResolver.registerContentObserver(uri, true, observer)
    }

    override fun onInactive() {
        context.contentResolver.unregisterContentObserver(observer)
    }

    abstract fun getContentProviderValue() : T

}
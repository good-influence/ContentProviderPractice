package com.example.myprovider

import com.example.myprovider.db.Result

interface SampleListener {
    fun onSuccess(result: Result)

    fun onFailure(result: Result)
}
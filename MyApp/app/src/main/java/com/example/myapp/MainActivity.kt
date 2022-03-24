package com.example.myapp

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private val CONTENT_URL: Uri = Uri.parse("content://com.example.myprovider/")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val doSomething = findViewById<Button>(R.id.btn_doSomething)
        doSomething.setOnClickListener() {
            val callDoSomething = this.contentResolver.call(
                CONTENT_URL,
                "getListenerValue",
                null,
                null
            )
            val data = callDoSomething?.getSerializable("result")
            Toast.makeText(this, "doSomething : $data", Toast.LENGTH_LONG).show()
        }

        val getListenerValue = findViewById<Button>(R.id.btn_getListenerValue)
        getListenerValue.setOnClickListener() {
            val liveData = ContactsLiveData(this)
            liveData.observe(this) {
                Toast.makeText(this, "data changed : $it", Toast.LENGTH_LONG).show()
            }
        }
    }
}
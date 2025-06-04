package com.nkh.nkhconnections

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lib.nkh.connections.models.NKHConnectionInfo
import com.lib.nkh.connections.network.NKH
import com.lib.nkh.connections.network.NKHConnection.OnConnectionListener
import com.lib.nkh.connections.network.NKHConnectionBuilder

class MainActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NKHConnectionBuilder
            .with(this)
            .setUrl("https://postman-echo.com/get?foo1=bar1&foo2=bar2")
            .setMethod(NKH.Method.GET)
            .setWay(NKH.Way.VOLLEY_STRING)
//            .setHeaders()
//            .setBody()
            .setListener(object: OnConnectionListener {
                override fun onStartConnecting() {
                    Toast.makeText(this@MainActivity, "Start connecting", Toast.LENGTH_SHORT).show()
                }

                override fun onConnectionCompleted() {
                    Toast.makeText(this@MainActivity, "Connection completed", Toast.LENGTH_SHORT).show()
                }

                override fun onNeedNetworkConnection() {
                    Toast.makeText(this@MainActivity, "No internet connection", Toast.LENGTH_SHORT).show()
                }

                override fun onConnectionFailed(
                    errorMessage: String,
                    serverResponse: String?,
                    resultCode: Int,
                    connectionInfo: NKHConnectionInfo
                ) {
                    Toast.makeText(this@MainActivity, serverResponse, Toast.LENGTH_SHORT).show()
                }

                override fun onParseConnectionResultResponse(
                    response: String,
                    connectionInfo: NKHConnectionInfo
                ) {
                    Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
                }

            })
            .build()?.execute()
    }

}
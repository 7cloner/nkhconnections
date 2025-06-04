package com.lib.nkh.connections.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.lib.nkh.connections.connections.VolleyConnection
import com.lib.nkh.connections.network.NKHConnection.OnConnectionListener
import com.lib.nkh.connections.network.NKH.Method
import com.lib.nkh.connections.network.NKH.Way

class NKHConnectionBuilder private constructor(private val context: Context) {

    private var headers = HashMap<String, String>()
    private var body = HashMap<String, String>()
    private var way: Way? = null
    private var method: Method? = null
    private var url: String = ""
    private var listener: OnConnectionListener? = null

    companion object {
        fun with(context: Context): NKHConnectionBuilder {
            return NKHConnectionBuilder(context)
        }

        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
    }

    fun setHeaders(headers: HashMap<String, String>): NKHConnectionBuilder {
        this.headers = headers
        return this
    }

    fun setBody(body: HashMap<String, String>): NKHConnectionBuilder {
        this.body = body
        return this
    }

    fun setWay(way: Way): NKHConnectionBuilder {
        this.way = way
        return this
    }

    fun setMethod(method: Method): NKHConnectionBuilder {
        this.method = method
        return this
    }

    fun setUrl(url: String): NKHConnectionBuilder {
        this.url = url
        return this
    }

    fun setListener(listener: OnConnectionListener?): NKHConnectionBuilder {
        this.listener = listener
        return this
    }

    fun build(): NKHConnection? {
        if (url.isEmpty()) {
            throw Exception("Url is empty")
            return null
        }
        if (way == null) {
            throw Exception("Way not selected")
            return null
        }
        if (method == null) {
            throw Exception("Method not selected")
            return null
        }
        return when (way) {
            Way.VOLLEY_STRING -> VolleyConnection(context)
                .setUrl(url)
                .setHeaders(headers)
                .setBody(body)
                .setMethod(method!!)
                .setListener(listener)

            else -> null
        }
    }
}
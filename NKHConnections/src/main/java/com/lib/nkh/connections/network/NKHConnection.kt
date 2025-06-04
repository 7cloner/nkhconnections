package com.lib.nkh.connections.network

import android.content.Context
import com.lib.nkh.connections.models.NKHConnectionInfo
import com.lib.nkh.connections.network.NKH.Method

abstract class NKHConnection(private val context: Context) {

    internal var connectionHeaders = HashMap<String, String>()
    internal var connectionBody = HashMap<String, String>()
    internal var connectionMethod: Method? = null
    internal var listener: OnConnectionListener? = null
    internal var url = ""
    internal var tag = "NKH-CONNECTION"

    fun setHeaders(headers: HashMap<String, String>): NKHConnection {
        this.connectionHeaders = headers
        return this
    }

    fun setBody(body: HashMap<String, String>): NKHConnection {
        this.connectionBody = body
        return this
    }

    fun setUrl(url: String): NKHConnection {
        this.url = url
        return this
    }

    fun setTag(tag: String): NKHConnection {
        this.tag = tag
        return this
    }

    fun setMethod(method: Method): NKHConnection {
        this.connectionMethod = method
        return this
    }

    fun setListener(listener: OnConnectionListener?): NKHConnection {
        this.listener = listener
        return this
    }

    fun getContext(): Context {
        return context
    }

    internal fun getConnectionInfo(): NKHConnectionInfo {
        return NKHConnectionInfo(
            headers = connectionHeaders,
            body = connectionBody,
            connectionMethod = connectionMethod,
            url = url
        )
    }

    abstract fun execute()

    abstract fun cancelPendingRequests(tag: String)

    interface OnConnectionListener {
        fun onStartConnecting()
        fun onConnectionCompleted()
        fun onConnectionFailed(errorMessage: String, serverResponse: String?, resultCode: Int, connectionInfo: NKHConnectionInfo)
        fun onParseConnectionResultResponse(response: String, connectionInfo: NKHConnectionInfo)
    }
}
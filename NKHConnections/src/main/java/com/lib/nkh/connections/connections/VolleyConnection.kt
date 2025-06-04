package com.lib.nkh.connections.connections

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.lib.nkh.connections.network.NKHConnection
import com.lib.nkh.connections.network.NKH.Method
import com.lib.nkh.connections.network.NKHConnectionBuilder

class VolleyConnection(context: Context) : NKHConnection(context) {

    private var mRequestQueue: RequestQueue? = null

    override fun execute() {
        if(NKHConnectionBuilder.isInternetAvailable(getContext())) {
            listener?.onStartConnecting()
            val myRequest = object : StringRequest(
                getMethod(), url,
                getSuccessRequestListener(),
                getErrorRequestListener()
            ) {
                override fun getHeaders(): Map<String, String> {
                    return connectionHeaders
                }

                override fun getParams(): Map<String, String> {
                    return connectionBody
                }

                override fun getPriority(): Priority {
                    return Priority.HIGH
                }
            }
            addToRequestQueue(myRequest)
        }else {
            listener?.onNeedNetworkConnection()
        }
    }

    override fun cancelPendingRequests(tag: String) {
        requestQueue.cancelAll(tag)
    }

    private fun getSuccessRequestListener(): Response.Listener<String> {
        return object : Response.Listener<String> {
            override fun onResponse(response: String) {
                listener?.onConnectionCompleted()
                listener?.onParseConnectionResultResponse(
                    response = response,
                    connectionInfo = getConnectionInfo()
                )
            }
        }
    }

    private fun getErrorRequestListener(): Response.ErrorListener {
        return object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                var serverStatusCode = -1
                var serverResponse = ""
                if (error.networkResponse != null) {
                    serverStatusCode = error.networkResponse.statusCode
                    serverResponse = String(error.networkResponse.data)
                }

                listener?.onConnectionFailed(
                    error.message.toString(),
                    serverResponse,
                    serverStatusCode,
                    getConnectionInfo()
                )
            }
        }
    }

    private fun getMethod(): Int {
        if (connectionMethod == Method.GET) {
            return Request.Method.GET
        } else if (connectionMethod == Method.POST) {
            return Request.Method.POST
        } else if (connectionMethod == Method.PUT) {
            return Request.Method.PUT
        } else if (connectionMethod == Method.DELETE) {
            return Request.Method.DELETE
        }

        throw Exception("Method not supported")
    }

    private val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(getContext())
            }

            return mRequestQueue!!
        }

    private fun <String> addToRequestQueue(req: Request<String>) {
        req.setTag(this.tag)
        req.retryPolicy = DefaultRetryPolicy(
            30 * 1000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(req)
    }
}
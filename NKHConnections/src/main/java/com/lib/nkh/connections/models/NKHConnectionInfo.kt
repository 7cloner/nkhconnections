package com.lib.nkh.connections.models

import com.lib.nkh.connections.network.NKH.Method

class NKHConnectionInfo(
    val headers: HashMap<String, String>,
    val body: HashMap<String, String>,
    val connectionMethod: Method?,
    val url: String
)
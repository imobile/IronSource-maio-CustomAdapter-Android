package com.ironsource.adapters.custom.maio

import android.content.Context
import com.ironsource.mediationsdk.adunit.adapter.BaseAdapter
import com.ironsource.mediationsdk.adunit.adapter.listener.NetworkInitializationListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData
import jp.maio.sdk.android.v2.Version
import jp.maio.sdk.android.v2.errorcode.ErrorCode

class MaioCustomAdapter : BaseAdapter() {

    companion object {
        fun errorMessage(errorCode: Int): String {
            return when (ErrorCode.getMajorCode(errorCode)) {
                0 -> "Unknown error"
                101 -> "No network"
                102 -> "Network timeout"
                103 -> "Aborted download"
                104 -> "Invalid response"
                105 -> "Zone not found"
                106 -> "Unavailable zone"
                107 -> "No fill"
                108 -> "Null args: MaioRequest"
                109 -> "Disc space not enough"
                110 -> "Unsupported OS version"

                201 -> "Ad expired"
                202 -> "Not ready yet"
                203 -> "Already shown"
                204 -> "Failed playback"
                205 -> "Null args: ViewContext"

                else -> "Unhandled error code"
            }
        }
    }

    override fun init(adData: AdData, context: Context, listener: NetworkInitializationListener?) {
        listener?.onInitSuccess()
    }

    override fun getNetworkSDKVersion(): String {
        return Version.instance.toString()
    }

    override fun getAdapterVersion(): String {
        return MaioCustomAdapterVersion
    }

}
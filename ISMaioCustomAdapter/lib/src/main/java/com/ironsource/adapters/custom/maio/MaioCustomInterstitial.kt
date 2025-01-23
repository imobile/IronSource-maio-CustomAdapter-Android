package com.ironsource.adapters.custom.maio

import android.app.Activity
import com.ironsource.mediationsdk.adunit.adapter.BaseInterstitial
import com.ironsource.mediationsdk.adunit.adapter.listener.InterstitialAdListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData
import com.ironsource.mediationsdk.adunit.adapter.utility.AdapterErrorType
import com.ironsource.mediationsdk.model.NetworkSettings
import jp.maio.sdk.android.v2.interstitial.IInterstitialLoadCallback
import jp.maio.sdk.android.v2.interstitial.IInterstitialShowCallback
import jp.maio.sdk.android.v2.interstitial.Interstitial
import jp.maio.sdk.android.v2.request.MaioRequest

class MaioCustomInterstitial(networkSettings: NetworkSettings?) :
    BaseInterstitial<MaioCustomAdapter>(networkSettings),
    IInterstitialLoadCallback,
    IInterstitialShowCallback {

    private var isReady = false

    private var viewContext: Activity? = null
    private var ad: Interstitial? = null
    private var loadListener: InterstitialAdListener? = null
    private var showListener: InterstitialAdListener? = null

    //region BaseInterstitial Methods
    override fun loadAd(adData: AdData, activity: Activity, listener: InterstitialAdListener) {
        val zoneId = adData.getString(paramKeyZoneId)
        if (zoneId == null) {
            listener.onAdLoadFailed(AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL, 10601, "Missing parameter: zoneId")
            return
        }

        viewContext = activity
        loadListener = listener

        val request = MaioRequest(zoneId, false, "")
        activity.runOnUiThread {
            ad = Interstitial.loadAd(request, activity, this)
        }
    }

    override fun isAdAvailable(adData: AdData): Boolean {
        return isReady
    }

    override fun showAd(adData: AdData, listener: InterstitialAdListener) {
        if (viewContext == null || ad == null) {
            listener.onAdShowFailed(20200, "Invalid show: Not ready")
            return
        }

        showListener = listener

        viewContext?.let {
            it.runOnUiThread {
                ad?.show(it, this)
            }
        }
    }
    //endregion

    //region Maio Callback Methods
    override fun loaded(interstitial: Interstitial) {
        isReady = true
        loadListener?.onAdLoadSuccess()
    }

    override fun opened(interstitial: Interstitial) {
        showListener?.onAdOpened()
        showListener?.onAdShowSuccess()
    }

    override fun closed(interstitial: Interstitial) {
        showListener?.onAdClosed()
    }

    override fun clicked(interstitial: Interstitial) {
        showListener?.onAdClicked()
    }

    override fun failed(interstitial: Interstitial, errorCode: Int) {
        val errorMessage = MaioCustomAdapter.errorMessage(errorCode)

        if (errorCode in 10000..<20000) {
            if (errorCode in 10700..<10800) {
                loadListener?.onAdLoadFailed(AdapterErrorType.ADAPTER_ERROR_TYPE_NO_FILL, errorCode, errorMessage)
            } else {
                loadListener?.onAdLoadFailed(AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL, errorCode, errorMessage)
            }
        } else if (errorCode in 20000..<30000) {
            showListener?.onAdShowFailed(errorCode, errorMessage)
        } else {
            // fallback
            if (!isReady) {
                loadListener?.onAdLoadFailed(AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL, errorCode, errorMessage)
            } else {
                showListener?.onAdShowFailed(errorCode, errorMessage)
            }
        }
    }
    //endregion

}
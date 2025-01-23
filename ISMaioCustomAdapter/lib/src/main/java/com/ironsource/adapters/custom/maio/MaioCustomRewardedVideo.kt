package com.ironsource.adapters.custom.maio

import android.app.Activity
import com.ironsource.mediationsdk.adunit.adapter.BaseRewardedVideo
import com.ironsource.mediationsdk.adunit.adapter.listener.RewardedVideoAdListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData
import com.ironsource.mediationsdk.adunit.adapter.utility.AdapterErrorType
import com.ironsource.mediationsdk.model.NetworkSettings
import jp.maio.sdk.android.v2.request.MaioRequest
import jp.maio.sdk.android.v2.rewarddata.RewardData
import jp.maio.sdk.android.v2.rewarded.IRewardedLoadCallback
import jp.maio.sdk.android.v2.rewarded.IRewardedShowCallback
import jp.maio.sdk.android.v2.rewarded.Rewarded

class MaioCustomRewardedVideo(networkSettings: NetworkSettings) :
    BaseRewardedVideo<MaioCustomAdapter>(networkSettings),
    IRewardedLoadCallback,
    IRewardedShowCallback {

    private var isReady = false

    private var viewContext: Activity? = null
    private var ad: Rewarded? = null
    private var loadListener: RewardedVideoAdListener? = null
    private var showListener: RewardedVideoAdListener? = null

    //region BaseRewardedVideo Methods
    override fun loadAd(adData: AdData, activity: Activity, listener: RewardedVideoAdListener) {
        val zoneId = adData.getString(paramKeyZoneId)
        if (zoneId == null) {
            listener.onAdLoadFailed(AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL, 10601, "Missing parameter: zoneId")
            return
        }

        viewContext = activity
        loadListener = listener
        val request = MaioRequest(zoneId, false, "")
        activity.runOnUiThread {
            ad = Rewarded.loadAd(request, activity, this)
        }
    }

    override fun isAdAvailable(adData: AdData): Boolean {
        return isReady
    }

    override fun showAd(adData: AdData, listener: RewardedVideoAdListener) {
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
    override fun loaded(rewarded: Rewarded) {
        isReady = true
        loadListener?.onAdLoadSuccess()
    }

    override fun opened(rewarded: Rewarded) {
        showListener?.onAdOpened()
        showListener?.onAdShowSuccess()
    }

    override fun closed(rewarded: Rewarded) {
        showListener?.onAdClosed()
    }

    override fun clicked(rewarded: Rewarded) {
        showListener?.onAdClicked()
    }

    override fun rewarded(rewarded: Rewarded, rewardData: RewardData) {
        showListener?.onAdRewarded()
    }

    override fun failed(rewarded: Rewarded, errorCode: Int) {
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
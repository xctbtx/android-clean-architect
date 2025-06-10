package com.xctbtx.cleanarchitectsample.ui.service

import android.telecom.Call
import android.telecom.InCallService
import android.util.Log

class CallService : InCallService(){

    override fun onCallAdded(call: Call?) {
        Log.d("TAG", "onCallAdded: $call")
        super.onCallAdded(call)
    }

    override fun onCallRemoved(call: Call?) {
        Log.d("TAG", "onCallRemoved: $call")
        super.onCallRemoved(call)
    }
}
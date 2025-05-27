package com.xctbtx.cleanarchitectsample.data

interface ApiCallBack {
    fun onSuccess()
    fun onFailure(error: String)
}
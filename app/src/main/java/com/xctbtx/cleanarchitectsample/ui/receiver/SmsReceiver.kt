package com.xctbtx.cleanarchitectsample.ui.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.extras
        val pdus = bundle?.get("pdus") as? Array<*>
        pdus?.forEach {
            val sms = SmsMessage.createFromPdu(it as ByteArray)
            val msg = sms.messageBody
            val sender = sms.originatingAddress
        }
    }
}
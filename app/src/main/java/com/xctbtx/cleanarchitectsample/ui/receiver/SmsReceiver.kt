package com.xctbtx.cleanarchitectsample.ui.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "") return
        val bundle = intent?.extras
        var pdus: Array<ByteArray>?
        if (Build.VERSION.SDK_INT >= 33) {
            pdus = bundle?.getParcelable("pdus", Array<ByteArray>::class.java)
        } else {
            pdus = bundle?.get("pdus") as? Array<ByteArray>
        }
        pdus?.forEach {
            val sms = SmsMessage.createFromPdu(it)
            val msg = sms.messageBody
            val sender = sms.originatingAddress
        }
    }
}
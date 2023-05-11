package com.aberon.flexbook.tool

import android.os.AsyncTask
import android.util.Log
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URLEncoder

class Translation : AsyncTask<String?, String?, String>() {
    private var listener: OnTranslationCompleteListener? = null
    override fun doInBackground(vararg p0: String?): String {
        val strArr = p0 as Array<String>
        val str = ""
        return try {
            val encode = URLEncoder.encode(strArr[0], "utf-8")
            val sb = StringBuilder()
            sb.append("https://translate.googleapis.com/translate_a/single?client=gtx&sl=")
            sb.append(strArr[1])
            sb.append("&tl=")
            sb.append(strArr[2])
            sb.append("&dt=t&q=")
            sb.append(encode)
            val execute: HttpResponse =
                HttpClientBuilder.create().build().execute(HttpGet(sb.toString()))
            val statusLine: StatusLine = execute.statusLine
            if (statusLine.statusCode == 200) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                execute.entity.writeTo(byteArrayOutputStream)
                val byteArrayOutputStream2 = byteArrayOutputStream.toString()
                byteArrayOutputStream.close()
                val jSONArray = JSONArray(byteArrayOutputStream2).getJSONArray(0)
                var str2 = str
                for (i in 0 until jSONArray.length()) {
                    val jSONArray2 = jSONArray.getJSONArray(i)
                    val sb2 = StringBuilder()
                    sb2.append(str2)
                    sb2.append(jSONArray2[0].toString())
                    str2 = sb2.toString()
                }
                return str2
            }
            execute.entity.content.close()
            throw IOException(statusLine.reasonPhrase)
        } catch (e: Exception) {
            Log.e("translate_api", e.message!!)
            listener!!.onError(e)
            str
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        listener!!.onStartTranslation()
    }

    override fun onPostExecute(text: String) {
        listener!!.onCompleted(text)
    }

    interface OnTranslationCompleteListener {
        fun onStartTranslation()
        fun onCompleted(text: String?)
        fun onError(e: Exception?)
    }

    fun setOnTranslationCompleteListener(listener: OnTranslationCompleteListener?) {
        this.listener = listener
    }
}
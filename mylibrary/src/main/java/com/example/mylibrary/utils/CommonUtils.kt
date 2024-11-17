package com.example.mylibrary.utils

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset

object CommonUtils {
    fun loadJSONFromAsset(context: Context?): String? {
        var json: String? = null
        try {
            context?.assets?.open("jobs_result.json")?.let { inputStream ->
                val size = inputStream.available()

                val buffer = ByteArray(size)

                inputStream.read(buffer)

                inputStream.close()

                json = String(buffer, Charset.defaultCharset())
            }
           /* val inStreem = context.assets.open("jobs_result.json")

            val size = inStreem.available()

            val buffer = ByteArray(size)

            inStreem.read(buffer)

            inStreem.close()

            json = String(buffer, Charset.defaultCharset())
*/

        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return json
    }
}
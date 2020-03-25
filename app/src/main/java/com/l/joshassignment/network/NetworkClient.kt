package com.l.joshassignment.network

import androidx.annotation.NonNull
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

object NetworkClient {

    fun getResult(responseBody : Call<ResponseBody>)  : Single<String> {
        return Single.create {
            try {
                responseBody.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(@NonNull call: Call<ResponseBody>, @NonNull response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val strResponse = response.body()?.string()
                            if (strResponse != null && response.isSuccessful) {
                                it.onSuccess(strResponse)
                            }
                        }
                    }
                    override fun onFailure(@NonNull call: Call<ResponseBody>, @NonNull t: Throwable) {
                        it.onError(t)
                    }
                })
            } catch (e: IOException) {
                if (!it.isDisposed) {
                    it.onError(Exception("Error in request : $e"))
                }
            }
        }
    }


}
package com.l.joshassignment.network

import com.l.joshassignment.network.MyLoggingInterceptor.provideOkHttpLogging
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.HashMap

class APIManager private constructor() {

    private val FLICKR_API_KEY = "6dea34e991808ad909d861a07ffd223c"
    private val MAX_PAGE_SIZE: Int = 15
    private val SEARCH_PHOTOS_METHOD = "flickr.photos.search"
    private val baseUrl = "https://api.flickr.com/services/rest/"


    private fun createRetrofitService(): JoshApiClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(provideOkHttpLogging())
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(JoshApiClient::class.java)
    }

    private val service: JoshApiClient
        get() = createRetrofitService()

    private interface JoshApiClient {
        @GET(".")
        fun getResult(@QueryMap body: Map<String, String>): Call<ResponseBody>
    }

    private fun getDefaultParams(): HashMap<String, String> {
        return hashMapOf(
            "api_key" to FLICKR_API_KEY,
            "format" to "json",
            "nojsoncallback" to "1",
            "safe_search" to "1"
        )
    }

    fun searchItem(searchTerm: String, page: Int): Call<ResponseBody> {
        val params = getDefaultParams()
        params["text"] = searchTerm
        params["page"] = page.toString()
        params["per_page"] = MAX_PAGE_SIZE.toString()
        params["method"] = SEARCH_PHOTOS_METHOD
        return service.getResult(params)
    }

    companion object {
        private var myInstance: APIManager? = null
        val instance: APIManager?
            get() {
                if (myInstance == null) {
                    myInstance =
                        APIManager()
                }
                return myInstance
            }
    }
}
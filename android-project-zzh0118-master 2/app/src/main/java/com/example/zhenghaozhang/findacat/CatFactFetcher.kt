package com.example.zhenghaozhang.findacat


import android.content.Context
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET



class CatFactFetcher(private val context: Context, val cat_fact_text: TextView) {
    private val TAG = "CatFactFetcher"
    var factSearchCompletionListener: FactSearchCompletionListener? = null

    interface FactSearchCompletionListener{
        fun onfetcher(fact: String)
        fun onError()
    }
    interface ApiEndpointInterface {
        @GET("/fact")
        fun getCatFactResponse(): Call<CatFactsResponse>
    }

    fun factFetched() {
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.CatFact_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val apiEndpoint = retrofit.create(ApiEndpointInterface::class.java)

        apiEndpoint.getCatFactResponse().enqueue(object : Callback<CatFactsResponse> {
            override fun onFailure(call: Call<CatFactsResponse>, t: Throwable) {
                Log.d(TAG, "Failed")

            }

            override fun onResponse(call: Call<CatFactsResponse>, response: Response<CatFactsResponse>) {
                val factResponseBody = response.body()
                if (factResponseBody != null) {
                    factSearchCompletionListener?.onfetcher(factResponseBody.fact)

                }else{
                    factSearchCompletionListener?.onError()
                }
            }

        })
    }
}
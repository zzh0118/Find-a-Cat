package com.example.zhenghaozhang.findacat


import com.squareup.moshi.Json

data class CatFactsResponse(

		@Json(name="fact")
		val fact: String,

		@Json(name="length")
		val length: Int? = null
)
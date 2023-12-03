package retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {
    val retrofit = Retrofit.Builder().baseUrl("http://tuxdroid.pythonanywhere.com").addConverterFactory(
        GsonConverterFactory.create()).build()
}
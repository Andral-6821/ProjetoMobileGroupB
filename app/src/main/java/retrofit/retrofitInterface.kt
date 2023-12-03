package retrofit

import com.example.projetomobiledef.retrofit.News
import com.example.projetomobiledef.retrofit.SymbolDetails
import com.example.projetomobiledef.retrofit.SymbolSummary
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface retrofitInterface {
    @GET("api/symbols")
    fun getSymbol(): Call<List<String>>

    @GET("api/symbol/summary/{symbol}")
    fun getSummary(@Path("symbol") summary: String): Call<SymbolSummary>

    @GET("api/news")
    fun getNews(): Call<List<News>>

    @GET("api/symbol/details/{symbol}")
    fun getSymbolDetails(@Path("symbol") symbol:String): Call<SymbolDetails>
}


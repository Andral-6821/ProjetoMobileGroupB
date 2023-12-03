package stocksDetailedFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetomobiledef.R
import com.example.projetomobiledef.SharedPreferencesHelper
import com.example.projetomobiledef.retrofit.SymbolDetails
import com.example.projetomobiledef.retrofit.SymbolSummary
import com.squareup.picasso.Picasso
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.GraphView
import retrofit.RetrofitConfig
import retrofit.retrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StocksDetailedFragment(stockssummary:SymbolSummary):Fragment() {
    private val summary = stockssummary
    private val details: MutableList<SymbolDetails> = mutableListOf()
    lateinit var chartGraph: GraphView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stock_details, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = StocksDetailedViewModel()
        viewModel
    }

    override fun onResume() {
        super.onResume()

        val savedStocks = SharedPreferencesHelper.loadSymbols(requireContext())

        val jsonNewsApi = RetrofitConfig.retrofit.create(retrofitInterface::class.java)
        val savedStocksDetailsList = mutableListOf<SymbolDetails>()

        savedStocks.forEach {
            val call = jsonNewsApi.getSymbolDetails(it.symbol)

            call.enqueue(object : Callback<SymbolDetails> {
                override fun onResponse(
                    call: Call<SymbolDetails>,
                    response: Response<SymbolDetails>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { symbolSummary ->
                            savedStocksDetailsList.add(symbolSummary)
                        }
                    }
                }

                override fun onFailure(call: Call<SymbolDetails>, t: Throwable) {
                    TODO("Not yet implemented")
                }
                //TRatar erros na resposta
            })
        }
    }
}


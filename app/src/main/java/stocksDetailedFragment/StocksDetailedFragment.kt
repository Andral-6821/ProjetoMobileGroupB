
package stocksDetailedFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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
import kotlin.math.E

class StocksDetailedFragment(stockssummary:SymbolSummary):Fragment() {
    private val summary = stockssummary
    private val details: MutableList<SymbolDetails> = mutableListOf()
    lateinit var chartGraph: GraphView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.summary_details_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Lê a funcionalidade que foi clicada para ver os detalhes
        val clickedSummary= arguments?.getString("symbol")?:null

        //Selecionar o container
        val stockcontainer=view.findViewById<LinearLayout>(R.id.detailsContainer)

        if(stockcontainer!=null){
            //Selecionar a view dos detalhes
            val stockDetailView= layoutInflater.inflate(R.layout.fragment_stock_details,null)

            //TextViews para cada um dos atributos de cada stock
            val stockTitle = stockDetailView.findViewById<TextView>(R.id.titleSymbol)
            val stockSector = stockDetailView.findViewById<TextView>(R.id.sector)
            val stockCeo = stockDetailView.findViewById<TextView>(R.id.ceo)
            val stockDescription = stockDetailView.findViewById<TextView>(R.id.description)
            val stockLogo = stockDetailView.findViewById<ImageView>(R.id.logoImageView)

            //selecionar um valor padrão
            stockTitle.text=clickedSummary
            stockSector.text = "Loading..."
            stockCeo.text = "Loading..."
            stockDescription.text = "Loading..."

            //adicionar uma view aos containers
            stockcontainer.addView(stockDetailView)

            //carregar os detalhes
            loadDetails(clickedSummary,stockSector,stockCeo,stockDescription)
            loadLogo(clickedSummary,stockLogo)
            val stockChart=view?.findViewById(R.id.lineChart)?:throw IllegalStateException("GraphView not found")
            loadChartData(clickedSummary,stockChart)
        }else{
            // Handle the case where assetContainer is null
            // Log an error, show a message, or take appropriate action
        }
    }

    private fun loadDetails(
        clickedSummary: String?,
        stockSector: TextView,
        stockCeo: TextView,
        stockDescription: TextView
    ) {
        val jsonApi = RetrofitConfig.retrofit.create(retrofitInterface::class.java)


        if (clickedSummary != null) {
            jsonApi.getSymbolDetails(clickedSummary).enqueue(object : Callback<SymbolDetails>) {
                override fun onResponse(call: Call<SymbolDetails>, response: Response<SymbolDetails>) {
                    if (response.isSuccessful) {
                        stockSector.text = "Sector: " + response.body()?.sector ?: "N/A"
                        stockCeo.text = "CEO: " + response.body()?.CEO ?: "N/A"
                        stockDescription.text =
                            "Description: " + response.body()?.description ?: "N/A"
                    } else {
                        stockSector.text = "Error: ${response.code()}"
                        stockCeo.text = "Error"
                        stockDescription.text = "Error"
                    }
                }

                override fun onFailure(call: Call<SymbolDetails>, t: Throwable) {
                    stockSector.text = "Fail: ${t.message}"
                    stockCeo.text = "Fail"
                    stockDescription.text = "Fail"
                }
            })
        }
    } }




















    private fun LoadSummary(view:View) {


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


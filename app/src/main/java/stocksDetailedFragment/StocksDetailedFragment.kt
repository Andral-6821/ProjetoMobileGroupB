
package stocksDetailedFragment

import android.annotation.SuppressLint
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.projetomobiledef.R
import com.example.projetomobiledef.SharedPreferencesHelper
import com.example.projetomobiledef.retrofit.SymbolDetails
import com.example.projetomobiledef.retrofit.SymbolSummary
import com.jjoe64.graphview.GraphView
import retrofit.RetrofitConfig
import retrofit.retrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StocksDetailedFragment():Fragment() {

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
            val stockChart=view.findViewById<LineChart>(R.id.lineChart)?:throw IllegalStateException("GraphView not found")
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

        if (clickedSummary != null) {
            val jsonApi = RetrofitConfig.retrofit.create(retrofitInterface::class.java)

            jsonApi.getSymbolDetails(clickedSummary).enqueue(object : Callback<SymbolDetails> {
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
    }



    //função para carregar o logo do stock consultado
    private fun loadLogo(clickedSummary: String?,logoImageView: ImageView){
        val jsonApi = RetrofitConfig.retrofit.create(retrofitInterface::class.java)

        //através do glide vamos carregar a imagem
        clickedSummary?.let{
            jsonApi.getSymbolDetails(it).enqueue(object :Callback<SymbolDetails>{
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<SymbolDetails>,
                    response: Response<SymbolDetails>
                ) {
                    if(response.isSuccessful){
                        val logoUrl=response.body()?.logo_url
                                logoUrl?.let{
                                    Glide.with(requireContext())
                                        .load(logoUrl)
                                        .into(logoImageView)
                                }
                    }else{
                        Log.d("StocksDetail","Error getting logo URL: ${response.code()}")
                    }

                }

                override fun onFailure(call: Call<SymbolDetails>, t: Throwable) {
                    Log.e("AssetsDetail","Failed to get logo URL:${t.message}")
                }

            })
        }
    }
                private fun loadChartData(clickedSummary: String?, lineChart: LineChart){
                    val jsonApi= RetrofitConfig.retrofit.create(retrofitInterface::class.java)

                    clickedSummary?.let{
                        jsonApi.getSymbolDetails(it).enqueue(object : Callback<SymbolDetails>{
                            override fun onResponse(
                                call: Call<SymbolDetails>,
                                response: Response<SymbolDetails>
                            ) {
                                if(response.isSuccessful){
                                    val chartData= response.body()?.chart_data
                                    chartData?.let{
                                        setupLineChart(lineChart,it.October_2022)
                                    }
                                }else{
                                    Log.d("StocksDetail","Error getting chart dara: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<SymbolDetails>, t: Throwable) {
                                Log.e("StocksDetail","Failed to get chart data: ${t.message}")
                            }
                        })
                    }
                }


    private fun setupLineChart(lineChart: LineChart, data: List<Double>) {
        val entries = ArrayList<Entry>()

        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), data[i].toFloat()))
        }

        val dataSet = LineDataSet(entries, "Chart Data")
        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.invalidate() // Atualiza o gráfico
    }




















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



package stocksDetailedFragment
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
import com.example.projetomobiledef.R
import com.example.projetomobiledef.retrofit.SymbolDetails
import com.example.projetomobiledef.retrofit.SymbolSummary
import com.squareup.picasso.Picasso
import retrofit.RetrofitConfig
import retrofit.retrofitInterface
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StocksDetailedFragment(passedSymbol : SymbolSummary):Fragment() {
    val passedSymbol = passedSymbol
    var symbolReturn : SymbolDetails? = null
    private val details: MutableList<SymbolDetails> = mutableListOf()
    private lateinit var linechart :LineChart
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.summary_details_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linechart = LineChart(requireContext())
        runBlocking {
            loadDetails()
        }


        //Selecionar o container
        val stockcontainer = view.findViewById<LinearLayout>(R.id.detailsContainer)

        if (stockcontainer != null) {
            //Selecionar a view dos detalhes
            val stockDetailView = layoutInflater.inflate(R.layout.fragment_stock_details, null)

            //TextViews para cada um dos atributos de cada stock
            val stockTitle = stockDetailView.findViewById<TextView>(R.id.titleSymbol)
            val stockSector = stockDetailView.findViewById<TextView>(R.id.sector)
            val stockCeo = stockDetailView.findViewById<TextView>(R.id.ceo)
            val stockDescription = stockDetailView.findViewById<TextView>(R.id.description)
            val stockLogo = stockDetailView.findViewById<ImageView>(R.id.logoImageView)

            //selecionar um valor padrão
            stockTitle.text = symbolReturn?.symbol
            stockSector.text = symbolReturn?.sector
            stockCeo.text = symbolReturn?.CEO
            stockDescription.text = symbolReturn?.description
            symbolReturn?.chart_data?.let { setupLineChart(linechart, it.October_2022) }
            symbolReturn?.logo_url.let{
                Picasso.get().load(it).into(stockLogo)
            }

            //adicionar uma view aos containers
            stockcontainer.addView(stockDetailView)
        }


    }
    fun setupLineChart(lineChart: LineChart, data : List<Double>) {

        val entries = ArrayList<Entry>()

        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), data[i].toFloat()))
        }

        val dataSet = LineDataSet(entries, "Chart Data")
        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.invalidate() // Atualiza o gráfico
    }

    private suspend fun loadDetails(){
        //val dispatcher = CoroutineScope(Dispatchers.IO)
        //dispatcher.launch {
            val jsonApi = RetrofitConfig.getInstance().create(retrofitInterface::class.java)
            val response : Response<SymbolDetails> = jsonApi.getSymbolDetails(passedSymbol.symbol)

            try {
                if (response.isSuccessful) {
                    symbolReturn =  response.body()
                } else {
                    // Handle unsuccessful response
                    // You can log the error message or take appropriate action
                    Log.e("APIError", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
                Log.e("APIError", "Exception: ${e.message}")
            }

    }
}





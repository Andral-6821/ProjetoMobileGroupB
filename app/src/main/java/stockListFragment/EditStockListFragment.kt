package stockListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetomobiledef.R
import com.example.projetomobiledef.retrofit.SymbolSummary
import com.example.projetomobiledef.ui.home.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit.RetrofitConfig
import retrofit.retrofitInterface

class EditStockListFragment : Fragment() , EditStockListRecyclerAdapter.OnSymbolToggleListener {

    val editStockListRecyclerAdapter = EditStockListRecyclerAdapter(mutableListOf(), this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_stock_list, container, false)
        val recyclerView:RecyclerView=view.findViewById(R.id.rvSymbols)
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        recyclerView.adapter=editStockListRecyclerAdapter
        loadList(recyclerView)
        return view
    }

    override fun onSymbolToggle(symbol: SymbolSummary, isChecked: Boolean) {
        // Handle the symbol toggle here
        if (!isChecked) {
            // Remove the symbol from the HomeFragment's list
            (requireActivity() as? HomeFragment)?.removeSymbolFromHome(symbol)
        }
    }

    private fun loadList(recyclerView: RecyclerView) {
        val apiCallsList = RetrofitConfig.getInstance().create(retrofitInterface::class.java)
        val list = mutableListOf<SymbolSummary>()


        apiCallsList.getSymbol().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    response.body()?.let { symbols ->
                        symbols.forEach {
                            val callApi = apiCallsList.getSummary(it)
                            callApi.enqueue(object : Callback<SymbolSummary> {
                                override fun onResponse(
                                    call: Call<SymbolSummary>,
                                    response: Response<SymbolSummary>
                                ) {
                                    if (response.isSuccessful) {
                                        response.body()?.let { summaryResponse ->
                                            if (!list.contains(summaryResponse)) {
                                                list.add(summaryResponse)
                                                editStockListRecyclerAdapter.setData(list)
                                            }
                                        }

                                    } else {
                                        // Handle unsuccessful response
                                        println("Unsuccessful response")
                                    }
                                }

                                override fun onFailure(call: Call<SymbolSummary>, t: Throwable) {
                                    // Handle failure
                                    println("Failure response")
                                }
                            })
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }









/*
    private fun addSymbolsToPage(symbols: MutableList<SymbolSummary>, recyclerView: RecyclerView) {
        val adapter = EditStockListRecyclerAdapter(symbols)
        recyclerView.adapter = adapter

    }

    private fun showErrorDialog(message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }.show()
    }
    */

}
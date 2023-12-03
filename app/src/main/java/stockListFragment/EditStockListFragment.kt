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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit.RetrofitConfig
import retrofit.retrofitInterface

class EditStockListFragment : Fragment() {

    private val editStockListRecyclerAdapter = EditStockListRecyclerAdapter(mutableListOf())

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

    private fun loadList(recyclerView: RecyclerView) {
        val apiCallsList = RetrofitConfig.retrofit.create(retrofitInterface::class.java)
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
                                            list.add(summaryResponse)
                                            editStockListRecyclerAdapter.setData(list)
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








    private fun addSymbolsToPage(symbols: MutableList<SymbolSummary>, recyclerView: RecyclerView) {
        val adapter = EditStockListRecyclerAdapter(symbols)
        recyclerView.adapter = adapter
/*
        adapter.setOnItemClickListener(object : EditStockListRecyclerAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedSymbol = symbols[position]

                val sharedPreferences =
                    requireContext().getSharedPreferences("sua_pref_name", Context.MODE_PRIVATE)
                val existingSymbols = SharedPreferencesHelper.loadSymbols(requireContext())

                if (!existingSymbols.contains(selectedSymbol)) {
                    SharedPreferencesHelper.addSymbol(selectedSymbol, requireContext())
                    showErrorDialog("Stock adicionado com sucesso!")
                    //TODO //getString(R.string.)
                } else {
                    showErrorDialog("O Stock já está na lista!")
                    //TODO //getString(R.string.)
                }

                findNavController().popBackStack(R.id.navigation_home, false)
            }
        })*/
    }

    private fun showErrorDialog(message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }.show()
    }
}
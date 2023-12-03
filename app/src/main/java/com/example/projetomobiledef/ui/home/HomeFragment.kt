package com.example.projetomobiledef.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetomobiledef.R
import com.example.projetomobiledef.SharedPreferencesHelper
import com.example.projetomobiledef.retrofit.SymbolSummary
import retrofit.RetrofitConfig
import retrofit.retrofitInterface
import retrofit2.Call
import retrofit2.Callback

import retrofit2.Response

class HomeFragment : Fragment(),HomeRecyclerAdapter.SymbolCLicked {
    val homeRecyclerAdapter = HomeRecyclerAdapter(mutableListOf())
    private val  savedStocksSummaryList = mutableListOf<SymbolSummary>()
    override fun onResume() {
        super.onResume()

        val savedStocks = SharedPreferencesHelper.loadSymbols(requireContext())

        val jsonNewsApi = RetrofitConfig.retrofit.create(retrofitInterface::class.java)


        savedStocks.forEach {
            val call = jsonNewsApi.getSummary(it.symbol)

            call.enqueue(object : Callback<SymbolSummary> {
                override fun onResponse(
                    call: Call<SymbolSummary>,
                    response: Response<SymbolSummary>
                ) {

                    if (response.isSuccessful) {
                        response.body()?.let { symbolSummary ->
                            savedStocksSummaryList.add(symbolSummary)
                        }

                        if (savedStocksSummaryList.size == savedStocks.size) {
                            homeRecyclerAdapter.updateData(savedStocksSummaryList)
                        }
                    } else {
                        //TRatar erros na resposta
                    }
                }

                override fun onFailure(call: Call<SymbolSummary>, t: Throwable) {
                    //tratar falhas
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val adapter= HomeRecyclerAdapter(savedStocksSummaryList)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvSavedStocks)
        adapter.setDetailsClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: Button = view.findViewById(R.id.openStockList_button)
        button.setOnClickListener {
            findNavController().navigate(R.id.navigation_stocklist)
        }
    }





    override fun onSymbolCLicked(symbol: SymbolSummary) {

        val switchFrag= stocksDetailedFragment.StocksDetailedFragment()
        val bundle= Bundle()
        bundle.putString("symbol", symbol.toString())
        switchFrag.arguments=bundle


        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.displayHome_fragment, switchFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}





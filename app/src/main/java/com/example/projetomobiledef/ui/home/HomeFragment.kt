package com.example.projetomobiledef.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
    val savedStocksSummaryList = MutableLiveData<MutableList<SymbolSummary>>()
    val homeRecyclerAdapter = HomeRecyclerAdapter(mutableListOf())



    override fun onResume() {
        super.onResume()

        val savedStocks = SharedPreferencesHelper.loadSymbols(requireContext())
        val jsonNewsApi = RetrofitConfig.getInstance().create(retrofitInterface::class.java)

        savedStocks.forEach {
            val call = jsonNewsApi.getSummary(it.symbol)

            call.enqueue(object : Callback<SymbolSummary> {
                override fun onResponse(
                    call: Call<SymbolSummary>,
                    response: Response<SymbolSummary>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { symbolSummary ->
                            val currentList = savedStocksSummaryList.value ?: mutableListOf()
                            if (!currentList.contains(symbolSummary)) {
                                currentList.add(symbolSummary)
                                savedStocksSummaryList.postValue(currentList)
                            }
                        }
                    } else {
                        // Handle errors in response
                    }
                }

                override fun onFailure(call: Call<SymbolSummary>, t: Throwable) {
                    // Handle failures
                }
            })
        }

        // Observe the changes in your list and update the adapter
        savedStocksSummaryList.observe(viewLifecycleOwner, Observer { list ->
            homeRecyclerAdapter.updateData(list)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvSavedStocks)
        homeRecyclerAdapter.setDetailsClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = homeRecyclerAdapter

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

        val switchFrag= stocksDetailedFragment.StocksDetailedFragment(symbol)

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.displayHome_fragment, switchFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun removeSymbolFromHome(symbol: SymbolSummary) {
        // Remove the symbol from the list and update the adapter
        val currentList = savedStocksSummaryList.value?.toMutableList() ?: mutableListOf()
        currentList.remove(symbol)
        savedStocksSummaryList.postValue(currentList)
        homeRecyclerAdapter.updateData(currentList)
    }
}





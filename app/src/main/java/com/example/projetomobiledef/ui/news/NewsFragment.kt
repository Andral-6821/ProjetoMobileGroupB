package com.example.projetomobiledef.ui.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetomobiledef.R
import com.example.projetomobiledef.retrofit.News
import retrofit.retrofitInterface
import com.example.projetomobiledef.ui.home.NewsRecyclerAdapter
import retrofit.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment: Fragment() {
    private lateinit var recyclerViewNews: RecyclerView
    private lateinit var emptyCardView: CardView

    private lateinit var emptyTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        recyclerViewNews = view.findViewById(R.id.recyclerViewNews)
        recyclerViewNews.layoutManager = LinearLayoutManager(context)
        emptyCardView = view.findViewById(R.id.withoutNews)

        emptyTextView = view.findViewById(R.id.textViewWithoutNews)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonNewsApi = RetrofitConfig.retrofit.create(retrofitInterface::class.java)
        val call = jsonNewsApi.getNews()

        call.enqueue(object : Callback<List<News>> {
            override fun onResponse(
                call: Call<List<News>>,
                response: Response<List<News>>
            ) {
                if (response.isSuccessful) {
                    val newsList: List<News> = response.body() ?: emptyList()

                    displayNews(newsList)

                } else {
                    //TRatar erros na resposta
                }
            }

            override fun onFailure(call: Call<List<News>>, t: Throwable) {
                //Tratar falha na chamada
            }
        })
    }

    private fun displayNews(newsList: List<News>) {
        val adapterNews = NewsRecyclerAdapter(newsList)
        recyclerViewNews.adapter = adapterNews

        // Verificar se a lista está vazia e exibir a mensagem apropriada
        if (newsList.isEmpty()) {
            recyclerViewNews.visibility = View.GONE
            emptyCardView.visibility = View.VISIBLE
            emptyTextView.text = "Without News..."

        } else {
            recyclerViewNews.visibility = View.VISIBLE
            emptyCardView.visibility = View.GONE
        }
    }
}





package com.example.projetomobiledef

import android.content.Context
import android.util.Log
import com.example.projetomobiledef.retrofit.SymbolSummary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesHelper {
    private const val PREFS_NAME = "StocksListPrefs"
    private const val KEY_SYMBOLS = "monitored_symbols"

    private var  symbols = mutableListOf<SymbolSummary>()

    fun addSymbol(symbol: SymbolSummary, context: Context) {
        symbols.add(symbol)
        saveSymbols(context)
        Log.d("SharedPreferencesHelper", "Added symbol: $symbol")
    }

    private fun saveSymbols(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = Gson().toJson(symbols)
        editor.putString(KEY_SYMBOLS, json)
        editor.apply()
    }

    fun loadSymbols(context: Context) : List<SymbolSummary> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_SYMBOLS, null)
        Log.d("SharedPreferencesHelper", "Loaded symbols JSON: $json")
        val type = object : TypeToken<List<SymbolSummary>>() {}.type
        symbols = Gson().fromJson(json, type) ?: mutableListOf()
        return symbols
    }
}


package com.example.projetomobiledef.retrofit

import java.util.Date


data class SymbolDetails(
    val CEO: String,
    val chart_data: Chart_data,
    val description: String,
    val details : SymbolDetailsDetails,
    val logo_url: String,
    val sector: String,
    val symbol: String,
)

package com.example.movieselector.mainmenu.models

class MovieResponse(
    var page: Int,
    var results: List<Movie>,
    var total_pages: Int,
    var total_results: Int,
)
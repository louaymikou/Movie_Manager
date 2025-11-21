package com.example.imbd;

public class AppConfig {
    // === 1. EXTERNAL API (TMDB) ===
    public static final String TMDB_API_KEY = "3eb1ae63cae30f7f59288876da6c30c7"; // your key here
    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";




    // === 2. LOCAL SERVER (PHP/MySQL) ===
    // IMPORTANT: Change this to your computer's IP (Run 'ipconfig' in CMD to find it)
    // If using Emulator, '10.0.2.2' often works better than 192.168...
    public static final String LOCAL_SERVER_IP = "192.168.1.38";

    // Based on your path C:\xampp\htdocs\lab7-movies_server_setup_v2,
    // the base URL path will be 'http://192.168.1.38/lab7-movies_server_setup_v2/'
    public static final String LOCAL_BASE_URL = "http://" + LOCAL_SERVER_IP + "/lab7-movies_server_setup_v2/";
    public static final String URL_ADD_MOVIE = LOCAL_BASE_URL + "add_movie.php";
    public static final String URL_GET_WATCHLIST = LOCAL_BASE_URL + "get_movies.php";

    // === Helper Methods ===
    public static String getPopularMoviesUrl(int page) {
        return TMDB_BASE_URL + "popular?api_key=" + TMDB_API_KEY + "&language=en-US&page=" + page;
    }
    public static String getMovieDetailsUrl(String movieId) {
        return TMDB_BASE_URL + movieId + "?api_key=" + TMDB_API_KEY + "&language=en-US";
    }
}
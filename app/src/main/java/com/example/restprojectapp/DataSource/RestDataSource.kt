package com.example.restprojectapp.DataSource


import com.example.restprojectapp.Model.ApiResponse
import retrofit2.http.GET

// Interfaz para el consumo de las rest apis
interface RestDataSource {

    // Usamos la etiqueta Get con los endpoints de cada API

    // Funcion que devuelve ApiResponse con el username
    @GET("?inc=name")
    suspend fun getUserName(): ApiResponse;

    // Funcion que devuelve ApiResponse con el userlocation
    @GET("?inc=location")
    suspend fun getUserLocation(): ApiResponse;

    // Funcion que devuelve ApiResponse con el userpicture
    @GET("?inc=picture")
    suspend fun getUserPicture(): ApiResponse;
}

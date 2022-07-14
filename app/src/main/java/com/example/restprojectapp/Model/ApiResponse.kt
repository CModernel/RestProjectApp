package com.example.restprojectapp.Model

// - Data class con la estructura del json para las apis de usuario
// - ApiResponse va a manejar una lista de resultados
// - Las clases data solo mantiene estado y no realizar ninguna operacion
data class ApiResponse (
    val results: List<Results> = emptyList()
)


// - La raiz de la estructura empieza en "results".
// - Se va a utilizar la clase ApiResponse para la respuesta de los consumos de las 3 apis del usuario.
// - Dependiendo de que api se llame, los demas pueden ser nulos
data class Results(
    val name: UserName?,
    val location: UserLocation?,
    val picture: UserPicture?
)
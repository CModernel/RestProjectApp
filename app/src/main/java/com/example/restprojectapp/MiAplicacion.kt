package com.example.restprojectapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Clase que extiende application
// Tambien se agrega al manifest el aplication
// https://developer.android.com/training/dependency-injection/hilt-android?hl=es-419
@HiltAndroidApp
class MiAplicacion : Application() {

}
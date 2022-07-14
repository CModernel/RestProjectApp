package com.example.restprojectapp.DataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.restprojectapp.Model.User
import com.example.restprojectapp.Model.UserDao

// Clase de la base de datos room
// Para utilizar una instancia de esta bd, vamos a utilizar DI
// DataSourceModule es donde viene la fuente de datos y es necesario inyectar la dependencia
@Database(entities = [User::class], version = 1)
abstract class DbDataSource : RoomDatabase(){

    abstract fun userDao(): UserDao
}
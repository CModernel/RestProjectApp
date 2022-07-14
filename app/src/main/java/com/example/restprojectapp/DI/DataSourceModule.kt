package com.example.restprojectapp.DI

import android.content.Context
import androidx.room.Room
import com.example.restprojectapp.DataSource.DbDataSource
import com.example.restprojectapp.DataSource.RestDataSource
import com.example.restprojectapp.Model.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


// Modulos en Hilt
// https://developer.android.com/training/dependency-injection/hilt-android?hl=es-419#hilt-modules
// SingletonComponent -> Inyector para Aplication (Va a existir en toda la aplicacion)
@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    @Named("BaseUrl") // Usamos etiqueta para posiblemente diferenciar futuros URLs
    fun provideBaseUrl() = "https://randomuser.me/api/"

    // Inyectamos la dependencia (BaseUrl) por parametro
    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("BaseUrl")
        baseUrl: String): Retrofit {

        // Retornamos una instancia de retrofit
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    // Se retorna RestDataSource con acceso a las interfaces de las APIs
    // Estos serán accedidos desde el repositorio del usuario
    @Singleton
    @Provides
    fun restDataSource(retrofit: Retrofit): RestDataSource =
        retrofit.create(RestDataSource::class.java)

    // Usamos fallbackToDestructive para que cuando hay una modificacion en la bd
    // esta se destruya y vuelva a crear
    @Singleton
    @Provides
    fun dbDataSource(@ApplicationContext context: Context): DbDataSource {
        return Room.databaseBuilder(context, DbDataSource::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    // Inyectamos el objeto UserDao para tener acceso
    // Luego de esto, debemos actualizar el UserRepository
    @Singleton
    @Provides
    fun userDao(db: DbDataSource): UserDao = db.userDao();
}

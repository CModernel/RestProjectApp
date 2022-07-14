package com.example.restprojectapp.DI

import com.example.restprojectapp.Repository.UserRepository
import com.example.restprojectapp.Repository.UserRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// Modulos en Hilt
// Creamos la clase como abstract
// Esto nos será util para hacer unit-test
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // No podemos usar @Provides ya que la funcion es abstracta
    @Singleton
    @Binds
    abstract fun userRepository(repo: UserRepositoryImp) : UserRepository

}

/*
Este es un ejemplo de mockRepository que devuelve un usuario con datos falsos
class MockRepository : UserRepository {
    override suspend fun getNewUser(): User {
        return User("NombreTest", "ApellidoTest", "CiudadTest", "ThumbnailTest")
    }
}*/

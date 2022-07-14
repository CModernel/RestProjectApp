package com.example.restprojectapp.Repository

import androidx.lifecycle.LiveData
import com.example.restprojectapp.DataSource.RestDataSource
import com.example.restprojectapp.Model.User
import com.example.restprojectapp.Model.UserDao
import javax.inject.Inject

// Utilizamos esta interfaz ya que nos permitirá hacer mocks para Unit test
interface UserRepository {
    // La funcion devuelve un objeto User
    suspend fun getNewUser() : User
    // Ahora que ya hemos inyectado el userDao, podemos crear otras funciones con acceso a la bd
    suspend fun deleteUser(deleteUser: User)
    fun getAllUser(): LiveData<List<User>>
}

// Creamos la clase que implementa UserRepository
// Inyectamos en el constructor el RestDataSource
// Una vez terminado con esta clase,
// Necesitamos inyectar el repositorio creando un nuevo module en el package DI

// Despues que inyectamos las dependencias en el DataSourceModule,
// Podemos inyectar el userDao y hacer insert del usuario
class UserRepositoryImp @Inject constructor(
    private val dataSource: RestDataSource,
    private val userDao: UserDao
): UserRepository {

    // Adentro de esta funcion llamamos a la API para obtener newUser
    override suspend fun getNewUser() : User {
        val name = dataSource.getUserName().results[0].name!!
        val location = dataSource.getUserLocation().results[0].location!!
        val picture = dataSource.getUserPicture().results[0].picture!!
        // Desde module, creamos una clase que encapsule estos 3 datos
        val mUser = User(name.first, name.last, location.city, picture.thumbnail)

        // Guardamos en la base de datos usando el userDao
        userDao.insert(mUser);
        return mUser;
    }

    override suspend fun deleteUser(deleteUser: User) = userDao.delete(deleteUser);

    override fun getAllUser(): LiveData<List<User>> = userDao.getAll();
}
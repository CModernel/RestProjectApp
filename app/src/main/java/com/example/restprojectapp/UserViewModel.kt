package com.example.restprojectapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restprojectapp.Model.User
import com.example.restprojectapp.Repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// Será necesario inyectar este ViewModel en la Activity
// Update: Anteriormente nombre era userRepositoryImp,
// Lo cambiamos a userRepo ya que será mas facil de entender a la hora de usar los mocks
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: UserRepository
): ViewModel() {
    //Funcion de prueba para verificar que getUser() esta funcionando
    /*
    // Esta funcion no utiliza suspend, por lo que utilizamos viewModelScope para el coroutine
    fun getUser(){
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepositoryImp.getNewUser();
            Log.d("UserViewModel", user.toString());
        }
    }*/

    // Utilizamos esta variable para que cuando se ejecute la api, cambiará a true
    // Nos va a servir para mostrar alguna notificacion o barra de progreso
    // Al terminar de responder la API, será false
    // La definimos como private ya que al ser MutableLiveData,
    // nuestra UI no debe modificarlo, por lo tanto, creamos un getter de LiveData
    private val _isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    // La variable users hace el llamado al repositorio de usuarios (el cual va a la bd)
    // Esta será un LiveData y se carga de forma "lazy"

    // Por definición lazy es una función que durante la primera invocación
    // ejecuta el lambda que se le haya pasado y en posteriores invocaciones
    // retornará el valor computado inicialmente.
    // https://medium.com/kotlin-dev-reactor/android-java-inicializando-variables-tard%C3%ADamente-lazy-y-lateinit-b648167c71b5
    val users: LiveData<List<User>> by lazy {
        userRepo.getAllUser();
    }

    // MutableLiveData extiende de LiveData, entonces puedo devolverlo como LiveData
    val isLoading: LiveData<Boolean> get() = _isLoading;

    fun addUser(){
        if(_isLoading.value == false){
            // Creamos una coroutine
            viewModelScope.launch(Dispatchers.IO) {
                // Seteamos isLoading en true
                _isLoading.postValue(true);

                // Va a insertar un nuevo usuario en la bd
                // y va a actualizar el LiveData de users automaticamente
                val user: User = userRepo.getNewUser();
                Log.d("UserViewModel", user.toString());

                // Una vez terminado el proceso, seteamos denuevo en false
                _isLoading.postValue(false);
            }
        }
    }

    fun deleteUser(deleteUser: User){
        // Creamos una coroutine
        viewModelScope.launch(Dispatchers.IO){
            userRepo.deleteUser(deleteUser);
        }
    }
}
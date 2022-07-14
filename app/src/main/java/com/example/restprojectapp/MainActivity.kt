package com.example.restprojectapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
//import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.restprojectapp.Model.User
import com.example.restprojectapp.ui.theme.RestProjectTheme
import com.valentinilk.shimmer.shimmer
import dagger.hilt.android.AndroidEntryPoint

// En las clases que queremos utilizar hilt
// para la injeccion de dependencia
// hay que usar la etiqueta AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MyApp1()
                }
            }
        }
    }
}

@Composable
fun MyApp1(viewModel: UserViewModel = hiltViewModel()) {
    // viewModel.users es un LiveData
    // Podemos agregar un observe en la variable
    // Valor inicial = lista vacia (arrayListOf())
    val users by viewModel.users.observeAsState(arrayListOf());

    // Guardamos la variable isLoading
    // Usamos observeAsState para obtener cambios
    // Valor inicial = false
    val isLoading by viewModel.isLoading.observeAsState(false);

    MyApp(onAddClick = {
        viewModel.addUser();
    },
        onDeleteClick = { viewModel.deleteUser(it); },
        users = users,
        isLoading = isLoading
    )
}

@Composable
fun MyApp(onAddClick: (()-> Unit)? = null,
          onDeleteClick: ((toDelete: User)-> Unit)? = null,
          users: List<User>,
          isLoading: Boolean) {

    // Similar flutter, usamos Scaffold para dibujar la interfaz
    // Modificamos el topBar y sus acciones
    Scaffold (
        topBar = {
            TopAppBar(title = { Text("Simple Rest +Room")},
                actions = { IconButton(onClick = {
                    onAddClick?.invoke();
                }) {
                    Icon(Icons.Filled.Add, "Add");
                }
                }
            )
        }
    ){
        // Creamos la estructura LazyColumn
        LazyColumn{
            var itemCount = users.size;
            if(isLoading) itemCount++;

            // LazyItemScope
            items(count = itemCount) {
                // Iteramos por cada elemento con el index
                index ->
                    // Guardamos index en variable auxiliar ya que necesitamos editarlo
                    var auxIndex = index;
                    if(isLoading){
                        // Si esta loading y el indice está en 0,
                        // entonces retornamos un item con texto "IsLoading"
                        if(auxIndex == 0)
                            return@items LoadingCard()
                        auxIndex--;
                    }
                val user = users[auxIndex];

                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 1.dp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                ){
                    Row(modifier = Modifier.padding(8.dp)){
                        Image(modifier = Modifier.size(50.dp),
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = user.thumbnail)
                                .apply(block = fun ImageRequest.Builder.() {
                                    placeholder(R.drawable.placeholder);
                                    error(R.drawable.placeholder)
                                }).build()
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight
                        )
                        Spacer()
                        Column(
                            Modifier.weight(1f),
                        ){
                            Text("${user.name} ${user.lastName}")
                            Text(user.city)
                        }
                        Spacer()
                        IconButton(onClick = {
                            onDeleteClick?.invoke(user);
                        }){
                            Icon(Icons.Filled.Delete, "Remove")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingCard(){
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 1.dp,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .testTag("LoadingCard")
    ){
        Row(modifier = Modifier.padding(8.dp)){
            ImageLoading()
            Spacer()
            Column{
                Spacer()
                Box(modifier = Modifier.shimmer()){
                    Column{
                        Box(
                            modifier = Modifier
                                .height(15.dp)
                                .fillMaxWidth()
                                .background(Color.Gray)
                        )
                        Spacer()
                        Box(
                            modifier = Modifier
                                .height(15.dp)
                                .fillMaxWidth()
                                .background(Color.Gray)
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun ImageLoading(){
    // Shimmer le agrega el efecto de reflejo, como si estuviera cargando
    Box(modifier = Modifier.shimmer()){
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Gray)
        )
    }
    Box(modifier = Modifier)
}

@Composable
fun Spacer(size: Int = 8) = Spacer(modifier = Modifier.size(size.dp))

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RestProjectTheme {
        MyApp(users = listOf(), isLoading = false)
    }
}
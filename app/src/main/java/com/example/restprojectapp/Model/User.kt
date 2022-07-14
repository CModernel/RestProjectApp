package com.example.restprojectapp.Model

import androidx.lifecycle.LiveData
import androidx.room.*

// Con room, crearemos una tabla User, para eso usamos la etiqueta Entity
@Entity(tableName = "User")
data class User(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @PrimaryKey(autoGenerate = true) var id: Int=0
)

// Creamos una interface para hacer queries a la tabla User
@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    // LiveData va a cambiar automaticamente en cuanto exista un nuevo User en la tabla
    // Se utiliza para actualizar la UI
    @Query("SELECT * FROM user ORDER BY id DESC")
    fun getAll(): LiveData<List<User>>

    @Delete
    fun delete(user: User)
}
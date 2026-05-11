package proway.capgemini.kotlin.atividade2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import proway.capgemini.kotlin.atividade2.model.Contato

@Database(entities = [Contato::class], version = 1)
abstract class ContatoDB : RoomDatabase() {
    abstract fun contatoDao(): ContatoDao

    companion object {
        @Volatile
        private var INSTANCE: ContatoDB? = null

        fun getDatabase(context: Context): ContatoDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, ContatoDB::class.java, "app.db")
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
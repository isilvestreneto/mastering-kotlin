package proway.capgemini.kotlin.atividade2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import proway.capgemini.kotlin.atividade2.model.Contato

@Dao
interface ContatoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun criar(contato: Contato)

    @Update
    suspend fun atualizar(contato: Contato)

    @Delete
    suspend fun deletar(contato: Contato)

    @Query("SELECT * FROM contatos ORDER BY nome")
    fun listarContatos(): Flow<List<Contato>>

    @Query("SELECT * FROM contatos WHERE nome = :nome ORDER BY nome")
    fun listarContatoPorNome(nome: String): Flow<List<Contato>>
}
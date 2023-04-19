package com.boswelja.truemanager.auth.serverstore

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class AuthenticatedServersStoreImpl(
    context: Context
) : AuthenticatedServersStore {

    private val database = Room.databaseBuilder(
        context,
        AuthenticatedServerDatabase::class.java,
        "authenticated-servers"
    ).build()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAll(): Flow<List<AuthenticatedServer>> {
        return database.authenticatedServerDao().getAll().mapLatest { servers ->
            servers.map { server ->
                AuthenticatedServer(server.serverAddress, server.token)
            }
        }
    }

    override suspend fun delete(server: AuthenticatedServer) {
        database.authenticatedServerDao().delete(server.token)
    }

    override suspend fun add(serverAddress: String, token: String) {
        database.authenticatedServerDao().insertAll(AuthenticatedServer(serverAddress, token))
    }
}

@Entity(tableName = "authenticated_servers")
data class AuthenticatedServerDto(
    @ColumnInfo(name = "server_address")
    val serverAddress: String,
    @PrimaryKey
    @ColumnInfo(name = "token")
    val token: String,
)

@Dao
interface AuthenticatedServerDao {
    @Query("SELECT * FROM authenticated_servers")
    fun getAll(): Flow<List<AuthenticatedServerDto>>

    @Insert
    suspend fun insertAll(vararg servers: AuthenticatedServer)

    @Delete
    suspend fun delete(token: String)
}

@Database(entities = [AuthenticatedServerDto::class], version = 1)
abstract class AuthenticatedServerDatabase : RoomDatabase() {
    abstract fun authenticatedServerDao(): AuthenticatedServerDao
}

package com.nasdroid.auth.data.serverstore

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

internal class AuthenticatedServersStoreImpl(
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
                AuthenticatedServer(
                    server.hostId,
                    server.serverAddress,
                    server.token,
                    server.name,
                )
            }
        }
    }

    override suspend fun get(id: String): AuthenticatedServer {
        val dto = database.authenticatedServerDao().get(id)
        return AuthenticatedServer(
            uid = dto.hostId,
            serverAddress = dto.serverAddress,
            token = dto.token,
            name = dto.name
        )
    }

    override suspend fun delete(server: AuthenticatedServer) {
        database.authenticatedServerDao().delete(
            AuthenticatedServerDto(
                server.uid,
                server.serverAddress,
                server.token,
                server.name,
            )
        )
    }

    override suspend fun add(server: AuthenticatedServer) {
        database.authenticatedServerDao().insertAll(
            AuthenticatedServerDto(
                server.uid,
                server.serverAddress,
                server.token,
                server.name,
            )
        )
    }
}

@Entity(tableName = "authenticated_servers")
internal data class AuthenticatedServerDto(
    @PrimaryKey
    @ColumnInfo("host_id")
    val hostId: String,
    @ColumnInfo(name = "server_address")
    val serverAddress: String,
    @ColumnInfo(name = "token")
    val token: String,
    @ColumnInfo("name")
    val name: String,
)

@Dao
internal interface AuthenticatedServerDao {
    @Query("SELECT * FROM authenticated_servers")
    fun getAll(): Flow<List<AuthenticatedServerDto>>

    @Query("SELECT * FROM authenticated_servers WHERE host_id = :id")
    suspend fun get(id: String): AuthenticatedServerDto

    @Insert
    suspend fun insertAll(vararg servers: AuthenticatedServerDto)

    @Delete
    suspend fun delete(server: AuthenticatedServerDto)
}

@Database(entities = [AuthenticatedServerDto::class], version = 2)
internal abstract class AuthenticatedServerDatabase : RoomDatabase() {
    abstract fun authenticatedServerDao(): AuthenticatedServerDao
}

package com.example.myapplication.data.socketmanager

import android.util.Log
import com.example.myapplication.data.mapper.TestMapper
import com.example.myapplication.data.models.DataTestResponse
import com.example.myapplication.domain.SocketRepository
import com.example.myapplication.domain.models.DomainTestRequest
import com.example.myapplication.domain.models.DomainTestResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.net.Socket
import java.nio.ByteBuffer
import javax.inject.Inject

private const val TAG = "SocketRepositoryImpl"

class SocketManager @Inject constructor(
    private val address: String,
    private val port: Int,
    private val gson: Gson,
    private val testMapper: TestMapper
) : SocketRepository {

    private var socket: Socket? = null

    override suspend fun connect() {
        withContext(Dispatchers.IO) {
            socket = Socket(address, port)
            Log.d(TAG, "connected: ${socket?.isConnected}")

        }
    }

    override suspend fun send(request: DomainTestRequest) {
        withContext(Dispatchers.IO) {
            val dataRequest = testMapper.mapToData(request)
            val message = gson.toJson(dataRequest)

            Log.i(TAG, "sending: $message")

            val messageBytes = message.toByteArray()
            val lengthBytes = ByteBuffer.allocate(4).putInt(messageBytes.size).array()

            val outputStream = socket?.getOutputStream()
            outputStream?.write(lengthBytes)
            outputStream?.write(messageBytes)
            outputStream?.flush()
        }
    }

    override fun receive(): Flow<DomainTestResponse> = callbackFlow {

        if (socket == null || socket?.isConnected == false) {
            close(IllegalStateException("Socket is not connected"))
            return@callbackFlow
        }

        try {
            val inputStream = DataInputStream(socket?.getInputStream())

            while (true) {
                val lengthBytes = ByteArray(4)
                inputStream.readFully(lengthBytes)
                val length = ByteBuffer.wrap(lengthBytes).int

                val buffer = ByteArray(length)
                inputStream.readFully(buffer)
                val message = String(buffer, 0, length)

                Log.d(TAG, "received: $message")

                val dataResponse = gson.fromJson(message, DataTestResponse::class.java)
                trySend(testMapper.mapToDomain(dataResponse))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading from socket", e)
            close(e)
        }

        awaitClose { close() }

    }.flowOn(Dispatchers.IO)

    override suspend fun close() {
        withContext(Dispatchers.IO) {
            socket?.close()
            socket = null
        }
    }
}
package com.example.myapplication.domain

import com.example.myapplication.domain.models.DomainTestRequest
import com.example.myapplication.domain.models.DomainTestResponse
import kotlinx.coroutines.flow.Flow

interface SocketRepository {
    suspend fun connect()
    suspend fun send(request: DomainTestRequest)
    fun receive(): Flow<DomainTestResponse>
    suspend fun close()
}
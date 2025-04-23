package com.example.myapplication.data.mapper

import com.example.myapplication.data.models.DataTestRequest
import com.example.myapplication.data.models.DataTestResponse
import com.example.myapplication.domain.models.DomainTestRequest
import com.example.myapplication.domain.models.DomainTestResponse
import javax.inject.Inject

class TestMapper @Inject constructor() {

    fun mapToData(request: DomainTestRequest): DataTestRequest {
        return DataTestRequest(
            gender = request.gender,
            age = request.age
        )
    }

    fun mapToDomain(response: DataTestResponse): DomainTestResponse {
        return DomainTestResponse(
            allowed = response.allowed
        )
    }
}
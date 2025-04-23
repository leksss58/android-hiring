package com.example.myapplication.ui.mapper

import com.example.myapplication.domain.models.DomainTestRequest
import com.example.myapplication.domain.models.DomainTestResponse
import com.example.myapplication.ui.models.UiTestRequest
import com.example.myapplication.ui.models.UiTestResponse
import javax.inject.Inject

class UiTestMapper @Inject constructor() {

    fun mapToDomain(ui: UiTestRequest): DomainTestRequest {
        return DomainTestRequest(
            gender = ui.gender,
            age = ui.age
        )
    }

    fun mapToUi(domain: DomainTestResponse): UiTestResponse {
        return UiTestResponse(
            allowed = domain.allowed
        )
    }
}
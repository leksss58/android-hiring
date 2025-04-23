package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.SocketRepository
import com.example.myapplication.providers.SharedPreferencesProvider
import com.example.myapplication.ui.mapper.UiTestMapper
import com.example.myapplication.ui.models.GenderType
import com.example.myapplication.ui.models.UiTestRequest
import com.example.myapplication.ui.models.UiTestResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val KEY_GENDER = "gender"
private const val KEY_AGE = "age"
private const val EMPTY_STRING = ""

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val socketRepository: SocketRepository,
    private val uiTestMapper: UiTestMapper,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) : ViewModel() {

    val watchGender: StateFlow<GenderType>
        get() = _gender
    val watchAge: Flow<Int>
        get() = _age
    val responses: StateFlow<UiTestResponse?>
        get() = _responses

    private val _gender = MutableStateFlow(GenderType.NONE)
    private val _age = MutableStateFlow(16)
    private val _responses =
        MutableStateFlow<UiTestResponse?>(null)

    private var receiveJob: Job? = null

    init {
        connect()
        if (sharedPreferencesProvider.getPreferencesInt(KEY_AGE) > 0) getSavedAge()
        if (sharedPreferencesProvider.getPreferencesString(
                KEY_GENDER,
                EMPTY_STRING
            )?.isNotEmpty() == true
        ) getSavedGender()
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

    fun setAge(age: Int) = viewModelScope.launch {
        _age.emit(age)
        saveAge(age)
    }

    fun setGender(gender: GenderType) = viewModelScope.launch {
        _gender.emit(gender)
        saveGender(gender.gender)
    }

    fun sendMessage() {
        viewModelScope.launch {
            val uiRequest = UiTestRequest(
                gender = _gender.value.gender,
                age = _age.value

            )
            val domainRequest = uiTestMapper.mapToDomain(uiRequest)
            socketRepository.send(domainRequest)
        }
    }

    private fun connect() {
        viewModelScope.launch {
            socketRepository.connect()
            startReceiving()
        }
    }

    private fun startReceiving() {
        // Перезапуск подписки, если уже была
        receiveJob?.cancel()
        receiveJob = viewModelScope.launch {
            socketRepository.receive()
                .map { uiTestMapper.mapToUi(it) }
                .collect {
                    _responses.value = it
                }
        }
    }

    private fun disconnect() {
        viewModelScope.launch {
            socketRepository.close()
        }
    }

    private fun getSavedGender() {
        val genderString = sharedPreferencesProvider.getPreferencesString(
            KEY_GENDER,
            EMPTY_STRING
        ) ?: EMPTY_STRING
        _gender.value = GenderType.value(genderString)
    }

    private fun getSavedAge() {
        _age.value =
            sharedPreferencesProvider.getPreferencesInt(KEY_AGE)
    }

    private fun saveGender(gender: String) {
        sharedPreferencesProvider.savePreferencesString(
            KEY_GENDER,
            gender
        )
    }

    private fun saveAge(age: Int) {
        sharedPreferencesProvider.savePreferencesInt(KEY_AGE, age)
    }
}
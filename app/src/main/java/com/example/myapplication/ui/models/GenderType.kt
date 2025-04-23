package com.example.myapplication.ui.models

enum class GenderType(val gender: String) {
    MALE("m"),
    FEMALE("f"),
    NONE("");

    companion object {
        fun value(findValue: String): GenderType = entries.first { it.gender == findValue }
    }
}
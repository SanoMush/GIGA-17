package com.example.giga17.data.model

data class SubBab(
    val id: Int,
    val judulSubBab: String,
    val isiTeks: String,
    var isSelesai: Boolean = false,
    var isLocked: Boolean = false,
    val kuisSubBab: Kuis?
)

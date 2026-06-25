package com.example.giga17.data.model

data class Bab(
    val id: Int,
    val judulBab: String,
    val listSubBab: List<SubBab>,
    var isSelesai: Boolean = false,
    val tipeSimulasi: String?,
    val kuisAkhirBab: Kuis?,
    var isLocked: Boolean = false
)

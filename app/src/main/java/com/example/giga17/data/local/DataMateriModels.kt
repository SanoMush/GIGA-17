package com.example.giga17.data.local

import org.json.JSONArray
import org.json.JSONObject

data class Soal(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class Kuis(
    val xpReward: Int,
    val questions: List<Soal>
) {
    fun toJson(): String {
        val json = JSONObject()
        json.put("xpReward", xpReward)
        val jsonArray = JSONArray()
        questions.forEach { q ->
            val qJson = JSONObject()
            qJson.put("question", q.question)
            val optArray = JSONArray()
            q.options.forEach { optArray.put(it) }
            qJson.put("options", optArray)
            qJson.put("correctIndex", q.correctIndex)
            qJson.put("explanation", q.explanation)
            jsonArray.put(qJson)
        }
        json.put("questions", jsonArray)
        return json.toString()
    }
}

data class SubBabData(
    val judulSubBab: String,
    val isiTeks: String,
    val kuis: Kuis?
)

data class BabData(
    val judulBab: String,
    val tipeSimulasi: String?,
    val subBabs: List<SubBabData>,
    val kuisAkhirBab: Kuis?
)

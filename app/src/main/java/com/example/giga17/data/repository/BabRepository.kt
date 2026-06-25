package com.example.giga17.data.repository

import com.example.giga17.data.local.dao.BabDao
import com.example.giga17.data.local.dao.SubBabDao
import com.example.giga17.data.local.entity.BabEntity
import com.example.giga17.data.local.entity.SubBabEntity
import com.example.giga17.data.model.Bab
import com.example.giga17.data.model.Kuis
import com.example.giga17.data.model.SubBab
import com.example.giga17.presentation.viewmodel.QuizQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

class BabRepository(
    private val babDao: BabDao,
    private val subBabDao: SubBabDao
) {
    fun getAllBab(): Flow<List<Bab>> = flow {
        val babEntities = babDao.getAllBab()
        val subBabEntities = subBabDao.getAllSubBab()

        val listBab = babEntities.map { babEntity ->
            val subBabsForBab = subBabEntities.filter { it.babId == babEntity.id }
            val mappedSubBabs = subBabsForBab.map { subBabEntity ->
                SubBab(
                    id = subBabEntity.id,
                    judulSubBab = subBabEntity.judulSubBab,
                    isiTeks = subBabEntity.isiTeks,
                    isSelesai = false, // Will be resolved by ViewModel/Progres
                    kuisSubBab = parseKuis(subBabEntity.kuisSubBabJson)
                )
            }

            Bab(
                id = babEntity.id,
                judulBab = babEntity.judulBab,
                listSubBab = mappedSubBabs,
                isSelesai = false, // Will be resolved by ViewModel/Progres
                tipeSimulasi = babEntity.tipeSimulasi,
                kuisAkhirBab = parseKuis(babEntity.kuisAkhirBabJson)
            )
        }
        emit(listBab)
    }

    suspend fun getBabById(id: Int): Bab? {
        val babEntity = babDao.getBabById(id) ?: return null
        val subBabsForBab = subBabDao.getSubBabByBabId(babEntity.id)

        val mappedSubBabs = subBabsForBab.map { subBabEntity ->
            SubBab(
                id = subBabEntity.id,
                judulSubBab = subBabEntity.judulSubBab,
                isiTeks = subBabEntity.isiTeks,
                isSelesai = false,
                kuisSubBab = parseKuis(subBabEntity.kuisSubBabJson)
            )
        }

        return Bab(
            id = babEntity.id,
            judulBab = babEntity.judulBab,
            listSubBab = mappedSubBabs,
            isSelesai = false,
            tipeSimulasi = babEntity.tipeSimulasi,
            kuisAkhirBab = parseKuis(babEntity.kuisAkhirBabJson)
        )
    }
    
    suspend fun getSubBabById(id: Int): SubBab? {
        val subBabEntity = subBabDao.getSubBabById(id) ?: return null
        return SubBab(
            id = subBabEntity.id,
            judulSubBab = subBabEntity.judulSubBab,
            isiTeks = subBabEntity.isiTeks,
            isSelesai = false,
            kuisSubBab = parseKuis(subBabEntity.kuisSubBabJson)
        )
    }

    private fun parseKuis(jsonStr: String?): Kuis? {
        if (jsonStr.isNullOrEmpty()) return null
        return try {
            val json = JSONObject(jsonStr)
            val xpReward = json.getInt("xpReward")
            val questionsArray = json.getJSONArray("questions")
            val questionsList = mutableListOf<QuizQuestion>()
            for (i in 0 until questionsArray.length()) {
                val qObj = questionsArray.getJSONObject(i)
                val optionsArray = qObj.getJSONArray("options")
                val options = mutableListOf<String>()
                for (j in 0 until optionsArray.length()) options.add(optionsArray.getString(j))

                questionsList.add(
                    QuizQuestion(
                        question = qObj.getString("question"),
                        options = options,
                        correctIndex = qObj.getInt("correctIndex"),
                        explanation = qObj.getString("explanation")
                    )
                )
            }
            Kuis(xpReward, questionsList)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

package com.example.giga17.data.repository

import com.example.giga17.data.model.JdoodleRequest
import com.example.giga17.data.model.JdoodleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class CompilerRepository {
    private val jsonConfig = Json { 
        ignoreUnknownKeys = true
        encodeDefaults = true 
    }

    suspend fun compilePascal(code: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val requestData = JdoodleRequest(
                    script = code,
                    language = "pascal",
                    versionIndex = "3",
                    clientId = "7e85dc6d0143fbaaf30094c55f3c91ca",
                    clientSecret = "5347f9d6ea35aefc381204830a6a9088fd8e261608ae08548ff3e28dcd119e7"
                )
                
                val jsonRequest = jsonConfig.encodeToString(requestData)
                
                val url = URL("https://api.jdoodle.com/v1/execute")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json; utf-8")
                connection.setRequestProperty("Accept", "application/json")
                connection.doOutput = true
                
                connection.outputStream.use { os ->
                    val input = jsonRequest.toByteArray(Charsets.UTF_8)
                    os.write(input, 0, input.size)
                }
                
                val responseCode = connection.responseCode
                
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseString = connection.inputStream.bufferedReader().use { it.readText() }
                    val responseObj = jsonConfig.decodeFromString<JdoodleResponse>(responseString)
                    responseObj.output ?: "Tidak ada output"
                } else {
                    val errorString = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error"
                    "Gagal (HTTP $responseCode):\n$errorString"
                }
            } catch (e: Exception) {
                "Error Exception:\n${e.message}"
            }
        }
    }
}

package br.com.fernando.servicestudy.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

object GsonHelper {
    private val gson: Gson by lazy {
        GsonBuilder()
            .serializeNulls() // Inclui campos nulos na serialização
            .create()
    }

    /**
     * Converte um objeto para JSON
     * @param src Objeto a ser convertido
     * @return String JSON ou null em caso de erro
     */
    fun <T> toJson(src: T): String? {
        return try {
            gson.toJson(src)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Converte uma String JSON para um objeto
     * @param json String JSON
     * @param classOfT Classe do objeto de destino
     * @return Objeto convertido ou null em caso de erro
     */
    fun <T> fromJson(json: String?, classOfT: Class<T>): T? {
        if (json.isNullOrEmpty()) return null

        return try {
            gson.fromJson(json, classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Converte uma String JSON para uma lista de objetos
     * @param json String JSON
     * @param typeOfT Tipo da lista (use com TypeToken)
     * @return Lista de objetos ou null em caso de erro
     */
    fun <T> fromJsonList(json: String?, typeOfT: Type): List<T>? {
        if (json.isNullOrEmpty()) return null

        return try {
            gson.fromJson<List<T>>(json, typeOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Converte um Map para um objeto
     * @param map Map a ser convertido
     * @param classOfT Classe do objeto de destino
     * @return Objeto convertido ou null em caso de erro
     */
    fun <T> mapToObject(map: Map<String, Any>, classOfT: Class<T>): T? {
        return try {
            val json = gson.toJson(map)
            gson.fromJson(json, classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
package com.am.projectinternalresto.service.source

import androidx.lifecycle.liveData
import com.am.projectinternalresto.data.params.CategoryBody
import com.am.projectinternalresto.data.params.MenuBody
import com.am.projectinternalresto.data.params.toMultipartBody
import com.am.projectinternalresto.data.params.toMultipartImagePart
import com.am.projectinternalresto.service.api.ApiService
import com.am.projectinternalresto.utils.Key
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

class MenuRepository(private val apiService: ApiService) {

    fun getCategoryMenu(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getCategoryMenu("Bearer $token")
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(Key.ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun addCategoryMenu(token: String, payload: CategoryBody) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                val response = apiService.addCategoryMenu("Bearer $token", payload)
                if (response.isSuccessful) {
                    emit(Resource.success(response.body()))
                } else {
                    response.errorBody()?.let {
                        val errorMessage = JSONObject(it.string()).getString(Key.ERROR_MESSAGE)
                        emit(Resource.error(null, errorMessage))
                    }
                }
            } catch (exception: Exception) {
                emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
            }
        }

    fun updateCategoryMenu(
        token: String,
        idCategory: String,
        payload: CategoryBody,
    ) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                val response =
                    apiService.updateCategoryMenu("Bearer $token", idCategory, payload)
                if (response.isSuccessful) {
                    emit(Resource.success(response.body()))
                } else {
                    response.errorBody()?.let {
                        val errorMessage = JSONObject(it.string()).getString(Key.ERROR_MESSAGE)
                        emit(Resource.error(null, errorMessage))
                    }
                }
            } catch (exception: Exception) {
                emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
            }
        }

    fun deleteCategoryMenu(token: String, idCategory: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.deleteCategoryMenu("Bearer $token", idCategory)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(Key.ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun getAllDataMenu(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getAllDataMenu("Bearer $token")
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(Key.ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun addMenu(
        token: String, menuBody: MenuBody
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val bodyPayload = menuBody.toMultipartBody()
        val imagePayload = menuBody.toMultipartImagePart()
        try {
            val response = apiService.addMenu("Bearer $token", bodyPayload, imagePayload)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(Key.ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun updateMenu(token: String, idMenu: String, menuBody: MenuBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val bodyPayload = menuBody.toMultipartBody()
        val imagePayload = menuBody.toMultipartImagePart()
        try {
            val response = apiService.updateMenu("Bearer $token", idMenu, bodyPayload, imagePayload)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(Key.ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }
}
package com.am.projectinternalresto.service.source

import android.util.Log
import androidx.lifecycle.liveData
import com.am.projectinternalresto.data.body_params.CategoryRequest
import com.am.projectinternalresto.data.body_params.MenuBody
import com.am.projectinternalresto.data.body_params.toMultipartBody
import com.am.projectinternalresto.data.body_params.toMultipartImagePart
import com.am.projectinternalresto.service.api.ApiService
import com.am.projectinternalresto.utils.Key
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

class MenuRepository(private val apiService: ApiService) {

    fun getMenuFavoriteSuperAdmin(token: String, locationId: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getMenuFavoriteSuperAdmin("Bearer $token", locationId)
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

    fun getMenuFavoriteAdmin(token: String, categoryId: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getMenuFavoriteAdmin("Bearer $token", categoryId)
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

    fun searchCategoryMenu(token: String, keyword: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.searchCategoryMenu("Bearer $token", keyword)
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

    fun addCategoryMenu(token: String, payload: CategoryRequest) =
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
        payload: CategoryRequest,
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
                Log.e("Check_menu_repo", "data : ${response.body()}")
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
                Log.e("Check_menu_repo", "data : $response")
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

    fun deleteMenu(token: String, idMenu: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.deleteMenu("Bearer $token", idMenu)
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

    fun searchMenu(token: String, keyword: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.searchMenu("Bearer $token", keyword)
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
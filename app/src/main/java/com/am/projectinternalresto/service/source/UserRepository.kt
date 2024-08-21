package com.am.projectinternalresto.service.source


import androidx.lifecycle.liveData
import com.am.projectinternalresto.data.params.AdminAndSuperAdminBody
import com.am.projectinternalresto.data.params.LoginBody
import com.am.projectinternalresto.data.params.StaffBody
import com.am.projectinternalresto.service.api.ApiService
import com.am.projectinternalresto.utils.Key
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

class UserRepository(private val apiService: ApiService) {
    fun login(username: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val bodyParameter = LoginBody(username, password)
            val response = apiService.login(bodyParameter)
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

    fun getAllDataAdminSuperAdmin(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getAllDataAdminAndSuperAdmin("Bearer $token")
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

    fun addAdminOrSuperAdmin(
        token: String,
        payload: AdminAndSuperAdminBody
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.addAdminOrSuperAdmin("Bearer $token", payload)
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

    fun updateAdminOrSuperAdmin(
        token: String,
        idUser: String,
        payload: AdminAndSuperAdminBody
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.updateAdminOrSuperAdmin("Bearer $token", idUser, payload)

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

    fun deleteAdminOrSuperAdmin(token: String, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.deleteAdminOrSuperAdmin("Bearer $token", id)
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

    fun getAllDataStaff(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getAllDataStaff("Bearer $token")
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

    fun addStaff(token: String, dataStaff: StaffBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.addStaff("Bearer $token", dataStaff)
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

    fun updateStaff(token: String, idStaff: String, dataStaff: StaffBody) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                val response = apiService.updateStaff("Bearer $token", idStaff, dataStaff)
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

    fun deleteStaff(token: String, idStaff: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.deleteStaff("Bearer $token", idStaff)
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
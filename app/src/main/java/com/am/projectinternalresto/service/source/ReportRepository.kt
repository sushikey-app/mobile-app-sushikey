package com.am.projectinternalresto.service.source

import androidx.lifecycle.liveData
import com.am.projectinternalresto.service.api.ApiService
import com.am.projectinternalresto.utils.Key.ERROR_MESSAGE
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

class ReportRepository(private val apiService: ApiService) {

    fun getListDataReportSuperAdmin(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getListDataReportSuperAdmin("Bearer $token")
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun getDataReportForPrintSuperAdmin(
        token: String,
        locationId: String,
        initialDate: String,
        deadlineDate: String
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response =
                apiService.getDataReportSuperAdmin(
                    "Bearer $token",
                    locationId,
                    initialDate,
                    deadlineDate
                )
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun getDetailDataReportSuperAdmin(token: String, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getDetailReportSuperAdmin("Bearer $token", id)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun getListDataReportAdmin(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getListDataReportAdmin("Bearer $token")
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun getDataReportForPrintAdmin(
        token: String,
        locationId: String,
        initialDate: String,
        deadlineDate: String
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response =
                apiService.getDataReportAdmin(
                    "Bearer $token",
                    locationId,
                    initialDate,
                    deadlineDate
                )
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun getDetailDataReportAdmin(token: String, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getDetailReportAdmin("Bearer $token", id)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun filterReportByLocation(token: String, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.filterReportByLocation("Bearer $token", id)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                response.errorBody()?.let {
                    val errorMessage = JSONObject(it.string()).getString(ERROR_MESSAGE)
                    emit(Resource.error(null, errorMessage))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!!"))
        }
    }

    fun deleteReportSuperAdmin(token: String, locationId: String, month: Int, year: Int) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                val response = apiService.deleteReport("Bearer $token", locationId, month, year)
                if (response.isSuccessful) {
                    emit(Resource.success(response.body()))
                } else {
                    response.errorBody()?.let {
                        val errorMessage = JSONObject(it.string()).getString(ERROR_MESSAGE)
                        emit(Resource.error(null, errorMessage))
                    }
                }
            } catch (exception: Exception) {
                emit(Resource.error(null, exception.message ?: "Error Occurred!"))
            }
        }
}
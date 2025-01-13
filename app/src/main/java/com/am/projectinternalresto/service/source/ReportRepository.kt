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
        startDate: Int,
        startMonth: Int,
        startYear: Int,
        endDate: Int,
        endMonth: Int,
        endYear: Int
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response =
                apiService.getReportForPrint(
                    "Bearer $token",
                    locationId,
                    startDate,
                    startMonth,
                    startYear,
                    endDate,
                    endMonth,
                    endYear
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

    fun getDataFilterReportSuperAdmin(
        token: String,
        locationId: String,
        startDate: Int,
        startMonth: Int,
        startYear: Int,
        endDate: Int,
        endMonth: Int,
        endYear: Int
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response =
                apiService.filterReportSuperAdmin(
                    "Bearer $token",
                    locationId,
                    startDate,
                    startMonth,
                    startYear,
                    endDate,
                    endMonth,
                    endYear
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

    fun deleteReportSuperAdmin(
        token: String,
        locationId: String,
        startDate: Int,
        startMonth: Int,
        startYear: Int,
        endDate: Int,
        endMonth: Int,
        endYear: Int
    ) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                val response = apiService.deleteReport(
                    "Bearer $token",
                    locationId,
                    startDate,
                    startMonth,
                    startYear,
                    endDate,
                    endMonth,
                    endYear
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
                emit(Resource.error(null, exception.message ?: "Error Occurred!"))
            }
        }


    fun getDataFilterReportAdmin(
        token: String,
        startDate: Int,
        startMonth: Int,
        startYear: Int,
        endDate: Int,
        endMonth: Int,
        endYear: Int
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response =
                apiService.filterReportAdmin(
                    "Bearer $token",
                    startDate, startMonth, startYear, endDate, endMonth, endYear
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

    fun getDataReportForPrintAdmin(
        token: String,
        startDate: Int,
        startMonth: Int,
        startYear: Int,
        endDate: Int,
        endMonth: Int,
        endYear: Int
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response =
                apiService.getDataPrintForAdmin(
                    "Bearer $token",
                    startDate, startMonth, startYear, endDate, endMonth, endYear
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
}
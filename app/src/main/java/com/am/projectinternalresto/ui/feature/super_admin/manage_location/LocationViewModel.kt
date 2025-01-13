package com.am.projectinternalresto.ui.feature.super_admin.manage_location

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.body_params.LocationRequest
import com.am.projectinternalresto.service.source.LocationRepository

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {
    fun getLocation(token: String) = repository.getLocation(token)
    fun searchLocation(token: String, keyword : String) = repository.searchLocation(token, keyword)
    fun addLocation(
        token: String, payload: LocationRequest
    ) = repository.addLocation("Bearer $token", payload)

    fun updateLocation(
        token: String, idLocation: String, payload : LocationRequest
    ) = repository.updateLocation(token, idLocation,payload)

    fun deleteLocation(token: String, idLocation: String) =
        repository.deleteLocation(token, idLocation)
}
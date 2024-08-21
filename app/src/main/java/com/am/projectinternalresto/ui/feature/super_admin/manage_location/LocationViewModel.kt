package com.am.projectinternalresto.ui.feature.super_admin.manage_location

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.params.LocationBody
import com.am.projectinternalresto.service.source.LocationRepository

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {
    fun getLocation(token: String) = repository.getLocation(token)
    fun addLocation(
        token: String, payload: LocationBody
    ) = repository.addLocation("Bearer $token", payload)

    fun updateLocation(
        token: String, idLocation: String, payload : LocationBody
    ) = repository.updateLocation(token, idLocation,payload)

    fun deleteLocation(token: String, idLocation: String) =
        repository.deleteLocation(token, idLocation)
}
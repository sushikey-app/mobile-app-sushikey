package com.am.projectinternalresto.service.api

import com.am.projectinternalresto.data.params.AdminAndSuperAdminBody
import com.am.projectinternalresto.data.params.CategoryBody
import com.am.projectinternalresto.data.params.LocationBody
import com.am.projectinternalresto.data.params.LoginBody
import com.am.projectinternalresto.data.params.StaffBody
import com.am.projectinternalresto.data.response.GeneralResponse
import com.am.projectinternalresto.data.response.admin.category.AddOrUpdateCategoryResponse
import com.am.projectinternalresto.data.response.admin.category.CategoryResponse
import com.am.projectinternalresto.data.response.admin.manage_staff.AddOrUpdateStaffResponse
import com.am.projectinternalresto.data.response.admin.manage_staff.StaffResponse
import com.am.projectinternalresto.data.response.admin.menu.AddOrUpdateMenuResponse
import com.am.projectinternalresto.data.response.admin.menu.MenuResponse
import com.am.projectinternalresto.data.response.auth.LoginResponse
import com.am.projectinternalresto.data.response.super_admin.location.AddOrUpdateLocationResponse
import com.am.projectinternalresto.data.response.super_admin.location.LocationResponse
import com.am.projectinternalresto.data.response.super_admin.manage_admin.AddOrUpdateAdminSuperAdminResponse
import com.am.projectinternalresto.data.response.super_admin.manage_admin.ManageAdminAndSuperAdminResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiService {
    /*user auth*/
    //login
    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): Response<LoginResponse>

    /*Super Admin*/
    // location
    @GET("lokasi")
    suspend fun getLocation(
        @Header("Authorization") bearer: String
    ): Response<LocationResponse>

    @POST("lokasi")
    suspend fun addLocation(
        @Header("Authorization") bearer: String,
        @Body locationBody: LocationBody
    ): Response<AddOrUpdateLocationResponse>

    @PUT("lokasi/{id}")
    suspend fun updateLocation(
        @Header("Authorization") bearer: String,
        @Path("id") idLocation: String,
        @Body locationBody: LocationBody
    ): Response<AddOrUpdateLocationResponse>

    @DELETE("lokasi/{id}")
    suspend fun deleteLocation(
        @Header("Authorization") bearer: String,
        @Path("id") idLocation: String,
    ): Response<GeneralResponse>

    // manage admin and super admin
    @GET("pegawai-super-admin")
    suspend fun getAllDataAdminAndSuperAdmin(
        @Header("Authorization") bearer: String
    ): Response<ManageAdminAndSuperAdminResponse>

    @POST("pegawai-super-admin")
    suspend fun addAdminOrSuperAdmin(
        @Header("Authorization") bearer: String,
        @Body addAdminAndSuperAdmin: AdminAndSuperAdminBody
    ): Response<AddOrUpdateAdminSuperAdminResponse>

    @PUT("pegawai-super-admin/{id}")
    suspend fun updateAdminOrSuperAdmin(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body updateAdminOrSuperAdmin: AdminAndSuperAdminBody
    ): Response<AddOrUpdateAdminSuperAdminResponse>

    @DELETE("pegawai-super-admin/{id}")
    suspend fun deleteAdminOrSuperAdmin(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): Response<GeneralResponse>

    // TODO :: create function search

    // Admin
    // category
    @GET("kategori")
    suspend fun getCategoryMenu(
        @Header("Authorization") bearer: String,
    ): Response<CategoryResponse>

    @POST("kategori")
    suspend fun addCategoryMenu(
        @Header("Authorization") bearer: String,
        @Body categoryBody: CategoryBody
    ): Response<AddOrUpdateCategoryResponse>

    @PUT("kategori/{id}")
    suspend fun updateCategoryMenu(
        @Header("Authorization") bearer: String,
        @Path("id") idCategory: String,
        @Body categoryBody: CategoryBody
    ): Response<AddOrUpdateCategoryResponse>

    @DELETE("kategori/{id}")
    suspend fun deleteCategoryMenu(
        @Header("Authorization") bearer: String,
        @Path("id") idCategory: String,
    ): Response<GeneralResponse>

    // menu
    @GET("menu")
    suspend fun getAllDataMenu(
        @Header("Authorization") bearer: String
    ): Response<MenuResponse>

    // try add menu using body
    @Multipart
    @POST("menu")
    suspend fun addMenu(
        @Header("Authorization") bearer: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<AddOrUpdateMenuResponse>

    @Multipart
    @PUT("menu/{id}")
    suspend fun updateMenu(
        @Header("Authorization") bearer: String,
        @Path("id") idMenu: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<AddOrUpdateMenuResponse>

    @GET("pegawai-admin")
    suspend fun getAllDataStaff(@Header("Authorization") bearer: String): Response<StaffResponse>

    @POST("pegawai-admin")
    suspend fun addStaff(
        @Header("Authorization") bearer: String,
        @Body staffBody: StaffBody
    ): Response<AddOrUpdateStaffResponse>

    @PUT("pegawai-admin/{id}")
    suspend fun updateStaff(
        @Header("Authorization") bearer: String,
        @Path("id") idStaff: String,
        @Body staffBody: StaffBody
    ): Response<AddOrUpdateStaffResponse>

    @DELETE("pegawai-admin/{id}")
    suspend fun deleteStaff(
        @Header("Authorization") bearer: String,
        @Path("id") idStaff: String,
    ): Response<GeneralResponse>
}